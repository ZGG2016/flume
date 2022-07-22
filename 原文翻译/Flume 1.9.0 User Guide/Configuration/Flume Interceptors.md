# Flume Interceptors

[TOC]

> Flume has the capability to modify/drop events in-flight. This is done with the help of interceptors. Interceptors are classes that implement org.apache.flume.interceptor.Interceptor interface. An interceptor can modify or even drop events based on any criteria chosen by the developer of the interceptor. Flume supports chaining of interceptors. This is made possible through by specifying the list of interceptor builder class names in the configuration. Interceptors are specified as a whitespace separated list in the source configuration. The order in which the interceptors are specified is the order in which they are invoked. The list of events returned by one interceptor is passed to the next interceptor in the chain. Interceptors can modify or drop events. If an interceptor needs to drop events, it just does not return that event in the list that it returns. If it is to drop all events, then it simply returns an empty list. Interceptors are named components, here is an example of how they are created through configuration:

Flume 能够**在数据流转中修改/删除 events**，这是 interceptors 的作用。

Interceptors 是实现 org.apache.flume.interceptor 接口的类。

interceptor 可以根据开发人员选择的任何规则修改甚至删除 events。

Flume 支持 interceptors 链，通过在配置中指定 interceptor builder 类名列表实现的。

Interceptors 在 source 配置中被指定为一个空格分隔的列表。**指定的 Interceptors 的顺序就是调用它们的顺序**。一个 interceptor 返回的 events 列表被传递给链中的下一个 interceptor。

Interceptors 可以修改或删除 events。如果 interceptor 需要删除 events，它就不会在它返回的列表中返回该 events。如果要删除所有 events，则只返回一个空列表。

Interceptors 是命名组件，下面是一个通过配置创建它们的例子:

	a1.sources = r1
	a1.sinks = k1
	a1.channels = c1
	a1.sources.r1.interceptors = i1 i2
	a1.sources.r1.interceptors.i1.type = org.apache.flume.interceptor.HostInterceptor$Builder
	a1.sources.r1.interceptors.i1.preserveExisting = false
	a1.sources.r1.interceptors.i1.hostHeader = hostname
	a1.sources.r1.interceptors.i2.type = org.apache.flume.interceptor.TimestampInterceptor$Builder
	a1.sinks.k1.filePrefix = FlumeData.%{CollectorHost}.%Y-%m-%d
	a1.sinks.k1.channel = c1

> Note that the interceptor builders are passed to the type config parameter. The interceptors are themselves configurable and can be passed configuration values just like they are passed to any other configurable component. In the above example, events are passed to the HostInterceptor first and the events returned by the HostInterceptor are then passed along to the TimestampInterceptor. You can specify either the fully qualified class name (FQCN) or the alias timestamp. If you have multiple collectors writing to the same HDFS path, then you could also use the HostInterceptor.

注意，interceptor builders 被传递给类型配置参数。interceptors 本身是可配置的，可以像传递给任何其他配置组件一样，传递配置值。

在上面的示例中，首先将 events 传递给 HostInterceptor，然后将 HostInterceptor 返回的 events 传递给 TimestampInterceptor。

你可以指定完全限定类名(FQCN)或别名 `timestamp`。

如果有多个 collectors 写入同一个 HDFS 路径，那么也可以使用 HostInterceptor。

## 1、Timestamp Interceptor

> This interceptor inserts into the event headers, the time in millis at which it processes the event. This interceptor inserts a header with key timestamp (or as specified by the header property) whose value is the relevant timestamp. This interceptor can preserve an existing timestamp if it is already present in the configuration.

这个 interceptor 插入到 event headers 中，它处理 event 的时间以毫秒为单位。

这个 interceptor **插入一个带有 key `timestamp` 的 header(或由 `header` 属性指定的)**，它的值是相关的时间戳。

如果配置中已经存在一个时间戳，这个 interceptor 可以保留它一个已存在的时间戳。

