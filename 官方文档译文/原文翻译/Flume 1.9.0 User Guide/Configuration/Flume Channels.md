# Flume Channels

[TOC]

> Channels are the repositories where the events are staged on a agent. Source adds the events and Sink removes it.

Channels 是存储库，在一个 agent 上，events 暂存其中。Source 往里添加 events，而 Sink 从中删除。

## 1、Memory Channel

> The events are stored in an in-memory queue with configurable max size. It’s ideal for flows that need higher throughput and are prepared to lose the staged data in the event of a agent failures. Required properties are in bold.

events 存储在一个具有可配置的最大大小的内存队列中。对于需要更高吞吐量，并准备在 agent 故障时丢失分段数据的流来说，它非常理想。

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name, needs to be `memory`【组件类型名称，必须是`memory`】
capacity	     |     100	   |    The maximum number of events stored in the channel【存储在channel中的最大事件数量】
transactionCapacity|   100	   |    The maximum number of events the channel will take from a source or give to a sink per transaction【在一次事务中，channel能从一个source中获取事件的最大数量，或给到一个sink的事件的最大数量】
keep-alive	     |      3	   |    Timeout in seconds for adding or removing an event【添加或删除一个事件的超时时长。以秒为单位。】
byteCapacityBufferPercentage| 20	|  Defines the percent of buffer between byteCapacity and the estimated total size of all events in the channel, to account for data in headers. See below.【byteCapacity和channel中所有事件的总大小之间的缓存百分比，以考虑headers中的数据。】
byteCapacity	 |see description |	Maximum total bytes of memory allowed as a sum of all events in this channel. The implementation only counts the Event body, which is the reason for providing the byteCapacityBufferPercentage configuration parameter as well. Defaults to a computed value equal to 80% of the maximum memory available to the JVM (i.e. 80% of the -Xmx value passed on the command line). Note that if you have multiple memory channels on a single JVM, and they happen to hold the same physical events (i.e. if you are using a replicating channel selector from a single source) then those event sizes may be double-counted for channel byteCapacity purposes. Setting this value to 0 will cause this value to fall back to a hard internal limit of about 200 GB.【该channel中所有事件的总和所允许的最大内存字节数。该实现只计算事件body，这也是提供byteCapacityBufferPercentage配置参数的原因。默认计算值等于JVM可用最大内存的80%(即通过命令行传递的-Xmx值的80%)。请注意，如果在单个JVM上有多个内存channels，并且它们碰巧保存相同的物理事件(例如，如果使用来自单个source的replicating channel selector)，那么出于channel byteCapacity的目的，这些事件大小可能会被计算两次。将该值设置为0将导致该值回落到大约200gb的硬内部限制。（所有事件的body的总和所占用的最大内存字节数(jvm)）】

> Example for agent named a1:

	a1.channels = c1
	a1.channels.c1.type = memory
	a1.channels.c1.capacity = 10000
	a1.channels.c1.transactionCapacity = 10000
	a1.channels.c1.byteCapacityBufferPercentage = 20
	a1.channels.c1.byteCapacity = 800000

## 2、JDBC Channel

> The events are stored in a persistent storage that’s backed by a database. The JDBC channel currently supports embedded Derby. This is a durable channel that’s ideal for flows where recoverability is important. Required properties are in bold.

events 存储在由数据库支持的持久存储中。

JDBC channel 目前支持嵌入式 Derby。这是一个持久的 channel，这对可恢复性很重要的流来说是理想的。

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name, needs to be `jdbc`【组件类型名称，必须是`jdbc`】
db.type	         |     DERBY   |    Database vendor, needs to be `DERBY`.
driver.class	 | org.apache.derby.jdbc.EmbeddedDriver	|  Class for vendor’s JDBC driver
driver.url	     |(constructed from other properties)	|  JDBC connection URL
db.username	     |     “sa”    |    User id for db connection【数据库连接用户名】
db.password 	 |      –	   |    password for db connection【数据库连接密码】
connection.properties.file|	–  |    JDBC Connection property file path【jdbc连接属性文件路径】
create.schema	 |     true	   |    If true, then creates db schema if not there【如果是true，没有schema的话，就创建一个】
create.index	 |     true	   |    Create indexes to speed up lookups【创建索引加速查询】
create.foreignkey|	   true	   |
transaction.isolation| “READ_COMMITTED”	| Isolation level for db session READ_UNCOMMITTED, READ_COMMITTED, SERIALIZABLE, REPEATABLE_READ【数据库会话的隔离级别】
maximum.connections|	10	   |    Max connections allowed to db【连接到数据库的最大连接数】
maximum.capacity |      0 (unlimited) |	Max number of events in the channel【channel中的最大事件数量】
sysprop.*	 	 |             |    DB Vendor specific properties
sysprop.user.home|	 	       |    Home path to store embedded Derby database

