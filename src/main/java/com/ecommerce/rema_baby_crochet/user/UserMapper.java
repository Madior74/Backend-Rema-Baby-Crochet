package com.ecommerce.rema_baby_crochet.user;


import com.ecommerce.rema_baby_crochet.auth.dto.RegisterRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(RegisterRequest source);
}
