package com.yorku.service;

import com.yorku.pojo.Clazz;
import com.yorku.pojo.ClazzQueryParam;
import com.yorku.pojo.PageResult;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Description: 班级管理的service
 */
public interface ClazzService {

    /**
     * 班级管理--分页查询
     * @param clazzQueryParam
     * @return
     */
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    /**
     * 班级管理--新增班级
     */
    void save(Clazz clazz);


    /**
     * 班级管理--根据id删除班级
     */
    void delete(Integer id);

    /**
     * 班级管理--根据id查询班级
     */
    Clazz findById(Integer id);

    /**
     * 班级管理--根据id修改班级
     */
    void update(Clazz clazz);

    /**
     * 班级管理--查询所有班级
     */
    List<Clazz> findAll();
}
