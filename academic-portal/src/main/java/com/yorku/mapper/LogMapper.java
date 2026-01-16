package com.yorku.mapper;

import com.yorku.pojo.LogList;
import com.yorku.pojo.LogQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * 
 *  : 2025/04/10/22:52
 * @Description: 日志分页查询
 */
@Mapper
public interface LogMapper {


    List<LogList> list(LogQueryParam logQueryParam);
}
