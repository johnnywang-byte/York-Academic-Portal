package com.yorku.mapper;

import com.yorku.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Department Mapper Interface
 * 部门 Mapper 接口
 * * Description: Handles database operations (CRUD) for the Department entity.
 * 描述：处理部门实体的数据库增删改查操作。
 */
@Mapper
public interface DeptMapper {

    /**
     * Retrieve all departments
     * 查询所有部门数据
     * * @return List<Dept> - A list of all departments ordered by ID. (按 ID 排序的所有部门列表)
     */
    @Select("select id, name, create_time , update_time  from dept order by id ")
    List<Dept> findAll();

    /**
     * Delete a department by ID
     * 根据 ID 删除部门信息
     * * @param id - The unique ID of the department to delete. (要删除的部门 ID)
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    /**
     * Insert a new department
     * 添加部门信息
     * * @param dept - The department object containing name and timestamps. (包含名称和时间戳的部门对象)
     */
    @Insert("insert into dept(name, create_time , update_time ) values(#{name}, #{createTime}, #{updateTime})")
    void add(Dept dept);

    /**
     * Get department details by ID
     * 根据 ID 查询部门信息
     * * @param id - The ID of the department to retrieve. (要查询的部门 ID)
     * @return Dept - The department object if found. (查询到的部门对象)
     */
    @Select("select id, name, create_time  , update_time  from dept where id = #{id}")
    Dept getInfo(Integer id);

    /**
     * Update department information
     * 更新部门信息
     * * @param dept - The department object with updated details (name, update_time). (包含更新详情的部门对象)
     */
    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

    /**
     * Count employees in a specific department
     * 统计指定部门下的员工数量
     * * Description: Often used to check for dependencies before deletion.
     * 描述：通常用于删除前的依赖检查（防止删除有员工的部门）。
     * * @param deptId - The department ID to check. (要检查的部门 ID)
     * @return Integer - The count of employees in this department. (该部门下的员工总数)
     */
    @Select("SELECT COUNT(*) FROM emp WHERE dept_id = #{deptId}")
    Integer countEmployeesByDeptId(Integer deptId);
}