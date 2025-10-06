package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ColorDao {
    Optional<Color> get(final Long id);

    List<Color> findAll(int offset, int limit);
    Set<Color> findAllPhoneColors(final Long phoneId);

    void save(final Color color);
    Set<Color> saveAll(final List<Color> colors);
}
