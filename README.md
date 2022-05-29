# dtmcli ![MIT][license-badge]

`dtmcli-java` is the Java client SDK for distributed transaction manager [dtm](https://github.com/dtm-labs/dtm)

## What is DTM

DTM is a distributed transaction framework which provides cross-service eventual data consistency. It provides saga, tcc, xa, 2-phase message, outbox patterns for a variety of application scenarios. It also supports multiple languages and multiple store engine to form up a transaction as following:

<img alt="function-picture" src="https://en.dtm.pub/assets/function.7d5618f8.png" height=250 />

## Features
* Support for multiple languages: Go, Java, PHP, C#, Python, Nodejs SDKs
* Support for multiple transaction patterns: SAGA, TCC, XA
* Support for OutBox pattern: 2-phase messages, a more elegant solution than OutBox
* Support for multiple database transactions: Mysql, Redis, MongoDB, Postgres, TDSQL, etc.
* Support for multiple storage engines: Mysql (common), Redis (high performance), MongoDB (in planning)
* Support for multiple microservices architectures: [go-zero](https://github.com/zeromicro/go-zero), go-kratos/kratos, polarismesh/polaris
* Support for high availability and easy horizontal scaling

## Usage

### Step 1: Determine the version
1. Springcloud Projects
- If the version of springboot >= 2.4.0, choose a corresponding version of dtmcli-springcloud.
- If the version of springboot <> 2.4.0, use dtmcli-java, it provide the interfaces for micro-services, please set the configuration for nacos.
2. Not Springcloud Projects.
- use dtmcli-java，and set your configurations.

|  artifact| version | dtmcli-java version |remark|
|:-----:|:----:|:----:|:----:|
|dtmcli-springcloud| 2.1.4.1| 2.4.0 <= springboot version < 2.5.13| for springboot >= 2.5.0，please set spring.cloud.compatibility-verifier.enabled=false|
|dtmcli-springcloud| 2.1.4.2| 2.6.0 <= springboot version < 2.6.latest| |
|dtmcli-java| 2.1.4| others| |

### Step 2: Add Dependencies

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

### Step 3：Configure dtmcli-java
If you are using dtmcli-java，new a file named `dtm-conf.properties`
- Case 1: Using naceos
```
serverAddr=127.0.0.1:8848
username=nacos
password=nacos
namespace=c3dc917d-906a-429d-90a9-85012b41014e
dtm.service.name=dtmService
dtm.service.registryType=nacos
```
- Case 2: Connect to dtmsvr directly
```
dtm.ipport=127.0.0.1:36789
```
## Sample

```bash
@RequestMapping("testTcc")
    public String testTcc() {
        // new dtm clinet
        DtmClient dtmClient = new DtmClient(ipPort);
        //create TCC transaction
        try {
            dtmClient.tccGlobalTransaction(dtmClient.genGid(), TccTestController::tccTrans);
        } catch (Exception e) {
            log.error("tccGlobalTransaction error", e);
            return "fail";
        }
        return "success";
    }

/**
     * define TCC sub-transactions by calling callBranch
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


### Complete Sample

#### Sample for dtmcli-java
[dtmcli-java-sample](https://github.com/dtm-labs/dtmcli-java-sample)
[dtmcli-java-sample-use-configuration](https://github.com/horseLk/dtmcli-java-sample-with-conf)
#### Sample for dtmcli-springcloud
[dtmcli-java-spring-sample](https://github.com/dtm-labs/dtmcli-java-spring-sample)

[MIT](https://github.com/dtm-labs/dtmcli/blob/main/LICENSE)

[license-badge]: https://img.shields.io/github/license/dtm-labs/dtmcli-java
