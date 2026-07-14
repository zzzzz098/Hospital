package com.hospital.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hospital.common.response.ResponseResult;
import com.hospital.domain.*;
import com.hospital.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired private MessageMapper messageMapper;

    @PostMapping
    public ResponseResult<String> create(@RequestBody Map<String, Object> body) {
        StpUtil.checkLogin();
        Message msg = new Message();
        msg.setPid(Integer.valueOf(StpUtil.getLoginIdAsString()));
        msg.setContent((String) body.get("content"));
        if (body.get("did") != null) msg.setDid((Integer) body.get("did"));
        msg.setCreateTime(LocalDateTime.now().toString());
        messageMapper.insert(msg);
        return ResponseResult.success("留言成功");
    }

    @GetMapping
    public ResponseResult<List<Message>> myList() {
        StpUtil.checkLogin();
        LambdaQueryWrapper<Message> w = new LambdaQueryWrapper<>();
        w.eq(Message::getPid, Integer.valueOf(StpUtil.getLoginIdAsString())).orderByDesc(Message::getCreateTime);
        return ResponseResult.success(messageMapper.selectList(w));
    }

    @GetMapping("/all")
    public ResponseResult<List<Message>> allList() {
        if (!"doctor".equals(StpUtil.getTokenSession().getString("role"))) throw new RuntimeException("无权限");
        LambdaQueryWrapper<Message> w = new LambdaQueryWrapper<>();
        w.orderByDesc(Message::getCreateTime);
        return ResponseResult.success(messageMapper.selectList(w));
    }

    @PutMapping("/{id}/reply")
    public ResponseResult<String> reply(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        if (!"doctor".equals(StpUtil.getTokenSession().getString("role"))) throw new RuntimeException("无权限");
        Message msg = messageMapper.selectById(id);
        if (msg != null) {
            msg.setReply(body.get("reply"));
            msg.setReplyTime(LocalDateTime.now().toString());
            messageMapper.updateById(msg);
        }
        return ResponseResult.success("回复成功");
    }

    @DeleteMapping("/{id}")
    public ResponseResult<String> delete(@PathVariable Integer id) {
        StpUtil.checkLogin();
        messageMapper.deleteById(id);
        return ResponseResult.success("删除成功");
    }
}
