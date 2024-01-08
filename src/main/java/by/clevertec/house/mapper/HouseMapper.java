package by.clevertec.house.mapper;

import by.clevertec.house.dto.HouseDto;
import by.clevertec.house.entity.HouseEntity;
import org.mapstruct.Mapper;

@Mapper
public interface HouseMapper {
    HouseDto toDto(HouseEntity entity);

    HouseEntity toEntity(HouseDto dto);
}