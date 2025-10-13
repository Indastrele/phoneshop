package com.es.core.model.phone.util;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PhoneListResultSetExtractor implements ResultSetExtractor<List<Phone>> {
    @Override
    public List<Phone> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Phone> phonesMap = new HashMap<>();
        List<Long> idOriginalSequence = new ArrayList<>();

        while(rs.next()) {
            Long phoneId = rs.getObject("id", Long.class);
            if (!idOriginalSequence.contains(phoneId)) {
                idOriginalSequence.add(phoneId);
            }
            Phone phone = phonesMap.computeIfAbsent(phoneId, id -> {
                try {
                    return PhoneMapper.mapResultSetToPhone(rs);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Long colorId = rs.getObject("colors.id", Long.class);
            if (colorId != null) {
                phone.getColors().add(new Color(
                        colorId,
                        rs.getString("colors.code")
                ));
            }
        }

        return idOriginalSequence.stream().map(phonesMap::get).toList();
    }
}
