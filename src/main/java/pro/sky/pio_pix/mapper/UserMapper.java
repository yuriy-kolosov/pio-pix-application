package pro.sky.pio_pix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pro.sky.pio_pix.dto.UserDTO;
import pro.sky.pio_pix.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(UserEntity userEntity);

    UserEntity toEntity(UserDTO userDTO);

}
