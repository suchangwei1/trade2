package com.trade.push.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.trade.model.Fuser;
import com.trade.mq.QueueConstants;
import com.trade.push.data.Constants;
import com.trade.push.data.RealDataService;
import com.trade.push.mq.MessageListener;
import com.trade.push.utils.HttpUtils;

import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.trade.auto.KlinePeriodData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Closeable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class PushServer  implements MessageListener<String>, ConnectListener, DisconnectListener, Closeable {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private boolean serverStarted;
    private boolean messageCenterStarted;

    @Resource
    private SocketIOServer server;
    @Resource
    private MessageCenter messageCenter;
    @Resource
    private JedisPool jedisPool;
    @Resource
    private RealDataService dataService;
    @Resource
    private SessionManager sessionManager;
    @Resource
    private ExecutorService executorService;
    private String siteUrl;
    private int delay;

    public void start() {
        log.debug("start push server");
        startListeners();
        server.start();
        serverStarted = true;
        messageCenter.start(this);
        messageCenterStarted = true;
    }

    public static String formatCoin(Double num, String pattern) {
        if (null == num) {
            return "0";
        }

        if (num > -0.00000002 && num < 0.00000001) {
            return "0";
        }

        DecimalFormat format = new DecimalFormat(pattern);
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(num);
    }

    private void startListeners() {
        // 交易实时行情
        SocketIONamespace tradeSocketIONamespace = server.addNamespace("/trade");
        tradeSocketIONamespace.addConnectListener(this);
        tradeSocketIONamespace.addDisconnectListener(this);

        SocketIONamespace chatSocketIONamespace = server.addNamespace("/market_chat");
        chatSocketIONamespace.addConnectListener(this);
        chatSocketIONamespace.addDisconnectListener(this);

        /*otc频道*/
        SocketIONamespace otcSocketIONamespace = server.addNamespace("/otc");
        otcSocketIONamespace.addConnectListener(this);
        otcSocketIONamespace.addDisconnectListener(this);

        SocketIONamespace klineSocketIONamespace = server.addNamespace("/kline");
        klineSocketIONamespace.addConnectListener(this);
        klineSocketIONamespace.addDisconnectListener(this);
    }


    public void period(SocketIOClient socketIOClient,String symbol,String step){

        int s= Integer.parseInt(step);
        int key= parseDate(s);
        String data = substringData(getJson(Integer.valueOf(symbol), key));
        socketIOClient.sendEvent("kline",data);


    /*    Map<String, Set<SocketIOClient>> sessionConnections = sessionManager.getSessionConnections();
        sessionConnections.forEach((key1, value) -> {
            value.forEach(socketIOClient -> {
                int symbol = Integer.valueOf(socketIOClient.getHandshakeData().getSingleUrlParam("symbol"));
                int step = Integer.valueOf(socketIOClient.getHandshakeData().getSingleUrlParam("step"));
                int key= parseDate(step);
                String data = substringData(getJson(symbol, key));
                socketIOClient.sendEvent("kline",data);
            });
        });*/
    }


    public int parseDate(int step){
        int key = 0;
        switch (step) {
            case 60:
                key = 0;

                break;
            case 60 * 3:
                key = 1;

                break;
            case 60 * 5:
                key = 2;

                break;
            case 60 * 15:
                key = 3;

                break;
            case 60 * 30:
                key = 4;

                break;
            case 60 * 60:
                key = 5;

                break;
            case 60 * 60 * 2:
                key = 6;

                break;
            case 60 * 60 * 4:
                key = 7;

                break;
            case 60 * 60 * 6:
                key = 8;

                break;
            case 60 * 60 * 12:
                key = 9;

                break;
            case 60 * 60 * 24:
                key = 10;

                break;
            case 60 * 60 * 24 * 3:
                key = 11;

                break;
            case 60 * 60 * 24 * 7:
                key = 12;

                break;
        }
        return key;

    }
    public void period(int symbol){
       // System.out.println("start:"+System.currentTimeMillis());
        List<Integer> list=new  ArrayList<Integer>();
        list.add(60);
        list.add(60 * 3);
        list.add(60 * 5);
        list.add(60 * 15);
        list.add(60 * 30);
        list.add(60 * 60);
        list.add(60 * 60 * 2);
        list.add(60 * 60 * 4);
        list.add(60 * 60 * 6);
        list.add(60 * 60 * 12);
        list.add(60 * 60 * 24);
        list.add(60 * 60 * 24 * 3);
        list.add(60 * 60 * 24 * 7);
        list.forEach(step -> {
            int key= parseDate(step);
            String data = substringData(getJson(symbol, key));
            String room = "/kline-" + symbol + "-" + step;
            log.info("开始推送房间号：***********"+ room);
           // System.out.println("开始推送房间号：***********"+ room);
            server.getRoomOperations(room).sendEvent("kline", data);
            sleep();
        });

       // System.out.println("end: "+System.currentTimeMillis());
    }

    private static String substringData(String data) {

        int length = data.length();
        int count = 0;
        for (int i = length - 1; i >= 0; i--) {
            if (data.charAt(i) == ']') {
                count++;
            }

            if (count == 802) {
                return "[" + data.substring(i + 2);
            }
        }
        return data;
    }

    private String getJson(int id, int key) {
        final byte[] mkey = ("cache:kline:" + id).getBytes();
        final byte[] jkey = ("" + key).getBytes();

        byte[] bytes;
        try (Jedis jedis = jedisPool.getResource()) {
            bytes = jedis.hget(mkey, jkey);
        }

        if (bytes != null) {
            return new String(bytes);
        }

        return "[]";
    }

    private void joinTradeRoom(SocketIOClient client) {

        String symbol = client.getHandshakeData().getSingleUrlParam("symbol");
        String deep = client.getHandshakeData().getSingleUrlParam("deep");
        client.sendEvent("ok"); // 连接成功后发送一条消息，可以加快客户端连接速度
        if (StringUtils.hasText(symbol)) {
            String room = "/trade-" + symbol + "-" + deep;
            client.joinRoom(room);
            sendAllMessage(client, symbol, deep);       // 连接的时候，发送所有最新消息到客户端，以免出现连接速度太慢，而造成丢失的现象
        } else {
            log.error("无效币ID", symbol);
        }
    }
    private void joinKlineRoom(SocketIOClient client) {

        String symbol = client.getHandshakeData().getSingleUrlParam("symbol");
        String step = client.getHandshakeData().getSingleUrlParam("step");
        client.sendEvent("ok"); // 连接成功后发送一条消息，可以加快客户端连接速度
        if (StringUtils.hasText(symbol) &&Integer.parseInt(step)!=4) {
            String room = "/kline-" + symbol + "-" + step;
            client.joinRoom(room);
            log.info("刚进来房间号：***********"+ room);
         //   System.out.println("刚进来房间号：***********"+ room );
            sendAllMessage(client, symbol, step);       // 连接的时候，发送所有最新消息到客户端，以免出现连接速度太慢，而造成丢失的现象
        } else {
            log.error("无效币ID", symbol);
        }
    }

    private void sendAllMessage(SocketIOClient client, String symbol, String deep) {
        try {

            sendDepthEntrustToClient(client, symbol + ":0:" + deep);
            sendDepthEntrustToClient(client, symbol + ":1:" + deep);
            sendEntrustLogToClient(client, symbol);
            sendRealPriceToClient(client, symbol);
            sendUserInfo(client, symbol);

            if(StringUtils.hasText(deep)&&Integer.parseInt(deep)!=4){
                period(client,symbol,deep);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendUserInfo(SocketIOClient client, String symbol) {
      //   long start= System.currentTimeMillis();
        System.out.println("推送用户相关信息开始："+System.currentTimeMillis());
        log.info("推送用户相关信息：@@@@@@@@@@@@@@@@@@"+sessionManager.getHttpSessionId(client)+"  交易对****"+symbol);
        String content = getUserInfoFromWebServer(Integer.parseInt(symbol), sessionManager.getHttpSessionId(client));
        client.sendEvent("entrust-update", content);
     //   long end= System.currentTimeMillis()-start;
      //  System.out.println("推送用户相关信息结束："+end);
    }

    private void sendDepthEntrustToClient(SocketIOClient client, String channel) {
        String[] params = channel.split(":");
        int type = Integer.valueOf(params[1]);
        String etype = type == 0 ? "entrust-buy" : "entrust-sell";
        log.trace("push {} data to client {}.", etype, client.getSessionId());
        client.sendEvent(etype, dataService.getDepthEntrust(channel));
    }

    private void sendEntrustLogToClient(SocketIOClient client, String symbol) {
        // 推送成交日志
        int fviFid = Integer.valueOf(symbol);
        String entrustLog = dataService.getEntrustLog(fviFid);
        log.trace("push entrust-log data to client {}.", client.getSessionId());
        client.sendEvent("entrust-log", entrustLog);
    }

    private void sendRealPriceToClient(SocketIOClient client, String symbol) {
        // 推送实时价格
        int fviFid = Integer.valueOf(symbol);
        Object realPrice = getTickerFromWebServer(fviFid);
        log.trace("push real data to client {}.", client.getSessionId());
        client.sendEvent("real", realPrice);
    }

    private void joinChatRoom(SocketIOClient client) {
        client.sendEvent("ok"); // 连接成功后发送一条消息，可以加快客户端连接速度
        client.joinRoom(Constants.MARKET_CHAT_ROOM);
    }

    private void joinOtcRoom(SocketIOClient client) {
        client.sendEvent("ok"); // 连接成功后发送一条消息，可以加快客户端连接速度
        client.joinRoom(Constants.OTC_ROOM);
    }

    @Override
    public void onConnect(SocketIOClient client) {
        log.debug("start push server");
        sessionManager.addSession(client);
        String namespace = client.getNamespace().getName();
        switch (namespace) {
            case "/trade":
                // 交易房间
                joinTradeRoom(client);
                break;
            case "/market_chat":
                // 聊天室
                joinChatRoom(client);
                break;
            case "/otc":
                // 聊天室
                joinOtcRoom(client);
                break;
            case "/kline":
                // 聊天室
                joinKlineRoom(client);
                break;

            default:
                log.error("not found namespace {}", namespace);
        }
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        // 客户端断开，发送当前在线人数 - 1
//        String room = client.getNamespace().getName();
//        server.getRoomOperations(room).sendEvent("lease", server.getAllClients().size() - 1);
        sessionManager.removeSession(client);
        }


        @Override
        public void onMessage(String channel, String message) {
            executorService.execute(() -> {
//            log.debug("onMessage {}, {}", channel, message);

             if (channel.startsWith("cache:fentrustlog")) {
                pushEntrustLog(channel);
            } else if (channel.startsWith("cache:latest")) {
                pushRealPrice(channel);
            } else if (channel.startsWith("user:entrust")) {
                pushUserEntrustData(channel, message);
            } else if (channel.startsWith(Constants.MARKET_CHAT_CHANNEL)) {
                pushChatMessage(channel, message);
            } else if (channel.startsWith(Constants.OTC_CHANNEL)) {
                pushOTCMessage(channel, message);
            }else if(channel.startsWith("user.trade.kline.queue")){
                 JSONObject obj = JSONObject.parseObject(message);
                 int symbol=Integer.parseInt(obj.get("symbol").toString());
                 period(symbol);
            }
            else {
                 pushEntrustData(channel, message);
            }
        });
    }

    private void pushChatMessage(String channel, String message) {
        log.trace("push message data to room {}.", Constants.MARKET_CHAT_ROOM);
        server.getRoomOperations(Constants.MARKET_CHAT_ROOM).sendEvent("chat-message", message);
    }

    private void pushOTCMessage(String channel, String message) {
        log.trace("push message data to room {}.", Constants.OTC_ROOM);
        String[] arr = channel.split(":");
        int id1 = Integer.valueOf(arr[4]);
        int id2 = Integer.valueOf(arr[3]);
        int fviFid = Integer.valueOf(arr[2]);
        pushOTCUser(sessionManager.getUserSessions(id1), fviFid, id1);
        pushOTCUser(sessionManager.getUserSessions(id2), fviFid, id2);
    }

    private void pushOTCUser(Set<SocketIOClient> userSessions, int fviFid, int uid) {
        if (userSessions != null && userSessions.size() > 0) {
            log.trace("push otc data to user {}.", uid);
            userSessions.forEach(client -> {
                String oid = client.getHandshakeData().getSingleUrlParam("oid");
                if (oid == null) {
                    return;
                }
                int symbol = Integer.valueOf(oid);
                if (fviFid == symbol) {
                    client.sendEvent("order", "");
                }
            });
        }
    }

    private void pushUserEntrustData(String channel, String message) {
        try {
            System.out.println("推送用户相关信息开始："+System.currentTimeMillis());
            int fviFid = Integer.valueOf(channel.split(":")[2]);
            int userId = Integer.valueOf(message);

            // 单个用户，多端登录，需要全部推送
            Set<SocketIOClient> userSessions = sessionManager.getUserSessions(userId);
            if (userSessions != null && userSessions.size() > 0) {
                log.trace("push entrust-update data to user {}.", userId);
                // 这里的推送逻辑改了一下
                // 原来是更新所有频道，现在只推送当前币的频道，其他频道定时更新就好了，不然判断有点多，查询的数据也多，影响效率
                for(SocketIOClient client:userSessions ){
                    int symbol = Integer.valueOf(client.getHandshakeData().getSingleUrlParam("symbol"));
                    if (fviFid == symbol) {
                        String sid = sessionManager.getHttpSessionId(client);
                        String content = getUserInfoFromWebServer(symbol, sid);
                        client.sendEvent("entrust-update", content);
                    }
                }
                System.out.println("推送用户相关信息结束："+System.currentTimeMillis());
               /* userSessions.forEach(client -> {
                    int symbol = Integer.valueOf(client.getHandshakeData().getSingleUrlParam("symbol"));
                    if (fviFid == symbol) {
                        String sid = sessionManager.getHttpSessionId(client);
                        String content = getUserInfoFromWebServer(symbol, sid);
                        client.sendEvent("entrust-update", content);
                    }
                });*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("pushUserEntrustData error reason " + e.getLocalizedMessage());
        }
    }

    private String getUserInfoFromWebServer(int fviFid, String sid) {
        return HttpUtils.get(siteUrl + "/api/v2/market/refreshUserInfo?symbol=" + fviFid, sid);
    }

    private Object getTickerFromWebServer(int fviFid) {
        String data = dataService.getReal(fviFid);
       // log.info("jsonData*************************************"+data);
      //  System.out.println( "jsonData*************************************"+data+"####"+fviFid);
        String  result="[]";
        if(StringUtils.hasText(data)&&!"[]".equals(data)){
            JSONObject obj = JSON.parseObject(data);
            Map map = new HashMap();
            map.put("fupanddown", obj.getDouble("fupanddown"));
            map.put("buy",StringUtils.doubleToString( obj.getDouble("higestBuyPrize"),"0.000000"));
            map.put("last", StringUtils.doubleToString( obj.getDouble("lastDealPrize"),"0.000000"));
            map.put("sell", StringUtils.doubleToString( obj.getDouble("lowestSellPrize"),"0.000000"));
            map.put("vol", formatCoin(obj.getDouble("totalDeal24"), "0.000000"));
            map.put("high", +obj.getDouble("highestPrize24"));
            map.put("low", +obj.getDouble("lowestPrize24"));
            return JSONObject.toJSONString(map)==null?result:JSONObject.toJSONString(map);
        }

          return null;
        //return HttpUtils.get(siteUrl + "/api/v2/market/real?symbol=" + fviFid, null);
    }

    private void pushRealPrice(String channel) {
        // 推送实时价格
        String[] params = channel.split(":");
        int fviFid = Integer.valueOf(params[2]);
        Object realPrice = getTickerFromWebServer(fviFid);
        for (int deep = 4; deep > 0; deep--) {
            String room = "/trade-" + fviFid + "-" + deep;
            log.trace("push real data to room {}.", room);
            server.getRoomOperations(room).sendEvent("real", realPrice);
        }
    }

    private void pushEntrustData(String channel, String message) {
        // 推送挂单数据
        String[] params = channel.split(":");
        int fviFid = Integer.valueOf(params[0]);
        int type = Integer.valueOf(params[1]);
        int deep = Integer.valueOf(params[2]);
        String etype = type == 0 ? "entrust-buy" : "entrust-sell";

        // 限速，过滤重复内容，减少推送次数
        if (dataService.isNewMessage(channel, message)) {
            String data = dataService.getNewMessage(channel);
            if (data != null) {
                String room = "/trade-" + fviFid + "-" + deep;
                log.trace("push {} data to room {}.", etype, room);
                server.getRoomOperations(room).sendEvent(etype, dataService.getDepthEntrust(channel));
                sleep();
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushEntrustLog(String channel) {
        // 推送成交日志
        int fviFid = Integer.valueOf(channel.split(":")[2]);
        String entrustLog = dataService.getEntrustLog(fviFid);
        for (int deep = 4; deep > 0; deep--) {
            String room = "/trade-" + fviFid + "-" + deep;
            log.trace("push entrust-log data to room {}.", room);
            server.getRoomOperations(room).sendEvent("entrust-log", entrustLog);
        }
    }

    @Override
    public void close() {
        log.debug("close push server");
        try {
            if (messageCenterStarted) {
                messageCenter.close();
            }
        } catch (Exception e) {
        }
        try {
            if (serverStarted) {
                server.stop();
            }
        } catch (Exception e) {
        }
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
