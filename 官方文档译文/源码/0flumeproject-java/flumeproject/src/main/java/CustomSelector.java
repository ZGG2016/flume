import org.apache.flume.Channel;
import org.apache.flume.ChannelSelector;
import org.apache.flume.Context;
import org.apache.flume.Event;

import java.util.List;
/*
* 定义Selector的简单应用
*    根据事件主体的前缀分流
*
* */
public class CustomSelector implements ChannelSelector {

    @Override
    public void setChannels(List<Channel> channels) {

    }

    @Override
    public List<Channel> getRequiredChannels(Event event) {
        return null;
    }

    @Override
    public List<Channel> getOptionalChannels(Event event) {
        return null;
    }

    @Override
    public List<Channel> getAllChannels() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void configure(Context context) {

    }
}
