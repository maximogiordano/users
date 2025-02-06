package com.maximogiordano.users.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "number", "city_code", "country_code"}))
public class Phone {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Long number;

    @Column(name = "city_code", nullable = false)
    private Integer cityCode;

    @Column(name = "country_code", nullable = false)
    private String countryCode;
}
