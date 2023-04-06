# Flume Channel Selectors

[TOC]

> If the type is not specified, then defaults to “replicating”.

如果不指定类型，默认是 “replicating”。

## 1、Replicating Channel Selector (default)

> Required properties are in bold.

必需的属性以粗体显示。

Property Name     |   Default        | 	Description
---|:---|:---
selector.type     |   replicating    |   The component type name, needs to be replicating 【组件的类型名称，需要设置为`replicating`】
selector.optional |   –              |   Set of channels to be marked as optional【将channels标记为`optional`】

> Example for agent named a1 and it’s source called r1:

agent 名为 a1，其 source 为 r1:

	a1.sources = r1
	a1.channels = c1 c2 c3
	a1.sources.r1.selector.type = replicating
	a1.sources.r1.channels = c1 c2 c3
	a1.sources.r1.selector.optional = c3

在上述配置中，**c3 是一个可选 channel。写入 c3 的失败将被忽略**。

因为 c1 和 c2 没有被标记为可选的，所以写到这些 channel 的失败将导致事务失败。

> In the above configuration, c3 is an optional channel. Failure to write to c3 is simply ignored. Since c1 and c2 are not marked optional, failure to write to those channels will cause the transaction to fail.

## 2、Multiplexing Channel Selector

【Multiplexing：多路复用】

> Required properties are in bold.

Property Name     |  Default                 |   Description
---|:---|:---
selector.type     |  replicating             |  The component type name, needs to be multiplexing【组件的类型名称，需要设置为`multiplexing`】
selector.header   |  flume.selector.header   |  	 
selector.default  |  –	                     | 
selector.mapping.*|  –	                     | 

> Example for agent named a1 and it’s source called r1:

agent 名为 a1，其 source 为 r1:

	a1.sources = r1
	a1.channels = c1 c2 c3 c4
	a1.sources.r1.selector.type = multiplexing
	a1.sources.r1.selector.header = state
	a1.sources.r1.selector.mapping.CZ = c1
	a1.sources.r1.selector.mapping.US = c2 c3
	a1.sources.r1.selector.default = c4

## 3、Custom Channel Selector

> A custom channel selector is your own implementation of the ChannelSelector interface. A custom channel selector’s class and its dependencies must be included in the agent’s classpath when starting the Flume agent. The type of the custom channel selector is its FQCN.

自定义 channel selector 需要实现 ChannelSelector 接口。

当启动 Flume agent，自定义的 channel selector 类及其依赖必须包含在 agent 的类路径下。

自定义的 channel selector 的类型是它的完全限定类名。

Property Name  | Default  | Description
---|:---|:---
selector.type  |   –      | The component type name, needs to be your FQCN

> Example for agent named a1 and its source called r1:

	a1.sources = r1
	a1.channels = c1
	a1.sources.r1.selector.type = org.example.MyChannelSelector