> Example for agent named a1:

	a1.channels = c1
	a1.channels.c1.type = jdbc

## 3、Kafka Channel

> The events are stored in a Kafka cluster (must be installed separately). Kafka provides high availability and replication, so in case an agent or a kafka broker crashes, the events are immediately available to other sinks

events 存储在 Kafka 集群中(必须单独安装)。

Kafka 提供高可用性和副本策略，所以如果一个 agent 或 Kafka broker 崩溃了，其他 sinks 可以立即获得 events。

> The Kafka channel can be used for multiple scenarios:

Kafka channel 可以用于多种场景:

- 和 Flume source、sink 一起使用：为 events 提供了一个可靠和高可用的 channel

- 和 Flume source 和 interceptor 一起使用，但没有 sink：允许将 Flume events 写入 Kafka topic，供其他应用程序使用

- 和 Flume sink 一起使用：这是一种以低延迟，容错的方式将 events 从 Kafka 发送到 Flume sink，如 HDFS、HBase 或 Solr

> With Flume source and sink - it provides a reliable and highly available channel for events

> With Flume source and interceptor but no sink - it allows writing Flume events into a Kafka topic, for use by other apps

> With Flume sink, but no source - it is a low-latency, fault tolerant way to send events from Kafka to Flume sinks such as HDFS, HBase or Solr

> This currently supports Kafka server releases 0.10.1.0 or higher. Testing was done up to 2.0.1 that was the highest avilable version at the time of the release.

目前支持 Kafka 服务器版本 0.10.1.0 或更高版本。测试一直进行到 2.0.1，这是发行时的最高可用版本。

> The configuration parameters are organized as such:

配置参数是这样组织的:

- 与 channel 相关的配置值通常在 channel 配置级别应用，例如：`a1.channel.k1.type =`

- 与 Kafka 相关的配置值或 channel 如何操作的前缀是 “Kafka.”，(这类似 CommonClient 配置)，例如：`a1.channels.k1.kafka.topic`和 `a1.channels.k1.kafka.bootstrap.servers`。这与 hdfs sink 的操作方式没有什么不同。

- 特定于生产者/消费者的属性以 `kafka.producer` 或 `kafka.consumer` 作为前缀。

- 在可能的情况下，使用 Kafka 参数名，例如： `bootstrap.servers` 和 `acks`

> Configuration values related to the channel generically are applied at the channel config level, eg: a1.channel.k1.type =

> Configuration values related to Kafka or how the Channel operates are prefixed with “kafka.”, (this are analgous to CommonClient Configs) eg: a1.channels.k1.kafka.topic and 
a1.channels.k1.kafka.bootstrap.servers. This is not dissimilar to how the hdfs sink operates

> Properties specific to the producer/consumer are prefixed by kafka.producer or kafka.consumer

> Where possible, the Kafka paramter names are used, eg: bootstrap.servers and acks

> This version of flume is backwards-compatible with previous versions, however deprecated properties are indicated in the table below and a warning message is logged on startup when they are present in the configuration file.Required properties are in bold.

