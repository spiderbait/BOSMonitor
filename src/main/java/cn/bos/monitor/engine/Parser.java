package cn.bos.monitor.engine;

import cn.bos.monitor.dao.EntityFetcher;
import cn.bos.monitor.launcher.CronLauncher;
import cn.bos.monitor.launcher.SpanLauncher;
import cn.bos.monitor.pool.ConnectionPool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;

public class Parser {
    ConnectionPool pool;
    EntityFetcher fetcher;
    Logger logger = LoggerFactory.getLogger(Parser.class);


    public Parser() {
        this.pool = new ConnectionPool();
        this.fetcher = new EntityFetcher();
        establishDBConnections();
    }

    private void establishDBConnections() {
        try {
            JSONArray config = this.fetcher.getConfig();
            for (int i=0; i<config.length(); i++) {
                JSONObject o = (JSONObject) config.get(i);
                this.pool.create(o.getString("mid"), o.getString("driver"), o.getString("user"), o.getString("password"), o.getString("url"));
                logger.info("Pool - created connection for "
                        + o.getString("mid") + ", "
                        + o.getString("driver") + ", "
                        + o.getString("user") + ", "
                        + o.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void parseRule() {
        try {
            JSONArray rules = this.fetcher.getRule();
            CronLauncher cronLauncher = new CronLauncher();
            SpanLauncher spanLauncher = new SpanLauncher();
            for (int i=0; i<rules.length(); i++) {
                JSONObject o = (JSONObject) rules.get(i);
                if ("span".equals(o.getString("type").toLowerCase())) {
                    spanLauncher.logging(this.pool, o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
