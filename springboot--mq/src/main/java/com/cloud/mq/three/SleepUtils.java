package com.cloud.mq.three;

/**
 * 线程等待
 */
public class SleepUtils {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
