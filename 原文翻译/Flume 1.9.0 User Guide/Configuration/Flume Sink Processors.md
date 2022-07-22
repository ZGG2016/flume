# Flume Sink Processors

[TOC]

> Sink groups allow users to group multiple sinks into one entity. Sink processors can be used to provide load balancing capabilities over all sinks inside the group or to achieve fail over from one sink to another in case of temporal failure. Required properties are in bold.

Sink 分组允许用户**将多个 sinks 分组到一个实体中**。

Sink processors 可用于在组内所有 sinks 上提供负载平衡功能，或者在出现临时故障时实现从一个 sink 到另一个 sink 的故障转移。

必需的属性以粗体显示。

Property Name     |   Default        | 	Description
---|:---|:---
sinks	          |     –	         |  Space-separated list of sinks that are participating in the group【参与分组的空格分隔的sinks列表】
processor.type	  |    `default`	     |  The component type name, needs to be `default`, `failover` or `load_balance`【组件类型名称，需要是`default`, `failover` or `load_balance`】

> Example for agent named a1:

	a1.sinkgroups = g1
	a1.sinkgroups.g1.sinks = k1 k2
	a1.sinkgroups.g1.processor.type = load_balance

## 1、Default Sink Processor

> Default sink processor accepts only a single sink. User is not forced to create processor (sink group) for single sinks. Instead user can follow the source - channel - sink pattern that was explained above in this user guide.

默认的 sink processor 仅接收一个 sink。对于一个 sink，用户不被强制创建 processor(sink group)。而是，**用户遵循 source - channel - sink 模式**。

## 2、Failover Sink Processor

> Failover Sink Processor maintains a prioritized list of sinks, guaranteeing that so long as one is available events will be processed (delivered).

Failover Sink Processor **维护一个 sinks 的优先级列表，确保只要有一个 sink 可用，events 就会被处理(传输)**。

> The failover mechanism works by relegating failed sinks to a pool where they are assigned a cool down period, increasing with sequential failures before they are retried. Once a sink successfully sends an event, it is restored to the live pool. The Sinks have a priority associated with them, larger the number, higher the priority. If a Sink fails while sending a Event the next Sink with highest priority shall be tried next for sending Events. For example, a sink with priority 100 is activated before the Sink with priority 80. If no priority is specified, thr priority is determined based on the order in which the Sinks are specified in configuration.

故障转移机制的工作原理是**将失败的 sinks 转移到池中，在池中为它们分配一个冷却期，在重新尝试它们之前，随着顺序故障的增加而增加**。

一旦 sink 成功地发送了一个 event，它就会被恢复到活跃的池。

Sinks 有一个与它们相关联的优先级，数量越大，优先级越高。如果一个 Sink 在发送 Event 时失败，下一个具有最高优先级的 Sink 将被尝试下一步发送 Events。

例如，优先级为 100 的 sink 在优先级为 80 的 sink 之前被激活。

**如果没有指定优先级，则它们的优先级根据配置中指定的 sink 的顺序确定**。

> To configure, set a sink groups processor to failover and set priorities for all individual sinks. All specified priorities must be unique. Furthermore, upper limit to failover time can be set (in milliseconds) using maxpenalty property.Required properties are in bold.

要进行配置，将 sink groups processor 设置为 failover，并为所有单个的 sinks 设置优先级。

所有指定的优先级必须是唯一的。

此外，可以使用 maxpenalty 属性设置故障转移时间的上限(以毫秒为单位)。

必需的属性以粗体显示。

Property Name     |   Default        | 	Description
---|:---|:---
sinks	          |      –	         |  Space-separated list of sinks that are participating in the group【参与分组的空格分隔的sinks列表】
processor.type	  |   `default`	     |  The component type name, needs to be `failover`【组件类型名称，必须是`failover`】
`processor.priority.<sinkName>` |–	 |  Priority value. `<sinkName>` must be one of the sink instances associated with the current sink group A higher priority value Sink gets activated earlier. A larger absolute value indicates higher priority【优先级值。`<sinkName>`必须是当前sink分组中的sink示例。具有更高优先级值的sink首先被激活。较大得绝对值表示更高的优先级】
processor.maxpenalty|	30000	     |  The maximum backoff period for the failed Sink (in millis)【故障转移时间的上限(以毫秒为单位)】

