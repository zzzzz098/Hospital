package com.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("workday")
public class WorkDay {
    @TableId(value = "wid", type = IdType.AUTO)
    private Integer wid;
    private Integer did;
    private String worktime;
    private String ampm;
    private Integer nsnum;
    private String state;
}
