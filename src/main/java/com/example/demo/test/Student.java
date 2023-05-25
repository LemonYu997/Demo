package com.example.demo.test;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mly
 * @date 2023/4/4 15:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    private String name;

    private BigDecimal amount;

    private Date birthday;
}
