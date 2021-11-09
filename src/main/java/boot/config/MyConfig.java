package boot.config;

import boot.bean.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/*
一、组件添加
      1、配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
      2、配置类本身也是组件
      3、proxyBeanMethods：代理bean的方法
           Full(proxyBeanMethods = true)
                【保证每个@Bean方法被调用多少次返回的组件都是单实例的】
           Lite(proxyBeanMethods = false)
                【每个@Bean方法被调用多少次返回的组件都是新创建的】
           组件依赖必须使用Full模式默认。其他默认是否Lite模式
      4、如果@Configuration(proxyBeanMethods = true)代理对象调用方法。
            SpringBoot总会检查这个组件是否在容器中有。

二、Full模式和Lite模式是针对spring配置而言的，和xml配置无关。
    (1)何时为Lite模式：
        1.类上有@Component注解
        2.类上有@ComponentScan注解
        3.类上有@Import注解
        4.类上有@ImportResource注解
        5.类上没有任何注解，但是类中存在@Bean方法
        6.类上有@Configuration(proxyBeanMethods = false)注解
    Lite总结：运行时不用生成CGLIB子类，提高运行性能，降低启动时间，可以作为普通类使用。
                但是不能声明@Bean之间的依赖
    (2)何时为Full模式：
        标注有@Configuration或者@Configuration(proxyBeanMethods = true)的类被称为Full模式的配置类。
    Full总结：单例模式能有效避免Lite模式下的错误。性能没有Lite模式好

三、@Conditional
    (1)派生了很多的子注解，它们可以添加在@Bean注解的方法上，也可以放在配置类上
    (2)在方法上满足所需条件时则执行方法中内容并注册到 IOC 容器中，如果不满足条件则不注册
    (3)在配置类中满足需求时则执行配置类中所有的@Bean方法并注册到 IOC 容器中，如果不满足条件则不注册
    (4)以@ConditionalOnBean(name="dog")为例，当 IOC 容器中拥有id为dog的组件时才会满足条件，否则
        不满足条件

四、@ImportResource
    (1)允许以spring配置文件的方式对组件进行注册，指以.xml结尾的配置文件，通过@ImportResource导入
        后SpringBoot进行解析，完成对应的组件注册
    (2)位置在主配置类的上方
    (3)主要是为了兼容第三方，注入IOC

五、配置绑定
    (1)两种方式
        1.@Component、@ConfigurationProperties(prefix = "milk")声明在要绑定的类的上方
        2.@ConfigurationProperties(prefix = "milk")声明在要绑定的类的上方
            在配置类的上方声明@EnableConfigurationProperties(Milk.class)，开启对应类的
            配置绑定功能，把Milk这个组件自动注入到容器中
    (2)如果@ConfigurationProperties是在第三方包中，那么@component是不能注入到容器的。
        只有@EnableConfigurationProperties才可以注入到容器。
    (3)
        1.只有在容器中的主件才会提供此功能
        2.只能是给springboot的核心配置文件进行绑定(application,properties/application.yaml)，
            不能是自己定义的配置文件

六、配置文件yaml
    适合用来做以数据为中心的配置文件
    (1) 基本语法
         > key: value；kv之间有空格
         > 大小写敏感
         > 使用缩进表示层级关系
         > 缩进不允许使用tab，只允许空格
         > 缩进的空格数不重要，只要相同层级的元素左对齐即可
         > '#'表示注释
         > 字符串无需加引号，如果要加，''与""表示字符串内容 会被 转义/不转义
    注：
         1、如果字符串为一段数字，该数字以零开头，必须添加上"" ，不然Springboot会将数字解析成八进制
         2、只有IDEA是可以使用TAB进行缩进的
    (2) 数据类型
        > 字面量：单个的、不可再分的值。date、boolean、string、number、null
            例：k: v
        > 对象：键值对的集合。map、hash、set、object
            例：行内写法：k: {k1:v1,k2:v2,k3:v3}
                #或      k:
                              k1: v1
                              k2: v2
                              k3: v3
        > 数组：一组按次序排列的值。array、list、queue
            例：行内写法：  k: [v1,v2,v3]
                #或        k:
                             - v1
                             - v2
                             - v3
    (3) 配置提示
        自定义的类和配置文件绑定一般没有提示。
        配置对应的pom文件，在application.yml中给自定义对象属性初始化值的时候会有属性名的提示，
        而插件的作用是优化打包效果，不把依赖引入的类打包，节省空间(新版本springboot自动排除)

 */
// 不能和@ConditionalOnBean(name = "milk")同时存在 否则会先执行@ConditionalOnBean 从而找不到Milk
// 也可以将@EnableConfigurationProperties写在主程序入口上
@EnableConfigurationProperties({Milk.class,Person.class})
//@ConditionalOnBean(name = "milk")
@ImportResource("classpath:SpringBean.xml") // 原生配置文件引入
@Configuration(proxyBeanMethods = true)
public class MyConfig {

    @Bean
    public Employee employee(){
        Employee employee = new Employee("林毅恒", 22);
        employee.setDepartment(department());
        return employee;
    }

    @Bean
    public Department department(){
        Department department = new Department();
        department.setName("开发部");
        return department;
    }

    // 如果不存在狗 则创建猫
    @ConditionalOnMissingBean (name = "dog")
    @Bean
    public Cat cat(){
        return new Cat("阿猫", 2);
    }
}
