package com.es.core.model.phone.util;

import com.es.core.model.phone.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class PhoneMapper {
    public static Phone mapResultSetToPhone(ResultSet rs) throws SQLException {
        Phone phone = new Phone();

        phone.setId(rs.getObject("phones.id", Long.class));
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
        // The line below is necessary
        // Compiler gives UnsupportedOperationException without it
        // Collections.EMPTY_SET is immutable Set
        phone.setColors(new HashSet<>());

        return phone;
    }
}
