import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {

        // 每个事件包含的字节数
        Map<String, String> headers = event.getHeaders();
        byte[] bodys = event.getBody();
        headers.put("num of bytes",String.valueOf(bodys.length));

        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        ArrayList<Event> interceptors = new ArrayList<>();
        for (Event event : list) {
            Event intercept1 = intercept(event);
            if (intercept1 != null){
                interceptors.add(intercept1);
            }
        }
        return interceptors;
    }

    @Override
    public void close() {

    }
    public static class Builder implements Interceptor.Builder{
        @Override
        public Interceptor build() {
            return new CustomInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
