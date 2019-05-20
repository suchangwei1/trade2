package com.trade.jobs.sms;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemArgsService {

    @Resource
    private JdbcTemplate jdbc;

    public Map<String, String> getSystemArgs(Object... keys) {
        Map<String, String> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select fkey, fvalue FROM fsystemargs WHERE fkey in (");
        for (int i = 0; i < keys.length; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        List<Map<String, Object>> list = jdbc.queryForList(sql.toString(), keys);
        list.forEach(item -> {
            map.put(item.get("fkey").toString(), item.get("fvalue").toString());
        });
        return map;
    }

}
