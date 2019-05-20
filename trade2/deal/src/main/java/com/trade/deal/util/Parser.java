package com.trade.deal.util;

import java.sql.ResultSet;

public interface Parser {

    <T> T parse(ResultSet rs, Class<T> clazz) throws Exception;

}
