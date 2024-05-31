# Aventus

## Extension: 业务扩展隔离框架

### 开发与配置:@注解方式
#### 扩展点开放
```java
@Extension(desc = "下单扩展能力")
public interface OrderCreateSpi {

  boolean isUseRedPacked(OrderCreateParam param);

  Map<String, Object> getCustomOrderAttributes(OrderCreateParam param);
}
```

#### 扩展点实现
- 默认实现
```java
@ExtensionBase
public class BaseOrderCreateSpiImpl implements OrderCreateSpi {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return false;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.emptyMap();
    }
}
```

- 业务实现: 普通交易
```java
@ExtensionBusiness(code = "normal", desc = "普通电商交易")
public class NormalOrderCreateSpiImpl extends BaseOrderCreateSpiImpl {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return true;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.singletonMap("order-source", "normal");
    }
}
```

- 业务实现: O2O交易
```java
@ExtensionBusiness(code = "o2o", desc = "O2O交易")
public class O2OOrderCreateSpiImpl extends BaseOrderCreateSpiImpl {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return false;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.singletonMap("order-source", "o2o");
    }
}
```

### 开发与配置(XML方式)
#### 扩展点开放
```java
public interface OrderCreateSpi {

    boolean isUseRedPacked(OrderCreateParam param);

    Map<String, Object> getCustomOrderAttributes(OrderCreateParam param);
}
```
> 不再需要@Extension注解声明

#### 扩展点业务实现
- 默认实现
```java
@Component
public class BaseOrderCreateSpiImpl implements OrderCreateSpi {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return false;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.emptyMap();
    }
}
```
> 不再需要@ExtensionBase注解, 但需要使用@Component注解声明为spring的bean

- 业务实现: 普通交易
```java
@Component
public class NormalOrderCreateSpiImpl extends BaseOrderCreateSpiImpl {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return true;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.singletonMap("order-source", "normal");
    }
}
```
> 不再需要@ExtensionBusiness注解, 但需要使用@Component注解声明为spring的bean

- 业务实现: O2O交易
```java
@Component
public class O2OOrderCreateSpiImpl extends BaseOrderCreateSpiImpl {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return false;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.singletonMap("order-source", "o2o");
    }
}
```
> 同上

#### 扩展点配置(xml方式特有)
```xml
<?xml version="1.0" encoding="utf-8"?>
<Extensions xmlns="https://www.alibaba.com/aventus/extension"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="https://www.alibaba.com/aventus/extension  https://www.alibaba.com/aventus/extension.xsd">
    <Extension class="com.alibaba.aventus.test.extension.spi.OrderCreateSpi" base="baseOrderCreateSpiImpl" desc="下单扩展能力">
        <Business code="normal" type="bean" desc="普通电商交易">
            <bean name="normalOrderCreateSpiImpl"/>
        </Business>
        <Business code="o2c" type="bean" desc="O2O交易">
            <bean name="o2OOrderCreateSpiImpl"/>
        </Business>
    </Extension>

</Extensions>
```

### 扩展点调用(API方式)
```java
@Test
public void test() throws Exception {

    // 业务身份解析
    Extension.parse(DemoExtensionParser.class, new String[]{"base"});


    OrderCreateParam param = new OrderCreateParam();

    // 扩展点调用
    boolean isUseRedPacket = Extension.execute(OrderCreateSpi.class, spi -> spi.isUseRedPacked(param));
    System.out.println(isUseRedPacket);

    List<Map<String, Object>> attributes = Extension.execute(OrderCreateSpi.class, spi -> spi.getCustomOrderAttributes(param), Reducers.collect());
    for (Map<String, Object> attribute : attributes) {
        System.out.println(attribute);
    }

    // 上下文清理
    Extension.clear();
}
```

### 扩展点调用(Bean注入方式)
- Bean配置
```java
@Bean
public ExtensionSpringBean<OrderCreateSpi> orderCreateSpi() {
    ExtensionSpringBean<OrderCreateSpi> springBean = new ExtensionSpringBean<>();
    springBean.setExtension(OrderCreateSpi.class);
    springBean.setReducer(Reducers.firstOf());
    return springBean;
}
```

- 发起调用
```java
@Resource
private OrderCreateSpi orderCreateSpi;

@Test
public void test() throws Exception {

    // 业务身份解析
    Extension.parse(DemoExtensionParser.class, new String[]{"base"});


    OrderCreateParam param = new OrderCreateParam();

    // 扩展点调用
    // boolean isUseRedPacket = Extension.execute(OrderCreateSpi.class, spi -> spi.isUseRedPacked(param));
    boolean isUseRedPacket = orderCreateSpi.isUseRedPacked(param);
    System.out.println(isUseRedPacket);

    // List<Map<String, Object>> attributes = Extension.execute(OrderCreateSpi.class, spi -> spi.getCustomOrderAttributes(param), Reducers.collect());
    Map<String, Object> attributes = orderCreateSpi.getCustomOrderAttributes(param);
    System.out.println(attributes);

    // 上下文清理
    Extension.clear();
}
```

### Parser编写
实现`ExtensionParser`接口, 覆盖`parseExtensionCode`方法
```java
public class DemoExtensionParser implements ExtensionParser<String[]> {

    @Override
    public String parseExtensionCode(String[] args) {
        return args[0];
    }
}
```

### 执行结果回收(Reducer)

### 远程扩展调用(Dubbo/Hsf)
> 仅支持xml方式配置(扩展点声明与实现可以与注解方式配置)

### 上下文机制(ExtensionContext)

### 路由机制(ExtensionRouter)

### 插件机制(ExtensionPlugin)

### 二次路由调用(SubGroup)

### 实现原理

---

### 框架结构

![](https://img.alicdn.com/imgextra/i4/O1CN01tXTb2K1CZYZL8dyWP_!!6000000000095-2-tps-1362-1482.png)

### 使用方式
![](https://img.alicdn.com/imgextra/i2/O1CN01w67nnS1zErqos8eRN_!!6000000006683-2-tps-2088-920.png)

---

## Flow: 流程编排引擎

#### 流程配置

```xml

<bean id="ORDER-CREATE-FLOW" class="com.alibaba.aventus.flow.Flow">
    <constructor-arg name="name" value="ORDER-CREATE"/>
    <constructor-arg name="nodes">
        <util:list>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode1"/>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode2"/>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode2"/>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode2"/>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode2"/>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode2"/>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode2"/>
            <bean class="com.alibaba.aventus.test.flow.node.OrderCreateNode3"/>
        </util:list>
    </constructor-arg>
</bean>
```

#### 流程执行

```java
@Resource
private Flow<OrderCreateContext, Long> flow;

@Test
public void test()throws Throwable{
        OrderCreateContext input=new OrderCreateContext();
        input.setItemId(new Random().nextInt());
        long bizOrderId=flow.execute(input);
        System.out.println("bizOrderId = "+bizOrderId);
        }
```

---

## 日志系统

- 已经适配logback、log4j2两种日志系统
- 执行日志地址: `${user.home}/logs/aventus`
    - 扩展执行日志: ${user.home}/logs/aventus/extension.log
    - 流程执行日志: ${user.home}/logs/aventus/flow.log