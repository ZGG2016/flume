import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
/**
 * Syslog TCP Source 发送数据代码
 * 配置文件为：jobs/flume-syslogTCP-source.conf
 */

public class SyslogTCPSource {

    public static void main(String[] args) throws IOException {
        String line="aaaaaaaaaaaaaaaaaaaaaa!";
        SyslogTCPSource sts = new SyslogTCPSource();
        sts.dataToFlume(line);
    }

    public void dataToFlume(String line) throws IOException {

        Socket s = new Socket("zgg", 44444);
        OutputStream out = s.getOutputStream();

        out.write((line + "\n").getBytes());
        out.flush();
        out.close();
        s.close();

    }
}
