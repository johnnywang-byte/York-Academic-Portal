package com.yorku.mapper;

import com.yorku.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Employee Work Experience Mapper Interface
 * 员工工作经历 Mapper 接口
 * * Description: Data Access Layer (DAL) for managing employee work history records.
 * 描述：用于管理员工工作经历记录的数据访问层接口。
 */
@Mapper
public interface EmpExprMapper {

    /**
     * Batch Insert Work Experiences
     * 批量插入员工工作经历
     * * Description: Efficiently inserts multiple work experience records in a single operation.
     * 描述：在单次操作中高效插入多条工作经历记录。
     * * @param exprList - The list of work experience objects to insert. (要插入的工作经历对象列表)
     */
    void insertBatch(List<EmpExpr> exprList);

    /**
     * Batch Delete by Employee IDs
     * 根据员工 ID 批量删除
     * * Description: Removes all work experience records associated with the provided list of employee IDs.
     * 描述：删除与提供的员工 ID 列表相关联的所有工作经历记录。
     * * Note: Usually called when deleting employees to maintain data integrity (Cascade Delete).
     * 注意：通常在删除员工时调用，以维护数据一致性（级联删除）。
     * * @param empIds - List of employee IDs whose work history should be deleted. (需要删除工作经历的员工 ID 列表)
     */
    void deleteByEmpIds(List<Integer> empIds);
}