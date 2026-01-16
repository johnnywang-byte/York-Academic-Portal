package com.yorku.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yorku.mapper.StudentMapper;
import com.yorku.pojo.PageResult;
import com.yorku.pojo.Student;
import com.yorku.pojo.StudentQueryParam;
import com.yorku.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Student Service Implementation
 * Refactored for York University Academic Portal
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<Student> studentList = studentMapper.list(studentQueryParam);
        Page<Student> pageResult = (Page<Student>) studentList;
        return new PageResult<>(pageResult.getTotal(), pageResult.getResult());
    }

    @Override
    public void add(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        if (student.getGpa() == null) student.setGpa(0.0);
        if (student.getCredits() == null) student.setCredits(0);
        studentMapper.save(student);
    }

    @Override
    public Student findById(Integer id) {
        return studentMapper.findById(id);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void delete(List<Integer> ids) {
        studentMapper.delete(ids);
    }

    @Override
    public void updateGPA(Integer id, Double gpa) {
        studentMapper.updateGPA(id, gpa);
    }

    @Override
    public Map<String, Object> getStudentCountData() {
        // 1. 获取原始数据 (List<Map>)
        List<Map<String, Object>> dataList = studentMapper.countStuNumData();

        // 2. 拆分为两个 List (Names 和 Values) 给前端 ECharts 使用
        // 使用 Stream 流提取数据
        List<Object> nameList = dataList.stream().map(m -> m.get("name")).collect(Collectors.toList());
        List<Object> valueList = dataList.stream().map(m -> m.get("value")).collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("clazzList", nameList); // 对应前端的 X轴
        map.put("dataList", valueList); // 对应前端的 Y轴数据

        return map;
    }

    @Override
    public List<Map<String, Object>> getStudentYearLevelData() {
        // 直接调用 Mapper 新写的 XML 查询
        return studentMapper.countStuYearLevelData();
    }
}