这个版本的 flume 与以前的版本向后兼容，但是不支持的属性在下表中被指出，并且当它们出现在配置文件中时，会在启动时记录一条警告消息。

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name, needs to be `org.apache.flume.channel.kafka.KafkaChannel` 【组件类型名称，必须是`org.apache.flume.channel.kafka.KafkaChannel`】
**kafka.bootstrap.servers**|–  |    List of brokers in the Kafka cluster used by the channel This can be a partial list of brokers, but we recommend at least two for HA. The format is comma separated list of hostname:port【这个channel使用的kafka集群的brokers列表。这可以是brokers的部分列表，但为了ha推荐至少两个。格式是`hostname:port`的列表，用逗号分隔】
kafka.topic	     |flume-channel|	Kafka topic which the channel will use【channel将使用的Kafka topic】
kafka.consumer.group.id |flume |	Consumer group ID the channel uses to register with Kafka. Multiple channels must use the same topic and group to ensure that when one agent fails another can get the data Note that having non-channel consumers with the same ID can lead to data loss.【channel用来向kafka注册的消费者组id。多个channels必须使用相同的topic和分组，来确保一个agent失败了，其他的可以获取数据。注意，使用相同ID的non-channel消费者可能会导致数据丢失。】
parseAsFlumeEvent|	  true	   |    Expecting Avro datums with FlumeEvent schema in the channel. This should be true if Flume source is writing to the channel and false if other producers are writing into the topic that the channel is using. Flume source messages to Kafka can be parsed outside of Flume by using org.apache.flume.source.avro.AvroFlumeEvent provided by the flume-ng-sdk artifact【期待在channel中带有FlumeEvent模式的Avro数据。如果Flume source正在向channel写入数据，则为true；如果其他生产者正在向channel正在使用的topic写入数据，则为false。发送到Kafka的Flume source消息可以通过使用flume-ng-sdk工件提供的`org.apache.flume.source.avro.AvroFlumeEvent`在Flume之外解析。】
pollTimeout	     |    500	   |    The amount of time(in milliseconds) to wait in the “poll()” call of the consumer. https://kafka.apache.org/090/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html#poll(long)【在消费者的“poll()”调用中等待的时间(以毫秒为单位)。】
defaultPartitionId|	   –	   |    Specifies a Kafka partition ID (integer) for all events in this channel to be sent to, unless overriden by partitionIdHeader. By default, if this property is not set, events will be distributed by the Kafka Producer’s partitioner - including by key if specified (or by a partitioner specified by kafka.partitioner.class).【在这个channel中为要发送的事件指定一个kafka分区id（整数），除非使用partitionIdHeader覆盖。默认，如果没有设置这个属性，事件被kafka生产者的分区器分发--如果指定的话，包含键（或者通过`kafka.partitioner.class`指定的分区器）】
partitionIdHeader|	   –	   |    When set, the producer will take the value of the field named using the value of this property from the event header and send the message to the specified partition of the topic. If the value represents an invalid partition the event will not be accepted into the channel. If the header value is present then this setting overrides defaultPartitionId.【设置后，生产者将从事件header获取使用该属性的值命名的字段的值，并发送消息到topic指定的分区。如果值表示一个无效的分区，事件将不会被channel接受。如果header值存在，那么这个设置将覆盖defaultPartitionId】
kafka.consumer.auto.offset.reset  |	latest	|  What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server (e.g. because that data has been deleted): earliest: automatically reset the offset to the earliest offset latest: automatically reset the offset to the latest offset none: throw exception to the consumer if no previous offset is found for the consumer’s group anything else: throw exception to the consumer.【当在kafka中不存在一个初始的偏移量，或如果当前的偏移量不存在任何的服务器中（如，因为数据被删除了），应该做什么：earliest：自动重置偏移量为最早的偏移量。latest：自动重置偏移量为最近的偏移量。none：如果没有在消费者组中找到先前的偏移量，则向消费者抛出异常。其他：向消费者抛出异常。】
kafka.producer.security.protocol  |	PLAINTEXT  |	Set to SASL_PLAINTEXT, SASL_SSL or SSL if writing to Kafka using some level of security. See below for additional info on secure setup.【如果写入数据到kafka使用安全级别，可以设置为`SASL_PLAINTEXT`、`SASL_SSL`、`SSL`】
kafka.consumer.security.protocol  |	PLAINTEXT  |	Same as kafka.producer.security.protocol but for reading/consuming from Kafka.【和`kafka.producer.security.protocol`相同，但是为了从kafka中读取/消费数据】
more producer/consumer security props |	 	   |    If using SASL_PLAINTEXT, SASL_SSL or SSL refer to [Kafka security](http://kafka.apache.org/documentation.html#security) for additional properties that need to be set on producer/consumer.

> Deprecated Properties

Property Name    |   Default   | 	Description
---|:---|:---
brokerList	     |     –	   |    List of brokers in the Kafka cluster used by the channel This can be a partial list of brokers, but we recommend at least two for HA. The format is comma separated list of hostname:port
topic	         |flume-channel|	Use kafka.topic
groupId	         |    flume	   |    Use kafka.consumer.group.id
readSmallestOffset|	  false	   |    Use kafka.consumer.auto.offset.reset
migrateZookeeperOffsets| true  |    When no Kafka stored offset is found, look up the offsets in Zookeeper and commit them to Kafka. This should be true to support seamless Kafka client migration from older versions of Flume. Once migrated this can be set to false, though that should generally not be required. If no Zookeeper offset is found the kafka.consumer.auto.offset.reset configuration defines how offsets are handled.

> Note Due to the way the channel is load balanced, there may be duplicate events when the agent first starts up

注意：由于 channel 的负载均衡方式，在 agent 第一次启动时可能会出现重复 events。

> Example for agent named a1:

	a1.channels.channel1.type = org.apache.flume.channel.kafka.KafkaChannel
	a1.channels.channel1.kafka.bootstrap.servers = kafka-1:9092,kafka-2:9092,kafka-3:9092
	a1.channels.channel1.kafka.topic = channel1
	a1.channels.channel1.kafka.consumer.group.id = flume-consumer

### 3.1、Security and Kafka Channel:

> Secure authentication as well as data encryption is supported on the communication channel between Flume and Kafka. For secure authentication SASL/GSSAPI (Kerberos V5) or SSL (even though the parameter is named SSL, the actual protocol is a TLS implementation) can be used from Kafka version 0.9.0.

Flume 和 Kafka 之间的通信通道支持安全认证和数据加密。

对于安全身份验证，可以在 Kafka 0.9.0 版本中使用 SASL/GSSAPI(Kerberos V5 )或 SSL(即使参数名为 SSL，实际的协议是 TLS 实现)。

> As of now data encryption is solely provided by SSL/TLS.

目前数据加密仅由 SSL/TLS 提供。

> Setting kafka.producer|consumer.security.protocol to any of the following value means:

设置 kafka.producer|consumer.security.protocol 为以下的任何一个值:

- SASL_PLAINTEXT - Kerberos or plaintext authentication with no data encryption
- SASL_SSL - Kerberos or plaintext authentication with data encryption
- SSL - TLS based encryption with optional authentication.

> Warning：There is a performance degradation when SSL is enabled, the magnitude of which depends on the CPU type and the JVM implementation. Reference: [Kafka security overview](http://kafka.apache.org/documentation#security_overview) and the jira for tracking this issue: [KAFKA-2561](https://issues.apache.org/jira/browse/KAFKA-2561)

当启用 SSL 时，性能会下降，下降程度取决于 CPU 类型和 JVM 实现。

### 3.2、TLS and Kafka Channel:

> Please read the steps described in [Configuring Kafka Clients SSL](http://kafka.apache.org/documentation#security_configclients) to learn about additional configuration settings for fine tuning for example any of the following: security provider, cipher suites, enabled protocols, truststore or keystore types.

请阅读 Configuring Kafka Clients SSL 中描述的步骤，以了解额外的配置调优，例如以下的任何一个:安全提供者，加密套件，启用协议，信任存储或密钥存储类型。

> Example configuration with server side authentication and data encryption.

服务器端身份认证和数据加密的配置示例。

	a1.channels.channel1.type = org.apache.flume.channel.kafka.KafkaChannel
	a1.channels.channel1.kafka.bootstrap.servers = kafka-1:9093,kafka-2:9093,kafka-3:9093
	a1.channels.channel1.kafka.topic = channel1
	a1.channels.channel1.kafka.consumer.group.id = flume-consumer
	a1.channels.channel1.kafka.producer.security.protocol = SSL
	# optional, the global truststore can be used alternatively
	a1.channels.channel1.kafka.producer.ssl.truststore.location = /path/to/truststore.jks
	a1.channels.channel1.kafka.producer.ssl.truststore.password = <password to access the truststore>
	a1.channels.channel1.kafka.consumer.security.protocol = SSL
	# optional, the global truststore can be used alternatively
	a1.channels.channel1.kafka.consumer.ssl.truststore.location = /path/to/truststore.jks
	a1.channels.channel1.kafka.consumer.ssl.truststore.password = <password to access the truststore>

> Specyfing the truststore is optional here, the global truststore can be used instead. For more details about the global SSL setup, see the [SSL/TLS support](http://flume.apache.org/FlumeUserGuide.html#ssl-tls-support) section.

在这里指定的 truststore 是可选的，可以使用全局 truststore。有关全局 SSL 设置的更多细节，请参见 SSL/TLS 支持部分。

> Note: By default the property ssl.endpoint.identification.algorithm is not defined, so hostname verification is not performed. In order to enable hostname verification, set the following properties

注意：默认情况下，不定义 ssl.endpoint.identification.algorithm 属性，所以不执行主机名验证。

为了启用主机名验证，设置以下属性：

	a1.channels.channel1.kafka.producer.ssl.endpoint.identification.algorithm = HTTPS
	a1.channels.channel1.kafka.consumer.ssl.endpoint.identification.algorithm = HTTPS

> Once enabled, clients will verify the server’s fully qualified domain name (FQDN) against one of the following two fields:

一旦启用，客户端将根据以下两个字段之一验证服务器的完全限定域名(FQDN):

- Common Name (CN) https://tools.ietf.org/html/rfc6125#section-2.3
- Subject Alternative Name (SAN) https://tools.ietf.org/html/rfc5280#section-4.2.1.6

> If client side authentication is also required then additionally the following needs to be added to Flume agent configuration or the global SSL setup can be used (see SSL/TLS support section). Each Flume agent has to have its client certificate which has to be trusted by Kafka brokers either individually or by their signature chain. Common example is to sign each client certificate by a single Root CA which in turn is trusted by Kafka brokers.

如果还需要客户端身份验证，那么还需要将以下内容添加到 Flume agent 配置中，或者可以使用全局 SSL 设置。

每个 Flume agent 必须有自己的客户端证书，该证书必须被 Kafka brokers 单独或通过其签名链信任。

常见的例子是通过一个被 Kafka brokers 信任的 Root CA 对每个客户端证书进行签名。

	# optional, the global keystore can be used alternatively
	a1.channels.channel1.kafka.producer.ssl.keystore.location = /path/to/client.keystore.jks
	a1.channels.channel1.kafka.producer.ssl.keystore.password = <password to access the keystore>
	# optional, the global keystore can be used alternatively
	a1.channels.channel1.kafka.consumer.ssl.keystore.location = /path/to/client.keystore.jks
	a1.channels.channel1.kafka.consumer.ssl.keystore.password = <password to access the keystore>

> If keystore and key use different password protection then ssl.key.password property will provide the required additional secret for both consumer and producer keystores:

如果 keystore 和 key 使用不同的密码保护，那么 ssl.key.password 属性将为两个消费者 keystores 提供所需的额外加密:

	a1.channels.channel1.kafka.producer.ssl.key.password = <password to access the key>
	a1.channels.channel1.kafka.consumer.ssl.key.password = <password to access the key>

### 3.3、Kerberos and Kafka Channel:

> To use Kafka channel with a Kafka cluster secured with Kerberos, set the producer/consumer.security.protocol properties noted above for producer and/or consumer. The Kerberos keytab and principal to be used with Kafka brokers is specified in a JAAS file’s “KafkaClient” section. “Client” section describes the Zookeeper connection if needed. See [Kafka doc](http://kafka.apache.org/documentation.html#security_sasl_clientconfig) for information on the JAAS file contents. The location of this JAAS file and optionally the system wide kerberos configuration can be specified via JAVA_OPTS in flume-env.sh:

为了使用 Kerberos 安全保护的 Kafka 集群的 Kafka source，为消费者设置consumer.security.protocol 属性。

与 Kafka broker 一起使用的 Kerberos keytab 和 principal 是在 JAAS 文件的 “kafkclient” 部分指定的。如果需要，“Client” 部分描述 Zookeeper 连接。

可以通过 flume-env.sh 中的 JAVA_OPTS 指定这个 JAAS 文件的位置以及系统范围的 kerberos 配置(可选):

	JAVA_OPTS="$JAVA_OPTS -Djava.security.krb5.conf=/path/to/krb5.conf"
	JAVA_OPTS="$JAVA_OPTS -Djava.security.auth.login.config=/path/to/flume_jaas.conf"

> Example secure configuration using SASL_PLAINTEXT:

	a1.channels.channel1.type = org.apache.flume.channel.kafka.KafkaChannel
	a1.channels.channel1.kafka.bootstrap.servers = kafka-1:9093,kafka-2:9093,kafka-3:9093
	a1.channels.channel1.kafka.topic = channel1
	a1.channels.channel1.kafka.consumer.group.id = flume-consumer
	a1.channels.channel1.kafka.producer.security.protocol = SASL_PLAINTEXT
	a1.channels.channel1.kafka.producer.sasl.mechanism = GSSAPI
	a1.channels.channel1.kafka.producer.sasl.kerberos.service.name = kafka
	a1.channels.channel1.kafka.consumer.security.protocol = SASL_PLAINTEXT
	a1.channels.channel1.kafka.consumer.sasl.mechanism = GSSAPI
	a1.channels.channel1.kafka.consumer.sasl.kerberos.service.name = kafka

> Example secure configuration using SASL_SSL:

	a1.channels.channel1.type = org.apache.flume.channel.kafka.KafkaChannel
	a1.channels.channel1.kafka.bootstrap.servers = kafka-1:9093,kafka-2:9093,kafka-3:9093
	a1.channels.channel1.kafka.topic = channel1
	a1.channels.channel1.kafka.consumer.group.id = flume-consumer
	a1.channels.channel1.kafka.producer.security.protocol = SASL_SSL
	a1.channels.channel1.kafka.producer.sasl.mechanism = GSSAPI
	a1.channels.channel1.kafka.producer.sasl.kerberos.service.name = kafka
	# optional, the global truststore can be used alternatively
	a1.channels.channel1.kafka.producer.ssl.truststore.location = /path/to/truststore.jks
	a1.channels.channel1.kafka.producer.ssl.truststore.password = <password to access the truststore>
	a1.channels.channel1.kafka.consumer.security.protocol = SASL_SSL
	a1.channels.channel1.kafka.consumer.sasl.mechanism = GSSAPI
	a1.channels.channel1.kafka.consumer.sasl.kerberos.service.name = kafka
	# optional, the global truststore can be used alternatively
	a1.channels.channel1.kafka.consumer.ssl.truststore.location = /path/to/truststore.jks
	a1.channels.channel1.kafka.consumer.ssl.truststore.password = <password to access the truststore>

> Sample JAAS file. For reference of its content please see client config sections of the desired authentication mechanism (GSSAPI/PLAIN) in Kafka documentation of [SASL configuration](http://kafka.apache.org/documentation#security_sasl_clientconfig). Since the Kafka Source may also connect to Zookeeper for offset migration, the “Client” section was also added to this example. This won’t be needed unless you require offset migration, or you require this section for other secure components. Also please make sure that the operating system user of the Flume processes has read privileges on the jaas and keytab files.

JAAS 文件示例。

因为 Kafka Source 也可以连接到 Zookeeper 进行偏移量迁移，“Client” 部分也被添加到这个例子中。除非需要偏移量迁移，或者其他安全组件需要使用本节，否则不需要这样做。

另外，请确保 Flume 进程的操作系统用户对 jaas 和 keytab 文件有读取权限。

	Client {
	  com.sun.security.auth.module.Krb5LoginModule required
	  useKeyTab=true
	  storeKey=true
	  keyTab="/path/to/keytabs/flume.keytab"
	  principal="flume/flumehost1.example.com@YOURKERBEROSREALM";
	};

	KafkaClient {
	  com.sun.security.auth.module.Krb5LoginModule required
	  useKeyTab=true
	  storeKey=true
	  keyTab="/path/to/keytabs/flume.keytab"
	  principal="flume/flumehost1.example.com@YOURKERBEROSREALM";
	};

## 4、File Channel

> Required properties are in bold.

必需的属性以粗体显示。

Property Name     |   Default   | 	Description
---|:---|:---
**type**	      |      –	    |    The component type name, needs to be `file`.【组件类型名称，必须是`file`】
checkpointDir	  |`~/.flume/file-channel/checkpoint`  |	The directory where checkpoint file will be stored【checkpoint文件存储的目录】
useDualCheckpoints|	   false	|    Backup the checkpoint. If this is set to true, backupCheckpointDir must be set 【备份checkpoint。如果设置为true，则必须设置backupCheckpointDir】
backupCheckpointDir|	–	    |    The directory where the checkpoint is backed up to. This directory must not be the same as the data directories or the checkpoint directory【checkpoint被备份的目录。该目录不能和数据目录或checkpoint目录相同】
dataDirs   |  `~/.flume/file-channel/data`  |  Comma separated list of directories for storing log files. Using multiple directories on separate disks can improve file channel peformance【存储日志文件的逗号分隔的目录列表。在不同磁盘上使用多个目录可以提高file channel性能。】
transactionCapacity |   10000   |  The maximum size of transaction supported by the channel【channel支持的事务的最大大小】
checkpointInterval  |	30000   |  Amount of time (in millis) between checkpoints【两次checkpoints间的间隔时间（毫秒）】
maxFileSize	        |2146435071	|  Max size (in bytes) of a single log file【一个日志文件的最大大小（字节）】
minimumRequiredSpace| 524288000	|  Minimum Required free space (in bytes). To avoid data corruption, File Channel stops accepting take/put requests when free space drops below this value【最小要求的空闲空间（字节）。为了避免数据损坏，当空闲空间低于这个值时，File Channel停止接受take/put请求】
capacity	        |  1000000	|  Maximum capacity of the channel【channel的最大容量】
keep-alive	        |     3	    |  Amount of time (in sec) to wait for a put operation【等待put操作的时间(以秒为单位)】
use-log-replay-v1	|   false	|  Expert: Use old replay logic
use-fast-replay	    |   false	|  Expert: Replay without using queue
checkpointOnClose	|   true	|  Controls if a checkpoint is created when the channel is closed. Creating a checkpoint on close speeds up subsequent startup of the file channel by avoiding replay.【当channel关闭时，控制是否创建一个checkpoint。通过避免重播，在close上创建checkpoint可以加快后续file channe的启动。】
encryption.activeKey|	  –	    |  Key name used to encrypt new data【用来加密新数据的key的名字】
encryption.cipherProvider|	–	|  Cipher provider type, supported types: AESCTRNOPADDING【密码提供者类型，支持的类型：AESCTRNOPADDING】
encryption.keyProvider|	  –	    |  Key provider type, supported types: JCEKSFILE【Key提供者类型，支持的类型：JCEKSFILE】
encryption.keyProvider.keyStoreFile|	–	|  Path to the keystore file【keystore文件的路径】
encrpytion.keyProvider.keyStorePasswordFile|	–	|  Path to the keystore password file【keystore文密码件的路径】
encryption.keyProvider.keys	 |   –	 |  List of all keys (e.g. history of the activeKey setting)【所有的keys的列表（如，activeKey设置的历史）】
encyption.keyProvider.keys.*.passwordFile |	 –	| Path to the optional key password file【可选的key密码文件的路径】

> Note By：default the File Channel uses paths for checkpoint and data directories that are within the user home as specified above. As a result if you have more than one File Channel instances active within the agent, only one will be able to lock the directories and cause the other channel initialization to fail. It is therefore necessary that you provide explicit paths to all the configured channels, preferably on different disks. Furthermore, as file channel will sync to disk after every commit, coupling it with a sink/source that batches events together may be necessary to provide good performance where multiple disks are not available for checkpoint and data directories.

说明：默认情况下，File Channel 使用的检查点路径和数据目录都在用户主目录中，如上所述。

因此，如果在 agent 中有多个活跃的 File Channel 实例，那么只有一个实例能够锁定目录，并导致其他 channel 初始化失败。

因此，有必要为所有配置的 channels 提供显式路径，最好是在不同的磁盘上。

此外，由于 file channel 将在每次提交后同步到磁盘，因此在检查点和数据目录无法使用多个磁盘的情况下，可能需要将它与一个将时间分批次的 sink/source 耦合在一起，从而提供良好的性能。

> Example for agent named a1:

	a1.channels = c1
	a1.channels.c1.type = file
	a1.channels.c1.checkpointDir = /mnt/flume/checkpoint
	a1.channels.c1.dataDirs = /mnt/flume/data

### 4.1、Encryption

> Below is a few sample configurations:

> Generating a key with a password seperate from the key store password:

	keytool -genseckey -alias key-0 -keypass keyPassword -keyalg AES \
	  -keysize 128 -validity 9000 -keystore test.keystore \
	  -storetype jceks -storepass keyStorePassword

> Generating a key with the password the same as the key store password:

	keytool -genseckey -alias key-1 -keyalg AES -keysize 128 -validity 9000 \
	  -keystore src/test/resources/test.keystore -storetype jceks \
	  -storepass keyStorePassword

	a1.channels.c1.encryption.activeKey = key-0
	a1.channels.c1.encryption.cipherProvider = AESCTRNOPADDING
	a1.channels.c1.encryption.keyProvider = key-provider-0
	a1.channels.c1.encryption.keyProvider = JCEKSFILE
	a1.channels.c1.encryption.keyProvider.keyStoreFile = /path/to/my.keystore
	a1.channels.c1.encryption.keyProvider.keyStorePasswordFile = /path/to/my.keystore.password
	a1.channels.c1.encryption.keyProvider.keys = key-0

> Let’s say you have aged key-0 out and new files should be encrypted with key-1:

	a1.channels.c1.encryption.activeKey = key-1
	a1.channels.c1.encryption.cipherProvider = AESCTRNOPADDING
	a1.channels.c1.encryption.keyProvider = JCEKSFILE
	a1.channels.c1.encryption.keyProvider.keyStoreFile = /path/to/my.keystore
	a1.channels.c1.encryption.keyProvider.keyStorePasswordFile = /path/to/my.keystore.password
	a1.channels.c1.encryption.keyProvider.keys = key-0 key-1

> The same scenerio as above, however key-0 has its own password:

	a1.channels.c1.encryption.activeKey = key-1
	a1.channels.c1.encryption.cipherProvider = AESCTRNOPADDING
	a1.channels.c1.encryption.keyProvider = JCEKSFILE
	a1.channels.c1.encryption.keyProvider.keyStoreFile = /path/to/my.keystore
	a1.channels.c1.encryption.keyProvider.keyStorePasswordFile = /path/to/my.keystore.password
	a1.channels.c1.encryption.keyProvider.keys = key-0 key-1
	a1.channels.c1.encryption.keyProvider.keys.key-0.passwordFile = /path/to/key-0.password

## 5、Spillable Memory Channel

> The events are stored in an in-memory queue and on disk. The in-memory queue serves as the primary store and the disk as overflow. The disk store is managed using an embedded File channel. When the in-memory queue is full, additional incoming events are stored in the file channel. This channel is ideal for flows that need high throughput of memory channel during normal operation, but at the same time need the larger capacity of the file channel for better tolerance of intermittent sink side outages or drop in drain rates. The throughput will reduce approximately to file channel speeds during such abnormal situations. In case of an agent crash or restart, only the events stored on disk are recovered when the agent comes online. This channel is currently experimental and not recommended for use in production.

events 存储在内存中的队列和磁盘上。内存队列作为主要存储，磁盘存储溢出的数据。

使用嵌入式 File channel 管理磁盘存储。当内存队列已满时，其他传入的 events 将存储在 file channel 中。

对于在正常运行期间需要 memory channel 高吞吐量的流，此 channel 是理想的，但同时需要更大容量的 file channel，以更好地容忍间歇的 sink 端中断或排泄率下降。在这种异常情况下，吞吐量将降低到 file channel 速度。

在 agent 崩溃或重新启动的情况下，只有存储在磁盘上的 events 在 agent 联机时恢复。

该 channel 目前还处于试验阶段，不建议在生产中使用。

> Required properties are in bold. Please refer to file channel for additional required properties.

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name, needs to be `SPILLABLEMEMORY`【组件类型名称，必须是`SPILLABLEMEMORY`】
memoryCapacity	 |    10000	   |    Maximum number of events stored in memory queue. To disable use of in-memory queue, set this to zero.【存储在内存队列中的事件的最大数量。要禁用内存队列，设置为0】
overflowCapacity |	100000000  |    Maximum number of events stored in overflow disk (i.e File channel). To disable use of overflow, set this to zero.【在溢写磁盘里存储的事件的最大数量。要禁用溢写，设置为0】
overflowTimeout  |      3	   |    The number of seconds to wait before enabling disk overflow when memory fills up.【当内存填满时，启用磁盘溢写前等待的秒数。】
byteCapacityBufferPercentage|  20	|  Defines the percent of buffer between byteCapacity and the estimated total size of all events in the channel, to account for data in headers. See below.【byteCapacity和channel中所有事件的总大小之间的缓存百分比，以考虑headers中的数据。】
byteCapacity	 | see description	|  Maximum bytes of memory allowed as a sum of all events in the memory queue. The implementation only counts the Event body, which is the reason for providing the byteCapacityBufferPercentage configuration parameter as well. Defaults to a computed value equal to 80% of the maximum memory available to the JVM (i.e. 80% of the -Xmx value passed on the command line). Note that if you have multiple memory channels on a single JVM, and they happen to hold the same physical events (i.e. if you are using a replicating channel selector from a single source) then those event sizes may be double-counted for channel byteCapacity purposes. Setting this value to 0 will cause this value to fall back to a hard internal limit of about 200 GB.【该channel中所有事件的总和所允许的最大内存字节数。该实现只计算事件body，这也是提供byteCapacityBufferPercentage配置参数的原因。默认计算值等于JVM可用最大内存的80%(即通过命令行传递的-Xmx值的80%)。请注意，如果在单个JVM上有多个内存channels，并且它们碰巧保存相同的物理事件(例如，如果使用来自单个source的replicating channel selector)，那么出于channel byteCapacity的目的，这些事件大小可能会被计算两次。将该值设置为0将导致该值回落到大约200gb的硬内部限制。（所有事件的body的总和所占用的最大内存字节数(jvm)）】
avgEventSize	 |      500	   |   Estimated average size of events, in bytes, going into the channel【进入channel的事件的估计平均大小(以字节为单位)】
`<file channel properties>`    |	see file channel	|    Any file channel property with the exception of ‘keep-alive’ and ‘capacity’ can be used. The keep-alive of file channel is managed by Spillable Memory Channel. Use ‘overflowCapacity’ to set the File channel’s capacity.【任意的file channel属性都可以使用，除了keep-alive和‘capacity’。file channel的keep-alive由Spillable Memory Channel管理。使用overflowCapacity来设置File channel的容量。】

> In-memory queue is considered full if either memoryCapacity or byteCapacity limit is reached.

如果达到 memoryCapacity 或 bytecapcapacity 限制，则认为内存队列已满。

> Example for agent named a1:

	a1.channels = c1
	a1.channels.c1.type = SPILLABLEMEMORY
	a1.channels.c1.memoryCapacity = 10000
	a1.channels.c1.overflowCapacity = 1000000
	a1.channels.c1.byteCapacity = 800000
	a1.channels.c1.checkpointDir = /mnt/flume/checkpoint
	a1.channels.c1.dataDirs = /mnt/flume/data

> To disable the use of the in-memory queue and function like a file channel:

禁用内存队列，使用像 file channel 的功能：

	a1.channels = c1
	a1.channels.c1.type = SPILLABLEMEMORY
	a1.channels.c1.memoryCapacity = 0
	a1.channels.c1.overflowCapacity = 1000000
	a1.channels.c1.checkpointDir = /mnt/flume/checkpoint
	a1.channels.c1.dataDirs = /mnt/flume/data

> To disable the use of overflow disk and function purely as a in-memory channel:

禁用溢出磁盘，纯粹作为内存队列的功能:

	a1.channels = c1
	a1.channels.c1.type = SPILLABLEMEMORY
	a1.channels.c1.memoryCapacity = 100000
	a1.channels.c1.overflowCapacity = 0

## 6、Pseudo Transaction Channel

> Warning：The Pseudo Transaction Channel is only for unit testing purposes and is NOT meant for production use.Required properties are in bold.

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name, needs to be `org.apache.flume.channel.PseudoTxnMemoryChannel`
capacity	     |      50	   |     The max number of events stored in the channel
keep-alive	     |       3	   |Timeout in seconds for adding or removing an event

## 7、Custom Channel

> A custom channel is your own implementation of the Channel interface. A custom channel’s class and its dependencies must be included in the agent’s classpath when starting the Flume agent. The type of the custom channel is its FQCN. Required properties are in bold.

自定义 channel 是你自己的 Channel 接口实现。启动 Flume agent 时，自定义 channel 的类及其依赖项必须包含在 agent 的类路径中。自定义 channel 的类型是它的 FQCN。

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name, needs to be a FQCN

> Example for agent named a1:

	a1.channels = c1
	a1.channels.c1.type = org.example.MyChannel