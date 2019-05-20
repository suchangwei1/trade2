package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UtilsDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(UtilsDAO.class);
}
