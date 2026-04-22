package br.com.growe.growe_backend.utils;

import br.com.growe.growe_backend.rules.SizeRange;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SizeRangeConverter implements AttributeConverter<SizeRange, String> {

  @Override
  public String convertToDatabaseColumn(SizeRange attribute) {
    if (attribute == null) return null;
    return attribute.getLabel(); // ✅ stores "1-10" in DB
  }

  @Override
  public SizeRange convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    return SizeRange.fromLabel(dbData); // ✅ reads "1-10" from DB
  }
}