package com.example.tttndemo.utils;

public class Items {

    private String name;

    private Long value;

    public Items(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public Items() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
