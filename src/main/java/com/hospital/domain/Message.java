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
@TableName("message")
public class Message {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    private Integer did;
    private String content;
    private String reply;
    private String createTime;
    private String replyTime;
}
