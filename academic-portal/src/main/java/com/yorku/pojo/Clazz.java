package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Course Section Entity
 * York University Academic Admin Portal
 *
 * Description:
 * Represents a specific offering of a course (e.g., EECS 1012 Section A)
 * scheduled for a specific academic term.
 *
 * @Description: 课程单元/班级实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clazz {

    /**
     * Unique Identifier
     * 主键 ID
     */
    private Integer id;

    /**
     * Course Code & Section
     * Example: "EECS 1012 Section A"
     * 课程名称/班级名
     */
    private String name;

    /**
     * Lecture Hall / Location
     * Example: "Lassonde Bldg A"
     * 教室/上课地点
     */
    private String room;

    /**
     * Term Start Date
     * 开课日期
     */
    private LocalDate beginDate;

    /**
     * Term End Date
     * 结课日期
     */
    private LocalDate endDate;

    /**
     * Instructor ID (Foreign Key -> Emp)
     * Reference to the Course Director or Lecturer.
     * 授课讲师 ID
     */
    private Integer masterId;

    /**
     * Academic Program / Subject
     * 1: Computer Science, 2: Software Eng, etc.
     * 所属学科/专业
     */
    private Integer subject;

    /**
     * Audit: Record Creation Timestamp
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * Audit: Last Modification Timestamp
     * 最后修改时间
     */
    private LocalDateTime updateTime;

    // ----------- Auxiliary Fields (Not in DB Table) -----------

    /**
     * Instructor Name
     * 讲师姓名
     */
    private String masterName;

    /**
     * Section Status
     * Logic: Upcoming / Active / Completed
     * 课程状态 (未开课/进行中/已结课)
     */
    private String status;
}