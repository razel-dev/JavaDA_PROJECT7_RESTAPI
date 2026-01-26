package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rulename")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RuleName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "name", length = 125)
    private String name;

    @Column(name = "description", length = 125)
    private String description;

    @Column(name = "json", length = 125)
    private String json;

    @Column(name = "template", length = 512)
    private String template;

    @Column(name = "sql_str", length = 125)
    private String sqlStr;

    @Column(name = "sql_part", length = 125)
    private String sqlPart;


}
