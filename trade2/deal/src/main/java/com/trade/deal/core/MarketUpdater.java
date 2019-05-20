package com.trade.deal.core;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class MarketUpdater implements Runnable {

    private int poolSize = 8;
    private ExecutorService[] es = new ExecutorService[poolSize];               // 更新线程池, 用来执行行情更新任务
    private DelayQueue<Event> queue = new DelayQueue<>();                       // 这里用一个延迟队列, 来合并数据推送, 舍弃一点非高并发时的实时性, 换来高并发时的实时性
    private Map<String, Event> updated = new ConcurrentHashMap<>();             // 已更新的数据缓存
    private Map<String, Event> waitUpdateQueue = new ConcurrentHashMap<>();     // 待更新的数据, 因为数据有时效性, 所以总是以新数据为准, 新数据直接覆盖旧数据

    public MarketUpdater() {
        for (int i = 0; i < poolSize; i++) {
            es[i] = Executors.newFixedThreadPool(1);
        }
    }

    public void publish(String type, Runnable delegate) {
        Event event = new Event(type, delegate);
        if (type != null) {
            waitUpdateQueue.put(type, event);
        }
        queue.add(event);
    }

    public void publish(Runnable delegate) {
        publish(null, delegate);
    }

    @PostConstruct
    public void start() {
        new Thread(this).start();
    }

    private void submit(String type, Runnable runnable) {
        int index;
        if (type != null) {
            index = type.hashCode() % poolSize + 1;
        } else {
            index = new Random().nextInt(poolSize);
        }
        ExecutorService executorService = es[index];
        if (executorService == null || runnable == null) {
            throw new RuntimeException();
        }
        executorService.submit(runnable);
    }

    @Override
    public void run() {
        for (;;) {
            try {
                Event e = queue.take();
                String key = e.getType();
                Event event, oldEvent = null;
                if (key != null) {
                    event = waitUpdateQueue.get(key);
                    oldEvent = updated.get(key);
                } else {
                    event = e;
                }
                if (oldEvent == null || event.getSort() > oldEvent.getSort()) {
                    if (key != null) {
                        updated.put(key, event);
                    }
                    submit(key, event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class Event implements Delayed, Runnable {

    private static final AtomicLong at = new AtomicLong();
    private static final long DELAY_TIME = 10;
    private static long lastCreateTime = 0;

    private String type;
    private long time;
    private long sort;      // sort 大的优先

    private Runnable delegate;

    public Event() {
        sort = at.incrementAndGet();
        long now = System.currentTimeMillis();
        // 合并一段时间的数据推送, 单位毫秒
        if (now - lastCreateTime > DELAY_TIME) {
            lastCreateTime = now;
        }
        time = lastCreateTime + DELAY_TIME;
    }

    public Event(String type, Runnable delegate) {
        this();
        this.type = type;
        this.delegate = delegate;
    }

    public String getType() {
        return type;
    }

    public long getSort() {
        return sort;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        Event e = (Event) o;
        return Long.compare(e.sort, sort);
    }

    @Override
    public void run() {
        if (delegate != null) {
            delegate.run();
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                ", type='" + type + '\'' +
                ", sort='" + sort + '\'' +
                ", at='" + at + '\'' +
                '}';
    }
}