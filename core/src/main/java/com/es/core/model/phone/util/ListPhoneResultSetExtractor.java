package com.es.core.model.phone.util;

import com.es.core.model.phone.entity.Color;
import com.es.core.model.phone.entity.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ListPhoneResultSetExtractor implements ResultSetExtractor<List<Phone>> {
    @Override
    public List<Phone> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Phone> phones = new ArrayList<>();

        while(rs.next()) {
            Long id = rs.getObject("phones.id", Long.class);

            Phone phone = new Phone();
            Set<Color> colors = new HashSet<>();

            phone.setId(id);
            phone.setBrand(rs.getString("phones.brand"));
            phone.setModel(rs.getString("phones.model"));
            phone.setPrice(rs.getBigDecimal("phones.price"));
            phone.setDisplaySizeInches(rs.getBigDecimal("phones.displaySizeInches"));
            phone.setWeightGr(rs.getObject("phones.weightGr", Integer.class));
            phone.setLengthMm(rs.getBigDecimal("phones.lengthMm"));
            phone.setWidthMm(rs.getBigDecimal("phones.widthMm"));
            phone.setHeightMm(rs.getBigDecimal("phones.heightMm"));
            phone.setAnnounced(rs.getDate("phones.announced"));
            phone.setDeviceType(rs.getString("phones.deviceType"));
            phone.setOs(rs.getString("phones.os"));
            phone.setDisplayResolution(rs.getString("phones.displayResolution"));
            phone.setPixelDensity(rs.getObject("phones.pixelDensity", Integer.class));
            phone.setDisplayTechnology(rs.getString("phones.displayTechnology"));
            phone.setBackCameraMegapixels(rs.getBigDecimal("phones.backCameraMegapixels"));
            phone.setFrontCameraMegapixels(rs.getBigDecimal("phones.frontCameraMegapixels"));
            phone.setRamGb(rs.getBigDecimal("phones.ramGb"));
            phone.setInternalStorageGb(rs.getBigDecimal("phones.internalStorageGb"));
            phone.setBatteryCapacityMah(rs.getObject("phones.batteryCapacityMah", Integer.class));
            phone.setTalkTimeHours(rs.getBigDecimal("phones.talkTimeHours"));
            phone.setStandByTimeHours(rs.getBigDecimal("phones.standByTimeHours"));
            phone.setBluetooth(rs.getString("phones.bluetooth"));
            phone.setPositioning(rs.getString("phones.positioning"));
            phone.setImageUrl(rs.getString("phones.imageUrl"));
            phone.setDescription(rs.getString("phones.description"));
            while(Objects.equals(phone.getId(), id)) {
                colors.add(new Color(rs.getObject(27, Long.class), rs.getString(28)));
                if (!rs.next()) {
                    break;
                }
                id = rs.getObject(1, Long.class);
            }
            phone.setColors(colors);
            phones.add(phone);
        }

        return null;
    }
}
