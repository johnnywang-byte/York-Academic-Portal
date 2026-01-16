package com.yorku.mapper;

import com.yorku.pojo.Clazz;
import com.yorku.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Course Section Data Access Object (Mapper)
 * York University Academic Admin Portal
 *
 * Description:
 * Provides direct database interactions for managing Course Sections (formerly 'Class').
 * Handles CRUD operations for the academic scheduling system.
 *
 * @Description: 课程/班级管理 Mapper 接口
 * 负责与数据库交互，执行课程单元（Course Section）的增删改查操作。
 */
@Mapper
public interface ClazzMapper {

    /**
     * Search Course Sections (Paginated)
     * Retrieves a list of courses based on dynamic filters (Name, Date).
     *
     * 课程管理 - 分页查询
     * 根据查询参数（如课程名、日期）获取课程列表。
     */
    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    /**
     * Schedule New Course Section
     * Inserts a new course record into the database.
     *
     * 课程管理 - 新增课程
     * 向数据库插入一条新的课程/班级记录。
     */
    void save(Clazz clazz);

    /**
     * Remove Course Section
     * Deletes a course record by its unique ID.
     *
     * 课程管理 - 根据ID删除
     * 根据主键 ID 删除指定的课程记录。
     */
    void delete(Integer id);

    /**
     * Find Course by ID
     * Retrieves full details of a single course section.
     *
     * 课程管理 - 根据ID查询详情
     * 获取单个课程的完整信息（用于回显或详情展示）。
     */
    Clazz findById(Integer id);

    /**
     * Update Course Details
     * Modifies existing course information (e.g., Room, Instructor).
     *
     * 课程管理 - 修改课程信息
     * 更新已有课程的详细信息。
     */
    void update(Clazz clazz);

    /**
     * List All Course Sections
     * Retrieves every course record in the system (Non-paginated).
     *
     * 课程管理 - 查询所有课程
     * 获取系统中所有课程的列表（通常用于下拉框选项）。
     */
    List<Clazz> findAll();
}