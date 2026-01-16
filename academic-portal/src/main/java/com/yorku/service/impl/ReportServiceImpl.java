package com.yorku.service.impl;

import com.yorku.mapper.EmpMapper;
import com.yorku.mapper.StudentMapper;
import com.yorku.pojo.JobOption;
import com.yorku.pojo.StudentCountOption;
import com.yorku.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Report Service Implementation
 * (Legacy Service: Handles Employee Stats. Student stats are now mostly in StudentService)
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 统计员工职位人数
     */
    @Override
    public JobOption getEmpJobData() {
        List<Map<String, Object>> list = empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("cnt")).toList();
        return new JobOption(jobList, dataList);
    }

    /**
     * 统计员工性别信息
     */
    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    /**
     * 统计课程人数 (Legacy Support)
     */
    @Override
    public StudentCountOption getStudentCountData() {
        List<Map<String, Object>> list = studentMapper.countStuNumData();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("name")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("value")).toList();
        return new StudentCountOption(clazzList, dataList);
    }

    /**
     * 统计学员年级分布
     */
    @Override
    public List<Map<String, Object>> getStudentEduData() {
        return studentMapper.countStuYearLevelData();
    }
}