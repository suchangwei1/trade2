package main;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Date;

public class ApiAppServer {

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.printf("USE SYSTEM %s %s %s \n", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
        System.out.printf("USE JDK %s %s \n", System.getProperty("java.version"), System.getProperty("java.vm.specification.name"));
        int port = 8090;

        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        ServletContextHandler servletContext = new ServletContextHandler();
        servletContext.setContextPath("/");

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });
//        WebAppContext webapp = new WebAppContext();
//        webapp.setContextPath("/");
//        webapp.setWar("web/src/main/webapp");

//        webapp.setClassLoader(Thread.currentThread().getContextClassLoader());
//        webapp.setConfigurationDiscovered(true);
//        webapp.setParentLoaderPriority(true);

        HandlerCollection handlerCollection = new HandlerCollection();
//        handlerCollection.setHandlers(new Handler[]{webapp, new DefaultHandler()});

        ServletContextHandler springMvcHandler = new ServletContextHandler();
        springMvcHandler.setContextPath("/");
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocations(new String[]{"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"});
        springMvcHandler.addEventListener(new ContextLoaderListener(context));

        springMvcHandler.addServlet(new ServletHolder("api", ServletHandler.Default404Servlet.class), "/static");
        ServletHolder druidServlet = new ServletHolder("druid", StatViewServlet.class);
        springMvcHandler.addServlet(druidServlet, "/druid/*");
        springMvcHandler.addServlet(new ServletHolder("mvc", new DispatcherServlet(context)), "/*");
        // WEB监控
        FilterHolder webStatFilter = new FilterHolder(new WebStatFilter());
        webStatFilter.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        springMvcHandler.addFilter(webStatFilter, "/*", null);

        handlerCollection.setHandlers(new Handler[]{springMvcHandler});

//        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault( server );
//        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration" );

//        webapp.setAttribute(
//                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
//                ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );


        server.setHandler(handlerCollection);

        server.start();
        System.out.printf("Server started take %d ms, open your in browser http://localhost:%d/\n", (System.currentTimeMillis() - startTime), port);
        System.out.println("Start Time " + new Date());
        server.join();

    }

}
