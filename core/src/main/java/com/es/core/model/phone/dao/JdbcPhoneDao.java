package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.util.Phone2ColorBatchPreparedStatementSetter;
import com.es.core.model.phone.util.PhoneListResultSetExtractor;
import com.es.core.model.phone.util.SortField;
import com.es.core.model.phone.util.SortOrder;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private static final String UPDATE_PHONES_BY_ID_SQL = "UPDATE phones SET brand = :brand, " +
            "model = :model, price = :price, displaySizeInches = :displaySizeInches, weightGr = :weightGr, " +
            "lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm,announced = :announced, " +
            "deviceType = :deviceType, os = :os, displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, " +
            "bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, description = :description " +
            "WHERE id = :id";
    private static final String INSERT_NEW_PHONE_WITHOUT_ID_SQL = "INSERT INTO phones (brand, model, price, " +
            "displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "VALUES (:brand, :model, :price, :displaySizeInches, :weightGr, " +
            ":lengthMm, :widthMm, :heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, " +
            ":displayTechnology, :backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, " +
            ":batteryCapacityMah, :talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String GET_PHONE_WITH_COLORS_BY_ID_SQL = "select phones.*, c.* from phones " +
            "left join phone2color pc on phones.id = pc.phoneId " +
            "left join colors c on pc.colorId = c.id " +
            "where phones.id = ?";
    private static final String DELETE_FROM_PHONE_2_COLOR_WHERE_PHONE_ID =
            "delete from phone2color where phoneId = ?";
    private static final String INSERT_INTO_PHONE_2_COLOR_PHONE_ID_COLOR_ID_VALUES =
            "insert into phone2color (phoneId, colorId) values (?, ?)";
    private static final String SELECT_COUNT_FROM_PHONES_WHERE_ID = "select COUNT(*) from phones where id = ?";
    private static final String FROM_PHONES_WITH_COLORS = "select phones.*, c.* from phones " +
            "left join phone2color pc on phones.id = pc.phoneId " +
            "left join colors c on pc.colorId = c.id ";
    private static final String STOCK_WHERE_PHONE_ID = "left join stocks s on phones.id = s.phoneId " +
            "where phones.model ILIKE CONCAT('%', ?, '%') and s.stock > 0 ";
    private static final String ORDER_BY = "order by ";
    private static final String OFFSET_LIMIT = "offset ? limit ? ";

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
            throw new InvalidIdException(Phone.class, key);
        }

        return jdbcTemplate.query(GET_PHONE_WITH_COLORS_BY_ID_SQL, phoneListResultSetExtractor, key).stream().findFirst();
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
        namedParameterJdbcTemplate.update(UPDATE_PHONES_BY_ID_SQL, new BeanPropertySqlParameterSource(phone));

        jdbcTemplate.update(DELETE_FROM_PHONE_2_COLOR_WHERE_PHONE_ID, phone.getId());

        jdbcColorDao.saveAll(phone.getColors().stream().toList());

        List<Color> phoneColors = jdbcColorDao.findAllByCode(phone.getColors().stream().map(Color::getCode).toList());
        phone.setColors(new HashSet<>(phoneColors));
        insertToPhone2Color(phone, phoneColors.stream().toList());
    }

    private void insert(Phone phone) {
        namedParameterJdbcTemplate.update(INSERT_NEW_PHONE_WITHOUT_ID_SQL, new BeanPropertySqlParameterSource(phone));

        List<Color> colors = phone.getColors().stream().toList();

        insertToPhone2Color(phone, colors);
    }

    private void insertToPhone2Color(Phone phone, List<Color> colors) {
        jdbcTemplate.batchUpdate(INSERT_INTO_PHONE_2_COLOR_PHONE_ID_COLOR_ID_VALUES,
                new Phone2ColorBatchPreparedStatementSetter(colors, phone));
    }

    @Override
    public List<Phone> findAll(int offset, int limit, String query, SortOrder order, SortField field) {
        StringBuilder sql = new StringBuilder(FROM_PHONES_WITH_COLORS);


        if (query != null && !query.isEmpty()) {
            sql.append(STOCK_WHERE_PHONE_ID);

            if (order == null || field == null) {
                sql.append(OFFSET_LIMIT);
                return jdbcTemplate.query(sql.toString(), phoneListResultSetExtractor, query, offset, limit);
            }
        }

        if (order != null && field != null) {
            sql.append(String.format("%s%s %s ", ORDER_BY, field.getStringValue(), order.getStringValue()));
        }

        sql.append(OFFSET_LIMIT);

        return jdbcTemplate.query(sql.toString(), phoneListResultSetExtractor, offset, limit);
    }
}
