# flume-parseAsFlumeEvent属性

> Expecting Avro datums with FlumeEvent schema in the channel. This should be true if Flume source is writing to the channel and false if other producers are writing into the topic that the channel is using. Flume source messages to Kafka can be parsed outside of Flume by using org.apache.flume.source.avro.AvroFlumeEvent provided by the flume-ng-sdk artifact

期待在 channel 中带有 FlumeEvent 模式的 Avro 数据。

如果 Flume source 正在向 channel 写入数据，则为 true

如果其他生产者正在向 channel 正在使用的 topic 写入数据，则为 false

发送到 Kafka 的 Flume source 消息可以通过使用 flume-ng-sdk 工件提供的 org.apache.flume.source.avro.AvroFlumeEvent 在 Flume 之外解析。

## 测试1

脚本

```
# Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = netcat
a1.sources.r1.bind = localhost
a1.sources.r1.port = 44444

# Describe the sink
a1.sinks.k1.type = logger

# Use a channel which buffers events in memory
a1.channels.c1.type = org.apache.flume.channel.kafka.KafkaChannel
a1.channels.c1.kafka.bootstrap.servers = bigdata101:9092
a1.channels.c1.kafka.topic = kafkachannel
a1.channels.c1.kafka.consumer.group.id = flumekafkachannel.g.id
a1.channels.c1.parseAsFlumeEvent = false

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
```


先把脚本执行起来，再启动一个生产者和一个telnet命令

```sh
root@bigdata101:/opt/flume-1.7.0# bin/flume-ng agent --conf conf --conf-file conf/test.conf --name a1 -Dflume.root.logger=INFO,console


root@bigdata101:/opt/kafka_2.12-3.2.1# bin/kafka-console-producer.sh --broker-list bigdata101:9092 --topic kafkachannel     

root@bigdata101:~# telnet localhost 44444
```

同时使用生产者和 telnet 命令生产数据，那么当 parseAsFlumeEvent 属性是默认值true时，会报如下错误：

```
[WARN - org.apache.flume.channel.kafka.KafkaChannel$KafkaTransaction.doTake(KafkaChannel.java:520)] Error while getting events from Kafka. This is usually caused by trying to read a non-flume event. Ensure the setting for parseAsFlumeEvent is correct
java.lang.IndexOutOfBoundsException............
```

如果设置为 false，那么就可以正常输出：

```
.............
2022-10-04 02:50:14,604 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 63 63 63 63 63 63 63 63 63 63 63 63 63 63 63 63 cccccccccccccccc }
2022-10-04 02:50:26,636 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 64 64 64 64 64 64 64 64 64 64 64 64 64 64 64 64 dddddddddddddddd }
2022-10-04 02:50:34,161 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 65 65 65 65 65 65 65 65 65 65 65 65 65 65 65 65 eeeeeeeeeeeeeeee }
.............
```

## 测试2

脚本

```
a1.sources=r1
a1.channels=c1

# configure source

a1.sources.r1.type = TAILDIR
a1.sources.r1.positionFile = /opt/flume-1.7.0/positionfile/log_position2.json
a1.sources.r1.filegroups = f1
a1.sources.r1.filegroups.f1 = /root/data/app-2022-09-20.log
a1.sources.r1.fileHeader = true
a1.sources.r1.channels = c1

# configure channel
a1.channels.c1.type = org.apache.flume.channel.kafka.KafkaChannel
a1.channels.c1.kafka.bootstrap.servers = bigdata101:9092
a1.channels.c1.kafka.topic = kafkachanneltest
a1.channels.c1.parseAsFlumeEvent = false
a1.channels.c1.kafka.consumer.group.id = flumekafkachannel.g.id
```

当 parseAsFlumeEvent 属性是默认值true时，输出是这种形式：

```
............
file:/root/data/app-2022-09-20.logaction":"1","ar":"MX","ba":"Sumsung","detail":"201","en":"start","entry":"4","extend1":"","g":"PF6ZKNG6@gmail.com","hw":"640*960","l":"pt","la":"-23.5","ln":"-60.2","loading_time":"13","md":"sumsung-11","mid":"993","nw":"WIFI","open_ad_type":"2","os":"8.1.4","sr":"G","sv":"V2.7.0","t":"1663578405293","uid":"993","vc":"7","vn":"1.1.0"}
file:/root/data/app-2022-09-20.logaction":"1","ar":"MX","ba":"HTC","detail":"","en":"start","entry":"2","extend1":"","g":"PMC5U67C@gmail.com","hw":"1080*1920","l":"en","la":"15.9","ln":"-47.0","loading_time":"15","md":"HTC-17","mid":"994","nw":"WIFI","open_ad_type":"2","os":"8.1.0","sr":"O","sv":"V2.5.3","t":"1663623650267","uid":"994","vc":"9","vn":"1.0.2"}
.............
```

注意这里 `file:/root/data/app-2022-09-20.logaction`，后面的 `action` 是日志类型，正常情况下，不应该包含前面的部分。

此时，可以将 parseAsFlumeEvent 设置为 false

```
{"action":"1","ar":"MX","ba":"Huawei","detail":"","en":"start","entry":"4","extend1":"","g":"T0UTO8T8@gmail.com","hw":"1080*1920","l":"pt","la":"-0.7000000000000002","ln":"-39.5","loading_time":"7","md":"Huawei-9","mid":"1284","nw":"3G","open_ad_type":"2","os":"8.2.4","sr":"F","sv":"V2.3.9","t":"1663660786105","uid":"1284","vc":"16","vn":"1.3.8"}
{"action":"1","ar":"MX","ba":"HTC","detail":"542","en":"start","entry":"5","extend1":"","g":"9Y4Y8239@gmail.com","hw":"640*1136","l":"pt","la":"3.8","ln":"-36.6","loading_time":"18","md":"HTC-19","mid":"1285","nw":"3G","open_ad_type":"1","os":"8.2.6","sr":"R","sv":"V2.1.2","t":"1663603404641","uid":"1285","vc":"11","vn":"1.2.0"}
```