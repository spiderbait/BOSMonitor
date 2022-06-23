package cn.bos.monitor.engine;

import cn.bos.monitor.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("BOS Monitor starting...");
        try {
            while (true) {
                Thread.sleep(Constant.POLLING_INTERVAL_IN_MS);
            }
        } catch (InterruptedException e) {
            logger.info("BOS Monitor terminated.");
        }
    }
}
