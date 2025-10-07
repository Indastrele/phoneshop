package com.es.core.model.phone.util;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Phone2ColorBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private final List<Color> colors;
    private final Phone phone;

    public Phone2ColorBatchPreparedStatementSetter(List<Color> colors, Phone phone) {
        this.colors = colors;
        this.phone = phone;
    }

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
}
