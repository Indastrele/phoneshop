package com.es.core.model.phone.util;

import com.es.core.model.phone.Color;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ColorBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private final List<Color> colors;

    public ColorBatchPreparedStatementSetter(List<Color> colors) {
        this.colors = colors;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Color color = colors.get(i);
        ps.setString(1, color.getCode());
        ps.setObject(2, color.getId());
    }

    @Override
    public int getBatchSize() {
        return colors.size();
    }
}
