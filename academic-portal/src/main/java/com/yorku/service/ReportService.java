package com.yorku.service;

import com.yorku.pojo.JobOption;
import com.yorku.pojo.StudentCountOption;

import java.util.List;
import java.util.Map;

/**
 * @Description: 报表统计的service接口
 */
public interface ReportService {

    /**
     * 统计员工职位人数
     */
    JobOption getEmpJobData();

    /**
     * 统计员工性别人数
     */
    List<Map<String, Object>> getEmpGenderData();

    /**
     * 统计班级人数
     */
    StudentCountOption getStudentCountData();

    /**
     * 统计学员学历
     */
    List<Map<String, Object>> getStudentEduData();
}
