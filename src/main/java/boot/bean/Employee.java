package boot.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
public class Employee {
    private String name;
    private int age;
    private Department department;

    public Employee(String name, int age) {
        log.info("Employee的有参构造器被调用了...");
        this.name = name;
        this.age = age;
    }
}
