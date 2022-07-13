package cn.bos.monitor.launcher;

import cn.bos.monitor.constant.Constant;
import cn.bos.monitor.pool.ConnectionPool;
import cn.bos.monitor.util.MailSender;
import cn.bos.monitor.util.TimestampFormatter;
import cn.bos.monitor.util.ResultSet2JSON;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Statement;
import java.util.Calendar;

public class CronLauncher implements Runnable{
    Logger logger = LoggerFactory.getLogger(CronLauncher.class);

    ConnectionPool pool;
    JSONObject o;

    public CronLauncher(ConnectionPool pool, JSONObject o) {
        this.pool = pool;
        this.o = o;
    }

    public void logging(ConnectionPool pool, JSONObject o) {
        int startHour = Integer.parseInt(o.getString("span").split(":")[0]);
        int startMinute = Integer.parseInt(o.getString("span").split(":")[1]);
        String path = o.getString("path");
        try {
            if (startHour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    && startMinute == Calendar.getInstance().get(Calendar.MINUTE)) {
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(
                                path.replace("yyyymmdd",
                                        TimestampFormatter.getNowTimestampString(Constant.PATH_FILENAME_FORMAT))));
                writer.write(TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT) + " START\n");
                writer.flush();
                logger.info("CronLauncher for task started: " + o);
                Statement stmt = pool.getByKey(o.getString("mid")).createStatement();
                JSONArray result = ResultSet2JSON.convert(stmt.executeQuery(o.getString("sql")));
                stmt.close();
                if (result.length() == 0) { // OK branch
                    logger.info("Task OK for " + o);
                    writer.write(TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT)
                            + " " + o.getString("end_keyword") + "\n");
                    writer.flush();
                } else { // ERROR branch
                    StringBuilder sb = new StringBuilder();
                    if (((JSONObject) result.get(0)).has("mprompt")) {
                        sb.append(((JSONObject) result.get(0)).getString("mprompt"));
                        sb.append("\n");
                    }
                    for (int i = 0; i < result.length(); i++) {
                        sb.append(((JSONObject) result.get(i)).getString("mresult"));
                        if (i != result.length() - 1) {
                            sb.append("\n");
                        }
                    }
                    writer.write(TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT) + " ERROR\n");
                    writer.write(sb.toString());
                    writer.flush();
                    MailSender.send(o.getString("receivers").split(","),
                            o.getString("name"),
                            sb.toString());
                }
                writer.close();
                logger.info("CronLauncher task ended for: " + o);
            }
//            } else {
//                logger.info("Cron time not reach for " + o);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        logging(this.pool, this.o);
    }
}
