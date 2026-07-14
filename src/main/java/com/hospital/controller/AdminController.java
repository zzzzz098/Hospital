package com.hospital.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hospital.common.response.ResponseResult;
import com.hospital.domain.*;
import com.hospital.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private DoctorMapper doctorMapper;
    @Autowired private PatientMapper patientMapper;
    @Autowired private OfficeMapper officeMapper;
    @Autowired private RecodeMapper recodeMapper;
    @Autowired private WorkDayMapper workDayMapper;
    @Autowired private MedicinePushMapper medicinePushMapper;

    @GetMapping("/dashboard")
    public ResponseResult<Map<String, Long>> dashboard() {
        StpUtil.checkRole("doctor");
        Map<String, Long> stats = new HashMap<>();
        stats.put("patients", patientMapper.selectCount(null));
        stats.put("doctors", doctorMapper.selectCount(null));
        stats.put("offices", officeMapper.selectCount(null));
        stats.put("appointments", recodeMapper.selectCount(null));
        return ResponseResult.success(stats);
    }

    @GetMapping("/patients")
    public ResponseResult<List<Map<String, Object>>> patients() {
        StpUtil.checkRole("doctor");
        List<Map<String, Object>> list = new ArrayList<>();
        for (Patient p : patientMapper.selectList(null)) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", p.getPid());
            m.put("account", p.getAccount());
            m.put("name", p.getName());
            m.put("email", p.getEmail());
            m.put("integrity", p.getIntegrity());
            list.add(m);
        }
        return ResponseResult.success(list);
    }

    @GetMapping("/appointments")
    public ResponseResult<List<Recode>> appointments() {
        StpUtil.checkRole("doctor");
        return ResponseResult.success(recodeMapper.selectList(null));
    }

    @PutMapping("/schedule/{wid}")
    public ResponseResult<String> updateSchedule(@PathVariable Integer wid, @RequestBody Map<String, Object> body) {
        StpUtil.checkRole("doctor");
        WorkDay wd = workDayMapper.selectById(wid);
        if (wd != null) {
            if (body.get("state") != null) wd.setState((String) body.get("state"));
            if (body.get("nsnum") != null) wd.setNsnum((Integer) body.get("nsnum"));
            workDayMapper.updateById(wd);
        }
        return ResponseResult.success("保存成功");
    }

    @PostMapping("/medicine-push")
    public ResponseResult<String> createPush(@RequestBody Map<String, String> body) {
        StpUtil.checkRole("doctor");
        MedicinePush push = new MedicinePush();
        push.setTitle(body.get("title"));
        push.setContent(body.get("content"));
        push.setPushTime(LocalDateTime.now().toString());
        medicinePushMapper.insert(push);
        return ResponseResult.success("发布成功");
    }

    @GetMapping("/medicine-push")
    public ResponseResult<List<MedicinePush>> listPushes() {
        return ResponseResult.success(medicinePushMapper.selectList(null));
    }
}
