package cn.bos.monitor.launcher;

import cn.bos.monitor.constant.Constant;
import cn.bos.monitor.pool.ConnectionPool;
import cn.bos.monitor.util.MailSender;
import cn.bos.monitor.util.ResultSet2JSON;
import cn.bos.monitor.util.TimestampFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Statement;
import java.util.Calendar;

public class SpanLauncher implements Runnable{
    Logger logger = LoggerFactory.getLogger(CronLauncher.class);

    ConnectionPool pool;
    JSONObject o;

    public SpanLauncher(ConnectionPool pool, JSONObject o) {
        this.pool = pool;
        this.o = o;
    }

    public void logging(ConnectionPool pool, JSONObject o) {

        try {
            int startHour = Integer.parseInt(o.getString("span").split("->")[0].trim().split(":")[0]);
            int startMinute = Integer.parseInt(o.getString("span").split("->")[0].trim().split(":")[1]);
            int endHour = Integer.parseInt(o.getString("span").split("->")[1].trim().split(":")[0]);
            int endMinute = Integer.parseInt(o.getString("span").split("->")[1].trim().split(":")[1]);
            String path = o.getString("path");

            if (startHour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    && startMinute == Calendar.getInstance().get(Calendar.MINUTE)) {
                logger.info("Span task started for " + o);
                Statement stmt = pool.getByKey(o.getString("mid")).createStatement();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(
                                        path.replace("yyyymmdd",
                                        TimestampFormatter.getNowTimestampString(Constant.PATH_FILENAME_FORMAT)), true)));
                JSONArray result;
                writer.write(TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT)+ " START\n");
                writer.flush();
                while (endHour != Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                        || endMinute != Calendar.getInstance().get(Calendar.MINUTE)) {
                    result = ResultSet2JSON.convert(stmt.executeQuery(o.getString("sql")));
                    if (result.length() == 0) {
                        writer.write(TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT) + " OK\n");
                        writer.flush();
                    } else {

                        StringBuilder sb = new StringBuilder();
                        if (((JSONObject)result.get(0)).has("mprompt")) {
                            sb.append(((JSONObject)result.get(0)).getString("mprompt"));
                            sb.append("\n");
                        }
                        for (int i = 0; i < result.length(); i++) {
                            sb.append(((JSONObject) result.get(i)).getString("mresult"));
                            if (i != result.length() - 1) {
                                sb.append("\n");
                            }
                        }
                        writer.write(TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT)+ " ERROR\n");
                        writer.write(sb.toString());
                        writer.write("\n");
                        MailSender.send(o.getString("receivers").split(","),
                                o.getString("name"),
                                sb.toString());
                        writer.flush();
                        break;

                    }
                    Thread.sleep(60000);
                }
                writer.write(TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT)
                        + " " + o.getString("end_keyword") + "\n");
                writer.flush();
                logger.info("Span task ended for " + o);
            }
//            else {
//                logger.info("Span task not reach time: " + o);
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
