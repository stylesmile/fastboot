package com.example;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class User {
    private String id;
    private String name;
    private Integer age;

}
