package cn.bos.monitor.util;

import cn.bos.monitor.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSpiller {

    Logger logger = LoggerFactory.getLogger(LogSpiller.class);

    public void spill(String content, String path) {
        if (!path.contains("yyyymmdd")) {
            logger.error("Log spills error at " + path);
        } else {
            path.replace("yyyymmdd", TimestampFormatter.getNowTimestampString(Constant.PATH_FILENAME_FORMAT));

        }

    }
}
