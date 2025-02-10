package com.maximogiordano.users.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class InvalidToken {
    @Id
    private String value;
}
