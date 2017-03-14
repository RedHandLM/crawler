package com.hunteron.crawler.dao.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SqlSession {

    private static Logger logger = LogManager.getLogger(SqlSession.class.getName());
    private static Connection conn = DBConfig.getConnection();
    private static QueryRunner qr = new QueryRunner();//实例化查询接口

    public static boolean insert(String sql,Object[] params) {
        try {
            if(qr.update(conn,sql, params) == 1){
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            logger.error("[数据库Insert错误:][" + e.toString() + "]");
        }
        return false;
    }

    public static List<Object[]> select(String sql, Object[] params) {
        try {
            return qr.query(conn,sql,new ArrayListHandler(),params);
        } catch (SQLException e) {
            logger.error("[数据库Select错误:][" + e.toString() + "]");
        }
        return null;
    }

    public static List<Object[]> select(String sql) {
        try {
            return qr.query(conn,sql,new ArrayListHandler());
        } catch (SQLException e) {
            logger.error("[数据库Select错误:][" + e.toString() + "]");
        }
        return null;
    }
}
