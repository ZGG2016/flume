# Security

> The HDFS sink, HBase sink, Thrift source, Thrift sink and Kite Dataset sink all support Kerberos authentication. Please refer to the corresponding sections for configuring the Kerberos-related options.

HDFS sink、HBase sink、Thrift source、Thrift sink 和 Kite Dataset sink 都支持 Kerberos 身份验证。请参考相应的章节来配置与 kerberos 相关的选项。

> Flume agent will authenticate to the kerberos KDC as a single principal, which will be used by different components that require kerberos authentication. The principal and keytab configured for Thrift source, Thrift sink, HDFS sink, HBase sink and DataSet sink should be the same, otherwise the component will fail to start.

Flume agent 将作为单个主体对 kerberos KDC 进行身份验证，需要 kerberos 身份验证的不同组件将使用该主体。

Thrift source、Thrift sink、HDFS sink、HBase sink 和 DataSet sink 配置的 principal 和 keytab 必须一致，否则组件启动失败。