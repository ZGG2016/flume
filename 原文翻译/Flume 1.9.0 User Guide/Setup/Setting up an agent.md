# Setting up an agent

[TOC]

## 1、Configuring individual components

> Each component (source, sink or channel) in the flow has a name, type, and set of properties that are specific to the type and instantiation. For example, an Avro source needs a hostname (or IP address) and a port number to receive data from. A memory channel can have max queue size (“capacity”), and an HDFS sink needs to know the file system URI, path to create files, frequency of file rotation (“hdfs.rollInterval”) etc. All such attributes of a component needs to be set in the properties file of the hosting Flume agent.

流中的每个组件(source、sink 或 channel)都有特定于其类型和实例的名称、类型和属性集。

例如，Avro source 需要主机名(或 IP 地址)和端口号来接收数据。内存 channel 可以有最大的队列大小(“capacity”)，HDFS sink 需要知道文件系统的 URI、创建文件的路径、文件旋转的频率(“HDFS.rollinterval”)等。

组件的所有这些属性都需要在托管 Flume agent 的属性文件中设置。

## 2、Wiring the pieces together

> The agent needs to know what individual components to load and how they are connected in order to constitute the flow. This is done by listing the names of each of the sources, sinks and channels in the agent, and then specifying the connecting channel for each sink and source. For example, an agent flows events from an Avro source called avroWeb to HDFS sink hdfs-cluster1 via a file channel called file-channel. The configuration file will contain names of these components and file-channel as a shared channel for both avroWeb source and hdfs-cluster1 sink.

agent 需要知道要加载哪些独立的组件，以及它们如何连接以构成流的。

为此，列出 agent 中每个 sources、sinks 和 channels 的名称，然后指定每个 sink 和 source 的连接 channel。

例如，agent 通过 file-channel 文件通道将 events 从名为 avroWeb 的 Avro source 流到 HDFS sink HDFS -cluster1。配置文件将包含这些组件的名称，并将 file-channel 作为 avroWeb source 和 hdfs-cluster1 sink 的共享 channel。

## 3、Starting an agent

> An agent is started using a shell script called flume-ng which is located in the bin directory of the Flume distribution. You need to specify the agent name, the config directory, and the config file on the command line:

使用名为 Flume-ng 的 shell 脚本启动 agent，该脚本位于 Flume 发行版的 bin 目录中。

需要在命令行中指定 agent 名称、配置目录和配置文件:

	$ bin/flume-ng agent -n $agent_name -c conf -f conf/flume-conf.properties.template

> Now the agent will start running source and sinks configured in the given properties file.

现在 agent 将开始运行在给定属性文件中配置的 source 和 sinks。

## 4、A simple example

> Here, we give an example configuration file, describing a single-node Flume deployment. This configuration lets a user generate events and subsequently logs them to the console.

这里，我们给出一个示例配置文件，描述一个单节点 Flume 部署。该配置允许用户生成 events，并将其输出到控制台。

	# example.conf: A single-node Flume configuration

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
	a1.channels.c1.type = memory
	a1.channels.c1.capacity = 1000
	a1.channels.c1.transactionCapacity = 100

	# Bind the source and sink to the channel
	a1.sources.r1.channels = c1
	a1.sinks.k1.channel = c1

> This configuration defines a single agent named a1. a1 has a source that listens for data on port 44444, a channel that buffers event data in memory, and a sink that logs event data to the console. The configuration file names the various components, then describes their types and configuration parameters. A given configuration file might define several named agents; when a given Flume process is launched a flag is passed telling it which named agent to manifest.

该配置定义了一个名为 a 1的 agent。a1 有一个监听端口 44444 上数据的 source，一个在内存中缓冲 event 数据的 channel，以及一个将 event 数据记录到控制台的 sink。

配置文件为各个组件命名，然后描述它们的类型和配置参数。给定的配置文件可能定义多个命名 agents；当启动一个给定的 Flume 进程时，会传递一个标志，告诉它要显示哪个指定的 agent。

> Given this configuration file, we can start Flume as follows:

根据这个配置文件，我们可以如下方式启动 Flume:

	$ bin/flume-ng agent --conf conf --conf-file example.conf --name a1 -Dflume.root.logger=INFO,console

> Note that in a full deployment we would typically include one more option: --conf=<conf-dir>. The <conf-dir> directory would include a shell script flume-env.sh and potentially a log4j properties file. In this example, we pass a Java option to force Flume to log to the console and we go without a custom environment script.

