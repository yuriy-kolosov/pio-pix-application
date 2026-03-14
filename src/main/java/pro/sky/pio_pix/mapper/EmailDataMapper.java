package pro.sky.pio_pix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pro.sky.pio_pix.dto.EmailDataDTO;
import pro.sky.pio_pix.entity.EmailDataEntity;

@Mapper(componentModel = "spring")
public interface EmailDataMapper {

    EmailDataMapper INSTANCE = Mappers.getMapper(EmailDataMapper.class);

    EmailDataDTO toDTO(EmailDataEntity emailDataEntity);

    EmailDataEntity toEntity(EmailDataDTO emailDataDTO);

}
