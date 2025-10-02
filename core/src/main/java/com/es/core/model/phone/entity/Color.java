package com.es.core.model.phone.entity;

import java.util.Objects;

public class Color {
    private Long id;
    private String code;

    public Color() {
    }

    public Color(String code) {
        this.code = code;
    }

    public Color(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Color color = (Color) obj;

        return Objects.equals(id, color.id) && Objects.equals(code, color.code);
    }
}
