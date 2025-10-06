package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.util.Phone2ColorBatchPreparedStatementSetter;
import com.es.core.model.phone.util.PhoneListResultSetExtractor;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private static final String UPDATE_SQL = "UPDATE phones SET brand = :brand, " +
            "model = :model, price = :price, displaySizeInches = :displaySizeInches, weightGr = :weightGr, " +
            "lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm,announced = :announced, " +
            "deviceType = :deviceType, os = :os, displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, " +
            "bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, description = :description " +
            "WHERE id = :id";
    private static final String INSERT_SQL = "INSERT INTO phones (brand, model, price, displaySizeInches, weightGr, " +
            "lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, " +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "VALUES (:brand, :model, :price, :displaySizeInches, :weightGr, " +
            ":lengthMm, :widthMm, :heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, " +
            ":displayTechnology, :backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, " +
            ":batteryCapacityMah, :talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String GET_SQL = "select phones.*, c.* from phones " +
            "left join phone2color pc on phones.id = pc.phoneId " +
            "left join colors c on pc.colorId = c.id " +
            "where phones.id = ?";
    private static final String DELETE_FROM_PHONE_2_COLOR_WHERE_COLOR_ID_AND_PHONE_ID =
            "delete from phone2color where colorId = ? and phoneId = ?";
    private static final String INSERT_INTO_PHONE_2_COLOR_PHONE_ID_COLOR_ID_VALUES =
            "insert into phone2color (phoneId, colorId) values (?, ?)";
    private static final String FROM_PHONES_OFFSET_LIMIT = "select phones.*, c.* from phones " +
            "left join phone2color pc on phones.id = pc.phoneId " +
            "left join colors c on pc.colorId = c.id " +
            "offset ? limit ?";
    private static final String SELECT_COUNT_FROM_PHONES_WHERE_ID = "select COUNT(*) from phones where id = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhoneListResultSetExtractor phoneListResultSetExtractor;
    @Resource
    private ColorDao jdbcColorDao;

    @Override
    public Optional<Phone> get(final Long key) {
        if (key == null) {
            return Optional.empty();
        }

        var result = jdbcTemplate.query(GET_SQL, ps -> ps.setLong(1, key),
                phoneListResultSetExtractor);

        return result.stream().filter( phone -> key.equals(phone.getId())).findFirst();
    }

    @Override
    public void save(final Phone phone) {
        Long countOfPhones = jdbcTemplate.queryForObject(SELECT_COUNT_FROM_PHONES_WHERE_ID,
                new SingleColumnRowMapper<>(Long.class), phone.getId());
        if (countOfPhones > 0) {
            update(phone);
        } else {
            insert(phone);
        }
    }

    private void update(Phone phone) {
        namedParameterJdbcTemplate.update(UPDATE_SQL, new BeanPropertySqlParameterSource(phone));

        Set<Color> dbPhoneColors = jdbcColorDao.findAllPhoneColors(phone.getId());
        jdbcTemplate.batchUpdate(DELETE_FROM_PHONE_2_COLOR_WHERE_COLOR_ID_AND_PHONE_ID,
                new Phone2ColorBatchPreparedStatementSetter(dbPhoneColors.stream().toList(), phone));

        Set<Color> phoneColors = jdbcColorDao.saveAll(phone.getColors().stream().toList());
        phone.setColors(phoneColors);
        insertToPhone2Color(phone, phoneColors.stream().toList());
    }

    private void insert(Phone phone) {
        namedParameterJdbcTemplate.update(INSERT_SQL, new BeanPropertySqlParameterSource(phone));

        List<Color> colors = phone.getColors().stream().toList();

        insertToPhone2Color(phone, colors);
    }

    private void insertToPhone2Color(Phone phone, List<Color> colors) {
        jdbcTemplate.batchUpdate(INSERT_INTO_PHONE_2_COLOR_PHONE_ID_COLOR_ID_VALUES,
                new Phone2ColorBatchPreparedStatementSetter(colors, phone));
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(FROM_PHONES_OFFSET_LIMIT, phoneListResultSetExtractor, offset, limit);
    }
}
