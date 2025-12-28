package com.linebk.assignment.repositories;

import com.linebk.assignment.models.dto.AccountDto;
import com.linebk.assignment.models.dto.AccountFlagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Repository
public class AccountRepository {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<AccountDto> getAccountByUserId(String userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            SELECT ac.account_id,
               ac.user_id,
               ac.type,
               ac.currency,
               ac.account_number,
               ac.issuer,
               acb.amount,
               acd.color,
               acd.is_main_account,
               acd.progress,
               acd.nickname
            FROM accounts ac
            INNER JOIN account_balances acb ON ac.account_id = acb.account_id
            LEFT JOIN account_details acd ON ac.account_id = acd.account_id
            WHERE ac.user_id = :userId
        """;

        params.addValue("userId", userId);

        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(AccountDto.class));
    }

    public List<AccountFlagDto> getAccountFlagByAccountIdList(List<String> accountIdList) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            SELECT account_id,
               flag_type,
               flag_value
            FROM account_flags
            WHERE account_id IN (:accountIdList)
        """;

        params.addValue("accountIdList", accountIdList);

        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(AccountFlagDto.class));
    }

    public BigDecimal getAccountBalance(String accountId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            SELECT amount
            FROM account_balances
            WHERE account_id = :accountId
        """;

        params.addValue("accountId", accountId);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, BigDecimal.class);
        } catch (Exception e) {
            log.error("Error retrieving account balance for accountId={}", accountId, e);
            return null;
        }
    }

    public void updateAccountBalance(String accountId, BigDecimal newAmount) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            UPDATE account_balances
            SET amount = :newAmount
            WHERE account_id = :accountId
        """;

        params.addValue("accountId", accountId);
        params.addValue("newAmount", newAmount);

        namedParameterJdbcTemplate.update(sql, params);
        log.info("Updated account balance for accountId={}, newAmount={}", accountId, newAmount);
    }

    public void setupMainAccount(String userId, String accountId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Set is_main_account = true for the specified account
        String sqlSetMain = """
            UPDATE account_details
            SET is_main_account = true
            WHERE account_id = :accountId AND user_id = :userId
        """;

        params.addValue("accountId", accountId);
        params.addValue("userId", userId);
        namedParameterJdbcTemplate.update(sqlSetMain, params);

        // Set is_main_account = false for all other accounts of the same user
        String sqlUnsetOthers = """
            UPDATE account_details
            SET is_main_account = false
            WHERE user_id = :userId AND account_id != :accountId
        """;

        namedParameterJdbcTemplate.update(sqlUnsetOthers, params);
        log.info("Setup main account for userId={}, accountId={}", userId, accountId);
    }

    public void updateAccountDetail(String accountId, String nickname, String color) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            UPDATE account_details
            SET nickname = :nickname, color = :color
            WHERE account_id = :accountId
        """;

        params.addValue("accountId", accountId);
        params.addValue("nickname", nickname);
        params.addValue("color", color);

        namedParameterJdbcTemplate.update(sql, params);
        log.info("Updated account detail for accountId={}, nickname={}, color={}", accountId, nickname, color);
    }

    public boolean isAccountExists(String accountId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            SELECT COUNT(*) FROM accounts WHERE account_id = :accountId
        """;

        params.addValue("accountId", accountId);

        try {
            Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("Error checking if account exists for accountId={}", accountId, e);
            return false;
        }
    }
}
