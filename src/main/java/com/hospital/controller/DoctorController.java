package com.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.common.response.ResponseResult;
import com.hospital.domain.*;
import com.hospital.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired private DoctorMapper doctorMapper;
    @Autowired private OfficeMapper officeMapper;
    @Autowired private WorkDayMapper workDayMapper;

    @GetMapping("/doctors")
    public ResponseResult<List<Doctor>> listDoctors(
            @RequestParam(defaultValue = "") String office,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {
        LambdaQueryWrapper<Doctor> w = new LambdaQueryWrapper<>();
        if (!office.isEmpty()) w.like(Doctor::getOffice, office);
        if (!name.isEmpty()) w.like(Doctor::getDname, name);
        Page<Doctor> p = new Page<>(page, size);
        return ResponseResult.success(doctorMapper.selectPage(p, w).getRecords());
    }

    @GetMapping("/doctors/{did}")
    public ResponseResult<Doctor> doctorDetail(@PathVariable Integer did) {
        return ResponseResult.success(doctorMapper.selectById(did));
    }

    @GetMapping("/doctors/{did}/schedule")
    public ResponseResult<List<WorkDay>> doctorSchedule(@PathVariable Integer did) {
        LambdaQueryWrapper<WorkDay> w = new LambdaQueryWrapper<>();
        w.eq(WorkDay::getDid, did).orderByAsc(WorkDay::getWorktime);
        return ResponseResult.success(workDayMapper.selectList(w));
    }

    @GetMapping("/offices")
    public ResponseResult<List<Office>> offices() {
        return ResponseResult.success(officeMapper.selectList(null));
    }
}
