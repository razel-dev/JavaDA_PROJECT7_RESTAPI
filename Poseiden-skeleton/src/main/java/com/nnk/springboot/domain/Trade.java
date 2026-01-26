package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "account", length = 30, nullable = false)
    private String account;

    @Column(name = "type", length = 30, nullable = false)
    private String type;

    @Column(name = "buy_quantity", precision = 19, scale = 4)
    private BigDecimal buyQuantity;

}
