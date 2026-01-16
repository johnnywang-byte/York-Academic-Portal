package com.yorku.mapper;

import com.yorku.pojo.Emp;
import com.yorku.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.MapKey; // 确保导入了这个包
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Staff Mapper Interface
 * York University Academic Portal
 * 员工管理持久层接口
 */
@Mapper
public interface EmpMapper {

    /**
     * Conditional Query (Staff List)
     * 条件查询员工信息
     */
    List<Emp> list(EmpQueryParam empQueryParam);

    /**
     * Add New Staff
     * 新增员工基本信息
     */
    void insert(Emp emp);

    /**
     * Batch Delete Staff by IDs
     * 批量根据ID删除员工
     */
    void deleteByIds(List<Integer> ids);

    /**
     * Get Staff by ID
     * 根据ID查询员工信息
     */
    Emp getById(Integer id);

    /**
     * Update Staff Information
     * 修改员工基本信息
     */
    void updateById(Emp emp);

    /**
     * Staff Role Statistics (for Charts)
     * 统计员工职位人数
     * @return List containing Map with keys: 'pos' (Position Name), 'cnt' (Count)
     */
    // 🚨 修复: 加回 @MapKey，并指定 SQL 中的别名 "pos"
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();

    /**
     * Staff Gender Statistics (for Charts)
     * 统计员工性别人数
     * @return List containing Map with keys: 'name' (Gender), 'value' (Count)
     */
    // 🚨 修复: 加回 @MapKey，并指定 SQL 中的别名 "name"
    @MapKey("name")
    List<Map<String, Object>> countEmpGenderData();

    /**
     * Find All Staff
     * 查询所有员工信息
     */
    List<Emp> findAll();

    /**
     * Staff Login
     * 员工登录
     */
    Emp selectByUsernameAndPwd(Emp emp);
}