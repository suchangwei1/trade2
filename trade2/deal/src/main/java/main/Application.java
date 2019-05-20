package main;

import com.trade.deal.core.MatchingEngine;
import com.trade.deal.core.TradeService;
import com.trade.deal.core.TradingSystem;
import com.trade.deal.listener.DealMarkingListener;
import com.trade.deal.listener.EntrustListener;
import com.trade.deal.mq.MessageQueueService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Application {

    private static final Application INSTANCE;

    private Object context;

    private Method getBeanMethod;

    static {
        INSTANCE = new Application();
        INSTANCE.init();
    }

    public static final Application getInstance() {
        return INSTANCE;
    }

    private Application() {
    }

    private void init() {
        try {
            Class<?> clazz = Class.forName("org.springframework.context.support.ClassPathXmlApplicationContext");
            context = clazz.newInstance();
            getBeanMethod = clazz.getMethod("getBean", Class.class);
            clazz.getMethod("setConfigLocation", String.class).invoke(context, "classpath:applicationContext.xml");
            clazz.getMethod("afterPropertiesSet").invoke(context);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return (T) INSTANCE.getBeanMethod.invoke(INSTANCE.context, clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getBean(String className) {
        try {
            return getBean(Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public static TradingSystem buildTradingSystem() {
        return new TradingSystem(getBean(MatchingEngine.class), getBean(EntrustListener.class), getBean(DealMarkingListener.class), getBean(TradeService.class), getBean(MessageQueueService.class));
    }

}
