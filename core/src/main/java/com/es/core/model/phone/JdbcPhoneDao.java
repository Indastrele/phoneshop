package com.es.core.model.phone;

import jakarta.annotation.Resource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class JdbcPhoneDao implements PhoneDao{
    private static final String UPDATE_SQL = "UPDATE phones SET brand = ?, model = ?, price = ?, " +
            "displaySizeInches = ?, weightGr = ?, lengthMm = ?, widthMm = ?, heightMm = ?, announced = ?, " +
            "deviceType = ?, os = ?, displayResolution = ?, pixelDensity = ?, displayTechnology = ?, " +
            "backCameraMegapixels = ?, frontCameraMegapixels = ?, ramGb = ?, internalStorageGb = ?, " +
            "batteryCapacityMah = ?, talkTimeHours = ?, standByTimeHours = ?, bluetooth = ?, " +
            "positioning = ?, imageUrl = ?, description = ? WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO phones (brand, model, price, displaySizeInches, weightGr, " +
            "lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, " +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_SQL = "select * from phones where id = ";
    private static final String GET_COLORS_SQL = "select * from colors " +
            "join phone2color on colors.id = phone2color.colorId " +
            "where phone2color.phoneId = ";

    @Resource
    private JdbcTemplate jdbcTemplate;

    public Optional<Phone> get(final Long key) {
        var phone = jdbcTemplate.queryForObject(GET_SQL + key,
                new BeanPropertyRowMapper<>(Phone.class));
        phone.setColors(getColors(phone.getId()));
        return Optional.of(phone);
    }

    public void save(final Phone phone) {
        if (get(phone.getId()).isPresent()) {
            update(phone);
        } else {
            insert(phone);
        }
    }

    private void update(Phone phone) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    UPDATE_SQL
            );

            setStatementParametersWithoutId(phone, ps);
            ps.setLong(26, phone.getId());

            return ps;
        });

        var colors = phone.getColors().stream().toList();
        var dbColors = getColors(phone.getId());
        var colorsToDelete = dbColors.stream().filter(item -> !colors.contains(item)).toList();
        if (colorsToDelete.size() > 0) {
            jdbcTemplate.batchUpdate("delete from phone2color where colorId = ? and phoneId = ?",
                    createPhone2CaseBatchPS(phone, colorsToDelete));
        }

        if (colors.size() > dbColors.size() - colorsToDelete.size()) {
            insertToPhone2Color(phone, colors);
        }
    }

    private void insert(Phone phone) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    INSERT_SQL
            );

            setStatementParametersWithoutId(phone, ps);

            return ps;
        });

        var colors = phone.getColors().stream().toList();

        insertToPhone2Color(phone, colors);
    }

    private void insertToPhone2Color(Phone phone, List<Color> colors) {
        jdbcTemplate.batchUpdate("insert into phone2color (phoneId, colorId) values (?, ?)",
                createPhone2CaseBatchPS(phone, colors));
    }

    private BatchPreparedStatementSetter createPhone2CaseBatchPS(Phone phone, List<Color> colors) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var color = colors.get(i);
                ps.setLong(1, phone.getId());
                ps.setLong(2, color.getId());
            }

            @Override
            public int getBatchSize() {
                return colors.size();
            }
        };
    }

    private void setStatementParametersWithoutId(Phone phone, PreparedStatement ps) throws SQLException {
        ps.setString(1, phone.getBrand());
        ps.setString(2, phone.getModel());
        ps.setBigDecimal(3, phone.getPrice());
        ps.setBigDecimal(4, phone.getDisplaySizeInches());
        ps.setInt(5, phone.getWeightGr());
        ps.setBigDecimal(6, phone.getLengthMm());
        ps.setBigDecimal(7, phone.getWidthMm());
        ps.setBigDecimal(8, phone.getHeightMm());
        ps.setTimestamp(9, phone.getAnnounced() != null ? new Timestamp(phone.getAnnounced().getTime())
                : null);
        ps.setString(10, phone.getDeviceType());
        ps.setString(11, phone.getOs());
        ps.setString(12, phone.getDisplayResolution());
        ps.setInt(13, phone.getPixelDensity());
        ps.setString(14, phone.getDisplayTechnology());
        ps.setBigDecimal(15, phone.getBackCameraMegapixels());
        ps.setBigDecimal(16, phone.getFrontCameraMegapixels());
        ps.setBigDecimal(17, phone.getRamGb());
        ps.setBigDecimal(18, phone.getInternalStorageGb());
        ps.setInt(19, phone.getBatteryCapacityMah());
        ps.setBigDecimal(20, phone.getTalkTimeHours());
        ps.setBigDecimal(21, phone.getStandByTimeHours());
        ps.setString(22, phone.getBluetooth());
        ps.setString(23, phone.getPositioning());
        ps.setString(24, phone.getImageUrl());
        ps.setString(25, phone.getDescription());
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query("select * from phones offset " + offset + " limit " + limit,
                new BeanPropertyRowMapper<>(Phone.class)).stream().peek(item -> item.setColors(getColors(item.getId())))
                .toList();
    }

    private Set<Color> getColors(Long phoneId) {
        return new HashSet<>(jdbcTemplate.query(GET_COLORS_SQL + phoneId, new BeanPropertyRowMapper<>(Color.class)));
    }
}
