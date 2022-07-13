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
    private String driver;
    private String url;
    private String user;
    private String password;

    public EntityFetcher() {
        this.driver = PropertiesUtil.getProperty("sys.config.mysql.driver");
        this.url = PropertiesUtil.getProperty("sys.config.mysql.url");
        this.user = PropertiesUtil.getProperty("sys.config.mysql.user");
        this.password = PropertiesUtil.getProperty("sys.config.mysql.password");

        this.conn = Connector.getConnection(this.driver, this.user, this.password, this.url);
    }

    private void revive() throws SQLException {
        if (this.conn.isClosed()) {
            this.conn = Connector.getConnection(this.driver, this.user, this.password, this.url);
        }
    }

    public JSONArray getList() throws SQLException {
        revive();
        return ResultSet2JSON.convert(Connector.getResultSet(this.conn, "select * from list"));
    }

    public JSONArray getRule() throws SQLException {
        revive();
        return ResultSet2JSON.convert(Connector.getResultSet(this.conn, "select * from rule where enable=1"));
    }

    public JSONArray getConfig() throws SQLException {
        revive();
        return ResultSet2JSON.convert(Connector.getResultSet(this.conn, "select * from config"));
    }
}
