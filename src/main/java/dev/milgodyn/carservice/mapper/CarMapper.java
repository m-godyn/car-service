package dev.milgodyn.carservice.mapper;

import dev.milgodyn.carservice.dto.CarDto;
import dev.milgodyn.carservice.persistence.entity.CarEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface CarMapper {

    @Mapping(target = "created", ignore = true)
    CarEntity toEntity(CarDto dto);

    CarDto toDto(CarEntity entity);

    List<CarDto> toDtoList(List<CarEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "created", ignore = true)
    void updateCarFromDto(CarDto dto, @MappingTarget CarEntity entity);
}