注意，在一个完整的部署中，我们通常会包含一个选项：`--conf=<conf-dir>`。`<conf-dir>` 目录将包括一个 shell 脚本 flume-env.sh，可能还包括一个 log4j 属性文件。

在这个例子中，我们传递了一个 Java 选项来强制 Flume 输出到控制台，我们没有使用自定义环境脚本。

> From a separate terminal, we can then telnet port 44444 and send Flume an event:

从一个单独的终端，我们可以 telnet 端口 44444 并发送 Flume 一个 event:

	$ telnet localhost 44444
	Trying 127.0.0.1...
	Connected to localhost.localdomain (127.0.0.1).
	Escape character is '^]'.
	Hello world! <ENTER>
	OK

> The original Flume terminal will output the event in a log message.

原始的 Flume 终端将在日志消息中输出 event。

	12/06/19 15:32:19 INFO source.NetcatSource: Source starting
	12/06/19 15:32:19 INFO source.NetcatSource: Created serverSocket:sun.nio.ch.ServerSocketChannelImpl[/127.0.0.1:44444]
	12/06/19 15:32:34 INFO sink.LoggerSink: Event: { headers:{} body: 48 65 6C 6C 6F 20 77 6F 72 6C 64 21 0D          Hello world!. }

> Congratulations - you’ve successfully configured and deployed a Flume agent! Subsequent sections cover agent configuration in much more detail.

## 5、Using environment variables in configuration files

> Flume has the ability to substitute environment variables in the configuration. For example:

Flume 有能力**在配置中替换环境变量**。例如

	a1.sources = r1
	a1.sources.r1.type = netcat
	a1.sources.r1.bind = 0.0.0.0
	a1.sources.r1.port = ${NC_PORT}
	a1.sources.r1.channels = c1

> NB: it currently works for values only, not for keys. (Ie. only on the “right side” of the = mark of the config lines.)

注意:它目前只适用于值，而不是键。(即，仅在配置行 = 标记的右侧。)

> This can be enabled via Java system properties on agent invocation by setting propertiesImplementation = org.apache.flume.node.EnvVarResolverProperties.

这可以通过设置 `propertiesImplementation = org.apache.flume.node.EnvVarResolverProperties`在 agent 调用上的 Java 系统属性来启用。

例如：

	$ NC_PORT=44444 bin/flume-ng agent –conf conf –conf-file example.conf –name a1 -Dflume.root.logger=INFO,console -DpropertiesImplementation=org.apache.flume.node.EnvVarResolverProperties

> Note the above is just an example, environment variables can be configured in other ways, including being set in conf/flume-env.sh.

注意，上面只是一个例子，**环境变量可以通过其他方式配置，包括在 `conf/flume-env.sh` 中设置**。

## 6、Logging raw data

> Logging the raw stream of data flowing through the ingest pipeline is not desired behaviour in many production environments because this may result in leaking sensitive data or security related configurations, such as secret keys, to Flume log files. By default, Flume will not log such information. On the other hand, if the data pipeline is broken, Flume will attempt to provide clues for debugging the problem.

在许多生产环境中，记录流经摄取管道的原始数据流并不是我们所希望的行为，因为这可能会导致敏感数据或与安全相关的配置(如密钥)泄漏到 Flume 日志文件。默认情况下，Flume 不会记录这些信息。另一方面，如果数据管道破裂，Flume 将尝试提供调试问题的线索。

> One way to debug problems with event pipelines is to set up an additional Memory Channel connected to a Logger Sink, which will output all event data to the Flume logs. In some situations, however, this approach is insufficient.

调试 event 管道问题的一种方法是设置一个连接到 Logger Sink 的额外 Memory Channel，它将把所有 event 数据输出到 Flume 日志。然而，在某些情况下，这种方法是不够的。

> In order to enable logging of event- and configuration-related data, some Java system properties must be set in addition to log4j properties.

为了能够记录与事件和配置相关的数据，除了 log4j 属性外，还必须设置一些 Java 系统属性。

> To enable configuration-related logging, set the Java system property -Dorg.apache.flume.log.printconfig=true. This can either be passed on the command line or by setting this in the JAVA_OPTS variable in flume-env.sh.

**要启用与配置相关的日志记录，请设置 Java 系统属性 `-Dorg.apache.flume.log.printconfig=true`**。这可以通过命令行传递，也可以通过在 flume-env.sh 中的 JAVA_OPTS 变量中设置。

> To enable data logging, set the Java system property -Dorg.apache.flume.log.rawdata=true in the same way described above. For most components, the log4j logging level must also be set to DEBUG or TRACE to make event-specific logging appear in the Flume logs.

