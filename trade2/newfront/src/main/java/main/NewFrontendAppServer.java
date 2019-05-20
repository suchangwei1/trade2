package main;

import com.alibaba.druid.support.http.WebStatFilter;
import com.trade.filter.ForceOfflineFilter;
import com.trade.servlet.ImageCode;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Date;

public class NewFrontendAppServer {

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.printf("USE SYSTEM %s %s %s \n", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
        System.out.printf("USE JDK %s %s \n", System.getProperty("java.version"), System.getProperty("java.vm.specification.name"));
        int port = 8888;

        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        ServletContextHandler servletContext = new ServletContextHandler();
        servletContext.setContextPath("/");

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });
        HashSessionIdManager sessionIdManager = new HashSessionIdManager();
        server.setSessionIdManager(sessionIdManager);

        HandlerCollection handlerCollection = new HandlerCollection();

        ServletContextHandler springMvcHandler = new ServletContextHandler();
        springMvcHandler.setContextPath("/");
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocations(new String[]{"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"});
        springMvcHandler.addEventListener(new ContextLoaderListener(context));

        springMvcHandler.addServlet(new ServletHolder("trade", ServletHandler.Default404Servlet.class), "/static");
        springMvcHandler.addServlet(new ServletHolder("ImageCode", ImageCode.class), "/api/v1/servlet/ImageCode");
        springMvcHandler.addServlet(new ServletHolder("druid", com.alibaba.druid.support.http.StatViewServlet.class), "/druid/*");
        springMvcHandler.addServlet(new ServletHolder("mvc", new DispatcherServlet(context)), "/*");
        // WEB监控
        FilterHolder webStatFilter = new FilterHolder(new WebStatFilter());
        webStatFilter.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        springMvcHandler.addFilter(webStatFilter, "/*", null);
        DelegatingFilterProxy filter = new DelegatingFilterProxy("springSessionRepositoryFilter");
        springMvcHandler.addFilter(new FilterHolder(filter), "/*", null);

        // 强制下线
        FilterHolder offlineFilter = new FilterHolder(new ForceOfflineFilter());
        springMvcHandler.addFilter(offlineFilter, "/api/*", null);

        springMvcHandler.setSessionHandler(new SessionHandler(new HashSessionManager()));

        handlerCollection.setHandlers(new Handler[]{springMvcHandler});

        server.setHandler(handlerCollection);

        server.start();
        System.out.printf("Server started take %d ms, open your in browser http://localhost:%d/\n", (System.currentTimeMillis() - startTime), port);
        System.out.println("Start Time " + new Date());
        server.join();
    }

}
