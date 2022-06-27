package cn.bos.monitor.pool;

import cn.bos.monitor.util.Connector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class ConnectionPool {
    private HashMap<String, Connection> connectionMapping;
    public ConnectionPool() {
        this.connectionMapping = new HashMap<>();
    }

    public void create(String key, String driver, String user, String password, String url) {
        this.connectionMapping.put(key, Connector.getConnection(driver, user, password, url));
    }

    public Connection getByKey(String key) {
        return this.connectionMapping.get(key);
    }

    public void destroyByKey(String key) throws SQLException {
        this.connectionMapping.get(key).close();
        this.connectionMapping.remove(key);
    }

}
