package com.mycompany.meetingroom.util;

import com.mycompany.meetingroom.model.RecurrenceType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RecurrenceTypeConverter implements AttributeConverter<RecurrenceType, String> {

    @Override
    public String convertToDatabaseColumn(RecurrenceType attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public RecurrenceType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (RecurrenceType type : RecurrenceType.values()) {
            if (type.name().equalsIgnoreCase(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for RecurrenceType: " + dbData);
    }
}