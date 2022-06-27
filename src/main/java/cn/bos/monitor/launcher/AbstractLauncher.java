package cn.bos.monitor.launcher;

import cn.bos.monitor.constant.Constant;
import cn.bos.monitor.util.TimestampFormatter;

import java.io.BufferedWriter;
import java.io.IOException;

public class AbstractLauncher implements Runnable {

    protected String title;
//    protected JdbcConfig jdbcConfig;
//    protected Rule rule;
//    protected BatchInfoFetcher bif;

    @Override
    public void run() {
    }

    public void writeLine(StringBuilder sb, BufferedWriter writer, String line) throws IOException {
        String aline = TimestampFormatter.getNowTimestampString(Constant.LOG_TS_FORMAT);
        aline = aline + " " + line + "\n";
        writer.write(aline);
        writer.flush();
        sb.append(aline);
    }

    public void writeContent(StringBuilder sb, BufferedWriter writer, String content) throws IOException {
        if (content == null) {
            return;
        }
        writer.write(content);
        writer.flush();
        sb.append(content);
    }
}
