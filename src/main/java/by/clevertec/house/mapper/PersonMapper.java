package by.clevertec.house.mapper;

import by.clevertec.house.dto.PersonDto;
import by.clevertec.house.entity.PersonEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {
    PersonDto toDto(PersonEntity entity);

    PersonEntity toEntity(PersonDto dto);
}
