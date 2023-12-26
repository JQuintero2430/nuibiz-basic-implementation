package com.jorge.practice.niubizdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
@Table("commerce")
public class Commerce {
    private Long id;
    private String name;
    private String username;
    private String password;
}
