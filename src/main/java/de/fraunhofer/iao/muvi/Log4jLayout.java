package de.fraunhofer.iao.muvi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A complete and informative, yet compact log4j Layout. Similar to the
 * {@link PatternLayout} but contains thread ids instead of thread names.
 * Example:
 * 
 * <pre>
 * 2010-07-05 10:38:49.234  [1] INFO:  FiktiveAbrechnung: Ermittle Referenzwerkstatt...
 * </pre>
 * 
 * Description:
 * <ol>
 * <li>Timestamp with {@link java.text.SimpleDateFormat}
 * "yyyy-MM-dd HH:mm:ss.SSS",
 * <li>Numeric thread ID with two digits (most programs have &lt;100 threads;
 * The thread id indicates thread creation order),
 * <li>Category (aka Level), right padded with space to length 6 (e.g. DEBUG,
 * INFO, NOTICE, WARN, ERROR),
 * <li>Logger name (class name),
 * <li>Actual log message,
 * <li>NDC (nested diagnostic context) in brackets if not null.
 * </ol>
 * 
 * @version 1.0.0
 * @author Bertram Frueh
 */
public class Log4jLayout extends Layout {

    private static final String LOG_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final DateFormat LOG_DATE_FORMAT = new SimpleDateFormat(LOG_DATE_FORMAT_STR);

    /**
     * Map log event to String.
     */
    public String format(LoggingEvent event) {

        String category = event.getLevel().toString();
        String msg = event.getRenderedMessage();
        String info = event.getNDC();
        String fileName = event.getLocationInformation().getFileName();
        String lineNumber = event.getLocationInformation().getLineNumber();

        Thread cthread = Thread.currentThread();
//        Assert.isEqual(event.getThreadName(), cthread.getName());
        String thread = "[" + Long.toString(cthread.getId()) + "]";
        if (thread.length() < 5)
            thread = " " + thread;

        int buflen = LOG_DATE_FORMAT_STR.length() + 1 + thread.length() + 1 + 6 + 1 + LINE_SEP_LEN;
        if (msg != null) {
            buflen += 2 + msg.length();
        }
        if (info != null) {
            buflen += 3 + info.length();
        }
        if (fileName != null) {
            buflen += fileName.length();
        }
        if (lineNumber != null) {
            buflen += lineNumber.length();
        }
        buflen += 3;

        StringBuffer sb = new StringBuffer(buflen);
        synchronized (LOG_DATE_FORMAT) {
            sb.append(LOG_DATE_FORMAT.format(event.timeStamp));
        }
        sb.append(' ').append(thread);
        sb.append(' ').append(category).append(":");
        for (int i = 6 - category.length(); --i >= 0;)
            sb.append(' ');
        // sb.append(name);
        sb.append("(");
        sb.append(fileName);
        sb.append(":");
        sb.append(lineNumber);
        sb.append(")");

        if (msg != null)
            sb.append(": ").append(msg);
        if (info != null)
            sb.append(" [").append(info).append(']');
        sb.append(LINE_SEP);

//        Assert.isEqual(sb.length(), buflen);

        return sb.toString();
    }

    /**
     * Return true so that the Appender prints the stack trace.
     */
    public boolean ignoresThrowable() {

        return true;
    }

    /**
     * Does nothing.
     */
    public void activateOptions() {

    }
}
