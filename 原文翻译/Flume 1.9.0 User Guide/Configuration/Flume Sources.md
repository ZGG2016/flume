# Flume Sources

[TOC]

## 1、Avro Source

> Listens on Avro port and receives events from external Avro client streams. When paired with the built-in Avro Sink on another (previous hop) Flume agent, it can create tiered collection topologies. Required properties are in bold.

监听 Avro 端口，并从外部 Avro 客户端流接收 events。

当与另一个(上一跳) Flume agent 上的内置 Avro Sink 配对时，它可以创建分层集合拓扑。

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**channels**     |     –	   |  
**type**	     |     –	   |    The component type name, needs to be `avro`【组件类型名称，必须是`avro`】
**bind**	     |     –	   |    hostname or IP address to listen on【监听的主机名或ip地址】
**port**	     |     –	   |    Port # to bind to 【绑定的端口】
threads  	     |     –	   |    Maximum number of worker threads to spawn【要生成的工作线程的最大数目】
selector.type    |             |	 	 
selector.*	 	 |             |
interceptors	 |     –	   |    Space-separated list of interceptors【空格分隔的拦截器列表】
interceptors.*	 |             | 	 
compression-type |	  none	   |    This can be “none” or “deflate”. The compression-type must match the compression-type of matching AvroSource【可以是`none`或`deflate`，compression-type必须与匹配的AvroSource的compression-type匹配】
ssl	             |    false	   |    Set this to true to enable SSL encryption. If SSL is enabled, you must also specify a “keystore” and a “keystore-password”, either through component level parameters (see below) or as global SSL parameters (see [SSL/TLS support section](http://flume.apache.org/FlumeUserGuide.html#ssl-tls-support)).【为true时，启用ssl加密。如果启用了ssl，必须也指定一个`“keystore”`和`“keystore-password”`，要么通过组件级的参数，要么作为全局的ssl参数。】
keystore	     |      –	   |    This is the path to a Java keystore file. If not specified here, then the global keystore will be used (if defined, otherwise configuration error).【这是一个java keystore文件的路径。如果不在这指定，将使用全局的keystore（如果定义的话，否则出现配置错误）】
keystore-password|	    –	   |     The password for the Java keystore. If not specified here, then the global keystore password will be used (if defined, otherwise configuration error).【Java keystore密码。如果不在这指定，将使用全局的keystore密码（如果定义的话，否则出现配置错误）】
keystore-type	 |      JKS	   |      The type of the Java keystore. This can be “JKS” or “PKCS12”. If not specified here, then the global keystore type will be used (if defined, otherwise the default is JKS).【Java keystore类型。可以是`“JKS”`或`“PKCS12”`。如果不在这指定，将使用全局的keystore类型（如果定义的话，否则出现默认是JKS）】
exclude-protocols|	   SSLv3   |      Space-separated list of SSL/TLS protocols to exclude. SSLv3 will always be excluded in addition to the protocols specified.【排除的空格分隔的SSL/TLS协议的列表。除了指定的协议，SSLv3总是被排除在外。】
include-protocols|	     –	   |      Space-separated list of SSL/TLS protocols to include. The enabled protocols will be the included protocols without the excluded protocols. If included-protocols is empty, it includes every supported protocols.【包含的空格分隔的SSL/TLS协议的列表。启用的协议将是要包含的协议，没有排除的协议，如果`included-protocols`为空，它包含每一个支持的协议。】
exclude-cipher-suites|	 –	   |      Space-separated list of cipher suites to exclude. 【排除的空格分隔的cipher suites的列表。】
include-cipher-suites|	 –	   |      Space-separated list of cipher suites to include. The enabled cipher suites will be the included cipher suites without the excluded cipher suites. If included-cipher-suites is empty, it includes every supported cipher suites.【包含的空格分隔的cipher suites的列表。启用的cipher suites将是要包含的cipher suites，没有排除的cipher suites，如果`included-cipher-suites`为空，它包含每一个支持的cipher suites。】
ipFilter         |	    false  |      Set this to true to enable ipFiltering for netty【设为true，来为netty启用ip过滤】
ipFilterRules	 |        –	   |      Define N netty ipFilter pattern rules with this config.【使用这个配置可以定义n个netty ipFilter规则】

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = avro
	a1.sources.r1.channels = c1
	a1.sources.r1.bind = 0.0.0.0
	a1.sources.r1.port = 4141
	
ipFilterRules 的示例：

> ipFilterRules defines N netty ipFilters separated by a comma a pattern rule must be in this format.

ipFilterRules 定义 N 个 netty ipFilter，使用逗号分隔，匹配规则必须是这个格式。

`<'allow' or deny>:<'ip' or 'name' for computer name>:<pattern>` 或 `allow/deny:ip/name:pattern`

例如: `ipFilterRules=allow:ip:127.*`,`allow:name:localhost,deny:ip:*`

> Note that the first rule to match will apply as the example below shows from a client on the localhost

注意，要匹配的第一个规则将应用于本地主机上的客户端，如下面的示例所示

This will Allow the client on localhost be deny clients from any other ip `"allow:name:localhost,deny:ip:"`.

This will deny the client on localhost be allow clients from any other ip `"deny:name:localhost,allow:ip:"`

## 2、Thrift Source

> Listens on Thrift port and receives events from external Thrift client streams. When paired with the built-in ThriftSink on another (previous hop) Flume agent, it can create tiered collection topologies. Thrift source can be configured to start in secure mode by enabling kerberos authentication. agent-principal and agent-keytab are the properties used by the Thrift source to authenticate to the kerberos KDC. Required properties are in bold.

监听 Thrift 端口，并从外部 Thrift 客户端流接收 events。

当与另一个(上一跳) Flume agent 上的内置 ThriftSink 配对时，它可以创建分层集合拓扑。

Thrift source 可以在安全模式下通过启用 kerberos 身份验证配置为开始。

agent-principal 和 agent-keytab 属性被 Thrift source 用来授权 kerberos KDC。

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**channels**	 |      –	   | 
**type**	     |      –	   |    The component type name, needs to be `thrift`【组件类型名称，必须是`thrift`】
**bind**	     |      –	   |    hostname or IP address to listen on【监听的主机名或ip地址】
**port**	     |      –	   |    Port # to bind to【绑定的端口】
threads	         |      –	   |    Maximum number of worker threads to spawn【要生成的工作线程的最大数目】
selector.type	 |             |	 
selector.*	 	 |             |
interceptors	 |      –	   |    Space separated list of interceptors【空格分隔的拦截器列表】
interceptors.*	 |             |	 
ssl	             |    false	   |    Set this to true to enable SSL encryption. If SSL is enabled, you must also specify a “keystore” and a “keystore-password”, either through component level parameters (see below) or as global SSL parameters (see SSL/TLS support section)【为true时，启用ssl加密。如果启用了ssl，必须也指定一个“keystore”和“keystore-password”，要么通过组件级的参数，要么作为全局的ssl参数。】
keystore	     |      –	   |    This is the path to a Java keystore file. If not specified here, then the global keystore will be used (if defined, otherwise configuration error).【这是一个java keystore文件的路径。如果不在这指定，将使用全局的keystore（如果定义的话，否则出现配置错误）】
keystore-password|	    –	   |    The password for the Java keystore. If not specified here, then the global keystore password will be used (if defined, otherwise configuration error).【Java keystore密码。如果不在这指定，将使用全局的keystore密码（如果定义的话，否则出现配置错误）】
keystore-type	 |     JKS	   |    The type of the Java keystore. This can be “JKS” or “PKCS12”. If not specified here, then the global keystore type will be used (if defined, otherwise the default is JKS).【Java keystore类型。可以是“JKS”或“PKCS12”。如果不在这指定，将使用全局的keystore类型（如果定义的话，否则出现默认是JKS）】
exclude-protocols|	   SSLv3   |    Space-separated list of SSL/TLS protocols to exclude. SSLv3 will always be excluded in addition to the protocols specified.【排除的空格分隔的SSL/TLS协议的列表。除了指定的协议，SSLv3总是被排除在外。】
include-protocols|	     –	   |    Space-separated list of SSL/TLS protocols to include. The enabled protocols will be the included protocols without the excluded protocols. If included-protocols is empty, it includes every supported protocols.【包含的空格分隔的SSL/TLS协议的列表。启用的协议将是要包含的协议，没有排除的协议，如果included-protocols为空，它包含每一个支持的协议。】
exclude-cipher-suites|	 –	   |    Space-separated list of cipher suites to exclude.【排除的空格分隔的cipher suites的列表。】
include-cipher-suites|	 –	   |    Space-separated list of cipher suites to include. The enabled cipher suites will be the included cipher suites without the excluded cipher suites.【包含的空格分隔的cipher suites的列表。启用的cipher suites将是要包含的cipher suites，没有排除的cipher suites】
kerberos	         | false   | 	Set to true to enable kerberos authentication. In kerberos mode, agent-principal and agent-keytab are required for successful authentication. The Thrift source in secure mode, will accept connections only from Thrift clients that have kerberos enabled and are successfully authenticated to the kerberos KDC.【设置为true，来启用kerberos身份验证，在kerberos模式下，对成功的身份验证，要求`agent-principal`和`agent-keytab`。在安全模式下，Thrift source将仅接收来自启用kerberos的，并成功给kerberos KDC身份验证的Thrift客户端的连接】
agent-principal      |	 –	   |     The kerberos principal used by the Thrift Source to authenticate to the kerberos KDC.【Thrift Source使用的kerberos principal对kerberos KDC进行身份验证。】
agent-keytab	     |   —	   |     The keytab location used by the Thrift Source in combination with the agent-principal to authenticate to the kerberos KDC.【Thrift Source与agent-principal一起使用的keytab位置，用于对kerberos KDC进行身份验证。】

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = thrift
	a1.sources.r1.channels = c1
	a1.sources.r1.bind = 0.0.0.0
	a1.sources.r1.port = 4141

## 3、Exec Source

> Exec source runs a given Unix command on start-up and expects that process to continuously produce data on standard out (stderr is simply discarded, unless property logStdErr is set to true). If the process exits for any reason, the source also exits and will produce no further data. This means configurations such as `cat [named pipe]` or `tail -F [file]` are going to produce the desired results where as `date` will probably not - the former two commands produce streams of data where as the latter produces a single event and exits.Required properties are in bold.

Exec source 在启动时，运行一个给定的 Unix 命令，那个进程会在标准输出上产生数据。(除非属性 logStdErr 设置为 true，否则 stderr 将被丢弃)

如果这个进程退出，那么 source 也会退出，不再产生数据。这就意味着，像 `cat [named pipe]` 或者 `tail -F [file]` 的这种配置会产生期望的结果，而 `date` 命令不会产生。

前两个命令产生数据流，而后一个命令产生单个事件并退出。

必需的属性以粗体显示。

Property Name    |   Default   | 	Description
---|:---|:---
**channels**	 |     –	   | 
**type**	     |     –	   |    The component type name, needs to be `exec`【组件类型名称，必须是`exec`】
**command**	     |     –	   |    The command to execute【要执行的命令】
shell	         |     –	   |    A shell invocation used to run the command. e.g. `/bin/sh -c`. Required only for commands relying on shell features like wildcards, back ticks, pipes etc.【用于运行命令的shell调用。如`/bin/sh -c`。仅用于依赖于shell特性的命令，如通配符、反引号、管道等。】
restartThrottle  |	  10000	   |    Amount of time (in millis) to wait before attempting a restart【在尝试重启前，等待的时间(毫秒)】
restart	         |     false   |    Whether the executed cmd should be restarted if it dies【如果执行的命令终止，是否应该重新启动】
logStdErr	     |     false   |    Whether the command’s stderr should be logged【命令的stderr是否应该输出】
batchSize	     |      20	   |    The max number of lines to read and send to the channel at a time【一次读取并发送给channel的最大行数，】
batchTimeout	 |      3000   |    Amount of time (in milliseconds) to wait, if the buffer size was not reached, before data is pushed downstream【在数据推到下游前，如果没有达到缓存大小，等待的时间(毫秒)】
selector.type	 |  replicating|	replicating or multiplexing
selector.*	     | 	           |    Depends on the selector.type value
interceptors	 |      –	   |    Space-separated list of interceptors【空格分隔的拦截器列表】
interceptors.*	 |             |

> Warning：The problem with ExecSource and other asynchronous sources is that the source can not guarantee that if there is a failure to put the event into the Channel the client knows about it. In such cases, the data will be lost. As a for instance, one of the most commonly requested features is the tail -F [file]-like use case where an application writes to a log file on disk and Flume tails the file, sending each line as an event. While this is possible, there’s an obvious problem; what happens if the channel fills up and Flume can’t send an event? Flume has no way of indicating to the application writing the log file that it needs to retain the log or that the event hasn’t been sent, for some reason. If this doesn’t make sense, you need only know this: Your application can never guarantee data has been received when using a unidirectional asynchronous interface such as ExecSource! As an extension of this warning - and to be completely clear - there is absolutely zero guarantee of event delivery when using this source. For stronger reliability guarantees, consider the Spooling Directory Source, Taildir Source or direct integration with Flume via the SDK.

警告：ExecSource 和其他异步源的问题是，source 不能保证，客户端知道有一个失败的事件放入了 Channel。

在这种情况下，数据将丢失。例如，最常见的请求特性之一是 `tail -F [file]` 用例，应用程序写入磁盘上的日志文件，Flume tail 文件，将每一行作为 event 发送。

虽然这是可能的，但有一个明显的问题；如果 channel 被填满了，Flume 不能发送一个事件怎么办？

Flume 无法向编写日志文件的应用程序指示它需要保留日志，或者由于某些原因不发送 event。

如果这没有意义，你只需要知道:当使用单向异步接口(比如ExecSource)时，你的应用程序永远不能保证数据已经被接收到!

作为此警告的扩展，并且要完全清楚：在使用此 source 时，event 传输绝对没有任何保证。

为了更强的可靠性保证，可以考虑 Spooling Directory Source、Taildir Source 或 通过 SDK 直接与 Flume 集成。

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = exec
	a1.sources.r1.command = tail -F /var/log/secure
	a1.sources.r1.channels = c1

> The 'shell' config is used to invoke the 'command' through a command shell (such as Bash or Powershell). The 'command' is passed as an argument to 'shell' for execution. This allows the 'command' to use features from the shell such as wildcards, back ticks, pipes, loops, conditionals etc. In the absence of the ‘shell’ config, the 'command' will be invoked directly. Common values for 'shell' : '/bin/sh -c', '/bin/ksh -c', 'cmd /c', 'powershell -Command', etc.

'shell' 配置用来通过一个命令行（Bash or Powershell）来调用 'command'。为了执行 'command'， 它作为一个参数传递给 'shell'。

它允许 'command' 使用 shell 特性，如通配符、反引号、管道、循环、条件语句等。

在 'shell' 配置中，直接调用 'command'。'shell' 常用的值：'/bin/sh -c'、'/bin/ksh -c'、 'cmd /c'、'powershell -Command' 等

	a1.sources.tailsource-1.type = exec
	a1.sources.tailsource-1.shell = /bin/bash -c
	a1.sources.tailsource-1.command = for i in /path/*.txt; do cat $i; done

## 4、JMS Source 【待做】

## 5、Spooling Directory Source

> This source lets you ingest data by placing files to be ingested into a “spooling” directory on disk. This source will watch the specified directory for new files, and will parse events out of new files as they appear. The event parsing logic is pluggable. After a given file has been fully read into the channel, completion by default is indicated by renaming the file or it can be deleted or the trackerDir is used to keep track of processed files.

这个 source 可以让你将要接收的文件放置到磁盘上的 spooling 目录下来接收数据。

该 source 会监控指定目录下的新文件，然后当新文件出现时，就解析其中的 events。解析 events 的逻辑是可插拔的。

在将一个给定的文件完全读入到 channel 后，默认是通过重命名文件来表示完成，或者可以删除该文件，或者使用 trackerDir 追踪已处理的文件。

> Unlike the Exec source, this source is reliable and will not miss data, even if Flume is restarted or killed. In exchange for this reliability, only immutable, uniquely-named files must be dropped into the spooling directory. Flume tries to detect these problem conditions and will fail loudly if they are violated:

不同于 Exec source，spooling source 是可靠的，即使 flume 被重启或被杀死时，不会失去数据。

为了获得这种可靠性，必须只将不可变的、唯一命名的文件放入 spooling 目录。

Flume 试图检测这些问题条件，如果违反，将会提示失败：

> If a file is written to after being placed into the spooling directory, Flume will print an error to its log file and stop processing.

（1）如果一个文件在放到 spooling 目录后，又写入了数据，flume 将会在日志文件中打印错误，停止处理。

> If a file name is reused at a later time, Flume will print an error to its log file and stop processing.

（2）如果一个文件名在后面被重复使用，Flume 将会在日志文件中打印错误，停止处理。 

> To avoid the above issues, it may be useful to add a unique identifier (such as a timestamp) to log file names when they are moved into the spooling directory.

为了避免上述情况，在移动到 spooling 目录下时，给日志文件名添加一个唯一的标识符（如时间戳）。

> Despite the reliability guarantees of this source, there are still cases in which events may be duplicated if certain downstream failures occur. This is consistent with the guarantees offered by other Flume components.

尽管该 source 具有可靠性保证，但如果某些下游出现故障，仍然存在 events 可能重复的情况。这与其他 Flume 组件提供的保证是一致的。

Property Name    |   Default   | 	Description
---|:---|:---
**channels**	 |      –	   |
**type**	     |      –	   |    The component type name, needs to be `spooldir`.【组件类型名称，必须是`spooldir`】
**spoolDir**	 |      –	   |    The directory from which to read files from.【读取文件的目录】
fileSuffix	     | .COMPLETED  |	Suffix to append to completely ingested files【完成后，追加到接收文件的后缀】
deletePolicy	 |    never	   |    When to delete completed files: `never` or `immediate`【什么时候删除完成的文件】
fileHeader	     |    false	   |    Whether to add a header storing the absolute path filename.【是否添加一个存储绝对路径的文件名的header】
fileHeaderKey	 |     file	   |    Header key to use when appending absolute path filename to event header.【当追加一个存储绝对路径的fileHeaderKey时，使用的Header key】
basenameHeader	 |     false   |    Whether to add a header storing the basename of the file.【是否添加一个存储文件basename的header】
basenameHeaderKey|	 basename  | 	Header Key to use when appending basename of file to event header.【当追加一个文件basename给事件header时，使用的Header key】
includePattern	 |   `^.*$`    |	Regular expression specifying which files to include. It can used together with `ignorePattern`. If a file matches both `ignorePattern` and `includePattern` regex, the file is ignored.【指定要包含文件的正则表达式。和`ignorePattern`一起使用。如果一个文件同时匹配`ignorePattern`和`includePattern`正则，那么该文件就被忽略。】
ignorePattern	 |     `^$`	   |    Regular expression specifying which files to ignore (skip). It can used together with `includePattern`. If a file matches both `ignorePattern` and `includePattern` regex, the file is ignored.【指定要忽略\跳过的文件的正则表达式。和`includePattern`一起使用。如果一个文件同时匹配`ignorePattern`和`includePattern`正则，那么该文件就被忽略。】
trackerDir	     | .flumespool |	Directory to store metadata related to processing of files. If this path is not an absolute path, then it is interpreted as relative to the spoolDir.【存储与文件处理相关的元数据的目录。如果这个路径不是一个绝对路径，那么就将其当作相对spoolDir的相对路径】
trackingPolicy	 |    rename   |    The tracking policy defines how file processing is tracked. It can be “rename” or “tracker_dir”. This parameter is only effective if the deletePolicy is “never”. “rename” - After processing files they get renamed according to the fileSuffix parameter. “tracker_dir” - Files are not renamed but a new empty file is created in the trackerDir. The new tracker file name is derived from the ingested one plus the fileSuffix.【追踪策略定义了如何追踪正在处理的文件。可以是`rename`或`tracker_dir`。只有`deletePolicy`为`never`时，此参数才有效。`rename`是在处理文件后，根据`filesufix`参数对文件进行重命名。`tracker_dir`是文件不被重命名，但在`trackerDir`中创建一个新的空文件。新的追踪文件名来自于输入的文件名加上`filesufix`。】
consumeOrder     |	oldest	   |    In which order files in the spooling directory will be consumed `oldest`,`youngest` and `random`. In case of `oldest` and `youngest`, the last modified time of the files will be used to compare the files. In case of a tie, the file with smallest lexicographical order will be consumed first. In case of `random` any file will be picked randomly. When using `oldest` and `youngest` the whole directory will be scanned to pick the oldest/youngest file, which might be slow if there are a large number of files, while using `random` may cause old files to be consumed very late if new files keep coming in the spooling directory.【spooling目录中的文件被消费的顺序，有`oldest`、`youngest`和`random`。对于`oldest`和`youngest`情况，使用文件的最后修改时间来比较文件。在出现并列的情况下，字典顺序最小的文件将首先被消费。在`random`情况下，任何文件将被随机选取。当使用`oldest`和`youngest`，将扫描整个目录选择最古老/最小的文件，如果有大量的文件，这可能比较慢，在使用`random`时，可能导致如果新文件不断在出现在spooling目录中，旧文件会很晚才被消费。】
pollDelay	     |     500	   |     Delay (in milliseconds) used when polling for new files.【轮询新文件时使用的延迟(毫秒)。】
recursiveDirectorySearch|false |     Whether to monitor sub directories for new files to read.【对于读取的新文件，是否监控子目录。】
maxBackoff	     |     4000	   |     The maximum time (in millis) to wait between consecutive attempts to write to the channel(s) if the channel is full. The source will start at a low backoff and increase it exponentially each time the channel throws a ChannelException, upto the value specified by this parameter.【如果channel满了，在两次连续尝试写入的最大时间间隔。source将从较低的回退开始，并在channel每次抛出ChannelException时以指数方式增加回退，直到达到此参数指定的值】
batchSize 	     |      100	   |     Granularity at which to batch transfer to the channel【批量传输到channel的粒度】
inputCharset     |     UTF-8   |     Character set used by deserializers that treat the input file as text.【反序列化器使用的将输入文件视为文本的字符集。】
decodeErrorPolicy|	   `FAIL`  |     What to do when we see a non-decodable character in the input file. `FAIL`: Throw an exception and fail to parse the file. `REPLACE`: Replace the unparseable character with the “replacement character” char, typically Unicode U+FFFD. `IGNORE`: Drop the unparseable character sequence.【当我们在输入文件中看到一个不可解码的字符时该怎么做。`FAIL`:抛出异常，无法解析文件。`REPLACE`:用"replacement character"替换不可解析字符，通常是Unicode U+FFFD。`IGNORE`:删除不可解析的字符序列。】
deserializer	 |     `LINE`  |      Specify the deserializer used to parse the file into events. Defaults to parsing each line as an event. The class specified must implement `EventDeserializer.Builder`.【指定用来解析文件成事件的反序列化器。默认解析每行为一个事件。指定的类必须实现`EventDeserializer.Builder`】
deserializer.*	 |    	       |      Varies per event deserializer.
bufferMaxLines	 |      –	   |      (Obselete) This option is now ignored.
bufferMaxLineLength|  5000	   |      (Deprecated) Maximum length of a line in the commit buffer. Use deserializer.maxLineLength instead.
selector.type	 | replicating |	  replicating or multiplexing
selector.*	     |    	       |      Depends on the selector.type value
interceptors	 |     –	   |      Space-separated list of interceptors
interceptors.*	 | 	           |

> Example for an agent named agent-1:

	a1.channels = ch-1
	a1.sources = src-1

	a1.sources.src-1.type = spooldir
	a1.sources.src-1.channels = ch-1
	a1.sources.src-1.spoolDir = /var/log/apache/flumeSpool
	a1.sources.src-1.fileHeader = true

### 5.1、Event Deserializers

> The following event deserializers ship with Flume.

事件反序列化器

#### 5.1.1、LINE

> This deserializer generates one event per line of text input.

对文本输入的每行生成一个 event。

Property Name    |   Default   | 	Description
---|:---|:---
deserializer.maxLineLength  |  2048   |  Maximum number of characters to include in a single event. If a line exceeds this length, it is truncated, and the remaining characters on the line will appear in a subsequent event.【在一个事件中，包含的字符的最大数量。如果一行超过了这个长度，就会被截断，这行的剩余字符将出现在后面】
deserializer.outputCharset  |  UTF-8  |  Charset to use for encoding events put into the channel.【用于编码事件的字符集，然后放入channel】

#### 5.1.2、AVRO

> This deserializer is able to read an Avro container file, and it generates one event per Avro record in the file. Each event is annotated with a header that indicates the schema used. The body of the event is the binary Avro record data, not including the schema or the rest of the container file elements.

这个反序列化器能够读取一个 Avro 容器文件，并把文件中的每个 Avro 记录生成一个 event。

每个 event 都有一个 header，表示使用的 schema。

event 的主体是二进制 Avro 记录数据，不包括 schema 或容器文件元素的其余部分。

> Note that if the spool directory source must retry putting one of these events onto a channel (for example, because the channel is full), then it will reset and retry from the most recent Avro container file sync point. To reduce potential event duplication in such a failure scenario, write sync markers more frequently in your Avro input files.

如果 spool directory source 必须重新尝试将其中的一个 events 放入一个 channel 中（比如，channel满了），那么它会重新设置，并从最新的 Avro 容器文件同步点开始重试。

为了减少这种失败场景中的潜在 event 重复，可以在 Avro 输入文件中更频繁地写入同步标记。

Property Name    |   Default   | 	Description
---|:---|:---
deserializer.schemaType	 |  HASH   | How the schema is represented. By default, or when the value `HASH` is specified, the Avro schema is hashed and the hash is stored in every event in the event header “flume.avro.schema.hash”. If `LITERAL` is specified, the JSON-encoded schema itself is stored in every event in the event header “flume.avro.schema.literal”. Using LITERAL mode is relatively inefficient compared to HASH mode.【如果表示schema。默认情况下，当指定了`HASH`，Avro schema就会被哈希，哈希值被存储在事件header“flume.avro.schema.hash”中的每个事件中。如果指定了`LITERAL`，json编码的schema本身将存储在事件header“flume.avro.schema.literal”中的每个事件中。与`HASH`模式相比，使用`LITERAL`模式效率相对较低】

#### 5.1.1、BlobDeserializer

> This deserializer reads a Binary Large Object (BLOB) per event, typically one BLOB per file. For example a PDF or JPG file. Note that this approach is not suitable for very large objects because the entire BLOB is buffered in RAM.

这个反序列化器读取每个 event 的一个二进制大对象(BLOB)，通常每个文件是一个 BLOB。例如一个 PDF 或 JPG 文件。

注意，这种方法不适用于非常大的对象，因为整个 BLOB 都缓存在 RAM 中。

Property Name    |   Default   | 	Description
---|:---|:---
deserializer	 |      –	   |   The FQCN of this class: `org.apache.flume.sink.solr.morphline.BlobDeserializer$Builder`
deserializer.maxBlobLength |	100000000 |  The maximum number of bytes to read and buffer for a given request【对一个给定的请求，读取和缓存的字节的最大数量】

## 6、Taildir Source

> Note This source is provided as a preview feature. It does not work on Windows.

> Watch the specified files, and tail them in nearly real-time once detected new lines appended to the each files. If the new lines are being written, this source will retry reading them in wait for the completion of the write.

注意：此 source 作为预览功能。 它不适用于 Windows。

监控指定的文件，并且，一旦有新的行添加到每个文件，近实时的读取它们。如果新行正在写入，这个 source 将在写操作完成时重新尝试读取它们。

> This source is reliable and will not miss data even when the tailing files rotate. It periodically writes the last read position of each files on the given position file in JSON format. If Flume is stopped or down for some reason, it can restart tailing from the position written on the existing position file.

该 source 是可靠的，即使在循环读取文件时，也不会丢失数据。

它定期以 JSON 格式将每个文件的最后读取位置写入到给定的位置文件。

如果 Flume 由于某种原因停止或关闭，则可以从现有位置文件上的写入位置重新开始读取。【断点续传】

> In other use case, this source can also start tailing from the arbitrary position for each files using the given position file. When there is no position file on the specified path, it will start tailing from the first line of each files by default.

该 source 也可以使用给定的位置文件从任意位置开始读取每个文件。当在指定路径上没有位置文件时，默认情况下，将从每个文件的第一行开始读取。

> Files will be consumed in order of their modification time. File with the oldest modification time will be consumed first.

文件将按其修改时间顺序消费。使用最老的修改时间的文件将首先被消费。

> This source does not rename or delete or do any modifications to the file being tailed. Currently this source does not support tailing binary files. It reads text files line by line.

此 source 不会对正在读取的文件重命名，或删除，或做任何修改。当前不支持读取二进制文件。它一行行地读取文本文件。

Property Name    |   Default   | 	Description
---|:---|:---
**channels**	 |     –	   | 
**type**	     |     –	   |    The component type name, needs to be `TAILDIR`.【组件类型名称，必须是`TAILDIR`】
**filegroups**	 |     –	   |    Space-separated list of file groups. Each file group indicates a set of files to be tailed.【空格分隔的文件分组列表。每个文件分组表示要读取的一组文件。】
**`filegroups.<filegroupName>`** |	–	|Absolute path of the file group. Regular expression (and not file system patterns) can be used for filename only.【文件分组的绝对路径。只能对文件名使用正则表达式(而不是文件系统模式)。】
positionFile     | `~/.flume/taildir_position.json`	 |  File in JSON format to record the inode, the absolute path and the last position of each tailing file.【记录inode、绝对路径和每个读取文件的最后位置的JSON格式的文件。】
`headers.<filegroupName>.<headerKey>` |	–	|  Header value which is the set with header key. Multiple headers can be specified for one file group.【Header值，它是用Header键设置的。可以为一个文件分组指定多个Header。】
byteOffsetHeader |	  false	   |    Whether to add the byte offset of a tailed line to a header called ‘byteoffset’.【是否给一个header添加一个读取行的字节偏移量，称为‘byteoffset’】
skipToEnd        |    false    | 	Whether to skip the position to EOF in the case of files not written on the position file.【如果该位置文件上没有写文件，是否跳过该位置到EOF。】
idleTimeout	     |   120000	   |    Time (ms) to close inactive files. If the closed file is appended new lines to, this source will automatically re-open it.【关闭非活跃文件的时间(ms)。如果关闭的文件追加了新行，此源将自动重新打开它。】
writePosInterval |	  3000	   |    Interval time (ms) to write the last position of each file on the position file.【在位置文件上，写每个文件的最后位置的间隔时间。】
batchSize	     |     100	   |    Max number of lines to read and send to the channel at a time. Using the default is usually fine.【同时读取并发送给channel的最大行数。通常使用默认的即可】
maxBatchCount	 | Long.MAX_VALUE | 	Controls the number of batches being read consecutively from the same file. If the source is tailing multiple files and one of them is written at a fast rate, it can prevent other files to be processed, because the busy file would be read in an endless loop. In this case lower this value.【控制从同一文件连续读取批次的数量。如果源正在读取多个文件，其中一个以较快的速度写入，它可以阻止其他文件被处理，因为繁忙的文件会无终止地读取。在这种情况下，减低这个值。】
backoffSleepIncrement  |	1000  |	The increment for time delay before reattempting to poll for new data, when the last attempt did not find any new data.【当上次尝试没有发现任何新数据时，在重新尝试轮询新数据之前的时间延迟的增量。】
maxBackoffSleep	 |     5000	      | The max time delay between each reattempt to poll for new data, when the last attempt did not find any new data.【当上次尝试没有找到任何新数据时，每次重新尝试轮询新数据之间的最大时间延迟。】
cachePatternMatching   |   true   |	Listing directories and applying the filename regex pattern may be time consuming for directories containing thousands of files. Caching the list of matching files can improve performance. The order in which files are consumed will also be cached. Requires that the file system keeps track of modification times with at least a 1-second granularity.【对于包含成千上万个文件的目录来说，列出目录并应用文件名正则表达式模式可能非常耗时。缓存匹配的文件列表可以提高性能。消费文件的顺序也将被缓存。要求文件系统以至少1秒的粒度跟踪修改时间。】
fileHeader       |     false      |  Whether to add a header storing the absolute path filename.【是否添加一个存储绝对路径的文件名的header】
fileHeaderKey    |      file      |  Header key to use when appending absolute path filename to event header.【当追加一个存储绝对路径的fileHeaderKey时，使用的Header key】

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = TAILDIR
	a1.sources.r1.channels = c1
	a1.sources.r1.positionFile = /var/log/flume/taildir_position.json
	a1.sources.r1.filegroups = f1 f2
	a1.sources.r1.filegroups.f1 = /var/log/test1/example.log
	a1.sources.r1.headers.f1.headerKey1 = value1
	a1.sources.r1.filegroups.f2 = /var/log/test2/.*log.*
	a1.sources.r1.headers.f2.headerKey1 = value2
	a1.sources.r1.headers.f2.headerKey2 = value2-2
	a1.sources.r1.fileHeader = true
	a1.sources.ri.maxBatchCount = 1000

## 7、Twitter 1% firehose Source (experimental) 【待做】

> Warning:This source is highly experimental and may change between minor versions of Flume. Use at your own risk.

> Experimental source that connects via Streaming API to the 1% sample twitter firehose, continously downloads tweets, converts them to Avro format and sends Avro events to a downstream Flume sink. Requires the consumer and access tokens and secrets of a Twitter developer account. Required properties are in bold.

Property Name     |   Default   | 	Description
---|:---|:---
**channels**	  |     –	   | 
**type**	      |     –	   |    The component type name, needs to be org.apache.flume.source.twitter.TwitterSource
**consumerKey**	  |     –	   |    OAuth consumer key
**consumerSecret**|	    –	   |    OAuth consumer secret
**accessToken**	  |     –	   |    OAuth access token
**accessTokenSecret**	–	   |    OAuth token secret
maxBatchSize	  |    1000	   |    Maximum number of twitter messages to put in a single batch
maxBatchDurationMillis |  1000	|   Maximum number of milliseconds to wait before closing a batch

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = org.apache.flume.source.twitter.TwitterSource
	a1.sources.r1.channels = c1
	a1.sources.r1.consumerKey = YOUR_TWITTER_CONSUMER_KEY
	a1.sources.r1.consumerSecret = YOUR_TWITTER_CONSUMER_SECRET
	a1.sources.r1.accessToken = YOUR_TWITTER_ACCESS_TOKEN
	a1.sources.r1.accessTokenSecret = YOUR_TWITTER_ACCESS_TOKEN_SECRET
	a1.sources.r1.maxBatchSize = 10
	a1.sources.r1.maxBatchDurationMillis = 200

## 8、Kafka Source

> Kafka Source is an Apache Kafka consumer that reads messages from Kafka topics. If you have multiple Kafka sources running, you can configure them with the same Consumer Group so each will read a unique set of partitions for the topics. This currently supports Kafka server releases 0.10.1.0 or higher. Testing was done up to 2.0.1 that was the highest avilable version at the time of the release.

Kafka Source 是一个 Apache Kafka 消费者，它从 Kafka topics 中读取消息。如果你有多个 Kafka sources 正在运行，你可以将它们配置成相同的消费组，这样每个 sources 将读取 topics 的一组唯一的分区。

目前支持 Kafka 服务器版本 0.10.1.0 或更高版本。测试一直进行到 2.0.1，这是发行时的最高可用版本。

Property Name             |  Default	|   Description
---|:---|:---
**channels**	          |     –	    |  
**type**	              |     –	    |   The component type name, needs to be `org.apache.flume.source.kafka.KafkaSource`【组件类型名称，需要是`org.apache.flume.source.kafka.KafkaSource`】
**kafka.bootstrap.servers**|     –	    |   List of brokers in the Kafka cluster used by the source【这个source使用的kafka集群中的brokers列表】
kafka.consumer.group.id	  |   flume	    |   Unique identified of consumer group. Setting the same id in multiple sources or agents indicates that they are part of the same consumer group【消费者组的唯一标识。在多个source和agents中设置相同的id，表示它们是相同消费者组的一部分】
**kafka.topics**	      |     –	    |   Comma-separated list of topics the kafka consumer will read messages from.【kafka消费者将从这个topics的列表中读取，逗号分隔。】
**kafka.topics.regex**	  |     –	    |   Regex that defines set of topics the source is subscribed on. This property has higher priority than `kafka.topics` and overrides `kafka.topics` if exists.【定义了source订阅的topics集合的正则。这个属性具有比`kafka.topics`更高的优先级，覆盖`kafka.topics`】
batchSize	              |    1000	    |   Maximum number of messages written to Channel in one batch【在一个批次中，写入到channel中的消息的最大数量】
batchDurationMillis	      |    1000	    |   Maximum time (in ms) before a batch will be written to Channel The batch will be written whenever the first of size and time will be reached.【一个批次写入channel前的最大时间(单位为ms)。无论是大小还是时间首先达到，批次就会被写入。】
backoffSleepIncrement	  |    1000	    |   Initial and incremental wait time that is triggered when a Kafka Topic appears to be empty. Wait period will reduce aggressive pinging of an empty Kafka Topic. One second is ideal for ingestion use cases but a lower value may be required for low latency operations with interceptors.【Kafka Topic为空时，触发的初始等待时间和增量等待时间。等待周期将减少对空Kafka Topic的频繁ping。对于接收用例，1秒是理想的，但是对于使用拦截器的低延迟操作，可能需要更低的值。】
maxBackoffSleep	          |    5000	    |   Maximum wait time that is triggered when a Kafka Topic appears to be empty. Five seconds is ideal for ingestion use cases but a lower value may be required for low latency operations with interceptors.【Kafka Topic为空时，触发的最大等待时间。对于接收用例，5秒是理想的，但是对于使用拦截器的低延迟操作，可能需要更低的值。】
useFlumeEventFormat	      |    false	|   By default events are taken as bytes from the Kafka topic directly into the event body. Set to true to read events as the Flume Avro binary format. Used in conjunction with the same property on the KafkaSink or with the parseAsFlumeEvent property on the Kafka Channel this will preserve any Flume headers sent on the producing side.【默认情况下，事件以字节的形式从Kafka topic直接进入事件体。设置为true，则以Flume Avro二进制格式读取事件。与KafkaSink上的相同属性或Kafka Channel上的parseAsFlumeEvent属性一起使用，这将保留在生产端发送的任何Flume headers信息。】
setTopicHeader	          |     true	|   When set to true, stores the topic of the retrieved message into a header, defined by the `topicHeader` property.【当设置为true时将接收消息的topic存入到一个header，默认由`topicHeader`属性定义。】
topicHeader	              |     topic	|   Defines the name of the header in which to store the name of the topic the message was received from, if the `setTopicHeader` property is set to `true`. Care should be taken if combining with the Kafka Sink `topicHeader` property so as to avoid sending the message back to the same topic in a loop.【当`setTopicHeader`属性设置为true，定义的header的名称，在这个header中存储了接收到的消息的topic的名称。如果结合使用Kafka Sink `topicHeader`属性，应该小心避免在循环中将消息发送回同一个topic。】
kafka.consumer.security.protocol |	PLAINTEXT	|  Set to SASL_PLAINTEXT, SASL_SSL or SSL if writing to Kafka using some level of security. See below for additional info on secure setup.【如果写入到的kafka使用了一些安全级别，设为SASL_PLAINTEXT、SASL_SSL或SSL】
more consumer security props |	 	|  If using SASL_PLAINTEXT, SASL_SSL or SSL refer to Kafka security for additional properties that need to be set on consumer.【如果设置的SASL_PLAINTEXT、SASL_SSL或SSL引用了kafka安全的一些属性，需要在消费者上设置。】
Other Kafka Consumer Properties |	–	|  These properties are used to configure the Kafka Consumer. Any consumer property supported by Kafka can be used. The only requirement is to prepend the property name with the prefix `kafka.consumer`. For example: `kafka.consumer.auto.offset.reset`【这些属性用来配置kafka消费者。Kafka支持的任何的消费者属性可以被使用。仅有的要求是属性以`kafka.consumer`为前缀。】

> Note:The Kafka Source overrides two Kafka consumer parameters: auto.commit.enable is set to “false” by the source and every batch is committed. Kafka source guarantees at least once strategy of messages retrieval. The duplicates can be present when the source starts. The Kafka Source also provides defaults for the key.deserializer(org.apache.kafka.common.serialization.StringSerializer) and value.deserializer(org.apache.kafka.common.serialization.ByteArraySerializer). Modification of these parameters is not recommended.

注意：Kafka Source 覆盖了两个 Kafka 消费者参数:`auto.commit.enable` 被 source 设置为 false，并且每个批次都被提交。

Kafka source 保证了至少一次的消息检索策略。

副本可以在 source 启动时出现。

Kafka Source 也为 `key.deserializer`((org.apache.kafka.common.serialization.StringSerializer)和 `value.deserializer`(org.apache.kafka.common.serialization.ByteArraySerializer)提供了默认值。不建议修改这些参数。

> Deprecated Properties

弃用属性：

Property Name             |  Default	|   Description
---|:---|:---
topic	                  |     –	    |  Use kafka.topics
groupId	                  |    flume	|  Use kafka.consumer.group.id
zookeeperConnect	      |     –	    |  Is no longer supported by kafka consumer client since 0.9.x. Use kafka.bootstrap.servers to establish connection with kafka cluster
migrateZookeeperOffsets   |    true	    |  When no Kafka stored offset is found, look up the offsets in Zookeeper and commit them to Kafka. This should be true to support seamless Kafka client migration from older versions of Flume. Once migrated this can be set to false, though that should generally not be required. If no Zookeeper offset is found, the Kafka configuration kafka.consumer.auto.offset.reset defines how offsets are handled. Check Kafka documentation for details

> Example for topic subscription by comma-separated topic list.

逗号分隔的订阅的 topic 列表：

	tier1.sources.source1.type = org.apache.flume.source.kafka.KafkaSource
	tier1.sources.source1.channels = channel1
	tier1.sources.source1.batchSize = 5000
	tier1.sources.source1.batchDurationMillis = 2000
	tier1.sources.source1.kafka.bootstrap.servers = localhost:9092
	tier1.sources.source1.kafka.topics = test1, test2
	tier1.sources.source1.kafka.consumer.group.id = custom.g.id

> Example for topic subscription by regex

正则表示的订阅的 topic 

	tier1.sources.source1.type = org.apache.flume.source.kafka.KafkaSource
	tier1.sources.source1.channels = channel1
	tier1.sources.source1.kafka.bootstrap.servers = localhost:9092
	tier1.sources.source1.kafka.topics.regex = ^topic[0-9]$
	# the default kafka.consumer.group.id=flume is used

### 8.1、Security and Kafka Source:

> Secure authentication as well as data encryption is supported on the communication channel between Flume and Kafka. For secure authentication SASL/GSSAPI (Kerberos V5) or SSL (even though the parameter is named SSL, the actual protocol is a TLS implementation) can be used from Kafka version 0.9.0.

Flume 和 Kafka 之间的通信通道支持安全认证和数据加密。

对于安全身份验证，可以在 Kafka 0.9.0 版本中使用 SASL/GSSAPI(Kerberos V5 )或 SSL(即使参数名为 SSL，实际的协议是 TLS 实现)。

> As of now data encryption is solely provided by SSL/TLS.

目前数据加密仅由 SSL/TLS 提供。

> Setting kafka.consumer.security.protocol to any of the following value means:

设置 `kafka.consumer.security.protocol` 为以下的任何一个值:

- SASL_PLAINTEXT - Kerberos or plaintext authentication with no data encryption
- SASL_SSL - Kerberos or plaintext authentication with data encryption
- SSL - TLS based encryption with optional authentication.

> Warning：There is a performance degradation when SSL is enabled, the magnitude of which depends on the CPU type and the JVM implementation. Reference: [Kafka security overview](http://kafka.apache.org/documentation#security_overview) and the jira for tracking this issue: [KAFKA-2561](https://issues.apache.org/jira/browse/KAFKA-2561)

当启用 SSL 时，性能会下降，下降程度取决于 CPU 类型和 JVM 实现。

### 8.2、TLS and Kafka Source:

> Please read the steps described in [Configuring Kafka Clients SSL](http://kafka.apache.org/documentation#security_configclients) to learn about additional configuration settings for fine tuning for example any of the following: security provider, cipher suites, enabled protocols, truststore or keystore types.

请阅读 Configuring Kafka Clients SSL 中描述的步骤，以了解额外的配置调调，例如以下的任何一个:安全提供者，加密套件，启用协议，信任存储或密钥存储类型。

> Example configuration with server side authentication and data encryption.

服务器端身份认证和数据加密的配置示例。

	a1.sources.source1.type = org.apache.flume.source.kafka.KafkaSource
	a1.sources.source1.kafka.bootstrap.servers = kafka-1:9093,kafka-2:9093,kafka-3:9093
	a1.sources.source1.kafka.topics = mytopic
	a1.sources.source1.kafka.consumer.group.id = flume-consumer
	a1.sources.source1.kafka.consumer.security.protocol = SSL
	# optional, the global truststore can be used alternatively
	a1.sources.source1.kafka.consumer.ssl.truststore.location=/path/to/truststore.jks
	a1.sources.source1.kafka.consumer.ssl.truststore.password=<password to access the truststore>

> Specyfing the truststore is optional here, the global truststore can be used instead. For more details about the global SSL setup, see the [SSL/TLS](http://flume.apache.org/FlumeUserGuide.html#ssl-tls-support) support section.

在这里指定的 truststore 是可选的，可以使用全局 truststore。有关全局 SSL 设置的更多细节，请参见 SSL/TLS 支持部分。

> Note: By default the property ssl.endpoint.identification.algorithm is not defined, so hostname verification is not performed. In order to enable hostname verification, set the following properties

注意：默认情况下，不定义 `ssl.endpoint.identification.algorithm` 属性，所以不执行主机名验证。

为了启用主机名验证，设置以下属性：

	a1.sources.source1.kafka.consumer.ssl.endpoint.identification.algorithm=HTTPS

> Once enabled, clients will verify the server’s fully qualified domain name (FQDN) against one of the following two fields:

一旦启用，客户端将根据以下两个字段之一验证服务器的完全限定域名(FQDN):

1.Common Name (CN) [https://tools.ietf.org/html/rfc6125#section-2.3](https://tools.ietf.org/html/rfc6125#section-2.3)

2.Subject Alternative Name (SAN) [https://tools.ietf.org/html/rfc5280#section-4.2.1.6](https://tools.ietf.org/html/rfc5280#section-4.2.1.6)

> If client side authentication is also required then additionally the following needs to be added to Flume agent configuration or the global SSL setup can be used (see [SSL/TLS support](http://flume.apache.org/FlumeUserGuide.html#ssl-tls-support) section). Each Flume agent has to have its client certificate which has to be trusted by Kafka brokers either individually or by their signature chain. Common example is to sign each client certificate by a single Root CA which in turn is trusted by Kafka brokers.

如果还需要客户端身份验证，那么还需要将以下内容添加到 Flume agent 配置中，或者可以使用全局 SSL 设置。

每个 Flume agent 必须有自己的客户端证书，该证书必须被 Kafka brokers 单独或通过其签名链信任。

常见的例子是通过一个被 Kafka brokers 信任的 Root CA 对每个客户端证书进行签名。

	# optional, the global keystore can be used alternatively
	a1.sources.source1.kafka.consumer.ssl.keystore.location=/path/to/client.keystore.jks
	a1.sources.source1.kafka.consumer.ssl.keystore.password=<password to access the keystore>

> If keystore and key use different password protection then ssl.key.password property will provide the required additional secret for both consumer keystores:

如果 keystore 和 key 使用不同的密码保护，那么 `ssl.key.password` 属性将为两个消费者 keystores 提供所需的额外加密:

	a1.sources.source1.kafka.consumer.ssl.key.password=<password to access the key>

### 8.3、Kerberos and Kafka Source:

> To use Kafka source with a Kafka cluster secured with Kerberos, set the consumer.security.protocol properties noted above for consumer. The Kerberos keytab and principal to be used with Kafka brokers is specified in a JAAS file’s “KafkaClient” section. “Client” section describes the Zookeeper connection if needed. See [Kafka doc](http://kafka.apache.org/documentation.html#security_sasl_clientconfig) for information on the JAAS file contents. The location of this JAAS file and optionally the system wide kerberos configuration can be specified via JAVA_OPTS in flume-env.sh:

为了使用 Kerberos 安全保护的 Kafka 集群的 Kafka source，为消费者设置`consumer.security.protocol` 属性。

与 Kafka broker 一起使用的 Kerberos keytab 和 principal 是在 JAAS 文件的 “kafkclient” 部分指定的。如果需要，“Client” 部分描述 Zookeeper 连接。

可以通过 flume-env.sh 中的 JAVA_OPTS 指定这个 JAAS 文件的位置以及系统范围的 kerberos 配置(可选):

	JAVA_OPTS="$JAVA_OPTS -Djava.security.krb5.conf=/path/to/krb5.conf"
	JAVA_OPTS="$JAVA_OPTS -Djava.security.auth.login.config=/path/to/flume_jaas.conf"

> Example secure configuration using SASL_PLAINTEXT:

使用 `SASL_PLAINTEXT` 的安全配置示例：

	a1.sources.source1.type = org.apache.flume.source.kafka.KafkaSource
	a1.sources.source1.kafka.bootstrap.servers = kafka-1:9093,kafka-2:9093,kafka-3:9093
	a1.sources.source1.kafka.topics = mytopic
	a1.sources.source1.kafka.consumer.group.id = flume-consumer
	a1.sources.source1.kafka.consumer.security.protocol = SASL_PLAINTEXT
	a1.sources.source1.kafka.consumer.sasl.mechanism = GSSAPI
	a1.sources.source1.kafka.consumer.sasl.kerberos.service.name = kafka

> Example secure configuration using SASL_SSL:

使用 `SASL_SSL` 的安全配置示例：

	a1.sources.source1.type = org.apache.flume.source.kafka.KafkaSource
	a1.sources.source1.kafka.bootstrap.servers = kafka-1:9093,kafka-2:9093,kafka-3:9093
	a1.sources.source1.kafka.topics = mytopic
	a1.sources.source1.kafka.consumer.group.id = flume-consumer
	a1.sources.source1.kafka.consumer.security.protocol = SASL_SSL
	a1.sources.source1.kafka.consumer.sasl.mechanism = GSSAPI
	a1.sources.source1.kafka.consumer.sasl.kerberos.service.name = kafka
	# optional, the global truststore can be used alternatively
	a1.sources.source1.kafka.consumer.ssl.truststore.location=/path/to/truststore.jks
	a1.sources.source1.kafka.consumer.ssl.truststore.password=<password to access the truststore>

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

## 9、NetCat TCP Source

> A netcat-like source that listens on a given port and turns each line of text into an event. Acts like `nc -k -l [host] [port]`. In other words, it opens a specified port and listens for data. The expectation is that the supplied data is newline separated text. Each line of text is turned into a Flume event and sent via the connected channel.

一种类似 netcat 的 source，它监听给定端口，并将文本的每行转换为一个 event，行为像 `nc -k -l [host] [port]`。换句话说，它打开一个指定的端口，并监听数据。

期望提供的数据是换行符分隔的文本。文本的每一行都被转换成一个 Flume event，并通过连接的 channel 发送。

必需的属性以粗体显示。

> Required properties are in bold.

Property Name   |  Default	|   Description
---|:---|:---
**channels**	|     –	    |
**type**	    |     –	    |   The component type name, needs to be `netcat`【组件类型的名称，需要是`netcat`】
**bind**	    |     –	    |   Host name or IP address to bind to【绑定的主机名或ip地址】
**port**	    |     –	    |   Port # to bind to【绑定的端口】
max-line-length	|    512	|   Max line length per event body (in bytes)【每个事件主机的最大行长度（字节）】
ack-every-event	|    true	|   Respond with an “OK” for every event received【是否对每个接收的事件以“OK”响应】
selector.type	|replicating|	replicating or multiplexing
selector.*	 	|           |   Depends on the selector.type value
interceptors	|     –	    |   Space-separated list of interceptors
interceptors.*	|           |	 

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = netcat
	a1.sources.r1.bind = 0.0.0.0
	a1.sources.r1.port = 6666
	a1.sources.r1.channels = c1

## 10、NetCat UDP Source

> As per the original Netcat (TCP) source, this source that listens on a given port and turns each line of text into an event and sent via the connected channel. Acts like `nc -u -k -l [host] [port]`.Required properties are in bold.

根据原始的 Netcat (TCP) source，该 source 监听给定端口，并将文本的每行转换为 event，并通过连接的 channel 发送。行为类似`nc -u -k -l [host] [port]`。

必需的属性以粗体显示。

Property Name   |  Default	|   Description
---|:---|:---
**channels**    |     –	    |
**type**	    |     –	    |   The component type name, needs to be `netcatudp`【组件类型的名称，需要是`netcatudp`】
**bind**	    |     –	    |   Host name or IP address to bind to【绑定的主机名或ip地址】
**port**	    |     –	    |   Port # to bind to【绑定的端口】
remoteAddressHeader|  –	    | 
selector.type	|replicating|	replicating or multiplexing
selector.*	 	|           |   Depends on the selector.type value
interceptors	|     –	    |   Space-separated list of interceptors
interceptors.*  |           |	 	  

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = netcatudp
	a1.sources.r1.bind = 0.0.0.0
	a1.sources.r1.port = 6666
	a1.sources.r1.channels = c1

## 11、Sequence Generator Source

> A simple sequence generator that continuously generates events with a counter that starts from 0, increments by 1 and stops at `totalEvents`. Retries when it can’t send events to the channel. Useful mainly for testing. During retries it keeps the body of the retried messages the same as before so that the number of unique events - after de-duplication at destination - is expected to be equal to the specified totalEvents. Required properties are in bold.

一个简单的序列生成器，用一个计数器连续生成 events，计数器从 0 开始，加1，直到 `totalEvents` 结束。

当无法将 events 发送到 channel 时会重试。

主要用于测试。在重试期间，它将保持重试消息体与之前相同，以便在目的地删除重复数据后，唯一 events 的数量将与指定的 `totalEvents` 相等。必需的属性以粗体显示。

Property Name   |  Default	|   Description
---|:---|:---
**channels**	|     –	    |
**type**	    |     –	    |   The component type name, needs to be `seq`【组件类型的名称，需要是`seq`】
selector.type   |           |	replicating or multiplexing
selector.*	    |replicating|	Depends on the selector.type value
interceptors	|     –	    |   Space-separated list of interceptors
interceptors.*	|           |   	 
batchSize	    |     1	    |   Number of events to attempt to process per request loop.【在每个请求循环中，尝试处理事件的数量】
totalEvents	    |Long.MAX_VALUE	 |   Number of unique events sent by the source.【由源发送的唯一事件的数量】

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = seq
	a1.sources.r1.channels = c1

## 12、Syslog Sources

> Reads syslog data and generate Flume events. The UDP source treats an entire message as a single event. The TCP sources create a new event for each string of characters separated by a newline (‘n’).Required properties are in bold.

读取 syslog 数据，并生成 Flume events。

UDP source 将整个消息视为单个 event。

TCP sources 为每个由换行符('n')分隔的字符串创建一个新 event。。

必需的属性以粗体显示。

### 12.1、Syslog TCP Source

> The original, tried-and-true syslog TCP source.

Property Name   |  Default	|   Description
---|:---|:---
**channels**	|    –	    |
**type**	    |    –	    |   The component type name, needs to be `syslogtcp`【组件类型的名称，需要是`syslogtcp`】
**host**	    |    –	    |   Host name or IP address to bind to【绑定的主机名或ip地址】
**port**	    |    –	    |   Port # to bind to【绑定的端口】
eventSize	    |   2500	|   Maximum size of a single event line, in bytes【一个事件行的最大大小，单位字节】
keepFields	    |   none	|   Setting this to ‘all’ will preserve the Priority, Timestamp and Hostname in the body of the event. A spaced separated list of fields to include is allowed as well. Currently, the following fields can be included: priority, version, timestamp, hostname. The values ‘true’ and ‘false’ have been deprecated in favor of ‘all’ and ‘none’.【设为`all`，将保留事件主体的Priority、Timestamp、Hostname。也允许包含空格划分的列表。当前，包含下列字段：priority、version、timestamp、hostname。弃用了`true`、`false`，取而代之的是`all`、`none`】
clientIPHeader	|     –	    |   If specified, the IP address of the client will be stored in the header of each event using the header name specified here. This allows for interceptors and channel selectors to customize routing logic based on the IP address of the client. Do not use the standard Syslog header names here (like `_host_`) because the event header will be overridden in that case.【如果指定，客户端的IP地址将使用这里指定的header名称存储在每个事件的header中。这就允许拦截器和channel选择器根据客户端的IP地址来自定义路由逻辑。不要使用标准的Syslog header名称，如`_host_`，因为事件header在那个案例中被覆盖。】
clientHostnameHeader| –	    |   If specified, the host name of the client will be stored in the header of each event using the header name specified here. This allows for interceptors and channel selectors to customize routing logic based on the host name of the client. Retrieving the host name may involve a name service reverse lookup which may affect the performance. Do not use the standard Syslog header names here (like `_host_`) because the event header will be overridden in that case.【如果指定，客户端的主机名将使用这里指定的header名称存储在每个事件的header中。这就允许拦截器和channel选择器根据客户端的主机名来自定义路由逻辑。检索主机名可能涉及到名称服务反向查找，这可能会影响性能。不要使用标准的Syslog header名称，如`_host_`，因为事件header在那个案例中被覆盖。】
selector.type	|   	    |   replicating or multiplexing
selector.*	    |replicating|	Depends on the selector.type value
interceptors	|     –	    |   Space-separated list of interceptors
interceptors.*	|           | 	 
ssl	            |   false	|   Set this to true to enable SSL encryption. If SSL is enabled, you must also specify a “keystore” and a “keystore-password”, either through component level parameters (see below) or as global SSL parameters (see SSL/TLS support section).【为true时，启用ssl加密。如果启用了ssl，必须也指定一个“keystore”和“keystore-password”，要么通过组件级的参数，要么作为全局的ssl参数。】
keystore	    |     –   	|   This is the path to a Java keystore file. If not specified here, then the global keystore will be used (if defined, otherwise configuration error).【这是一个java keystore文件的路径。如果不在这指定，将使用全局的keystore（如果定义的话，否则出现配置错误）】
keystore-password|	  –	    |   The password for the Java keystore. If not specified here, then the global keystore password will be used (if defined, otherwise configuration error).【Java keystore密码。如果不在这指定，将使用全局的keystore密码（如果定义的话，否则出现配置错误）】
keystore-type	|    JKS	|   The type of the Java keystore. This can be “JKS” or “PKCS12”. If not specified here, then the global keystore type will be used (if defined, otherwise the default is JKS).【Java keystore类型。可以是“JKS”或“PKCS12”。如果不在这指定，将使用全局的keystore类型（如果定义的话，否则出现默认是JKS）】
exclude-protocols|	SSLv3	|   Space-separated list of SSL/TLS protocols to exclude. SSLv3 will always be excluded in addition to the protocols specified.【排除的空格分隔的SSL/TLS协议的列表。除了指定的协议，SSLv3总是被排除在外。】
include-protocols|	 –	    |   Space-separated list of SSL/TLS protocols to include. The enabled protocols will be the included protocols without the excluded protocols. If included-protocols is empty, it includes every supported protocols.【包含的空格分隔的SSL/TLS协议的列表。启用的协议将是要包含的协议，没有排除的协议，如果included-protocols为空，它包含每一个支持的协议。】
exclude-cipher-suites|	–	|   Space-separated list of cipher suites to exclude.【排除的空格分隔的cipher suites的列表。】
include-cipher-suites|	–	|   Space-separated list of cipher suites to include. The enabled cipher suites will be the included cipher suites without the excluded cipher suites. If included-cipher-suites is empty, it includes every supported cipher suites.【包含的空格分隔的cipher suites的列表。启用的cipher suites将是要包含的cipher suites，没有排除的cipher suites。如果此项是空，它包含每一个支持的cipher suites】

> For example, a syslog TCP source for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = syslogtcp
	a1.sources.r1.port = 5140
	a1.sources.r1.host = localhost
	a1.sources.r1.channels = c1

### 12.2、Multiport Syslog TCP Source

> This is a newer, faster, multi-port capable version of the Syslog TCP source. Note that the ports configuration setting has replaced port. Multi-port capability means that it can listen on many ports at once in an efficient manner. This source uses the Apache Mina library to do that. Provides support for RFC-3164 and many common RFC-5424 formatted messages. Also provides the capability to configure the character set used on a per-port basis.

这是一个更新、更快、支持多端口的 Syslog TCP source 版本。

注意，端口配置设置已经替换了端口。多端口功能意味着它可以以一种高效的方式同时监听多个端口。

这个 source 使用 Apache Mina 库来实现这一点。

提供对 RFC-3164 和许多常见 RFC-5424 格式消息的支持。还提供了在每个端口上配置使用的字符集的功能。

Property Name   |  Default	|   Description
---|:---|:---
**channels**	|    –	    |
**type**	    |    –	    |   The component type name, needs to be `multiport_syslogtcp`【组件类型的名称，需要是`multiport_syslogtcp`】
**host**	    |    –	    |   Host name or IP address to bind to.【绑定的主机名或ip地址】
**ports**	    |    –	    |   Space-separated list (one or more) of ports to bind to.【绑定的端口的列表，用空格分隔】
eventSize	    |   2500	|   Maximum size of a single event line, in bytes.【一个事件行的最大大小，单位字节】
keepFields	    |   none	|   Setting this to ‘all’ will preserve the Priority, Timestamp and Hostname in the body of the event. A spaced separated list of fields to include is allowed as well. Currently, the following fields can be included: priority, version, timestamp, hostname. The values ‘true’ and ‘false’ have been deprecated in favor of ‘all’ and ‘none’.【设为all，将保留事件主体的Priority、Timestamp、Hostname。也允许包含空格划分的列表。当前，包含下列字段：priority、version、timestamp、hostname。弃用了true、false，取而代之的是all、none】
portHeader	    |     –	    |   If specified, the port number will be stored in the header of each event using the header name specified here. This allows for interceptors and channel selectors to customize routing logic based on the incoming port.【如果指定，端口数字将使用这里指定的header名称存储在每个事件的header中。这就允许拦截器和channel选择器根据传入的端口来自定义路由逻辑。】
clientIPHeader	|     –	    |   If specified, the IP address of the client will be stored in the header of each event using the header name specified here. This allows for interceptors and channel selectors to customize routing logic based on the IP address of the client. Do not use the standard Syslog header names here (like `_host_`) because the event header will be overridden in that case.【如果指定，客户端的IP地址将使用这里指定的header名称存储在每个事件的header中。这就允许拦截器和channel选择器根据客户端的IP地址来自定义路由逻辑。不要使用标准的Syslog header名称，如_host_，因为事件header在那个案例中被覆盖。】
clientHostnameHeader|	–	|   If specified, the host name of the client will be stored in the header of each event using the header name specified here. This allows for interceptors and channel selectors to customize routing logic based on the host name of the client. Retrieving the host name may involve a name service reverse lookup which may affect the performance. Do not use the standard Syslog header names here (like `_host_`) because the event header will be overridden in that case.【如果指定，客户端的主机名将使用这里指定的header名称存储在每个事件的header中。这就允许拦截器和channel选择器根据客户端的主机名来自定义路由逻辑。检索主机名可能涉及到名称服务反向查找，这可能会影响性能。不要使用标准的Syslog header名称，如_host_，因为事件header在那个案例中被覆盖。】
charset.default	|   UTF-8	|   Default character set used while parsing syslog events into strings.【当解析syslog events为字符串时，使用的默认字符集】
`charset.port.<port>`|	–	|   Character set is configurable on a per-port basis.【在每个端口字符集是可配置的】
batchSize	    |    100	|   Maximum number of events to attempt to process per request loop. Using the default is usually fine.【在每个请求循环中，尝试处理事件的数量。使用默认值即可】
readBufferSize	|    1024	|   Size of the internal Mina read buffer. Provided for performance tuning. Using the default is usually fine.【内部Mina读取缓冲区的大小。提供性能调优。使用默认值即可】
numProcessors	|(auto-detected)|	Number of processors available on the system for use while processing messages. Default is to auto-detect # of CPUs using the Java Runtime API. Mina will spawn 2 request-processing threads per detected CPU, which is often reasonable.【处理消息时，系统可使用的处理器的数量。】
selector.type	|replicating|	replicating, multiplexing, or custom
selector.*	    |     –	    |   Depends on the selector.type value
interceptors	|     –	    |   Space-separated list of interceptors.
interceptors.*	|           |	 
ssl	            |   false	|   Set this to true to enable SSL encryption. If SSL is enabled, you must also specify a “keystore” and a “keystore-password”, either through component level parameters (see below) or as global SSL parameters (see SSL/TLS support section).【为true时，启用ssl加密。如果启用了ssl，必须也指定一个“keystore”和“keystore-password”，要么通过组件级的参数，要么作为全局的ssl参数。】
keystore	    |     –	    |   This is the path to a Java keystore file. If not specified here, then the global keystore will be used (if defined, otherwise configuration error).【这是一个java keystore文件的路径。如果不在这指定，将使用全局的keystore（如果定义的话，否则出现配置错误）】
keystore-password|	  –	    |   The password for the Java keystore. If not specified here, then the global keystore password will be used (if defined, otherwise configuration error).【Java keystore密码。如果不在这指定，将使用全局的keystore密码（如果定义的话，否则出现配置错误）】
keystore-type	|    JKS	|   The type of the Java keystore. This can be “JKS” or “PKCS12”. If not specified here, then the global keystore type will be used (if defined, otherwise the default is JKS).【Java keystore类型。可以是“JKS”或“PKCS12”。如果不在这指定，将使用全局的keystore类型（如果定义的话，否则出现默认是JKS）】
exclude-protocols|	SSLv3	|   Space-separated list of SSL/TLS protocols to exclude. SSLv3 will always be excluded in addition to the protocols specified.【排除的空格分隔的SSL/TLS协议的列表。除了指定的协议，SSLv3总是被排除在外。】
include-protocols|	–	    |   Space-separated list of SSL/TLS protocols to include. The enabled protocols will be the included protocols without the excluded protocols. If included-protocols is empty, it includes every supported protocols.【包含的空格分隔的SSL/TLS协议的列表。启用的协议将是要包含的协议，没有排除的协议，如果included-protocols为空，它包含每一个支持的协议。】
exclude-cipher-suites|	–	|   Space-separated list of cipher suites to exclude.【排除的空格分隔的cipher suites的列表。】
include-cipher-suites|	–	|   Space-separated list of cipher suites to include. The enabled cipher suites will be the included cipher suites without the excluded cipher suites. If included-cipher-suites is empty, it includes every supported cipher suites.【包含的空格分隔的cipher suites的列表。启用的cipher suites将是要包含的cipher suites，没有排除的cipher suites。如果此项是空，它包含每一个支持的cipher suites】

> For example, a multiport syslog TCP source for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = multiport_syslogtcp
	a1.sources.r1.channels = c1
	a1.sources.r1.host = 0.0.0.0
	a1.sources.r1.ports = 10001 10002 10003
	a1.sources.r1.portHeader = port

### 12.3、Syslog UDP Source

Property Name   |  Default	|   Description
---|:---|:---
**channels**	|     –	    |
**type**	    |     –	    |   The component type name, needs to be `syslogudp`【组件类型的名称，需要是`syslogudp`】
**host**	    |     –	    |   Host name or IP address to bind to【绑定的主机名或ip地址】
**port**	    |     –	    |   Port # to bind to【绑定的端口】
keepFields	    |   false	|   Setting this to true will preserve the Priority, Timestamp and Hostname in the body of the event.【设为all，将保留事件主体的Priority、Timestamp、Hostname。】
clientIPHeader	|     –	    |   If specified, the IP address of the client will be stored in the header of each event using the header name specified here. This allows for interceptors and channel selectors to customize routing logic based on the IP address of the client. Do not use the standard Syslog header names here (like `_host_`) because the event header will be overridden in that case.【如果指定，客户端的IP地址将使用这里指定的header名称存储在每个事件的header中。这就允许拦截器和channel选择器根据客户端的IP地址来自定义路由逻辑。不要使用标准的Syslog header名称，如`_host_`，因为事件header在那个案例中被覆盖。】
clientHostnameHeader|	–	|   If specified, the host name of the client will be stored in the header of each event using the header name specified here. This allows for interceptors and channel selectors to customize routing logic based on the host name of the client. Retrieving the host name may involve a name service reverse lookup which may affect the performance. Do not use the standard Syslog header names here (like `_host_`) because the event header will be overridden in that case.【如果指定，客户端的主机名将使用这里指定的header名称存储在每个事件的header中。这就允许拦截器和channel选择器根据客户端的主机名来自定义路由逻辑。检索主机名可能涉及到名称服务反向查找，这可能会影响性能。不要使用标准的Syslog header名称，如`_host_`，因为事件header在那个案例中被覆盖。】
selector.type   |	 	    |   replicating or multiplexing
selector.*	    |replicating|	Depends on the selector.type value
interceptors	|     –	    |   Space-separated list of interceptors
interceptors.*	|           |	 

> For example, a syslog UDP source for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = syslogudp
	a1.sources.r1.port = 5140
	a1.sources.r1.host = localhost
	a1.sources.r1.channels = c1

## 13、HTTP Source

> A source which accepts Flume Events by HTTP POST and GET. GET should be used for experimentation only. HTTP requests are converted into flume events by a pluggable “handler” which must implement the HTTPSourceHandler interface. This handler takes a HttpServletRequest and returns a list of flume events. All events handled from one Http request are committed to the channel in one transaction, thus allowing for increased efficiency on channels like the file channel. If the handler throws an exception, this source will return a HTTP status of 400. If the channel is full, or the source is unable to append events to the channel, the source will return a HTTP 503 - Temporarily unavailable status.

一个通过 HTTP POST 和 GET 接受 Flume Events 的 source。

GET 只能用于实验。HTTP 请求通过一个可插拔的 “handler” 转换成 flume events，这个 “handler” 必须实现 HTTPSourceHandler 接口。

这个 handler 接受 HttpServletRequest 并返回一个 flume events 列表。从一个 Http 请求处理的所有 events 都提交到一个事务中的 channel，从而提高了像 file channel 的效率。

如果 handler 抛出异常，此 source 将返回 HTTP 状态为 400。如果 channel 已满，或者 source 无法向 channel 追加 events，则 source 将返回 HTTP 503 -临时不可用状态。

> All events sent in one post request are considered to be one batch and inserted into the channel in one transaction.

在一个 post 请求中发送的所有 events 都被认为是一个批次，并插入到一个事务中的 channel 中。

> This source is based on Jetty 9.4 and offers the ability to set additional Jetty-specific parameters which will be passed directly to the Jetty components.

该 source 基于 Jetty 9.4，提供了设置将直接传递给 Jetty 组件的额外 Jetty 特定参数的能力。

Property Name   |  Default	|   Description
---|:---|:---
**type**	 	|           |   The component type name, needs to be `http`【组件类型的名称，需要是`http`】
**port**	    |     –	    |   The port the source should bind to. 【source绑定的主机名或ip地址】
bind	        |   0.0.0.0	|   The hostname or IP address to listen on 【监听的端口】
handler	        |`org.apache.flume.source.http.JSONHandler`	| The FQCN of the handler class.【handler类的完全限定类名】
handler.*	    |     –	    |   Config parameters for the handler【handler的配置参数】
selector.type	|replicating|	replicating or multiplexing
selector.*	 	|           |   Depends on the `selector.type` value
interceptors	|     –	    |   Space-separated list of interceptors
interceptors.*  |           |	 	 
ssl	            |   false	|   Set the property true, to enable SSL. HTTP Source does not support SSLv3.【设为true，启用SSL。HTTP Source不支持SSLv3】
exclude-protocols|	SSLv3	|   Space-separated list of SSL/TLS protocols to exclude. SSLv3 will always be excluded in addition to the protocols specified.【排除的空格分隔的SSL/TLS协议的列表。除了指定的协议，SSLv3总是被排除在外。】
include-protocols|	  –	    |   Space-separated list of SSL/TLS protocols to include. The enabled protocols will be the included protocols without the excluded protocols. If included-protocols is empty, it includes every supported protocols.【包含的空格分隔的SSL/TLS协议的列表。启用的协议将是要包含的协议，没有排除的协议，如果included-protocols为空，它包含每一个支持的协议。】
exclude-cipher-suites|	–	|  Space-separated list of cipher suites to exclude.【排除的空格分隔的cipher suites的列表。】
include-cipher-suites|	–	|  Space-separated list of cipher suites to include. The enabled cipher suites will be the included cipher suites without the excluded cipher suites.【包含的空格分隔的cipher suites的列表。启用的cipher suites将是要包含的cipher suites，没有排除的cipher suites。】
keystore	 	 |          |  Location of the keystore including keystore file name. If SSL is enabled but the keystore is not specified here, then the global keystore will be used (if defined, otherwise configuration error).【包含keystore文件名的keystore路径。如果启用了SSL，但不在这指定，将使用全局的keystore（如果定义的话，否则出现配置错误）】
keystore-password|	 	    |  Keystore password. If SSL is enabled but the keystore password is not specified here, then the global keystore password will be used (if defined, otherwise configuration error).【keystore密码。如果启用了SSL，不在这指定，将使用全局的keystore密码（如果定义的话，否则出现配置错误）】
keystore-type	|    JKS	|  Keystore type. This can be “JKS” or “PKCS12”.【keystore类型。可以是“JKS”或“PKCS12”。】
QueuedThreadPool.*|	 	    |  Jetty specific settings to be set on org.eclipse.jetty.util.thread.QueuedThreadPool. N.B. QueuedThreadPool will only be used if at least one property of this class is set.【在`org.eclipse.jetty.util.thread.QueuedThreadPool`上设置特定的Jetty配置。注意:QueuedThreadPool只有在设置了该类的至少一个属性后才会被使用。】
HttpConfiguration.*|	 	|  Jetty specific settings to be set on org.eclipse.jetty.server.HttpConfiguration【在`org.eclipse.jetty.server.HttpConfiguration`上设置特定的Jetty配置。】
SslContextFactory.*|	 	|  Jetty specific settings to be set on org.eclipse.jetty.util.ssl.SslContextFactory (only applicable when ssl is set to true).【在`org.eclipse.jetty.util.ssl.SslContextFactory`上设置特定的Jetty配置。（仅在ssl设为true时可用）】
ServerConnector.*  |	 	|Jetty specific settings to be set on `org.eclipse.jetty.server.ServerConnector`【在`org.eclipse.jetty.server.ServerConnector`上设置特定的Jetty配置。】

> Deprecated Properties

Property Name   |  Default	|   Description
---|:---|:---
keystorePassword|	–	    |   Use keystore-password. Deprecated value will be overwritten with the new one.
excludeProtocols|	SSLv3	|   Use exclude-protocols. Deprecated value will be overwritten with the new one.
enableSSL	    |   false	|   Use ssl. Deprecated value will be overwritten with the new one.

> N.B. Jetty-specific settings are set using the setter-methods on the objects listed above. For full details see the Javadoc for these classes ([QueuedThreadPool](http://www.eclipse.org/jetty/javadoc/9.4.6.v20170531/org/eclipse/jetty/util/thread/QueuedThreadPool.html), [HttpConfiguration](http://www.eclipse.org/jetty/javadoc/9.4.6.v20170531/org/eclipse/jetty/server/HttpConfiguration.html), [SslContextFactory](http://www.eclipse.org/jetty/javadoc/9.4.6.v20170531/org/eclipse/jetty/util/ssl/SslContextFactory.html) and [ServerConnector](http://www.eclipse.org/jetty/javadoc/9.4.6.v20170531/org/eclipse/jetty/server/ServerConnector.html)).

注意:特定于 jetty 的设置是使用上面列出的对象的 setter 方法设置的。

> When using Jetty-specific setings, named properites above will take precedence (for example excludeProtocols will take precedence over SslContextFactory.ExcludeProtocols). All properties will be inital lower case.

当使用特定于 jetty 的设置时，上面命名的属性将具有优先级(例如，excludeProtocols 将优先于 SslContextFactory.ExcludeProtocols)。所有属性都是小写字母。

> An example http source for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = http
	a1.sources.r1.port = 5140
	a1.sources.r1.channels = c1
	a1.sources.r1.handler = org.example.rest.RestHandler
	a1.sources.r1.handler.nickname = random props
	a1.sources.r1.HttpConfiguration.sendServerVersion = false
	a1.sources.r1.ServerConnector.idleTimeout = 300

### 13.1、JSONHandler

> A handler is provided out of the box which can handle events represented in JSON format, and supports UTF-8, UTF-16 and UTF-32 character sets. The handler accepts an array of events (even if there is only one event, the event has to be sent in an array) and converts them to a Flume event based on the encoding specified in the request. If no encoding is specified, UTF-8 is assumed. The JSON handler supports UTF-8, UTF-16 and UTF-32. Events are represented as follows.

它提供了一个开箱即用的 handler，可以处理 JSON 格式的 events，并支持 UTF-8、UTF-16 和 UTF-32 字符集。

handler 接受一个 events 数组(即使只有一个事件，事件也必须以数组的形式发送)，并根据请求中指定的编码将它们转换为 Flume events。

如果没有指定编码，则假定为 UTF-8。JSON handler支持 UTF-8, UTF-16 和 UTF-32。

Events 如下所示。

	[{
	  "headers" : {
	             "timestamp" : "434324343",
	             "host" : "random_host.example.com"
	             },
	  "body" : "random_body"
	  },
	  {
	  "headers" : {
	             "namenode" : "namenode.example.com",
	             "datanode" : "random_datanode.example.com"
	             },
	  "body" : "really_random_body"
	  }]

> To set the charset, the request must have content type specified as application/json; charset=UTF-8 (replace UTF-8 with UTF-16 or UTF-32 as required).

要设置字符集，请求的内容类型必须指定为application/json；charset=UTF-8(请根据实际情况使用 UTF-16 或 UTF-32 进行替换 UTF-8)。

> One way to create an event in the format expected by this handler is to use JSONEvent provided in the Flume SDK and use Google Gson to create the JSON string using the Gson#fromJson(Object, Type) method. The type token to pass as the 2nd argument of this method for list of events can be created by:

以此 handler 所期望的格式创建 event 的一种方法是使用 Flume SDK 提供的 JSONEvent，并使用 Google Gson 的 Gson#fromJson(Object, Type) 方法创建 JSON 字符串。

作为该方法的第二个参数传递的类型令牌可以通过以下方式创建:

	Type type = new TypeToken<List<JSONEvent>>() {}.getType();


### 13.2、BlobHandler

> By default HTTPSource splits JSON input into Flume events. As an alternative, BlobHandler is a handler for HTTPSource that returns an event that contains the request parameters as well as the Binary Large Object (BLOB) uploaded with this request. For example a PDF or JPG file. Note that this approach is not suitable for very large objects because it buffers up the entire BLOB in RAM.

默认情况下，HTTPSource 将 JSON 输入拆分为 Flume events。

作为另一种选择，BlobHandler 是 HTTPSource 的处理程序，它返回一个事件，该事件包含请求参数以及与此请求一起上传的二进制大对象(BLOB)。例如 PDF 或 JPG 文件。

注意，这种方法不适用于非常大的对象，因为它会在 RAM 中缓冲整个 BLOB。

Property Name   |  Default	|   Description
---|:---|:---
handler	        |     –	    |   The FQCN of this class: `org.apache.flume.sink.solr.morphline.BlobHandler`
handler.maxBlobLength |	100000000	|  The maximum number of bytes to read and buffer for a given request【对一个给定请求，读取和缓存的最大字节数】

## 14、Stress Source

> StressSource is an internal load-generating source implementation which is very useful for stress tests. It allows User to configure the size of Event payload, with empty headers. User can configure total number of events to be sent as well maximum number of Successful Event to be delivered.Required properties are in bold.

StressSource 是一种内部产生负载的 source 实现，对压力测试非常有用。

它允许用户配置 Event 有效负载的大小，并带有空 headers。

用户可以配置要发送的 events 总数以及成功发送的 Event 的最大数量。

必需的属性以粗体显示。

Property Name   |  Default	|   Description
---|:---|:---
**type**	    |     –	    |    The component type name, needs to be `org.apache.flume.source.StressSource`【组件类型的名称，需要是`org.apache.flume.source.StressSource`】
size	        |    500	|    Payload size of each Event. Unit:**byte**【每个事件的有效负载的大小】
maxTotalEvents	|     -1	|    Maximum number of Events to be sent【要发生的最大事件数量】
maxSuccessfulEvents|  -1	|    Maximum number of Events successfully sent【最大成功发送事件数】
batchSize	    |      1	|    Number of Events to be sent in one batch【在一个批次中要发生的事件数量】
maxEventsPerSecond|    0	|    When set to an integer greater than zero, enforces a rate limiter onto the source.【当设置为一个大于0的整数时，在源上强制设置一个速率限制。】

> Example for agent named a1:

	a1.sources = stresssource-1
	a1.channels = memoryChannel-1
	a1.sources.stresssource-1.type = org.apache.flume.source.StressSource
	a1.sources.stresssource-1.size = 10240
	a1.sources.stresssource-1.maxTotalEvents = 1000000
	a1.sources.stresssource-1.channels = memoryChannel-1

## 15、Legacy Sources 【待做】

### 15.1、Avro Legacy Source

### 15.2、Thrift Legacy Source

## 16、Custom Source

> A custom source is your own implementation of the Source interface. A custom source’s class and its dependencies must be included in the agent’s classpath when starting the Flume agent. The type of the custom source is its FQCN.

实现 Source 接口的实现。当启动 Flume agent 时，一个自定义的 source 类和它的依赖必须包含在 agent 的类路径中，

自定义的 source 的类型是它的完全限定类名。

Property Name   |  Default	|   Description
---|:---|:---
**channels**	|     –	    |
**type**	    |     –	    |   The component type name, needs to be your FQCN
selector.type	| 	        |   replicating or multiplexing
selector.*	    |replicating|	Depends on the selector.type value
interceptors	|     –	    |   Space-separated list of interceptors
interceptors.*	|           |

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = org.example.MySource
	a1.sources.r1.channels = c1

## 17、Scribe Source

> Scribe is another type of ingest system. To adopt existing Scribe ingest system, Flume should use ScribeSource based on Thrift with compatible transfering protocol. For deployment of Scribe please follow the guide from Facebook. Required properties are in bold.

Scribe 是另一种摄入系统。为了采用现有的 Scribe 系统，Flume 应采用基于 Thrift 的 ScribeSource，并具有兼容的传输协议。

必需的属性以粗体显示。

Property Name   |  Default	|   Description
---|:---|:---
**type**	    |    –	    |   The component type name, needs to be `org.apache.flume.source.scribe.ScribeSource`
port	        |   1499	|   Port that Scribe should be connected
maxReadBufferBytes|	16384000|	Thrift Default FrameBuffer Size
workerThreads	|    5	    |   Handing threads number in Thrift
selector.type	|	        |
selector.*	 	|           |

> Example for agent named a1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.type = org.apache.flume.source.scribe.ScribeSource
	a1.sources.r1.port = 1463
	a1.sources.r1.workerThreads = 5
	a1.sources.r1.channels = c1