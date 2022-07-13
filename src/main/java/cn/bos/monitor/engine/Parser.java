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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Parser {
    ConnectionPool pool;
    EntityFetcher fetcher;
    ThreadPoolExecutor executor;
    Logger logger = LoggerFactory.getLogger(Parser.class);

    public Parser() {
        this.pool = new ConnectionPool();
        this.fetcher = new EntityFetcher();
        this.executor = new ThreadPoolExecutor(20, 100, 10, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());
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
            for (int i=0; i<rules.length(); i++) {
                JSONObject o = (JSONObject) rules.get(i);
                switch (o.getString("type")) {
                    case "span":
                        executor.execute(new SpanLauncher(this.pool, o));
                        break;
                    case "cron":
                        executor.execute(new CronLauncher(this.pool, o));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
