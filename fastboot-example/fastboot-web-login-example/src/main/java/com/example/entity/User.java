package com.example.entity;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Stylesmile
 */
@Data
@Builder
@ToString
public class User implements Serializable {
    private String username;
    private String age;
}
