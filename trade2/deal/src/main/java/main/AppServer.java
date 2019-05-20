package main;

import com.trade.deal.core.MessageCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppServer {

    private static final Logger log = LoggerFactory.getLogger(AppServer.class);

    public static void main(String[] args) throws Exception {
        System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                "                   _ooOoo_\n" +
                "                  o8888888o\n" +
                "                  88\" . \"88\n" +
                "                  (| -_- |)\n" +
                "                  O\\  =  /O\n" +
                "               ____/`---'\\____\n" +
                "             .'  \\\\|     |//  `.\n" +
                "            /  \\\\|||  :  |||//  \\\n" +
                "           /  _||||| -:- |||||-  \\\n" +
                "           |   | \\\\\\  -  /// |   |\n" +
                "           | \\_|  ''\\---/''  |   |\n" +
                "           \\  .-\\__  `-`  ___/-. /\n" +
                "         ___`. .'  /--.--\\  `. . __\n" +
                "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n" +
                "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n" +
                "======`-.____`-.___\\_____/___.-`____.-'======\n" +
                "                   `=---='\n" +
                "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                "");

        // 内嵌jetty, 提供web管理功能
        long startTime = System.currentTimeMillis();

        Application application = Application.getInstance();

//        Object server = application.getBean("org.eclipse.jetty.server.Server");

//        Object servletContext = application.getBean("org.eclipse.jetty.servlet.ServletContextHandler");
//        Object handlers = application.getBean("org.eclipse.jetty.server.handler.HandlerCollection");

//        servletContext.getClass().getMethod("setClassLoader", ClassLoader.class).invoke(servletContext, Thread.currentThread().getContextClassLoader());
//        servletContext.getClass().getMethod("setContextPath", String.class).invoke(servletContext, "/");
//        servletContext.getClass().getMethod("addServlet", String.class, String.class).invoke(servletContext, "com.alibaba.druid.support.http.StatViewServlet", "/druid/*");
//        servletContext.getClass().getMethod("addServlet", String.class, String.class).invoke(servletContext, "com.trade.deal.stat.IndexServlet", "/");

//        server.getClass().getMethod("setHandler", Class.forName("org.eclipse.jetty.server.Handler")).invoke(server, handlers);

//        server.getClass().getMethod("start").invoke(server);

        log.info("app server started {} ms", (System.currentTimeMillis() - startTime));

        System.out.println("deal started");

        MessageCenter messageCenter = application.getBean(MessageCenter.class);
        messageCenter.init();

//        server.getClass().getMethod("join").invoke(server);

    }

}
