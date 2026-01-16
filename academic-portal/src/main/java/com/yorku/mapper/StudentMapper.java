package com.yorku.mapper;

import com.yorku.pojo.Student;
import com.yorku.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Student Mapper Interface
 * Refactored for York University Academic System
 */
@Mapper
public interface StudentMapper {

    // 分页查询 (需要去 StudentQueryParam 里把 degree 改为 yearLevel)
    List<Student> list(StudentQueryParam studentQueryParam);

    // 统计某个专业(班级)的人数
    @Select("select count(*) from student where clazz_id = #{id}")
    Integer countByClazzId(Integer id);

    // 新增学生
    void save(Student student);

    // 根据ID查询
    Student findById(Integer id);

    // 修改学生信息
    void update(Student student);

    // 批量删除
    void delete(List<Integer> ids);

    /**
     * 更新 GPA
     * @param id 学生ID
     * @param gpa 新的 GPA 值 (Double类型)
     */

    void updateGPA(Integer id, Double gpa);

    /**
     * 统计各专业学生数量
     */
    @MapKey("clazz_id")
    List<Map<String, Object>> countStuNumData();

    /**
     * 统计年级分布 (原学历统计)
     * 对应图表: Dashboard 饼图
     */
    @MapKey("year_level")
    List<Map<String, Object>> countStuYearLevelData();

}