Property Name    |   Default   | 	Description
---|:---|:---
**type**         |   –         |   The component type name, has to be timestamp or the FQCN【组件类型名称，必须是`timestamp`或完全限定类名】
headerName       |   timestamp |   The name of the header in which to place the generated timestamp.【要在其中放置生成的时间戳的header的名称。】
preserveExisting |   false     |   If the timestamp already exists, should it be preserved - true or false【如果时间戳已存在，是否应该保留它】

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.channels =  c1
	a1.sources.r1.type = seq
	a1.sources.r1.interceptors = i1
	a1.sources.r1.interceptors.i1.type = timestamp

## 2、Host Interceptor

> This interceptor inserts the hostname or IP address of the host that this agent is running on. It inserts a header with key host or a configured key whose value is the hostname or IP address of the host, based on configuration.

这个 interceptor **插入运行这个 agent 的主机的主机名或IP地址**。

它插入一个 header ，这个 header 带有 key `host` 或一个配置的 key ，该 key 的值是基于配置的主机的主机名或IP地址。

Property Name    |   Default   | 	Description
---|:---|:---
**type**         |   –	       |   The component type name, has to be host【组件类型名称，必须是`host`】
preserveExisting |   false     |   If the host header already exists, should it be preserved - true or false【如果主机header已存在，是否应该保留它】
useIP            |   true      |   Use the IP Address if true, else use hostname.【如果是true，使用ip地址，否则使用主机名】
hostHeader       |   host      |   The header key to be used.

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.interceptors = i1
	a1.sources.r1.interceptors.i1.type = host

## 3、Static Interceptor

> Static interceptor allows user to append a static header with static value to all events.

静态 interceptor 允许用户**为所有的 events 追加一个带有静态值的静态 header**。

> The current implementation does not allow specifying multiple headers at one time. Instead user might chain multiple static interceptors each defining one static header.

当前的实现不允许同时指定多个 headers。相反，用户可以将多个静态 interceptors 链接起来，每个 interceptor 定义一个静态 header。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |     –	   |    The component type name, has to be static【组件类型名称，必须是`static`】
preserveExisting |	  true	   |    If configured header already exists, should it be preserved - true or false【如果配置header已存在，是否应该保留它】
key              |	  key	   |    Name of header that should be created【应该创建的header的名称】
value            |	  value	   |    Static value that should be created【应该创建的静态值】

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.channels =  c1
	a1.sources.r1.type = seq
	a1.sources.r1.interceptors = i1
	a1.sources.r1.interceptors.i1.type = static
	a1.sources.r1.interceptors.i1.key = datacenter
	a1.sources.r1.interceptors.i1.value = NEW_YORK

## 4、Remove Header Interceptor

> This interceptor manipulates Flume event headers, by removing one or many headers. It can remove a statically defined header, headers based on a regular expression or headers in a list. If none of these is defined, or if no header matches the criteria, the Flume events are not modified.

这个 interceptor 通过**删除一个或多个 headers 来操作 Flume event headers**。

它可以删除静态定义的 header、基于正则表达式的 header 或列表中的 header。如果这些都没有定义，或者没有 header 匹配规则，则不修改 Flume events。

> Note that if only one header needs to be removed, specifying it by name provides performance benefits over the other 2 methods.

Property Name    |   Default   | 	Description
---|:---|:---
**type**         |	   –	   |    The component type name has to be remove_header【组件类型名称，必须是`remove_header`】
withName         |     –	   |    Name of the header to remove【要删除的header的名称】
fromList	     |     –	   |    List of headers to remove, separated with the separator specified by fromListSeparator【要删除的header的列表，用`fromListSeparator`指定的分隔符来分隔】
fromListSeparator|	 `\s*`,`\s*`   |    Regular expression used to separate multiple header names in the list specified by fromList. Default is a comma surrounded by any number of whitespace characters【正则表达式，用于分隔`fromList`指定的列表中的多个header名称。默认是由任意数量的空白字符包围的逗号】
matching	     |   –	       |    All the headers which names match this regular expression are removed【删除所有的名称匹配这个正则表达式的headers】

