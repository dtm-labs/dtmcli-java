# dtmcli-java ![MIT][license-badge]

English | [简体中文](./README.md)

`dtmcli-java` 是分布式事务管理器 [dtm](https://github.com/dtm-labs/dtm) 的Java客户端sdk

## DTM是什么

DTM是一款变革性的分布式事务框架，提供了傻瓜式的使用方式，极大的降低了分布式事务的使用门槛，改变了“能不用分布式事务就不用”的行业现状，优雅的解决了服务间的数据一致性问题。

## 特性
* 支持多种语言：支持Go、Java、PHP、C#、Python、Nodejs 各种语言的SDK
* 支持多种事务模式：SAGA、TCC、XA
* 支持消息最终一致性：二阶段消息，比本地消息表更优雅的方案
* 支持多种数据库事务：Mysql、Redis、MongoDB、Postgres、TDSQL等
* 支持多种存储引擎：Mysql（常用）、Redis（高性能）、MongoDB（规划中）
* 支持多种微服务架构：[go-zero](https://github.com/zeromicro/go-zero)、go-kratos/kratos、polarismesh/polaris
* 支持高可用，易水平扩展

## 使用方式

### 步骤一：确定你需要使用的版本
1. 您的项目是springcloud项目
- 您的项目中springboot版本>=2.4.0，请选择dtmcli-springcloud相应的版本直接接入即可
- 您的项目中的springboot版本<2.4.0，请选择dtmcli-java接入，dtmcli-java也提供了微服务相关的接口，请设置nacos服务中心的相关配置即可使用
2. 您的项目是普通项目/没有接入微服务的spring(boot)项目
- 请选择dtmcli-java，并设置相应的配置即可

|  artifact| version | 适用版本 |备注|
|:-----:|:----:|:----:|:----:|
|dtmcli-springcloud| 2.1.4.1| 2.4.0 <= springboot version < 2.5.13| springboot 版本>=2.5.0，需要设置spring.cloud.compatibility-verifier.enabled=false|
|dtmcli-springcloud| 2.1.4.2| 2.6.0 <= springboot version < 2.6.latest| |
|dtmcli-java| 2.1.4| others| |

### 步骤二：添加依赖项

Maven：

```bash
<dependency>
	<groupId>io.github.dtm-labs</groupId>
	<artifactId>dtmcli-springcloud</artifactId>
	<version>${dtmcli.version}</version>
</dependency>
```

Gradle:

```bash
dependencies {
	        implementation 'io.github.dtm-labs:dtmcli-springcloud:${dtmcli.version}'
	}
```

### 步骤三：设置dtmcli-java配置
如果您引入了dtmcli-java，则需要新建一个`dtm-conf.properties`配置文件
- 情形一：您引入了nacos等服务中心组件的配置文件
```
serverAddr=127.0.0.1:8848
username=nacos
password=nacos
namespace=c3dc917d-906a-429d-90a9-85012b41014e
dtm.service.name=dtmService
dtm.service.registryType=nacos
```
- 情形二：您直连dtmsvr
```
dtm.ipport=127.0.0.1:36789
```
## 示例

```bash
@RequestMapping("testTcc")
    public String testTcc() {
        //创建dtm clinet
        DtmClient dtmClient = new DtmClient(ipPort);
        //创建tcc事务
        try {
            dtmClient.tccGlobalTransaction(dtmClient.genGid(), TccTestController::tccTrans);
        } catch (Exception e) {
            log.error("tccGlobalTransaction error", e);
            return "fail";
        }
        return "success";
    }

/**
     * 定义tcc事务函数，内部需要通过callBranch注册事务子分支
     *
     * @param tcc
     * @return
     * @see TransController
     */
    public static void tccTrans(Tcc tcc) throws Exception {
        Response outResponse = tcc
                .callBranch("", svc + "/TransOutTry", svc + "/TransOutConfirm", svc + "/TransOutCancel");
        log.info("outResponse:{}", outResponse);
        Response inResponse = tcc.callBranch("", svc + "/TransInTry", svc + "/TransInConfirm", svc + "/TransInCancel");
        log.info("inResponse:{}", inResponse);
    }
```


### 完整示例

#### dtmcli-java使用示例
[dtmcli-java-sample](https://github.com/dtm-labs/dtmcli-java-sample)
[dtmcli-java-sample-use-configuration](https://github.com/horseLk/dtmcli-java-sample-with-conf)
#### dtmcli-springcloud使用示例
[dtmcli-java-spring-sample](https://github.com/dtm-labs/dtmcli-java-spring-sample)

[MIT](https://github.com/dtm-labs/dtmcli/blob/main/LICENSE)

[license-badge]: https://img.shields.io/github/license/dtm-labs/dtmcli-java
