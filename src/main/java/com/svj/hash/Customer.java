package com.svj.hash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String phone;
}
