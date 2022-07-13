package cn.bos.monitor.pool;

import cn.bos.monitor.Main;
import cn.bos.monitor.entity.JDBCString;
import cn.bos.monitor.entity.JdbcConfig;
import cn.bos.monitor.util.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class ConnectionPool {
    Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private HashMap<String, Connection> connectionMapping;
    private HashMap<String, JDBCString> configMapping;
    public ConnectionPool() {
        this.configMapping = new HashMap<>();
        this.connectionMapping = new HashMap<>();
    }

    public void create(String key, String driver, String user, String password, String url) {
        this.connectionMapping.put(key, Connector.getConnection(driver, user, password, url));
        this.configMapping.put(key, new JDBCString(driver, user, password, url));
    }

    public Connection getByKey(String key) throws SQLException {
        if (this.connectionMapping.get(key).isClosed()) {
            this.connectionMapping.put(key, Connector.getConnection(this.configMapping.get(key).getDriver(),
                    this.configMapping.get(key).getUser(),
                    this.configMapping.get(key).getPassword(),
                    this.configMapping.get(key).getUrl()));
            logger.info("Connection revived for url: " + this.configMapping.get(key).getUrl());
        }
        return this.connectionMapping.get(key);
    }

    public void destroyByKey(String key) throws SQLException {
        this.connectionMapping.get(key).close();
        this.connectionMapping.remove(key);
        this.configMapping.remove(key);
    }

}
