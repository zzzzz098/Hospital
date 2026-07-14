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
@TableName("medicine_push")
public class MedicinePush {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String pushTime;
}
