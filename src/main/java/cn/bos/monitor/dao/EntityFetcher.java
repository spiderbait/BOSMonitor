package cn.bos.monitor.dao;

import cn.bos.monitor.util.Connector;
import cn.bos.monitor.util.PropertiesUtil;
import cn.bos.monitor.util.ResultSet2JSON;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class EntityFetcher {

    private Connection conn;

    public EntityFetcher() {
        String driver = PropertiesUtil.getProperty("sys.config.mysql.driver");
        String url = PropertiesUtil.getProperty("sys.config.mysql.url");
        String user = PropertiesUtil.getProperty("sys.config.mysql.user");
        String password = PropertiesUtil.getProperty("sys.config.mysql.password");

        this.conn = Connector.getConnection(driver, user, password, url);
    }

    public JSONArray getList() throws SQLException {
        return ResultSet2JSON.convert(Connector.getResultSet(this.conn, "select * from list"));
    }

    public JSONArray getRule() throws SQLException {
        return ResultSet2JSON.convert(Connector.getResultSet(this.conn, "select * from rule where enable=1"));
    }

    public JSONArray getConfig() throws SQLException {
        return ResultSet2JSON.convert(Connector.getResultSet(this.conn, "select * from config"));
    }
}
