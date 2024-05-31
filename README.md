# Aventus

## Extension: 业务扩展框架

## 框架结构
![](https://img.alicdn.com/imgextra/i4/O1CN01tXTb2K1CZYZL8dyWP_!!6000000000095-2-tps-1362-1482.png)

## 使用方式
![](https://img.alicdn.com/imgextra/i2/O1CN01w67nnS1zErqos8eRN_!!6000000006683-2-tps-2088-920.png)



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
public void test() throws Throwable {
    OrderCreateContext input = new OrderCreateContext();
    input.setItemId(new Random().nextInt());
    long bizOrderId = flow.execute(input);
    System.out.println("bizOrderId = " + bizOrderId);
}
```