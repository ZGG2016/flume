# Event Serializers

[TOC]

> The file_roll sink and the hdfs sink both support the EventSerializer interface. Details of the EventSerializers that ship with Flume are provided below.

`file_roll` sink 和 `hdfs` sink 都支持 EventSerializer 接口。

## 1、Body Text Serializer

> Alias: text. This interceptor writes the body of the event to an output stream without any transformation or modification. The event headers are ignored. Configuration options are as follows:

别名：`text`

该 interceptor **将 event body 写入到输出流，无需任何转换或修改**。event headers 被忽略。

配置选项如下:

Property Name     |   Default        | 	Description
---|:---|:---
appendNewline	  |    true	         |  Whether a newline will be appended to each event at write time. The default of true assumes that events do not contain newlines, for legacy reasons.【在写入时，是否将换行符追加到每个事件。由于遗留的原因，默认的true假设事件不包含换行符。】

> Example for agent named a1:

	a1.sinks = k1
	a1.sinks.k1.type = file_roll
	a1.sinks.k1.channel = c1
	a1.sinks.k1.sink.directory = /var/log/flume
	a1.sinks.k1.sink.serializer = text
	a1.sinks.k1.sink.serializer.appendNewline = false

## 2、“Flume Event” Avro Event Serializer

> Alias: avro_event.This interceptor serializes Flume events into an Avro container file. The schema used is the same schema used for Flume events in the Avro RPC mechanism.

别名：`avro_event`

这个 interceptor **将 Flume events 序列化到一个 Avro 容器文件中**。使用的模式与 Avro RPC 机制中用于 Flume events 的模式相同。

> This serializer inherits from the AbstractAvroEventSerializer class.

这个序列化器继承自 AbstractAvroEventSerializer 类。

> Configuration options are as follows:

配置选项如下:

Property Name     |   Default        | 	Description
---|:---|:---
syncIntervalBytes |   2048000	     |  Avro sync interval, in approximate bytes.【Avro同步间隔，以近似字节为单位。】
compressionCodec  |    null	         |  Avro compression codec. For supported codecs, see Avro’s CodecFactory docs.【Avro压缩编解码器。有关支持的编解码器，请参阅Avro的CodecFactory文档。】

> Example for agent named a1:

	a1.sinks.k1.type = hdfs
	a1.sinks.k1.channel = c1
	a1.sinks.k1.hdfs.path = /flume/events/%y-%m-%d/%H%M/%S
	a1.sinks.k1.serializer = avro_event
	a1.sinks.k1.serializer.compressionCodec = snappy

## 3、Avro Event Serializer

> Alias: This serializer does not have an alias, and must be specified using the fully-qualified class name class name.

别名:此序列化器没有别名，必须使用完全限定类名指定类名。

> This serializes Flume events into an Avro container file like the “Flume Event” Avro Event Serializer, however the record schema is configurable. The record schema may be specified either as a Flume configuration property or passed in an event header.

它将 Flume events 序列化到一个 Avro 容器文件中，就像 “Flume Event” Avro Event Serializer，**但是记录模式是可配置的**。记录模式可以指定为 Flume 配置属性或者传入到 event header 中。

> To pass the record schema as part of the Flume configuration, use the property schemaURL as listed below.

要将记录模式作为 Flume 配置的一部分传递，请使用下面列出的 schemaURL 属性。

> To pass the record schema in an event header, specify either the event header flume.avro.schema.literal containing a JSON-format representation of the schema or flume.avro.schema.url with a URL where the schema may be found (hdfs:/... URIs are supported).

要在 event header 中传递记录模式，可以指定包含模式的 JSON 格式表示的 event header `flume.avro.schema.literal` ，或者可以找到模式 URL 的 `flume.avro.schema.url`。（支持`hdfs:/... URIs`）

> This serializer inherits from the AbstractAvroEventSerializer class.

这个序列化器继承自 AbstractAvroEventSerializer 类。

> Configuration options are as follows:

配置选项如下:

Property Name     |   Default        | 	Description
---|:---|:---
syncIntervalBytes |   2048000	     |  Avro sync interval, in approximate bytes.【Avro同步间隔，以近似字节为单位。】
compressionCodec  | 	null	     |  Avro compression codec. For supported codecs, see Avro’s CodecFactory docs.【Avro压缩编解码器。有关支持的编解码器，请参阅Avro的CodecFactory文档。】
schemaURL	      |     null	     |  Avro schema URL. Schemas specified in the header ovverride this option.【avro模式url。在header中指定的模式覆盖这个选项。】

> Example for agent named a1:

	a1.sinks.k1.type = hdfs
	a1.sinks.k1.channel = c1
	a1.sinks.k1.hdfs.path = /flume/events/%y-%m-%d/%H%M/%S
	a1.sinks.k1.serializer = org.apache.flume.sink.hdfs.AvroEventSerializer$Builder
	a1.sinks.k1.serializer.compressionCodec = snappy
	a1.sinks.k1.serializer.schemaURL = hdfs://namenode/path/to/schema.avsc