package com.hunteron.crawler.dao.db;

import com.hunteron.crawler.configuration.Path;
import com.hunteron.crawler.configuration.RegularParser;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 读取配置文件数据库参数，连接数据及关闭数据库连接
 */
public class DBConfig {

	private static Logger logger = LogManager.getLogger(DBConfig.class.getName());
	private static HashMap<String, String> map = RegularParser.getXML(Path.MYSQL).get("mysql");

	private static ComboPooledDataSource dataSource;
	static {
	    try {
	        dataSource = new ComboPooledDataSource();
			dataSource.setJdbcUrl(map.get("url"));
			dataSource.setDriverClass(map.get("name"));
			dataSource.setUser(map.get("user"));
	        dataSource.setPassword(map.get("password"));
	        dataSource.setInitialPoolSize(10);
	        dataSource.setMinPoolSize(5);  
	        dataSource.setMaxPoolSize(50);  
	        dataSource.setMaxStatements(100);  
	        dataSource.setMaxIdleTime(60);  
	    } catch (Exception e) {
	    	logger.error(e.toString());
	    }
	}
	
	/** 
	 * 从连接池中获取数据源链接
	 * @return Connection 数据源链接 
	 */  
	public static Connection getConnection() {
	    Connection conn = null;  
	    if (dataSource != null) {
	        try {  
	            conn = dataSource.getConnection();
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				logger.error(e.toString());
	        }  
	    }  
	    return conn;  
	}
}

