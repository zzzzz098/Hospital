package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.domain.Recode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecodeMapper extends BaseMapper<Recode> {

    @Select("select p.name as pname, r.ordertime, r.state, r.rid, r.serialnumber, "
            + "ns.nstime, wd.worktime "
            + "from workday wd, numsource ns, recode r, patient p "
            + "where wd.did = #{did} and wd.wid = ns.workdayid and ns.id = r.wid "
            + "and r.pid = p.pid order by r.ordertime desc")
    List<Map<String, String>> patientQueue(Integer did);
}
