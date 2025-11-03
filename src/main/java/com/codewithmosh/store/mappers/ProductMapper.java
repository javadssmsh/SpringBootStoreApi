package com.codewithmosh.store.mappers;


import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    public ProductDto toDto(Product product);

    public Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    public void update(ProductDto productDto, @MappingTarget Product product);
}
