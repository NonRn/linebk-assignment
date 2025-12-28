package com.linebk.assignment.repositories;

import com.linebk.assignment.models.dto.DebitCardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class DebitCardRepository {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<DebitCardDto> getDebitCardByUserId(String userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sql = new StringBuilder();

        sql.append("""
            SELECT dc.card_id, dc.user_id, dc.name,
               dcd.number, dcd.issuer,
               dcs.status,
               dcds.color,dcds.border_color
            FROM debit_cards dc
            LEFT JOIN debit_card_details dcd ON dc.card_id = dcd.card_id
            LEFT JOIN debit_card_status dcs ON dc.card_id = dcs.card_id
            LEFT JOIN debit_card_design dcds ON dc.card_id = dcds.card_id
            WHERE dc.user_id = :userId
            ORDER BY dcs.status, dc.card_id
        """);

        params.addValue("userId", userId);

        return namedParameterJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(DebitCardDto.class));
    }
}
