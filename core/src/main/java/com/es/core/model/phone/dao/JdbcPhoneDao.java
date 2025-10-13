package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.exception.InvalidDaoParamException;
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
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, " +
            "standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, " +
            "imageUrl = :imageUrl, description = :description " +
            "WHERE id = :id";
    private static final String INSERT_NEW_PHONE_WITHOUT_ID_SQL = "INSERT INTO phones (brand, model, price, " +
            "displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "VALUES (:brand, :model, :price, :displaySizeInches, :weightGr, " +
            ":lengthMm, :widthMm, :heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, " +
            ":displayTechnology, :backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, " +
            ":batteryCapacityMah, :talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String GET_PHONE_WITH_COLORS_BY_ID_SQL = "select " +
            "p.id, p.brand, p.model, p.price, " +
            "p.displaySizeInches, p.weightGr, p.lengthMm, p.widthMm, p.heightMm, p.announced, p.deviceType, p.os, " +
            "p.displayResolution, p.pixelDensity, p.displayTechnology, p.backCameraMegapixels, p.frontCameraMegapixels, " +
            "p.ramGb, p.internalStorageGb, p.batteryCapacityMah, p.talkTimeHours, p.standByTimeHours, p.bluetooth, " +
            "p.positioning, p.imageUrl, p.description, c.* " +
            "from phones p " +
            "left join phone2color pc on p.id = pc.phoneId " +
            "left join colors c on pc.colorId = c.id " +
            "where p.id = ?";
    private static final String DELETE_FROM_PHONE_2_COLOR_WHERE_PHONE_ID =
            "delete from phone2color where phoneId = ?";
    private static final String INSERT_INTO_PHONE_2_COLOR_PHONE_ID_COLOR_ID_VALUES =
            "insert into phone2color (phoneId, colorId) values (?, ?)";
    private static final String SELECT_COUNT_FROM_PHONES_WHERE_ID = "select distinct COUNT(*) from phones where id = ?";
    private static final String SELECT_FROM_PHONES_WITH_SUBQUERY_BEGINNING = "select " +
            "p.id, p.brand, p.model, p.price, " +
            "p.displaySizeInches, p.weightGr, p.lengthMm, p.widthMm, p.heightMm, p.announced, p.deviceType, p.os, " +
            "p.displayResolution, p.pixelDensity, p.displayTechnology, p.backCameraMegapixels, p.frontCameraMegapixels, " +
            "p.ramGb, p.internalStorageGb, p.batteryCapacityMah, p.talkTimeHours, p.standByTimeHours, p.bluetooth, " +
            "p.positioning, p.imageUrl, p.description, c.* " +
            "from (" +
            "select * from phones " +
            "left join stocks s on phones.id = s.phoneId " +
            "where s.stock - s.reserved > 0 and price is not null ";
    private static final String WHERE_PHONES_MODEL_ILIKE_CONCAT = "and phones.model ilike CONCAT('%%', ?, '%%') " +
            "or phones.brand ilike CONCAT('%%', ?, '%%') ";
    private static final String ALIAS_TO_SUBQUERY_AND_LEFT_JOIN_TO_COLORS_WITH_TWO_ORDER_BY = "order by %s %s " +
            "offset ? limit ?) p " +
            "left join phone2color pc on p.id = pc.phoneId " +
            "left join colors c on pc.colorId = c.id " +
            "order by %s %s ";
    private static final String SELECT_COUNT_FROMS_PHONES_IN_STOCK = "select COUNT(distinct phones.id) from phones " +
            "join stocks s on phones.id = s.phoneId " +
            "where s.stock - s.reserved > 0 and phones.price is not null";
    private static final String SELECT_FROM_PHONES_IN_STOCK_ILIKE_QUERY = "select COUNT(distinct phones.id) from phones " +
            "join stocks s on phones.id = s.phoneId " +
            "where s.stock - s.reserved > 0 and phones.price is not null and phones.model ilike CONCAT('%', ?, '%') " +
            "or phones.brand ilike CONCAT('%', ?, '%') ";

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
        if (order == null || field == null) {
            throw new InvalidDaoParamException();
        }

        StringBuilder sql = new StringBuilder(SELECT_FROM_PHONES_WITH_SUBQUERY_BEGINNING);

        if (query != null && !query.isEmpty()) {
            sql.append(WHERE_PHONES_MODEL_ILIKE_CONCAT);
            sql.append(ALIAS_TO_SUBQUERY_AND_LEFT_JOIN_TO_COLORS_WITH_TWO_ORDER_BY);
            return jdbcTemplate.query(String.format(sql.toString(), field.getStringValue(), order.getStringValue(),
                            field.getStringValue(), order.getStringValue()), phoneListResultSetExtractor, query, query,
                    offset, limit);
        }

        sql.append(ALIAS_TO_SUBQUERY_AND_LEFT_JOIN_TO_COLORS_WITH_TWO_ORDER_BY);

        return jdbcTemplate.query(String.format(sql.toString(), field.getStringValue(), order.getStringValue(),
                        field.getStringValue(), order.getStringValue()), phoneListResultSetExtractor, offset, limit);
    }

    @Override
    public Long getAmountOfPhones(String query) {
        if (query != null && !query.isEmpty()) {
            return jdbcTemplate.queryForObject(SELECT_FROM_PHONES_IN_STOCK_ILIKE_QUERY,
                    new SingleColumnRowMapper<>(Long.class), query, query);
        }

        return jdbcTemplate.queryForObject(SELECT_COUNT_FROMS_PHONES_IN_STOCK, new SingleColumnRowMapper<>(Long.class));
    }
}
