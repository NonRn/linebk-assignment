package com.linebk.assignment.repositories;

import com.linebk.assignment.models.dto.AccountDto;
import com.linebk.assignment.models.dto.AccountFlagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
