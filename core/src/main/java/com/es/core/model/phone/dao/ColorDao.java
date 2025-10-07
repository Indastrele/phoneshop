package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ColorDao {
    Optional<Color> get(final Long id);

    List<Color> findAll(int offset, int limit);
    List<Color> findAllByCode(List<String> codes);

    void save(final Color color);
    void saveAll(final List<Color> colors);
}
