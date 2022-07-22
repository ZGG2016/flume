# Ganglia Reporting

> Flume can also report these metrics to Ganglia 3 or Ganglia 3.1 metanodes. To report metrics to Ganglia, a flume agent must be started with this support. The Flume agent has to be started by passing in the following parameters as system properties prefixed by flume.monitoring., and can be specified in the flume-env.sh:

Flume 也支持将这些指标报告给 Ganglia 3 或 Ganglia 3.1。

要向 Ganglia 报告指标，必须使用这个支持启动 flume agent。Flume agent 必须通过传递以下参数作为以 `flume.monitoring.` 为前缀的系统属性来启动，可以在 flume-env.sh 中指定:

Property Name     |   Default        | 	Description
---|:---|:---
**type**	      |      –	         |  The component type name, has to be `ganglia`【组件类型的名称，必须是`ganglia】
**hosts**	      |      –	         |  Comma-separated list of hostname:port of Ganglia servers【逗号分隔的Ganglia服务器hostname:port的列表】
pollFrequency	  |      60	         |  Time, in seconds, between consecutive reporting to Ganglia server【在连续两次报告给Ganglia服务器的时间间隔，以秒为单位】
isGanglia3	      |     false	     |  Ganglia server version is 3. By default, Flume sends in Ganglia 3.1 format【Ganglia服务器版本是3。默认，flume以3.1格式发送】

> We can start Flume with Ganglia support as follows:

	$ bin/flume-ng agent --conf-file example.conf --name a1 -Dflume.monitoring.type=ganglia -Dflume.monitoring.hosts=com.example:1234,com.example2:5455