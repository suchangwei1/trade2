package com.trade.deal.data;

import com.trade.deal.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTemplate {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private DataSource dataSource;

    private boolean showSql = false;

    public JdbcTemplate() {
    }

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        Config config = Config.getInstance();
        if (config.getProperty("showSql") != null) {
            log.debug("init jdbcTemplate set showSql = true");
            showSql = true;
        } else {
            log.debug("init jdbcTemplate set showSql = false");
        }
    }

    public <T> T queryForObject(String sql, Class<T> clazz, Object... args) {
        List<T> list = queryForObject(sql, new SingleColumnRowMapper<>(clazz), args);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public Map<String, Object> queryForMap(String sql, Object... args) {
        List<Map<String, Object>> list = queryForObject(sql, new ColumnMapRowMapper(), args);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public <T> List<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        List<T> ret = new ArrayList<>();
        log(sql, args);
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                T row = rowMapper.mapRow(rs);
                ret.add(row);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    void log(String sql, Object... args) {
        if (showSql) {
            for (int i = 0; i < args.length; i++) {
                sql = sql.replaceFirst("[?]", "'" + args[i].toString() + "'");
            }
            log.debug(sql);
        }
    }

}
