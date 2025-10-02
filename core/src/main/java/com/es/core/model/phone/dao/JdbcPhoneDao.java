package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.util.EntityClassConvert;
import com.es.core.model.phone.util.PhoneDaoPreparedStatementSetterHelper;
import com.es.core.model.phone.util.PhoneResultSetExtractor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
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
    private static final String GET_COLORS_SQL = "select * from colors " +
            "join phone2color on colors.id = phone2color.colorId " +
            "where phone2color.phoneId = ?";
    private static final String GET_SQL = "select phones.*, c.* from phones " +
            "left join phone2color pc on phones.id = pc.phoneId " +
            "left join colors c on pc.colorId = c.id " +
            "where phones.id = ?";
    private static final String DELETE_FROM_PHONE_2_COLOR_WHERE_COLOR_ID_AND_PHONE_ID =
            "delete from phone2color where colorId = ? and phoneId = ?";
    private static final String INSERT_INTO_PHONE_2_COLOR_PHONE_ID_COLOR_ID_VALUES =
            "insert into phone2color (phoneId, colorId) values (?, ?)";
    private static final String FROM_PHONES_OFFSET_LIMIT = "select * from phones offset ? limit ?";
    private static final String SELECT_FROM_COLORS = "select * from colors";
    private static final String INSERT_INTO_COLORS_ID_CODE_VALUES = "insert into colors (id, code) values(?, ?)";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PhoneResultSetExtractor phoneResultSetExtractor;

    // TODO: simplify queries
    public Optional<Phone> get(final Long key) {
        if (key == null) {
            throw new InvalidIdException(Phone.class, key);
        }

        return Optional.ofNullable(jdbcTemplate.query(GET_SQL, ps -> ps.setLong(1, key),
                phoneResultSetExtractor));
    }

    public void save(final Phone phone) {
        if (get(phone.getId()).isPresent()) {
            update(phone);
        } else {
            insert(phone);
        }
    }

    private void update(Phone phone) {
        namedParameterJdbcTemplate.update(UPDATE_SQL, EntityClassConvert.convertEntityToMap(phone));

        List<Color> phoneColors = phone.getColors().stream().toList();
        Set<Color> dbPhoneColors = getColors(phone.getId());

        if (dbPhoneColors.size() > 0) {
            jdbcTemplate.batchUpdate(DELETE_FROM_PHONE_2_COLOR_WHERE_COLOR_ID_AND_PHONE_ID,
                    PhoneDaoPreparedStatementSetterHelper.createPhone2CaseBatchPreparedStatementSetter(phone,
                            dbPhoneColors.stream().toList()));
        }

        List<Color> newColors = new ArrayList<>();
        List<Color> allColorsInDB = jdbcTemplate.query(SELECT_FROM_COLORS, new BeanPropertyRowMapper<>(Color.class));
        phoneColors.stream().filter(color -> !allColorsInDB.contains(color)).forEach(newColors::add);

        if (newColors.size() > 0) {
            jdbcTemplate.batchUpdate(INSERT_INTO_COLORS_ID_CODE_VALUES,
                    PhoneDaoPreparedStatementSetterHelper.createColorBatchPrepareStatementSetter(newColors));

        }

        if (phoneColors.size() > 0) {
            insertToPhone2Color(phone, phoneColors);
        }
    }

    private void insert(Phone phone) {
        namedParameterJdbcTemplate.update(INSERT_SQL, EntityClassConvert.convertEntityToMap(phone));

        List<Color> colors = phone.getColors().stream().toList();

        insertToPhone2Color(phone, colors);
    }

    private void insertToPhone2Color(Phone phone, List<Color> colors) {
        jdbcTemplate.batchUpdate(INSERT_INTO_PHONE_2_COLOR_PHONE_ID_COLOR_ID_VALUES,
                PhoneDaoPreparedStatementSetterHelper.createPhone2CaseBatchPreparedStatementSetter(phone, colors));
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(FROM_PHONES_OFFSET_LIMIT,
                        ps -> {
                            ps.setInt(1, offset);
                            ps.setInt(2, limit);
                        },
        new BeanPropertyRowMapper<>(Phone.class)).stream().peek(item -> item.setColors(getColors(item.getId())))
                .toList();
    }

    private Set<Color> getColors(Long phoneId) {
        return new HashSet<>(jdbcTemplate.query(GET_COLORS_SQL, ps -> ps.setLong(1, phoneId),
                new BeanPropertyRowMapper<>(Color.class)));
    }
}
