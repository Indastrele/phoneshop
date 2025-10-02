package com.es.core.model.phone.util;

import com.es.core.model.phone.entity.Color;
import com.es.core.model.phone.entity.Phone;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PhoneDaoPreparedStatementSetterHelper {
    public static BatchPreparedStatementSetter  createPhone2CaseBatchPreparedStatementSetter(Phone phone, List<Color> colors) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Color color = colors.get(i);
                ps.setLong(1, phone.getId());
                ps.setLong(2, color.getId());
            }

            @Override
            public int getBatchSize() {
                return colors.size();
            }
        };
    }
}
