package com.yorku.service;

import com.yorku.pojo.Emp;
import com.yorku.pojo.EmpQueryParam;
import com.yorku.pojo.LoginInfo;
import com.yorku.pojo.PageResult;

import java.util.List;

/**
 * @Description: 员工管理的业务层接口
 */

public interface EmpService {
    /**
     * 员工管理-分页查询
     */
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    /**
     * 员工管理--新增员工
     */
    void save(Emp emp);

    /**
     * 员工管理--批量删除员工
     */
    void delete(List<Integer> ids);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Emp getInfo(Integer id);

    /**
     * 员工管理--修改员工
     */
    void update(Emp emp);

    /**
     * 员工管理--查询所有员工
     */
    List<Emp> findAll();

    /**
     * 员工登录的基础功能
     */
    LoginInfo login(Emp emp);
}
