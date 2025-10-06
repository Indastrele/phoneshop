package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.util.ColorBatchPreparedStatementSetter;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcColorDao implements ColorDao {

    private static final String FROM_COLORS_WHERE_ID = "select * from colors where id = ?";
    private static final String UPDATE_COLORS_SET_CODE_WHERE_ID = "update colors set code = ? where id = ?";
    private static final String INSERT_INTO_COLORS_CODE_VALUES = "insert into colors (code) values (?)";
    private static final String GET_SQL_WITH_OFFSET_AND_LIMIT = "select * from colors offset ? limit ?";
    private static final String SELECT_FROM_COLORS_FOR_THE_PHONE = "select * from colors " +
            "join phone2color pc on colors.id = pc.colorId " +
            "where pc.phoneId = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Color> get(final Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(jdbcTemplate.queryForObject(FROM_COLORS_WHERE_ID,
                new BeanPropertyRowMapper<>(Color.class), id));
    }

    @Override
    public List<Color> findAll(int offset, int limit) {
        return jdbcTemplate.query(GET_SQL_WITH_OFFSET_AND_LIMIT, new BeanPropertyRowMapper<>(Color.class), offset,
                limit);
    }

    private Set<Color> findAllByCode(List<String> codes) {
        return new HashSet<>(jdbcTemplate.query("select * from colors where code in (?)",
                new BeanPropertyRowMapper<>(Color.class), codes));
    }

    @Override
    public Set<Color> findAllPhoneColors(final Long phoneId) {
        return new HashSet<>(jdbcTemplate.query(SELECT_FROM_COLORS_FOR_THE_PHONE,
                new BeanPropertyRowMapper<>(Color.class),phoneId));
    }

    @Override
    public void save(final Color color) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(UPDATE_COLORS_SET_CODE_WHERE_ID, color.getCode(), color.getId()) ==
                Statement.EXECUTE_FAILED) {
            jdbcTemplate.update(INSERT_INTO_COLORS_CODE_VALUES, keyHolder, color.getCode());
            color.setId(keyHolder.getKeyAs(Long.class));
        }
    }

    @Override
    public Set<Color> saveAll(List<Color> colors) {
        int[] updateResults = jdbcTemplate.batchUpdate(UPDATE_COLORS_SET_CODE_WHERE_ID,
                new ColorBatchPreparedStatementSetter(colors));

        if (Arrays.stream(updateResults).filter(res -> res == Statement.EXECUTE_FAILED).count() > 0) {
            jdbcTemplate.batchUpdate(INSERT_INTO_COLORS_CODE_VALUES, new ColorBatchPreparedStatementSetter(colors));
        }

        return findAllByCode(colors.stream().map(Color::getCode).toList());
    }
}
