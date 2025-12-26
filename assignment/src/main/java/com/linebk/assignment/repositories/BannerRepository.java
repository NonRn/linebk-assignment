package com.linebk.assignment.repositories;

import com.linebk.assignment.models.dto.BannerDto;
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
public class BannerRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<BannerDto> getBannerByUserId(String userId) {
        log.debug("BannerRepository.getBannerByUserId userId={}", userId);

        if (!StringUtils.hasText(userId)) {
            log.warn("getBannerByUserId called with blank userId");
            return Collections.emptyList();
        }

        MapSqlParameterSource params = new MapSqlParameterSource();

        String sql = """
            SELECT banner_id,
                   user_id,
                   title,
                   description,
                   image,
                   link_url
            FROM banners
            WHERE user_id = :userId
        """;

        params.addValue("userId", userId);

        try {
            List<BannerDto> result = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(BannerDto.class));
            log.debug("getBannerByUserId found {} banners for userId={}", result.size(), userId);
            return result;
        } catch (Exception e) {
            log.error("Error retrieving banners for userId={}", userId, e);
            return Collections.emptyList();
        }
    }
}