## 5、UUID Interceptor

> This interceptor sets a universally unique identifier on all events that are intercepted. An example UUID is b5755073-77a9-43c1-8fad-b7a586fc1b97, which represents a 128-bit value.

这个 interceptor **对所拦截的所有 events 设置一个通用唯一标识符**。例如，UUID为:`b5755073-77a9-43c1-8fad-b7a586fc1b97`，表示一个 128 位值。

> Consider using UUIDInterceptor to automatically assign a UUID to an event if no application level unique key for the event is available. It can be important to assign UUIDs to events as soon as they enter the Flume network; that is, in the first Flume Source of the flow. This enables subsequent deduplication of events in the face of replication and redelivery in a Flume network that is designed for high availability and high performance. If an application level key is available, this is preferable over an auto-generated UUID because it enables subsequent updates and deletes of event in data stores using said well known application level key.

如果对 event 没有可用的应用程序级的唯一键，考虑使用 `UUIDInterceptor` 自动为一个 event 分配 一个 UUID。

一旦 events 进入 Flume 网络，给它们分配 uuid 是很重要的，即在流的第一个 Flume Source。

这使得在面对为高可用性和高性能而设计的 Flume 网络中的复制和重新交付时，能够随后进行 events 去重。

如果有一个应用程序级的键可用，那么它比自动生成的 UUID 更可取，因为它允许后续使用已知的应用程序级键更新和删除数据存储中的 event。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |     –	   |    The component type name has to be `org.apache.flume.sink.solr.morphline.UUIDInterceptor$Builder`
headerName       |     id	   |    The name of the Flume header to modify【要修改的Flume header的名称】
preserveExisting |	 true	   |    If the UUID header already exists, should it be preserved - true or false 【如果UUID header已存在，是否应该保留它】
prefix           |  `“”`       |	The prefix string constant to prepend to each generated UUID 【在每个生成的UUID前添加前缀字符串常量】

## 6、Morphline Interceptor

