package com.linebk.assignment.repositories;

import com.linebk.assignment.models.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final int MAX_LIMIT = 100;
    private static final int MIN_LIMIT = 1;

    public List<TransactionDto> getTransactionByUserId(String userId) {
        log.debug("TransactionRepository.getTransactionByUserId userId={}", userId);

        if (!StringUtils.hasText(userId)) {
            log.warn("getTransactionByUserId called with blank userId");
            return Collections.emptyList();
        }

        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            SELECT transaction_id,
                   user_id,
                   name,
                   image,
                   is_bank
            FROM transactions
            WHERE user_id = :userId
        """;

        params.addValue("userId", userId);

        try {
            List<TransactionDto> result = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(TransactionDto.class));
            log.debug("getTransactionByUserId found {} transactions for userId={}", result.size(), userId);
            return result;
        } catch (Exception e) {
            log.error("Error retrieving transactions for userId={}", userId, e);
            return Collections.emptyList();
        }
    }

    public List<TransactionDto> getTransactionByUserIdWithPagination(String userId, int limit, int offset) {
        log.debug("TransactionRepository.getTransactionByUserIdWithPagination userId={}, limit={}, offset={}", userId, limit, offset);

        if (!StringUtils.hasText(userId)) {
            log.warn("getTransactionByUserIdWithPagination called with blank userId");
            return Collections.emptyList();
        }

        if (offset < 0) {
            log.warn("getTransactionByUserIdWithPagination called with negative offset={}", offset);
            offset = 0;
        }

        // Validate and constrain limit to prevent excessive queries
        if (limit < MIN_LIMIT) {
            limit = MIN_LIMIT;
        } else if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
            log.warn("Limit exceeded maximum allowed value, constraining to {}", MAX_LIMIT);
        }

        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            SELECT transaction_id,
                   user_id,
                   name,
                   image,
                   is_bank
            FROM transactions
            WHERE user_id = :userId
            ORDER BY transaction_id
            LIMIT :limit OFFSET :offset
        """;

        params.addValue("userId", userId);
        params.addValue("limit", limit);
        params.addValue("offset", offset);

        try {
            List<TransactionDto> result = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(TransactionDto.class));
            log.debug("getTransactionByUserIdWithPagination found {} transactions for userId={}", result.size(), userId);
            return result;
        } catch (Exception e) {
            log.error("Error retrieving transactions for userId={}, limit={}, offset={}", userId, limit, offset, e);
            return Collections.emptyList();
        }
    }
}

