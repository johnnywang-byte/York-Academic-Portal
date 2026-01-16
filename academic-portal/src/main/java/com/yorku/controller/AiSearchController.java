package com.yorku.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yorku.pojo.Result;
import com.yorku.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * York University AI Command Interface
 * Handles natural language queries for Student Records & Faculty Data.
 * 智能助手控制层 - 处理自然语言查询与数据库交互
 */
@Slf4j
@CrossOrigin
@RestController
public class AiSearchController {

    @Autowired
    private AiService aiService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * AI Search Endpoint
     * Endpoint: /ai/search
     */
    @GetMapping("/ai/search")
    public Result search(@RequestParam String question) {
        // Log the incoming request (Audit Trail)
        log.info("📢 [York U AI] Incoming Inquiry: {}", question);

        try {
            // 1. Call AI Agent (The York U Brain)
            // 调用 AI 服务，获取结构化的 JSON 响应
            String jsonResponse = aiService.generateAiResponse(question);
            log.info("🤖 [AI Reasoning]: {}", jsonResponse);

            // 2. Parse JSON Response
            // 解析 AI 返回的意图 (SELECT / DML / CHAT)
            JsonNode aiResult = objectMapper.readTree(jsonResponse);
            String type = aiResult.path("type").asText();
            String sql = aiResult.path("sql").asText(null);
            String message = aiResult.path("message").asText();

            // Prepare response object
            Map<String, Object> finalResult = new HashMap<>();
            finalResult.put("type", type);
            finalResult.put("aiMessage", message);

            // 3. Routing Logic (Execute Intent on Database)
            if ("SELECT".equals(type) && sql != null && !sql.isEmpty()) {
                // READ Operation: Execute Query
                log.info("🔍 [DB Query]: {}", sql);
                List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
                finalResult.put("data", data);

            } else if ("DML".equals(type) && sql != null && !sql.isEmpty()) {
                // WRITE Operation: Update/Insert/Delete
                log.warn("⚠️ [DB Update]: {}", sql); // Warn log for write operations

                int rowsAffected = jdbcTemplate.update(sql);
                finalResult.put("data", "Operation Confirmed. Database Records Affected: " + rowsAffected);

            } else {
                // CHAT mode (General Q&A or Error handling)
                finalResult.put("data", Collections.emptyList());
            }

            return Result.success(finalResult);

        } catch (Exception e) {
            log.error("❌ [System Failure]: ", e);
            return Result.error("York U AI System Malfunction: " + e.getMessage());
        }
    }
}