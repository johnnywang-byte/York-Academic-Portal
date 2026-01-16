package com.yorku.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Service class for interacting with the Google Gemini AI Model.
 * It translates natural language queries into SQL based on a specific academic schema.
 * * 用于与 Google Gemini AI 模型交互的服务类。
 * 它基于特定的学术数据库模式，将自然语言查询转换为 SQL 语句。
 */
@Slf4j
@Service
public class AiService {

    // ⚠️ SECURITY WARNING: Never commit real API Keys to version control (Git).
    // ⚠️ 安全警告：永远不要将真实的 API Key 提交到版本控制系统（Git）中。
    // In production, use environment variables (e.g., System.getenv("GEMINI_API_KEY")).
    // 在生产环境中，请使用环境变量。
    private static final String API_KEY = "Your Gemini API Key";

    // API Endpoint for Gemini 2.5 Flash model (Optimized for speed and cost).
    // Gemini 2.5 Flash 模型的 API 端点（针对速度和成本进行了优化）。
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    // HTTP Client for sending requests.
    // 用于发送请求的 HTTP 客户端。
    private final HttpClient httpClient = HttpClient.newHttpClient();

    // Jackson Object Mapper for JSON processing.
    // 用于 JSON 处理的 Jackson 对象映射器。
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * York University AI Agent Logic
     * Processes user input and returns a JSON string containing SQL or a chat message.
     * * York University AI 代理逻辑
     * 处理用户输入并返回包含 SQL 或聊天消息的 JSON 字符串。
     *
     * @param userQuestion The natural language query from the user (e.g., "Show me all CS students"). / 用户输入的自然语言查询。
     * @return A JSON string representing the AI's response. / 代表 AI 响应的 JSON 字符串。
     */
    public String generateAiResponse(String userQuestion) {

        // =================================================================================
        // STEP 1: Define the Database Schema (Context)
        // 步骤 1：定义数据库模式（上下文）
        // =================================================================================
        // This string describes the table structure to the AI. It is the "knowledge base".
        // 此字符串向 AI 描述表结构。这是它的“知识库”。
        String yorkSchema = """
            
            -- 1. Departments (Faculties / 学院表)
            CREATE TABLE dept (
              id int unsigned PRIMARY KEY,
              name varchar(50) COMMENT 'Faculty Name (e.g., Lassonde School of Engineering)'
            );
            
            -- 2. Faculty & Staff (教职工表)
            CREATE TABLE emp (
              id int unsigned PRIMARY KEY,
              name varchar(20) COMMENT 'Staff Name',
              gender tinyint unsigned COMMENT '1:Male, 2:Female',
              phone char(11),
              job tinyint unsigned COMMENT 'Role: 1:Course Director, 2:Lecturer, 3:Admin',
              salary int unsigned COMMENT 'Annual Salary (CAD)',
              entry_date date COMMENT 'Hire Date',
              dept_id int unsigned COMMENT 'Foreign Key: dept.id'
            );
            
            -- 3. Course Sections (课程班级表)
            CREATE TABLE clazz (
              id int unsigned PRIMARY KEY,
              name varchar(30) COMMENT 'Course Code (e.g., EECS 1012 Section A)',
              room varchar(20) COMMENT 'Lecture Hall (e.g., LAS A)',
              begin_date date COMMENT 'Term Start',
              end_date date COMMENT 'Term End',
              master_id int unsigned COMMENT 'Instructor ID (Foreign Key: emp.id)',
              subject tinyint unsigned COMMENT 'Program: 1:CS, 2:SE, 3:Digital Media, 4:IT, 5:Comp Eng'
            );
            
            -- 4. Student Records (学生记录表 - 核心表)
            CREATE TABLE student (
              id int unsigned PRIMARY KEY,
              name varchar(20) COMMENT 'Student Name',
              no char(10) COMMENT 'York Student Number (starts with 2)',
              gender tinyint unsigned COMMENT '1:Male, 2:Female',
              phone varchar(11) COMMENT 'Contact Number',
              
              -- 🚨 KEY ACADEMIC METRICS (York U Standard / 关键学术指标)
              year_level tinyint unsigned COMMENT 'Academic Year: 1-4, 5:Grad',
              enrollment_status tinyint unsigned COMMENT '1:Full-time, 0:Part-time',
              gpa decimal(3,1) COMMENT 'Cumulative GPA (Scale: 0.0 - 9.0)',
              credits int COMMENT 'Total Credits Earned',
              
              clazz_id int unsigned COMMENT 'Enrolled Course Section (Foreign Key: clazz.id)'
            );
            """;

        // =================================================================================
        // STEP 2: Construct the System Prompt
        // 步骤 2：构造系统提示词
        // =================================================================================
        // We instruct the AI on its role, rules, and expected output format.
        // 我们指示 AI 它的角色、规则以及期望的输出格式。
        String prompt = String.format("""
            ROLE: You are the 'York U Academic Database Agent'.
            MISSION: Convert natural language queries into SQL for the York University database.
            
            STRICT RULES (严格规则):
            1. MEMORY WIPE: Do NOT use old columns like 'violation_count'. They do not exist.
            2. NEW LOGIC: 
               - Use 'gpa' for academic performance (Scale is 9.0).
               - Use 'year_level' for Year 1-4 queries.
            3. SQL SYNTAX: MySQL dialect. Always join tables when necessary.
            
            DATABASE SCHEMA (数据库结构):
            %s
            
            USER QUERY (用户查询): "%s"
            
            RESPONSE FORMAT (JSON ONLY / 仅限 JSON):
            {
              "type": "SELECT" (for queries) or "DML" (for update/insert) or "CHAT" (for greetings/errors),
              "sql": "The SQL statement" (or null if CHAT),
              "message": "A brief summary of what you did"
            }
            """, yorkSchema, userQuestion);

        try {
            // =================================================================================
            // STEP 3: Build and Send Request to Gemini
            // 步骤 3：构建并发送请求给 Gemini
            // =================================================================================

            // Create JSON structure: { "contents": [{ "parts": [{ "text": prompt }] }] }
            // 创建 JSON 结构，符合 Gemini API 的要求。
            String requestBody = objectMapper.createObjectNode()
                    .set("contents", objectMapper.createArrayNode()
                            .add(objectMapper.createObjectNode()
                                    .set("parts", objectMapper.createArrayNode()
                                            .add(objectMapper.createObjectNode()
                                                    .put("text", prompt)))))
                    .toString();

            // Build HTTP Request
            // 构建 HTTP 请求对象
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Send Synchronous Request
            // 发送同步请求
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // =================================================================================
            // STEP 4: Parse the Response
            // 步骤 4：解析响应
            // =================================================================================
            JsonNode rootNode = objectMapper.readTree(response.body());

            // Check for API errors (e.g., Invalid API Key)
            // 检查 API 错误（例如：无效的 API Key）
            if (rootNode.has("error")) {
                String errorMessage = rootNode.path("error").path("message").asText();
                return "{\"type\":\"CHAT\", \"message\":\"[AI ERROR] " + errorMessage + "\"}";
            }

            // Extract the actual text content from the nested JSON response
            // 从嵌套的 JSON 响应中提取实际的文本内容
            String content = rootNode.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // Clean up Markdown code blocks (Gemini often wraps JSON in ```json ... ```)
            // 清理 Markdown 代码块标记（Gemini 经常将 JSON 包裹在 ```json ... ``` 中）
            return content.replace("```json", "").replace("```", "").trim();

        } catch (Exception e) {
            // =================================================================================
            // STEP 5: Error Handling (Fallback)
            // 步骤 5：错误处理（兜底机制）
            // =================================================================================
            log.error("York U AI Service Critical Failure:", e);

            // Return a valid JSON even if the system fails, so the frontend doesn't crash.
            // 即使系统失败也返回有效的 JSON，这样前端就不会崩溃。
            return "{\"type\":\"CHAT\", \"message\":\"[SYSTEM FAILURE] AI Service Unreachable.\"}";
        }
    }
}