package com.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("patient")
public class Patient {
    @TableId(value = "pid", type = IdType.AUTO)
    private Integer pid;
    private String account;
    private String email;
    private String password;
    private String name;
    private String integrity;
}
