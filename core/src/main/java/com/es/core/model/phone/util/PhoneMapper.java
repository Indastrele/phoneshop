package com.es.core.model.phone.util;

import com.es.core.model.phone.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class PhoneMapper {
    public static Phone mapResultSetToPhone(ResultSet rs) throws SQLException {
        Phone phone = new Phone();

        phone.setId(rs.getObject("id", Long.class));
        phone.setBrand(rs.getString("brand"));
        phone.setModel(rs.getString("model"));
        phone.setPrice(rs.getBigDecimal("price"));
        phone.setDisplaySizeInches(rs.getBigDecimal("displaySizeInches"));
        phone.setWeightGr(rs.getObject("weightGr", Integer.class));
        phone.setLengthMm(rs.getBigDecimal("lengthMm"));
        phone.setWidthMm(rs.getBigDecimal("widthMm"));
        phone.setHeightMm(rs.getBigDecimal("heightMm"));
        phone.setAnnounced(rs.getDate("announced"));
        phone.setDeviceType(rs.getString("deviceType"));
        phone.setOs(rs.getString("os"));
        phone.setDisplayResolution(rs.getString("displayResolution"));
        phone.setPixelDensity(rs.getObject("pixelDensity", Integer.class));
        phone.setDisplayTechnology(rs.getString("displayTechnology"));
        phone.setBackCameraMegapixels(rs.getBigDecimal("backCameraMegapixels"));
        phone.setFrontCameraMegapixels(rs.getBigDecimal("frontCameraMegapixels"));
        phone.setRamGb(rs.getBigDecimal("ramGb"));
        phone.setInternalStorageGb(rs.getBigDecimal("internalStorageGb"));
        phone.setBatteryCapacityMah(rs.getObject("batteryCapacityMah", Integer.class));
        phone.setTalkTimeHours(rs.getBigDecimal("talkTimeHours"));
        phone.setStandByTimeHours(rs.getBigDecimal("standByTimeHours"));
        phone.setBluetooth(rs.getString("bluetooth"));
        phone.setPositioning(rs.getString("positioning"));
        phone.setImageUrl(rs.getString("imageUrl"));
        phone.setDescription(rs.getString("description"));
        // The line below is necessary
        // Compiler gives UnsupportedOperationException without it
        // Collections.EMPTY_SET is immutable Set
        phone.setColors(new HashSet<>());

        return phone;
    }
}
