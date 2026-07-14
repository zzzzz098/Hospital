package com.hospital.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hospital.common.response.ResponseResult;
import com.hospital.domain.*;
import com.hospital.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AdminMapper adminMapper;
    @Autowired private DoctorMapper doctorMapper;
    @Autowired private PatientMapper patientMapper;

    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String account = body.get("account");
        String password = body.get("password");
        // 自动识别身份：doc=医生，pat=患者，其他=管理员
        String role;
        if (account.startsWith("doc")) role = "doctor";
        else if (account.startsWith("pat")) role = "patient";
        else role = "admin";

        if ("admin".equals(role)) {
            Admin admin = adminMapper.selectById(account);
            if (admin != null && checkPwd(password, admin.getPassword())) {
                StpUtil.login(account);
                StpUtil.getTokenSession().set("role", "admin");
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", admin.getAccount());
                userInfo.put("account", admin.getAccount());
                userInfo.put("name", admin.getName());
                userInfo.put("role", "admin");
                return buildLoginResult(userInfo);
            }
        } else if ("doctor".equals(role)) {
            LambdaQueryWrapper<Doctor> w = new LambdaQueryWrapper<>();
            w.eq(Doctor::getAccount, account);
            Doctor doctor = doctorMapper.selectOne(w);
            if (doctor != null && checkPwd(password, doctor.getPassword())) {
                StpUtil.login(doctor.getDid().toString());
                StpUtil.getTokenSession().set("role", "doctor");
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", doctor.getDid());
                userInfo.put("account", doctor.getAccount());
                userInfo.put("name", doctor.getDname());
                userInfo.put("role", "doctor");
                userInfo.put("office", doctor.getOffice());
                return buildLoginResult(userInfo);
            }
        } else {
            LambdaQueryWrapper<Patient> w = new LambdaQueryWrapper<>();
            w.eq(Patient::getAccount, account);
            Patient patient = patientMapper.selectOne(w);
            if (patient != null && checkPwd(password, patient.getPassword())) {
                StpUtil.login(patient.getPid().toString());
                StpUtil.getTokenSession().set("role", "patient");
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", patient.getPid());
                userInfo.put("account", patient.getAccount());
                userInfo.put("name", patient.getName());
                userInfo.put("role", "patient");
                userInfo.put("integrity", patient.getIntegrity());
                return buildLoginResult(userInfo);
            }
        }
        return ResponseResult.error(1001, "用户名或密码错误");
    }

    @PostMapping("/register")
    public ResponseResult<String> register(@RequestBody Map<String, String> body) {
        String account = body.get("account");
        String password = body.get("password");
        String name = body.get("name");
        // 账号必须为8位纯数字
        if (account == null || !account.matches("\\d{8}")) {
            return ResponseResult.error("账号必须为8位纯数字");
        }
        LambdaQueryWrapper<Patient> w = new LambdaQueryWrapper<>();
        w.eq(Patient::getAccount, account);
        if (patientMapper.selectCount(w) > 0) {
            return ResponseResult.error("账号已存在");
        }
        Patient patient = new Patient();
        patient.setAccount(account);
        patient.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        patient.setName(name);
        patient.setEmail("");
        patient.setIntegrity("100");
        patientMapper.insert(patient);
        return ResponseResult.success("注册成功");
    }

    private ResponseResult<Map<String, Object>> buildLoginResult(Map<String, Object> userInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("userInfo", userInfo);
        return ResponseResult.success(result);
    }

    private boolean checkPwd(String raw, String stored) {
        if (stored == null) return false;
        if (stored.startsWith("$2")) return BCrypt.checkpw(raw, stored);
        return stored.equals(raw);
    }
}
