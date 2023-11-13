package com.week.gdsc.service.transaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public SomeEntity(String name) {
        this.name = name;
    }

    public SomeEntity() {

    }

    public Long getId() {
        return id;
    }
}
