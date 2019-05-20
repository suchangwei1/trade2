package main;

import com.trade.push.core.PushServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        Class<?> clazz = Class.forName("org.springframework.context.support.ClassPathXmlApplicationContext");
        Object context = clazz.newInstance();
        clazz.getMethod("setConfigLocation", String.class).invoke(context, "classpath:applicationContext.xml");
        clazz.getMethod("refresh").invoke(context);
        PushServer pushServer = (PushServer) clazz.getMethod("getBean", Class.class).invoke(context, PushServer.class);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pushServer.close()));
        pushServer.start();
        log.info("push server started {} ms.", (System.currentTimeMillis() - startTime));
    }

}
