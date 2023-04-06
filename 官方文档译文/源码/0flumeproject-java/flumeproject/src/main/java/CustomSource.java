import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

import java.util.HashMap;
/*
* 自定义Source的简单应用
*
* */
public class CustomSource extends AbstractSource implements Configurable, PollableSource {

    public String url = null;

    @Override
    public Status process() throws EventDeliveryException {

        Status status = null;

        try{
            HashMap<String,String> headers = new HashMap<>();
            headers.put("appname","meituan");

            SimpleEvent event = new SimpleEvent();

            event.setHeaders(headers);
            event.setBody(url.getBytes());

            getChannelProcessor().processEvent(event);

            status = Status.READY;

        }catch (Throwable t) {
            status = Status.BACKOFF;

            // re-throw all Errors
            if (t instanceof Error) {
                throw (Error)t;
            }
        }
        return status;

    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    @Override
    public void configure(Context context) {
        url = context.getString("url","www.defaultvalue.com");
    }
}
