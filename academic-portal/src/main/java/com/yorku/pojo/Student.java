package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Student Entity
 * Represents an undergraduate student record in the York U Portal.
 * Refactored to match the new database schema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    // Primary Key
    private Integer id;

    // Full Name
    private String name;

    // Student Number (e.g., 9-digit York ID)
    private String no;

    // Gender: 1=Male, 2=Female
    private Integer gender;

    // Contact Phone Number
    private String phone;

    // Government ID / SIN
    private String idCard;

    // --- 🚨 REFACTORED FIELDS START ---

    /**
     * Enrollment Status
     * Previously: isCollege
     * 1: Full-time, 0: Part-time
     */
    private Integer enrollmentStatus;

    // Residential Address
    private String address;

    /**
     * Year Level
     * Previously: degree
     * 1: 1st Year, 2: 2nd Year, 3: 3rd Year, 4: 4th Year, 5: Grad
     */
    private Integer yearLevel;

    /**
     * Graduation Date
     */
    private LocalDate graduationDate;

    /**
     * Major / Program ID
     * Maps to the 'clazz' table (now representing Majors/Courses)
     */
    private Integer clazzId;

    /**
     * GPA (9.0 Scale)
     * Previously: violationCount
     * Type changed from Short to Double to support decimals (e.g., 7.5)
     */
    private Double gpa;

    /**
     * Credits Earned
     * Previously: violationScore
     * Represents total academic credits
     */
    private Integer credits;

    // --- 🚨 REFACTORED FIELDS END ---

    // Audit Fields
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // DTO Field: Major Name (Not in 'student' table, used for query results)
    private String clazzName;
}