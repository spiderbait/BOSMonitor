package cn.bos.monitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class TimePrison {
    static Logger logger = LoggerFactory.getLogger(TimePrison.class);

    public static void trial() {
        logger.info("Entering time prison...");
        while(true) {
            if (Calendar.getInstance().get(Calendar.SECOND) == 0) {
                break;
            }

        }
        logger.info("Escaped from prison.");
    }

    public static void main(String[] args) {
        TimePrison.trial();
    }


}
