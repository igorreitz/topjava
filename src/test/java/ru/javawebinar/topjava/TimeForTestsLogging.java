package ru.javawebinar.topjava;

import java.util.concurrent.TimeUnit;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TimeForTestsLogging extends Stopwatch {

    public static final Logger log = LoggerFactory.getLogger("test_logger");
    public static StringBuilder sb = new StringBuilder();

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        log.debug("Test {} {}, spent {} milliseconds", testName, status, TimeUnit.NANOSECONDS.toMillis(nanos));
        sb.append(String.format("Test %s spent %d milliseconds",
                testName, TimeUnit.NANOSECONDS.toMillis(nanos)) + "\n");
    }

/*    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", nanos);
    }*/

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "finished", nanos);
    }
}