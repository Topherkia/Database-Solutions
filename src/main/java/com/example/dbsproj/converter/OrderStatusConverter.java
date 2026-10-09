package com.example.dbsproj.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        if (attribute == null) {
            return "NEW"; // Default status
        }
        return attribute.name();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return OrderStatus.NEW;
        }
        try {
            return OrderStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OrderStatus.NEW;
        }
    }
}