要启用数据日志记录，设置 Java 系统属性 `-Dorg.apache.flume.log.rawdata=true` ，与上面描述的方式相同。对于大多数组件，必须将 log4j 日志记录级别设置为 DEBUG 或 TRACE，以使特定于 event 的日志记录出现在 Flume 日志中。

> Here is an example of enabling both configuration logging and raw data logging while also setting the Log4j loglevel to DEBUG for console output:

下面是一个启用配置日志记录和原始数据日志记录的例子，同时还将 Log4j loglevel 设置为 DEBUG 控制台输出:

	$ bin/flume-ng agent --conf conf --conf-file example.conf --name a1 -Dflume.root.logger=DEBUG,console -Dorg.apache.flume.log.printconfig=true -Dorg.apache.flume.log.rawdata=true

## 7、Zookeeper based Configuration

> Flume supports Agent configurations via Zookeeper. This is an experimental feature. The configuration file needs to be uploaded in the Zookeeper, under a configurable prefix. The configuration file is stored in Zookeeper Node data. Following is how the Zookeeper Node tree would look like for agents a1 and a2

Flume 通过 Zookeeper 支持 Agent 配置。这是一个实验性的功能。

配置文件需要上传到 Zookeeper 中，在一个可配置的前缀下。配置文件保存在 Zookeeper 节点数据中。下面是 agents a1 和 a2 的 Zookeeper 节点树的样子

	- /flume
	 |- /a1 [Agent config file]
	 |- /a2 [Agent config file]

> Once the configuration file is uploaded, start the agent with following options

上传配置文件后，使用以下选项启动代理

	$ bin/flume-ng agent –conf conf -z zkhost:2181,zkhost1:2181 -p /flume –name a1 -Dflume.root.logger=INFO,console

Argument Name  | Default  |  Description
z	           |    –	  |  Zookeeper connection string. Comma separated list of hostname:port【Zookeeper连接字符串。逗号分隔的hostname:port列表】
p	           |  /flume  |  Base Path in Zookeeper to store Agent configurations【Zookeeper中存储配置Agent的基路径】

## 8、Installing third-party plugins

> Flume has a fully plugin-based architecture. While Flume ships with many out-of-the-box sources, channels, sinks, serializers, and the like, many implementations exist which ship separately from Flume.

Flume 有一个完全基于插件的架构。虽然 Flume 提供了许多开箱即用的 sources、channels、sinks、serializers 等，但存在许多与 Flume 分开发布的实现。

> While it has always been possible to include custom Flume components by adding their jars to the FLUME_CLASSPATH variable in the flume-env.sh file, Flume now supports a special directory called plugins.d which automatically picks up plugins that are packaged in a specific format. This allows for easier management of plugin packaging issues as well as simpler debugging and troubleshooting of several classes of issues, especially library dependency conflicts.

通过将它们的 jar 添加到 Flume-env.sh 文件中的 FLUME_CLASSPATH 变量中来包含自定义 Flume 组件一直是可能的，但 **Flume 现在支持一个名为 plugins.d  的特殊目录，它自动选取以特定格式打包的插件**。这允许更容易的管理插件打包问题，以及更简单的调试和排除一些问题，特别是库依赖冲突。

### 8.1、The plugins.d directory

> The plugins.d directory is located at $FLUME_HOME/plugins.d. At startup time, the flume-ng start script looks in the plugins.d directory for plugins that conform to the below format and includes them in proper paths when starting up java.

plugins.d 目录位于 `$FLUME_HOME/plugins.d`。在启动时，flume-ng 启动脚本会在 plugins.d 中查找符合下列格式的插件，并在启动 java 时将它们包含在正确的路径中。

### 8.2、Directory layout for plugins

> Each plugin (subdirectory) within plugins.d can have up to three sub-directories:

每个 plugins.d 下的插件都有如下三个子目录：

- lib - the plugin’s jar(s)
- libext - the plugin’s dependency jar(s)
- native - any required native libraries, such as .so files

> Example of two plugins within the plugins.d directory:

plugins.d 目录中的两个插件的例子：

	plugins.d/
	plugins.d/custom-source-1/
	plugins.d/custom-source-1/lib/my-source.jar
	plugins.d/custom-source-1/libext/spring-core-2.5.6.jar
	plugins.d/custom-source-2/
	plugins.d/custom-source-2/lib/custom.jar
	plugins.d/custom-source-2/native/gettext.so