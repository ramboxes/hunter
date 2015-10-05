### 分布式跟踪系统

#### 问题
我们需要解决的问题有两个维度：

##### 一、性能跟踪
要能发现系统中的性能瓶颈
1. 单机系统中各个方法的性能跟踪
2. 分布式系统中各个服务之间调用的性能跟踪

##### 二、日志关联
1. 分布式环境中，需要能够把一次调用中各个服务的日志关联起来，便于排查问题

#### 问题的解决方向
##### 一、性能跟踪
可以借鉴google的这篇论文：http://bigbully.github.io/Dapper-translation/ 
很多公司都借鉴了该论文有了实现了自己的系统：
+ twitter的zipkin
+ 阿里的“鹰眼”
+ 京东的“Hydra”
+ 窝窝的"Tracing"

其中，开源并且持续更新的只有zipkin。为了能够减小实现的成本，我们需要尽可能的使用稳定的开源资源，所以我们会在zipkin的基础上实现跟踪功能。

##### 二、日志关联
业内对于分布式日志的处理有了系统的解决方案ELK，我们需要做的是给每次调用都配置一个唯一的Id，这样就可以利用这个Id统一搜索。

##### 三、要求
1. 低开销：对系统性能的影响需要可控
2. 对应用透明：业务系统不能依赖于跟踪系统，业务系统可以通过简单配置选择是否需要跟踪。

#### 总体方案
##### 一、性能跟踪
###### 选型
zipkin的架构图见zipkin：https://github.com/openzipkin/zipkin
Collector：数据收集
Cassandra：数据存储，这里支持使用redis代替，数据过期时间默认是7天
Query：数据检索
Web：数据展示
我们只需要使用ZipKin支持的格式，把日志传输给Collector，我们就可以直接复用Zipkin的上述模块。
Zipkin本身不提供日志的生成和传输功能，twitter内部大量使用了finagle框架，finagle框架内部直接与zipkin进行集成，不用做任何其他工作。但是由于我们的系统中并没有使用finagle，所以需要寻找其他的解决方案。
目前也有一些其他的项目提供和zipkin进行集成的功能，比如Brave（https://github.com/openzipkin/brave），但是从提供的具体功能和代码的复杂性上说，直接使用不太合适，如果出现问题，成本甚至超过自己开发，所以我们完全自己开发。

###### 集成方式
1. 通过MVC框架和interceptor和Spring的AOP进行拦截，添加跟踪功能。需要跟踪的项目，只需要进行一些简单的配置就可以，对业务代码没有任何侵入性。
2. 性能跟踪不用处理每个请求，需要采样的功能，按照某种规则进行采样，这样可以控制跟踪功能对性能的影响。

##### 二、日志关联
###### 选型
由于我们所有项目都用到了log4j，所以最方便的方式就是自定义log4j的Layout

###### 集成方式
需要修改log4j配置文件，把layout替换成我们自定义的layout，

##### 三、提供形式
提供一个jar包，和几个示例工程。需要使用跟踪功能的系统，首先引入Jar包，然后按照示例工程

