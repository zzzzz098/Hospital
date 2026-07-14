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
@TableName("recode")
public class Recode {
    @TableId(value = "rid", type = IdType.AUTO)
    private Integer rid;
    private Integer pid;
    @TableField("wid")
    private Integer wid;
    private Integer did;
    private Integer serialnumber;
    private String visitdate;
    private String visitnoon;
    private String visittime;
    private String ordertime;
    private String state;
}
