# Developing custom components

[TOC]

## 1、Client

### 1.1、Client SDK

### 1.2、RPC client interface

### 1.3、RPC clients - Avro and Thrift

### 1.4、Secure RPC client - Thrift

### 1.5、Failover Client

### 1.6、LoadBalancing RPC client

## 2、Embedded agent

## 3、Transaction interface

## 4、Sink

> The purpose of a Sink to extract Events from the Channel and forward them to the next Flume Agent in the flow or store them in an external repository. A Sink is associated with exactly one Channels, as configured in the Flume properties file. There’s one SinkRunner instance associated with every configured Sink, and when the Flume framework calls SinkRunner.start(), a new thread is created to drive the Sink (using SinkRunner.PollingRunner as the thread’s Runnable). This thread manages the Sink’s lifecycle. The Sink needs to implement the start() and stop() methods that are part of the LifecycleAware interface. The Sink.start() method should initialize the Sink and bring it to a state where it can forward the Events to its next destination. The Sink.process() method should do the core processing of extracting the Event from the Channel and forwarding it. The Sink.stop() method should do the necessary cleanup (e.g. releasing resources). The Sink implementation also needs to implement the Configurable interface for processing its own configuration settings. For example:

Sink 的目的是从 Channel 中提取 Events，并将它们转发到流中的下一个 Flume Agent 或将它们存储在外部存储库中。

一个 Sink 仅与一个 Channels 相关联，如 Flume 属性文件中配置的那样。

每个配置的 Sink 都有一个 SinkRunner 实例，当 Flume 框架调用 SinkRunner.start() 时，创建一个新的线程来驱动 Sink(使用SinkRunner.PollingRunner作为线程的Runnable)。

这个线程管理 Sink 的生命周期。Sink 需要实现作为 LifecycleAware 接口一部分的 start() 和 stop() 方法。

Sink.start() 方法应该初始化 Sink，并使其处于可以将 Events 转发到下一个目的地的状态。

Sink.process() 方法应该执行从 Channel 提取 Events 并转发它的核心处理。

Sink.stop() 方法应该做必要的清理(例如释放资源)。

Sink 实现还需要实现 Configurable 接口，以处理其自己的配置设置。例如:

```java
public class MySink extends AbstractSink implements Configurable {
  private String myProp;

  @Override
  public void configure(Context context) {
    String myProp = context.getString("myProp", "defaultValue");

    // Process the myProp value (e.g. validation)

    // Store myProp for later retrieval by process() method
    this.myProp = myProp;
  }

  @Override
  public void start() {
    // Initialize the connection to the external repository (e.g. HDFS) that
    // this Sink will forward Events to ..
  }

  @Override
  public void stop () {
    // Disconnect from the external respository and do any
    // additional cleanup (e.g. releasing resources or nulling-out
    // field values) ..
  }

  @Override
  public Status process() throws EventDeliveryException {
    Status status = null;

    // Start transaction
    Channel ch = getChannel();
    Transaction txn = ch.getTransaction();
    txn.begin();
    try {
      // This try clause includes whatever Channel operations you want to do

      Event event = ch.take();

      // Send the Event to the external repository.
      // storeSomeData(e);

      txn.commit();
      status = Status.READY;
    } catch (Throwable t) {
      txn.rollback();

      // Log exception, handle individual exceptions as needed

      status = Status.BACKOFF;

      // re-throw all Errors
      if (t instanceof Error) {
        throw (Error)t;
      }
    }
    return status;
  }
}
```

## 5、Source

> The purpose of a Source is to receive data from an external client and store it into the configured Channels. A Source can get an instance of its own ChannelProcessor to process an Event, commited within a Channel local transaction, in serial. In the case of an exception, required Channels will propagate the exception, all Channels will rollback their transaction, but events processed previously on other Channels will remain committed.

Source 的目的是从外部客户端接收数据，并将其存储到配置的 Channels 中。

Source 可以获得自己的 ChannelProcessor 实例，以串行方式处理在 Channel 本地事务中提交 Event。

在出现异常的情况下，所需的 Channels 将传播异常，所有 Channels 将回滚它们的事务，但以前在其他 Channels 上处理的 events 将保持提交。

> Similar to the SinkRunner.PollingRunner Runnable, there’s a PollingRunner Runnable that executes on a thread created when the Flume framework calls PollableSourceRunner.start(). Each configured PollableSource is associated with its own thread that runs a PollingRunner. This thread manages the PollableSource’s lifecycle, such as starting and stopping. A PollableSource implementation must implement the start() and stop() methods that are declared in the LifecycleAware interface. The runner of a PollableSource invokes that Source‘s process() method. The process() method should check for new data and store it into the Channel as Flume Events.

类似于 SinkRunner.PollingRunner Runnable。当 Flume 框架调用 PollableSourceRunner.start() 时，有一个 PollingRunner Runnable 在一个线程上执行。

每个配置的 PollableSource 都与它自己运行一个 PollingRunner 的线程相关联。这个线程管理 PollableSource 的生命周期，比如启动和停止。

PollableSource 实现必须实现在 LifecycleAware 接口中声明的 start() 和 stop() 方法。

PollableSource 的运行器调用该 Source 的 process() 方法。process() 方法应该检查新数据，并将其作为 Flume Events 存储到 Channel 中。

> Note that there are actually two types of Sources. The PollableSource was already mentioned. The other is the EventDrivenSource. The EventDrivenSource, unlike the PollableSource, must have its own callback mechanism that captures the new data and stores it into the Channel. The EventDrivenSources are not each driven by their own thread like the PollableSources are. Below is an example of a custom PollableSource:

注意，实际上有两种类型的 Sources。 PollableSource 已经提到了。

另一个是 EventDrivenSource。与 PollableSource 不同，EventDrivenSource 必须拥有自己的回调机制，来捕获新数据，并将其存储到 Channel 中。

EventDrivenSources 不像 PollableSources 那样每个都由自己的线程驱动。

下面是一个自定义 PollableSource 的例子:

```java
public class MySource extends AbstractSource implements Configurable, PollableSource {
  private String myProp;

  @Override
  public void configure(Context context) {
    String myProp = context.getString("myProp", "defaultValue");

    // Process the myProp value (e.g. validation, convert to another type, ...)

    // Store myProp for later retrieval by process() method
    this.myProp = myProp;
  }

  @Override
  public void start() {
    // Initialize the connection to the external client
  }

  @Override
  public void stop () {
    // Disconnect from external client and do any additional cleanup
    // (e.g. releasing resources or nulling-out field values) ..
  }

  @Override
  public Status process() throws EventDeliveryException {
    Status status = null;

    try {
      // This try clause includes whatever Channel/Event operations you want to do

      // Receive new data
      Event e = getSomeData();

      // Store the Event into this Source's associated Channel(s)
      getChannelProcessor().processEvent(e);

      status = Status.READY;
    } catch (Throwable t) {
      // Log exception, handle individual exceptions as needed

      status = Status.BACKOFF;

      // re-throw all Errors
      if (t instanceof Error) {
        throw (Error)t;
      }
    } finally {
      txn.close();
    }
    return status;
  }
}
```

## 6、Channel

TBD