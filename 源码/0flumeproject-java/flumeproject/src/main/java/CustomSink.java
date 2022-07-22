import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 自定义Sink的简单应用
 *
 * */
public class CustomSink extends AbstractSink implements Configurable {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractSink.class);

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;
        Channel ch = getChannel();
        Transaction txn = ch.getTransaction();
        txn.begin();

        try {
            Event event = ch.take();
            //处理事件，即将其拆分成几部分，分别在日志上输出
            String eventstr = new String(event.getBody());
            String[] ss = eventstr.split(",");
            for(String s:ss){
                LOG.info(s);
            }
            txn.commit();
            status = Status.READY;
        } catch (Throwable t) {
            txn.rollback();

            status = Status.BACKOFF;

            if (t instanceof Error) {
                throw (Error)t;
            }
        }finally {
            txn.close();
        }
        return status;
    }

    @Override
    public void configure(Context context) {
    }
}
