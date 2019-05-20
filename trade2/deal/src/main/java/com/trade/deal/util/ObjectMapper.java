package com.trade.deal.util;

import com.trade.deal.model.FentrustData;
import com.trade.deal.model.FentrustlogData;
import com.trade.deal.util.Parser;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapper {

    private Map<Class, Parser> mappers = new HashMap<>();

    public ObjectMapper() {
        mappers.put(FentrustData.class, entrustMapper());
        mappers.put(FentrustlogData.class, entrustLogMapper());
    }

    public Parser entrustMapper() {
        return new Parser() {
            @Override
            public <T> T parse(ResultSet rs, Class<T> clazz) throws Exception {
                FentrustData fentrust = new FentrustData();

                fentrust.setFid(rs.getInt("fid"));
                fentrust.setFuid(rs.getInt("fUs_fId"));
                fentrust.setFviFid(rs.getInt("fVi_fId"));
                fentrust.setFentrustType(rs.getInt("fEntrustType"));
                fentrust.setFprize(rs.getDouble("fPrize"));
                fentrust.setFamount(rs.getDouble("fAmount"));
                fentrust.setFsuccessAmount(rs.getDouble("fsuccessAmount"));
                fentrust.setFcount(rs.getDouble("fCount"));
                fentrust.setFleftCount(rs.getDouble("fleftCount"));
                fentrust.setFstatus(rs.getInt("fstatus"));
                fentrust.setFfees(rs.getDouble("ffees"));
                fentrust.setFleftfees(rs.getDouble("fleftfees"));
                fentrust.setRobotStatus(rs.getInt("robotStatus"));
                fentrust.setFcreateTime(rs.getTimestamp("fCreateTime"));
                try {
                    fentrust.setFneedFee(rs.getBoolean("fneedfee"));
//                    fentrust.setFlevel(rs.getInt("flevel"));
                } catch (Exception e) {
                }
                return (T) fentrust;
            }
        };

    }
    public Parser entrustLogMapper() {
        return new Parser() {
            @Override
            public <T> T parse(ResultSet rs, Class<T> clazz) throws Exception {
                FentrustlogData fentrust = new FentrustlogData();

                fentrust.setFid(rs.getInt("fid"));
                fentrust.setFviFid(rs.getInt("FVI_type"));
                fentrust.setfEntrustType(rs.getInt("fEntrustType"));
                fentrust.setFprize(rs.getDouble("fPrize"));
                fentrust.setFamount(rs.getDouble("fAmount"));
                fentrust.setFcount(rs.getDouble("fCount"));
                fentrust.setIsactive(rs.getBoolean("isactive"));
                fentrust.setFcreateTime(rs.getTimestamp("fCreateTime"));

                return (T) fentrust;
            }
        };

    }

    public <T> T parse(ResultSet rs,  Class<T> clazz) throws Exception {
        Parser parser = mappers.get(clazz);
        return parser.parse(rs, clazz);
    }

}
