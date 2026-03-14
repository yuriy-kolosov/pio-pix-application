package pro.sky.pio_pix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pro.sky.pio_pix.dto.TransferDataDTO;
import pro.sky.pio_pix.entity.TransferDataEntity;

@Mapper(componentModel = "spring")
public interface TransferDataMapper {

    TransferDataMapper INSTANCE = Mappers.getMapper(TransferDataMapper.class);

    TransferDataDTO toDto(TransferDataEntity transferDataEntity);

    TransferDataEntity toEntity(TransferDataDTO transferDataDTO);

}
