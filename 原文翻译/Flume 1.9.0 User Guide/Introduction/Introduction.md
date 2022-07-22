# Introduction

[TOC]

## 1、Overview

> Apache Flume is a distributed, reliable, and available system for efficiently collecting, aggregating and moving large amounts of log data from many different sources to a centralized data store.

Flume 是一个分布式的、可靠的和可用的系统，用来从大量不同的数据源高效地收集、聚合和移动海量日志数据到一个中心数据存储。

> The use of Apache Flume is not only restricted to log data aggregation. Since data sources are customizable, Flume can be used to transport massive quantities of event data including but not limited to network traffic data, social-media-generated data, email messages and pretty much any data source possible.

Flume 的使用不仅限于日志数据聚合。由于数据源是可定制的，Flume 可以用于传输大量的事件数据，包括但不限于网络流量数据、社交媒体生成的数据、电子邮件消息以及几乎任何可能的数据源。

> Apache Flume is a top level project at the Apache Software Foundation.

## 2、System Requirements

- Java 运行环境：Java 1.8 及更新

- 内存：为 sources、channels 或 sinks 使用的配置分配足够的内存

- 磁盘空间：为 channels 或 sinks 使用的配置分配足够的磁盘空间

- 目录权限：agent 使用目录的读/写权限

> Java Runtime Environment - Java 1.8 or later
> Memory - Sufficient memory for configurations used by sources, channels or sinks
> Disk Space - Sufficient disk space for configurations used by channels or sinks
> Directory Permissions - Read/Write permissions for directories used by agent

## 3、Architecture

### 3.1、Data flow model

> A Flume event is defined as a unit of data flow having a byte payload and an optional set of string attributes. A Flume agent is a (JVM) process that hosts the components through which events flow from an external source to the next destination (hop).

一个 Flume event 被定义为一个数据流单元，它具有一个字节有效负载和一组可选字符串属性的。

一个 Flume agent 是一个 (JVM) 进程，它承载着从外部源到下一个目标(hop)的事件流。

![UserGuide_image00](./image/UserGuide_image00.png)

> A Flume source consumes events delivered to it by an external source like a web server. The external source sends events to Flume in a format that is recognized by the target Flume source. For example, an Avro Flume source can be used to receive Avro events from Avro clients or other Flume agents in the flow that send events from an Avro sink. A similar flow can be defined using a Thrift Flume Source to receive events from a Thrift Sink or a Flume Thrift Rpc Client or Thrift clients written in any language generated from the Flume thrift protocol.When a Flume source receives an event, it stores it into one or more channels. The channel is a passive store that keeps the event until it’s consumed by a Flume sink. The file channel is one example – it is backed by the local filesystem. The sink removes the event from the channel and puts it into an external repository like HDFS (via Flume HDFS sink) or forwards it to the Flume source of the next Flume agent (next hop) in the flow. The source and sink within the given agent run asynchronously with the events staged in the channel.

一个 Flume source 消费由外部源(如 web 服务器)传输给它的 events。

外部源以目标 Flume source 能识别的格式向 Flume 发送 events。例如，Avro Flume source 可用于接收来自 Avro 客户端或流中的其他 Flume agents Avro sink 的 Avro events。

类似的流，还有可以使用 Thrift Flume Source 接收来自 Thrift Sink 或 Flume Thrift Rpc 客户端或 Thrift 客户端的 events，这些客户端是从 Flume Thrift 协议生成的语言编写的。

当一个 Flume source 接收一个 events，它会将其存储到一个或多个 channels。channel 是一个被动存储，它保存 events 直到被一个 Flume sink 消费。

file channel 就是一个例子：它由本地文件系统支持。

sink 将 event 从 channel 中移除，并将其放入外部存储库，如 HDFS(通过 Flume HDFS sink)，或将其转发到流中的下一个 Flume agent(下一跳)的 Flume source。

给定代理中的源和接收与通道中暂存的事件异步运行。

### 3.2、Complex flows

> Flume allows a user to build multi-hop flows where events travel through multiple agents before reaching the final destination. It also allows fan-in and fan-out flows, contextual routing and backup routes (fail-over) for failed hops.

Flume 允许用户构建多跳流，events 在到达最终目的地之前穿过多个 agents。

它还允许扇入和扇出流、上下文路由和为失败跳的备份路由(故障转移)。

### 3.3、Reliability

> The events are staged in a channel on each agent. The events are then delivered to the next agent or terminal repository (like HDFS) in the flow. The events are removed from a channel only after they are stored in the channel of next agent or in the terminal repository. This is a how the single-hop message delivery semantics in Flume provide end-to-end reliability of the flow.

events 在每个 agent 的 channel 中暂存。然后 events 被传输到流中的下一个 agent 或终端存储库(如 HDFS)。

events 在存储到下一个 agent 的 channel 或终端存储库中之后才会从 channel 中删除。

这就是 Flume 中的单跳消息传递语义如何提供流的端到端的可靠性。

> Flume uses a transactional approach to guarantee the reliable delivery of the events. The sources and sinks encapsulate in a transaction the storage/retrieval, respectively, of the events placed in or provided by a transaction provided by the channel. This ensures that the set of events are reliably passed from point to point in the flow. In the case of a multi-hop flow, the sink from the previous hop and the source from the next hop both have their transactions running to ensure that the data is safely stored in the channel of the next hop.

Flume 使用事务性方法来保证 events 的可靠传输。

The sources and sinks encapsulate in a transaction the storage/retrieval, respectively, of the events placed in or provided by a transaction provided by the channel.【存储事件是事务性的，检索也是事务性的】

这可以确保事件集在流中可靠地从一点传递到另一点。

在多跳流的情况下，来自上一跳的 sink 和来自下一跳的 source 都在运行它们的事务，以确保数据安全地存储在下一跳的 channel 中。

### 3.4、Recoverability

> The events are staged in the channel, which manages recovery from failure. Flume supports a durable file channel which is backed by the local file system. There’s also a memory channel which simply stores the events in an in-memory queue, which is faster but any events still left in the memory channel when an agent process dies can’t be recovered.

events 在 channel 中暂存，由 channel 管理故障恢复。

Flume 支持由本地文件系统支持的持久化的 file channel。

还有一个 memory channel，它只是将 events 存储在内存队列中，速度更快，但当 agent 进程死亡时，memory channel 中仍然保留的任何事件都无法恢复。