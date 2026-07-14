package com.hospital.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hospital.common.response.ResponseResult;
import com.hospital.domain.*;
import com.hospital.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired private RecodeMapper recodeMapper;

    @PostMapping
    public ResponseResult<String> create(@RequestBody Recode recode) {
        StpUtil.checkLogin();
        recode.setState("成功");
        recodeMapper.insert(recode);
        return ResponseResult.success("预约成功");
    }

    @GetMapping
    public ResponseResult<List<Recode>> myList() {
        StpUtil.checkLogin();
        String pid = StpUtil.getLoginIdAsString();
        LambdaQueryWrapper<Recode> w = new LambdaQueryWrapper<>();
        w.eq(Recode::getPid, Integer.valueOf(pid)).orderByDesc(Recode::getOrdertime);
        return ResponseResult.success(recodeMapper.selectList(w));
    }

    @PutMapping("/{rid}/cancel")
    public ResponseResult<String> cancel(@PathVariable Integer rid) {
        StpUtil.checkLogin();
        Recode recode = recodeMapper.selectById(rid);
        if (recode != null) {
            recode.setState("取消");
            recodeMapper.updateById(recode);
        }
        return ResponseResult.success("取消成功");
    }

    @GetMapping("/all")
    public ResponseResult<List<Recode>> allList() {
        if (!"doctor".equals(StpUtil.getTokenSession().getString("role"))) throw new RuntimeException("无权限");
        LambdaQueryWrapper<Recode> w = new LambdaQueryWrapper<>();
        w.orderByDesc(Recode::getOrdertime);
        return ResponseResult.success(recodeMapper.selectList(w));
    }
}
