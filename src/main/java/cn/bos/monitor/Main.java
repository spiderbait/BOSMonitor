package cn.bos.monitor;

import cn.bos.monitor.constant.Constant;
import cn.bos.monitor.engine.Parser;
import cn.bos.monitor.util.TimePrison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("BOS Monitor starting...");
        Parser parser = new Parser();
        TimePrison.trial();
        try {
            while (true) {
                parser.parseRule();
                Thread.sleep(Constant.POLLING_INTERVAL_IN_MS);
            }
        } catch (InterruptedException e) {
            logger.info("BOS Monitor terminated.");
        }
    }
}