> This interceptor filters the events through a [morphline configuration file](http://cloudera.github.io/cdk/docs/current/cdk-morphlines/index.html) that defines a chain of transformation commands that pipe records from one command to another. For example the morphline can ignore certain events or alter or insert certain event headers via regular expression based pattern matching, or it can auto-detect and set a MIME type via Apache Tika on events that are intercepted. For example, this kind of packet sniffing can be used for content based dynamic routing in a Flume topology. MorphlineInterceptor can also help to implement dynamic routing to multiple Apache Solr collections (e.g. for multi-tenancy).

该 interceptor 通过 morphline 配置文件过滤 events，该配置文件定义了一个转换命令链，将记录从一个命令传输到另一个命令。

例如，morphline 可以忽略某些 events 或通过基于正则表达式的模式匹配更改或插入某些 event headers，或者可以通过 Apache Tika 自动检测并设置被截获事件的 MIME 类型。

例如，这种包嗅探可以用于 Flume 拓扑中的基于内容的动态路由。MorphlineInterceptor 还可以帮助实现到多个 Apache Solr 集合的动态路由(例如多租户)。

> Currently, there is a restriction in that the morphline of an interceptor must not generate more than one output record for each input event. This interceptor is not intended for heavy duty ETL processing - if you need this consider moving ETL processing from the Flume Source to a Flume Sink, e.g. to a MorphlineSolrSink.

目前，有一个限制，interceptor 的 morphline 不能为每个输入 event 生成一个以上的输出记录。

这个拦截器不适合繁重的 ETL 处理：如果你需要，可以考虑将 ETL 处理从 Flume Source 移动到 Flume Sink，例如，MorphlineSolrSink。

> Required properties are in bold.

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name has to be `org.apache.flume.sink.solr.morphline.MorphlineInterceptor$Builder`
**morphlineFile**|	    –	   |    The relative or absolute path on the local file system to the morphline configuration file. Example: `/etc/flume-ng/conf/morphline.conf` 【本地文件系统上morphline配置文件的相对或绝对路径】
morphlineId	     |     null	   |    Optional name used to identify a morphline if there are multiple morphlines in a morphline config file 【如果morphline配置文件中有多个morphlines，则用于标识一个morphline的可选名称】

> Sample flume.conf file:

	a1.sources.avroSrc.interceptors = morphlineinterceptor
	a1.sources.avroSrc.interceptors.morphlineinterceptor.type = org.apache.flume.sink.solr.morphline.MorphlineInterceptor$Builder
	a1.sources.avroSrc.interceptors.morphlineinterceptor.morphlineFile = /etc/flume-ng/conf/morphline.conf
	a1.sources.avroSrc.interceptors.morphlineinterceptor.morphlineId = morphline1

## 7、Search and Replace Interceptor

> This interceptor provides simple string-based search-and-replace functionality based on Java regular expressions. Backtracking / group capture is also available. This interceptor uses the same rules as in the Java Matcher.replaceAll() method.

这个 interceptor 提供了简单的**对字符串的基于 Java 正则表达式的搜索和替换功能**。

回溯/组捕获也可用。这个 interceptor 使用与 Java 的 `Matcher.replaceAll()` 方法相同的规则。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |     –	   |    The component type name has to be search_replace 【组件类型名称，必须是`search_replace`】
searchPattern	 |     –	   |    The pattern to search for and replace.【搜索的模式，并替换】
replaceString	 |     –	   |    The replacement string.【替换的字符串】
charset	         |    UTF-8	   |    The charset of the event body. Assumed by default to be UTF-8.【事件主体的字符集，默认是UTF-8】

> Example configuration:

	a1.sources.avroSrc.interceptors = search-replace
	a1.sources.avroSrc.interceptors.search-replace.type = search_replace

	# Remove leading alphanumeric characters in an event body.
	a1.sources.avroSrc.interceptors.search-replace.searchPattern = ^[A-Za-z0-9_]+
	a1.sources.avroSrc.interceptors.search-replace.replaceString =

> Another example:

	a1.sources.avroSrc.interceptors = search-replace
	a1.sources.avroSrc.interceptors.search-replace.type = search_replace

	# Use grouping operators to reorder and munge words on a line.
	a1.sources.avroSrc.interceptors.search-replace.searchPattern = The quick brown ([a-z]+) jumped over the lazy ([a-z]+)
	a1.sources.avroSrc.interceptors.search-replace.replaceString = The hungry $2 ate the careless $1

## 8、Regex Filtering Interceptor

> This interceptor filters events selectively by interpreting the event body as text and matching the text against a configured regular expression. The supplied regular expression can be used to include events or exclude events.

该 interceptor 通过将 event body 解释为文本，并**根据配置的正则表达式匹配文本，有选择地过滤 events**。提供的正则表达式可用于包含 events 或排除 events。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	     |      –	   |    The component type name has to be `regex_filter`【组件类型名称，必须是`regex_filter`】
regex	         |   `”.*”`	   |    Regular expression for matching against events【匹配事件的正则表达式】
excludeEvents	 |    false	   |    If true, regex determines events to exclude, otherwise regex determines events to include.【如果是true，正则确定排除的事件，否则绝对包含的事件】

## 9、Regex Extractor Interceptor

> This interceptor extracts regex match groups using a specified regular expression and appends the match groups as headers on the event. It also supports pluggable serializers for formatting the match groups before adding them as event headers.

这个 interceptor **使用指定的正则表达式提取正则匹配组，并将匹配组附加为 event 的 headers**。

它还支持可插入的序列化器，以便在将匹配组添加为 event headers 之前对它们进行格式化。

Property Name    |   Default   | 	Description
---|:---|:---
**type**	    |      –	   |    The component type name has to be `regex_extractor` 【组件类型名称，必须是`regex_extractor`】
**regex**	    |      –	   |    Regular expression for matching against events【匹配事件的正则表达式】
**serializers**	|      –	   |    Space-separated list of serializers for mapping matches to header names and serializing their values. (See example below) Flume provides built-in support for the following serializers: `org.apache.flume.interceptor.RegexExtractorInterceptorPassThroughSerializer` `org.apache.flume.interceptor.RegexExtractorInterceptorMillisSerializer`【用空格分隔的序列化器列表，用于映射匹配头名称，并序列化它们的值。】
`serializers.<s1>.type`  |  default  | Must be `default` (`org.apache.flume.interceptor.RegexExtractorInterceptorPassThroughSerializer`), `org.apache.flume.interceptor.RegexExtractorInterceptorMillisSerializer`, or the FQCN of a custom class that implements `org.apache.flume.interceptor.RegexExtractorInterceptorSerializer`
`serializers.<s1>.name`  |  –	       |
serializers.*	         |  –	       |   Serializer-specific properties

> The serializers are used to map the matches to a header name and a formatted header value; by default, you only need to specify the header name and the default `org.apache.flume.interceptor.RegexExtractorInterceptorPassThroughSerializer` will be used. This serializer simply maps the matches to the specified header name and passes the value through as it was extracted by the regex. You can plug custom serializer implementations into the extractor using the fully qualified class name (FQCN) to format the matches in anyway you like.

**序列化器用于将匹配项映射到 header 名称和格式化的 header 值**；

默认情况下，你只需要指定 header 名，然后使用默认的 `org.apache.flume.interceptor.RegexExtractorInterceptorPassThroughSerializer`。

这个序列化器只是将匹配项映射到指定的 header 名称，并按照正则提取的方式传递值。

你可以使用完全限定类名(FQCN)将自定义序列化器实现插入提取器，以任何你喜欢的方式格式化匹配。

## 10、Example 1:

> If the Flume event body contained 1:2:3.4foobar5 and the following configuration was used

如果 Flume event body 包含 `1:2:3.4foobar5`，使用下面的配置：

	a1.sources.r1.interceptors.i1.regex = (\\d):(\\d):(\\d)
	a1.sources.r1.interceptors.i1.serializers = s1 s2 s3
	a1.sources.r1.interceptors.i1.serializers.s1.name = one
	a1.sources.r1.interceptors.i1.serializers.s2.name = two
	a1.sources.r1.interceptors.i1.serializers.s3.name = three

提取的 event 将包含相同的 body，但如下的 headers 将被添加:`one=>1`、`two=>2`、`three=>3`

> The extracted event will contain the same body but the following headers will have been added one=>1, two=>2, three=>3

## 11、Example 2:

> If the Flume event body contained 2012-10-18 18:47:57,614 some log line and the following configuration was used

如果 Flume event body 包含 `2012-10-18 18:47:57,614 some log line`，使用以下配置

	a1.sources.r1.interceptors.i1.regex = ^(?:\\n)?(\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d)
	a1.sources.r1.interceptors.i1.serializers = s1
	a1.sources.r1.interceptors.i1.serializers.s1.type = org.apache.flume.interceptor.RegexExtractorInterceptorMillisSerializer
	a1.sources.r1.interceptors.i1.serializers.s1.name = timestamp
	a1.sources.r1.interceptors.i1.serializers.s1.pattern = yyyy-MM-dd HH:mm

提取的 event 将包含相同的 body，但如下的 headers 将被添加: `timestamp=>1350611220000`

> the extracted event will contain the same body but the following headers will have been added timestamp=>1350611220000