> Example for agent named a1:

	a1.sinkgroups = g1
	a1.sinkgroups.g1.sinks = k1 k2
	a1.sinkgroups.g1.processor.type = failover
	a1.sinkgroups.g1.processor.priority.k1 = 5
	a1.sinkgroups.g1.processor.priority.k2 = 10
	a1.sinkgroups.g1.processor.maxpenalty = 10000

## 3、Load balancing Sink Processor

> Load balancing sink processor provides the ability to load-balance flow over multiple sinks. It maintains an indexed list of active sinks on which the load must be distributed. Implementation supports distributing load using either via round_robin or random selection mechanisms. The choice of selection mechanism defaults to round_robin type, but can be overridden via configuration. Custom selection mechanisms are supported via custom classes that inherits from AbstractSinkSelector.

Load balancing sink processor 提供了**在多个 sinks 上负载平衡流的能力**。

它维护一个活跃的 sinks 的索引列表，必须在该列表上分配负载。

实现支持使用 `round_robin` 或 `random` 选择机制来分配负载。默认为 `round_robin`类型，但可以通过配置覆盖。

通过继承自 `AbstractSinkSelector` 的自定义类来支持自定义选择机制。

> When invoked, this selector picks the next sink using its configured selection mechanism and invokes it. For round_robin and random In case the selected sink fails to deliver the event, the processor picks the next available sink via its configured selection mechanism. This implementation does not blacklist the failing sink and instead continues to optimistically attempt every available sink. If all sinks invocations result in failure, the selector propagates the failure to the sink runner.

**当调用时，该 selector 使用其配置的选择机制选择下一个 sink 并调用它**。

对于 `round_robin` 和 `random`，如果选择的 sink 无法传递 event，processor 将通过其配置的选择机制选择下一个可用的 sink。

这个实现不会将失败的 sink 列入黑名单，而是继续乐观地尝试每个可用的 sink。如果所有 sinks 调用都导致失败，则 selector 将失败传播到 sink runner。

> If backoff is enabled, the sink processor will blacklist sinks that fail, removing them for selection for a given timeout. When the timeout ends, if the sink is still unresponsive timeout is increased exponentially to avoid potentially getting stuck in long waits on unresponsive sinks. With this disabled, in round-robin all the failed sinks load will be passed to the next sink in line and thus not evenly balanced。 Required properties are in bold.

如果启用了 backoff，sink processor 将把失败的 sink 列入黑名单，将它们删除。

当超时时间结束时，如果 sink 仍然没有响应，则超时时间以指数形式增加，以避免潜在地在响应迟钝的 sinks 上陷入长时间等待。

禁用此功能后，在 `round_robin` 中，所有失败的 sinks 负载将被传递到行中的下一个 sinks，因此不会得到均衡。

必需的属性以粗体显示

Property Name     |   Default        | 	Description
---|:---|:---
processor.sinks	  |      –	         |  Space-separated list of sinks that are participating in the group【参与分组的空格分隔的sinks列表】
processor.type	  |   `default`	     |  The component type name, needs to be `load_balance`【组件类型名称，必须是`load_balance`】
processor.backoff |  	false	     |  Should failed sinks be backed off exponentially.
processor.selector|  `round_robin`	 |  Selection mechanism. Must be either `round_robin`, `random` or FQCN of custom class that inherits from `AbstractSinkSelector`【选择机制】
processor.selector.maxTimeOut|30000  |  Used by backoff selectors to limit exponential backoff (in milliseconds)【回退selectors用于限制指数回退(以毫秒为单位)】

> Example for agent named a1:

	a1.sinkgroups = g1
	a1.sinkgroups.g1.sinks = k1 k2
	a1.sinkgroups.g1.processor.type = load_balance
	a1.sinkgroups.g1.processor.backoff = true
	a1.sinkgroups.g1.processor.selector = random

## 4、Custom Sink Processor

> Custom sink processors are not supported at the moment.

当前不支持自定义 sink processors。