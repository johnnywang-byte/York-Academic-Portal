package com.yorku.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Faculty & Staff Entity
 * York University Academic Admin Portal
 *
 * Description:
 * Represents a university employee record.
 * Includes Faculty members (Professors, Lecturers) and Admin Staff.
 *
 * @Description: 教职工实体类
 */
@Data
public class Emp {

    /**
     * Primary Key
     * 主键 ID
     */
    private Integer id;

    /**
     * Passport York ID (Username)
     * 约克大学通行证 ID
     */
    private String username;

    /**
     * Access Password
     * 密码
     */
    private String password;

    /**
     * Full Legal Name
     * 真实姓名
     */
    private String name;

    /**
     * Gender
     * 1: Male, 2: Female
     * 性别
     */
    private Integer gender;

    /**
     * Contact Number (North American Format)
     * 手机号
     */
    private String phone;

    /**
     * Job Role / Title (Mapped to DB ID)
     * 1: Course Director (课程主管)
     * 2: Lecturer (讲师)
     * 3: Student Success Officer (学务专员)
     * 4: Research Lead (教研主管)
     * 5: Academic Advisor (学术顾问)
     * 6: Administrative Staff (行政人员)
     */
    private Integer job;

    /**
     * Annual Salary (CAD)
     * 薪资
     */
    private Integer salary;

    /**
     * Profile Photo URL (Hosted on OSS)
     * 头像
     */
    private String image;

    /**
     * Appointment Date (Hire Date)
     * 任命/入职日期
     */
    private LocalDate entryDate;

    /**
     * Academic Unit ID (Foreign Key -> Dept)
     * 关联的部门/院系 ID
     */
    private Integer deptId;

    /**
     * Audit: Create Time
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * Audit: Last Update Time
     * 修改时间
     */
    private LocalDateTime updateTime;

    // ----------- Auxiliary Fields (Not in DB Table) -----------

    /**
     * Faculty/Department Name (For Display)
     * 部门/院系名称
     */
    private String deptName;

    /**
     * Professional History
     * 工作经历列表
     */
    private List<EmpExpr> exprList;
}