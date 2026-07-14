package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.domain.Patient;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
}
