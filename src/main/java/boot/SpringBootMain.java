package boot;

import boot.bean.*;
import boot.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;

import java.util.Map;
import java.util.Set;

/*
一、@SpringBootApplication：这是一个SpringBoot应用
        @SpringBootApplication
        等同于
        @SpringBootConfiguration
        @EnableAutoConfiguration
        @ComponentScan("xxx")

二、默认的包结构
    要将Application（入口类）放在最外层，也就是要包含所有子包。
     (1) 主程序所在包及其下面的所有子包里面的组件都会被默认扫描进来
     (2) 无需以前的包扫描配置
     (3) 想要改变扫描路径，@SpringBootApplication(scanBasePackages="xxx")
            或者@ComponentScan 指定扫描路径，注解添加到主程序所在类上面

三、LomBok
    (1)简化JavaBean开发
        @ToString---帮助生成ToString方法
        @AllArgsConstructor---生成有参构造器
        @NoArgsConstructor---生成无参构造方法
        @EqualsAndHashCode---生成HashCode和Equals方法
        @Data 包含了 @ToString、@EqualsAndHashCode、@Getter/@Setter
                 和@RequiredArgsConstructor的功能
    (2)简化日志开发
        例如: 添加@Slf4j后可以使用log.info("xxx");

 */
@SpringBootApplication
public class SpringBootMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBootMain.class, args);

        System.out.println("===================SpringMVC常用组件===================");
        DispatcherServlet dispatcherServlet =
                (DispatcherServlet) context.getBean("dispatcherServlet");
        CharacterEncodingFilter characterEncodingFilter =
                (CharacterEncodingFilter) context.getBean("characterEncodingFilter");
        ViewResolver viewResolver = (ViewResolver) context.getBean("viewResolver");
        MultipartResolver multipartResolver =
                (MultipartResolver) context.getBean("multipartResolver");
        System.out.println("dispatcherServlet = " + dispatcherServlet);
        System.out.println("characterEncodingFilter = " + characterEncodingFilter);
        System.out.println("viewResolver = " + viewResolver);
        System.out.println("multipartResolver = " + multipartResolver);

        System.out.println("========================组件添加========================");
        // 原始方式获取MyConfig对象
        MyConfig myConfig = new MyConfig();
        // 从IOC容器中取出MyConfig对象
        MyConfig myConfigByIOC = context.getBean(MyConfig.class);
        // 从IOC容器中取出Employee对象
        Employee employeeByIOC = context.getBean(Employee.class);
        // 当配置类中的方法调用配置类中的其他方法时，会检查容器中是否有已经注册了的实例
        Employee employeeBymyConfigIOC = myConfigByIOC.employee();
        // 用类.方法的方式创建一个Employee对象
        Employee employeeBymyConfig = myConfig.employee();
        // 测试
        System.out.println("从两个配置类取出的Employee是否为同一个对象："
                + (employeeBymyConfig == employeeBymyConfigIOC));
        System.out.println("从IOC容器中取出的Employee是否为同一个对象："
                + (employeeByIOC == employeeBymyConfigIOC));

        // 从容器中获取组件
        Department department = context.getBean(Department.class);
        Department department1 = context.getBean(Department.class);
        System.out.println("department与department1是否是同一个对象："
                + (department == department1));
        // 从在IOC容器中取出的Employee对象中获取department2
        //Department department2 = employeeByIOC.getDepartment();
        //System.out.println("department与department2是否是同一个对象："
        //        + (department == department2));

        System.out.println("========================条件装配========================");
        if (context.containsBean("dog")) {
            Dog dog = (Dog) context.getBean("dog");
            dog.setAge(11);
            dog.setName("旺财");
            System.out.println("dog = " + dog);
        } else {
            Cat cat = (Cat) context.getBean("cat");
            System.out.println("cat = " + cat);
        }

        System.out.println("======================配置文件引入======================");
        Dog springDog = (Dog) context.getBean("springDog");
        Cat springCat = (Cat) context.getBean("springCat");
        System.out.println("springCat = " + springCat);
        System.out.println("springDog = " + springDog);

        Milk milk = context.getBean(Milk.class);
        System.out.println("milk = " + milk);
        Person person = context.getBean(Person.class);
        System.out.println(person);
        Map<String, Object> score = person.getScore();
        Set<String> keySet = score.keySet();
        for (String key : keySet) {
            Object value = score.get(key);
            System.out.println("key = " + key + "<--->" + "value = " + value);
        }

    }
}
