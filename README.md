# dtmcli ![MIT License][license-badge]


a client for distributed transaction manager [dtm](https://github.com/dtm-labs/dtm)

`dtmcli-java` 是分布式事务管理器 [dtm](https://github.com/dtm-labs/dtm) 的Java客户端sdk

## dtm分布式事务管理服务

DTM是一款跨语言的开源分布式事务管理器，优雅的解决了幂等、空补偿、悬挂等分布式事务难题。提供了简单易用、高性能、易水平扩展的分布式事务解决方案。

## 亮点

* 极易接入
    - 支持HTTP，提供非常简单的接口，极大降低上手分布式事务的难度，新手也能快速接入
* 使用简单
    - 开发者不再担心悬挂、空补偿、幂等各类问题，框架层代为处理
* 跨语言
    - 可适合多语言栈的公司使用。方便go、python、php、nodejs、ruby各类语言使用。
* 易部署、易扩展
    - 仅依赖mysql，部署简单，易集群化，易水平扩展
* 多种分布式事务协议支持
    - TCC、SAGA、XA、事务消息

## 与其他框架对比

目前开源的分布式事务框架，暂未看到非Java语言有成熟的框架。而Java语言的较多，有阿里的SEATA、华为的ServiceComb-Pack，京东的shardingsphere，以及himly，tcc-transaction，ByteTCC等等，其中以seata应用最为广泛。

下面是dtm和seata的主要特性对比：

|  特性| DTM | SEATA |备注|
|:-----:|:----:|:----:|:----:|
| 支持语言 |<span style="color:green">Golang、python、php及其他</span>|<span style="color:orange">Java</span>|dtm可轻松接入一门新语言|
|异常处理| <span style="color:green">[子事务屏障自动处理](https://zhuanlan.zhihu.com/p/388444465)</span>|<span style="color:orange">手动处理</span> |dtm解决了幂等、悬挂、空补偿|
| TCC事务| <span style="color:green">✓</span>|<span style="color:green">✓</span>||
| XA事务|<span style="color:green">✓</span>|<span style="color:green">✓</span>||
|AT事务|<span style="color:red">✗</span>|<span style="color:green">✓</span>|AT与XA类似，性能更好，但有脏回滚|
| SAGA事务 |<span style="color:orange">简单模式</span> |<span style="color:green">状态机复杂模式</span> |dtm的状态机模式在规划中|
|事务消息|<span style="color:green">✓</span>|<span style="color:red">✗</span>|dtm提供类似rocketmq的事务消息|
|通信协议|HTTP|dubbo等协议，无HTTP|dtm后续将支持grpc类协议|
|star数量|<img src="https://img.shields.io/github/stars/dtm-labs/dtm.svg?style=social" alt="github stars"/>|<img src="https://img.shields.io/github/stars/seata/seata.svg?style=social" alt="github stars"/>|dtm从20210604发布0.1，发展快|

从上面对比的特性来看，如果您的语言栈包含了Java之外的语言，那么dtm是您的首选。如果您的语言栈是Java，您也可以选择接入dtm，使用子事务屏障技术，简化您的业务编写。


## 使用方式

### 步骤一：确定你需要使用的版本
1. 您的项目是springcloud项目
- 您的项目中springboot版本>=2.4.0，请选择dtmcli-springcloud相应的版本直接接入即可
- 您的项目中的springboot版本<2.4.0，请选择dtmcli-java接入，dtmcli-java也提供了微服务相关的接口，请设置nacos服务中心的相关配置即可使用
2. 您的项目是普通项目/没有接入微服务的spring(boot)项目
- 请选择dtmcli-java，并设置相应的配置即可

|  artifact| version | 适用版本 |备注|
|:-----:|:----:|:----:|:----:|
|dtmcli-springcloud| 2.1.0.1| 2.4.0 <= springboot version < 2.5.13| springboot 版本>=2.5.0，需要设置spring.cloud.compatibility-verifier.enabled=false|
|dtmcli-springcloud| 2.1.0.2| 2.6.0 <= springboot version < 2.6.latest| |
|dtmcli-java| 2.1.0| others| |

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

### License

[MIT](https://github.com/dtm-labs/dtmcli/blob/master/LICENSE)

[license-badge]:   https://img.shields.io/github/license/dtm-labs/dtmcli-py