package pro.sky.pio_pix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pro.sky.pio_pix.dto.PhoneDataDTO;
import pro.sky.pio_pix.entity.PhoneDataEntity;

@Mapper(componentModel = "spring")
public interface PhoneDataMapper {

    PhoneDataMapper INSTANCE = Mappers.getMapper(PhoneDataMapper.class);

    PhoneDataDTO toDTO(PhoneDataEntity phoneDataEntity);

    PhoneDataEntity toEntity(PhoneDataDTO phoneDataDTO);

}
