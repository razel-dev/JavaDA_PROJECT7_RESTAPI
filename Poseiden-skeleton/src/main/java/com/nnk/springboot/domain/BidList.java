package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bidlist")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BidList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_list_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "account", length = 30, nullable = false)
    private String account;

    @Column(name = "type", length = 30, nullable = false)
    private String type;

    @Column(name = "bid_quantity", precision = 19, scale = 4)
    private BigDecimal bidQuantity;


    public BidList(String account, String type, BigDecimal bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
