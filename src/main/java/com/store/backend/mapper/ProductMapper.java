package com.store.backend.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.store.backend.dao.ProductDAO;
import com.store.backend.dao.ProductImageDAO;
import com.store.backend.dao.ProductTagDAO;
import com.store.backend.dao.ReviewDAO;
import com.store.backend.dto.ProductDTO;
import com.store.backend.dto.ReviewDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	/**
     * Converts a ProductDTO to a ProductDAO.
     * Maps various fields from the DTO to the DAO including dimensions and metadata.
     *
     * @param productDTO The ProductDTO object to be converted.
     * @return The corresponding ProductDAO object.
     */
    @Mapping(target = "width", source = "dimensions.width")
    @Mapping(target = "height", source = "dimensions.height")
    @Mapping(target = "depth", source = "dimensions.depth")
    @Mapping(target = "createdAt", source = "meta.createdAt")
    @Mapping(target = "updatedAt", source = "meta.updatedAt")
    @Mapping(target = "barcode", source = "meta.barcode")
    @Mapping(target = "qrCode", source = "meta.qrCode")
    ProductDAO toDAO(ProductDTO productDTO);

    /**
     * Converts a ProductDAO to a ProductDTO.
     * Maps fields from DAO to DTO including reviews, images, and tags.
     *
     * @param productDAO The ProductDAO object to be converted.
     * @param reviewDAO A list of ReviewDAO objects to map to reviews.
     * @param imageDAO A list of ProductImageDAO objects to map to images.
     * @param productTagDAO A list of ProductTagDAO objects to map to tags.
     * @return The corresponding ProductDTO object.
     */
    @Mapping(target = "dimensions.width", source = "productDAO.width")
    @Mapping(target = "dimensions.height", source = "productDAO.height")
    @Mapping(target = "dimensions.depth", source = "productDAO.depth")
    @Mapping(target = "meta.createdAt", source = "productDAO.createdAt")
    @Mapping(target = "meta.updatedAt", source = "productDAO.updatedAt")
    @Mapping(target = "meta.barcode", source = "productDAO.barcode")
    @Mapping(target = "meta.qrCode", source = "productDAO.qrCode")
    @Mapping(target = "reviews", source = "reviewDAO")
    @Mapping(target = "images", source = "imageDAO", qualifiedByName = "mapImages")
    @Mapping(target = "tags", source = "productTagDAO", qualifiedByName = "mapTags")
    ProductDTO toDTO(ProductDAO productDAO, List<ReviewDAO> reviewDAO, List<ProductImageDAO> imageDAO, List<ProductTagDAO> productTagDAO);

    /**
     * Converts ProductDTO and ReviewDTO into ReviewDAO.
     * Maps the product ID and review rating to the ReviewDAO object.
     *
     * @param productDTO The ProductDTO object.
     * @param reviewDTO The ReviewDTO object.
     * @return The corresponding ReviewDAO object.
     */
    @Mapping(target = "productId", source = "productDTO.id")
    @Mapping(target = "rating", source = "reviewDTO.rating")
    ReviewDAO toDAO(ProductDTO productDTO, ReviewDTO reviewDTO);

    /**
     * Converts a ReviewDAO to a ReviewDTO.
     * Maps fields of ReviewDAO into ReviewDTO.
     *
     * @param reviewDAO The ReviewDAO object to be converted.
     * @return The corresponding ReviewDTO object.
     */
    ReviewDTO toDTO(ReviewDAO reviewDAO);

    /**
     * Converts a list of ReviewDTO to a list of ReviewDAO.
     * Maps each ReviewDTO object into its corresponding ReviewDAO.
     *
     * @param productDTO The ProductDTO object containing reviews.
     * @return A list of corresponding ReviewDAO objects.
     */
    default List<ReviewDAO> toReviewDAOList(ProductDTO productDTO) {
        return productDTO.getReviews().stream()
                .map(reviewDTO -> toDAO(productDTO, reviewDTO))
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of ReviewDAO to a list of ReviewDTO.
     * Maps each ReviewDAO object into its corresponding ReviewDTO.
     *
     * @param reviewDAOList A list of ReviewDAO objects to be converted.
     * @return A list of corresponding ReviewDTO objects.
     */
    List<ReviewDTO> toReviewDTOList(List<ReviewDAO> reviewDAOList);

    /**
     * Converts a ProductDTO and a tag string into a ProductTagDAO.
     * Maps the product ID and tag string to the ProductTagDAO.
     *
     * @param productDTO The ProductDTO object.
     * @param tag The tag string to be converted.
     * @return The corresponding ProductTagDAO object.
     */
    @Mapping(target = "productId", source = "productDTO.id")
    @Mapping(target = "tag", source = "tag")
    ProductTagDAO toProductTagDao(ProductDTO productDTO, String tag);

    /**
     * Converts a list of tags in ProductDTO to a list of ProductTagDAO objects.
     * Maps each tag to its corresponding ProductTagDAO.
     *
     * @param productDTO The ProductDTO object containing tags.
     * @return A list of corresponding ProductTagDAO objects.
     */
    @IterableMapping(qualifiedByName = "toProductTagDaos")
    default List<ProductTagDAO> toProductTagDaos(ProductDTO productDTO) {
        return productDTO.getTags().stream()
                .map(tag -> toProductTagDao(productDTO, tag))
                .toList();
    }

    /**
     * Converts a ProductDTO and an image URL string into a ProductImageDAO.
     * Maps the product ID and image URL string to the ProductImageDAO.
     *
     * @param productDTO The ProductDTO object.
     * @param img The image URL string to be converted.
     * @return The corresponding ProductImageDAO object.
     */
    @Mapping(target = "productId", source = "productDTO.id")
    @Mapping(target = "imageUrl", source = "img")
    ProductImageDAO toProductImageDao(ProductDTO productDTO, String img);

    /**
     * Converts a list of image URLs in ProductDTO to a list of ProductImageDAO objects.
     * Maps each image URL to its corresponding ProductImageDAO.
     *
     * @param productDTO The ProductDTO object containing images.
     * @return A list of corresponding ProductImageDAO objects.
     */
    @IterableMapping(qualifiedByName = "toProductImageDaos")
    default List<ProductImageDAO> toProductImageDaos(ProductDTO productDTO) {
        return productDTO.getImages().stream()
                .map(img -> toProductImageDao(productDTO, img))
                .toList();
    }

    /**
     * Custom mapping to convert a list of ProductImageDAO objects into a list of image URLs.
     * Extracts the image URL from each ProductImageDAO object.
     *
     * @param imageDAOs A list of ProductImageDAO objects.
     * @return A list of image URL strings.
     */
    @Named("mapImages")
    default List<String> mapImages(List<ProductImageDAO> imageDAOs) {
        if (imageDAOs == null) return Collections.emptyList();
        return imageDAOs.stream()
                .map(ProductImageDAO::getImageUrl)
                .collect(Collectors.toList());
    }

    /**
     * Custom mapping to convert a list of ProductTagDAO objects into a list of tag strings.
     * Extracts the tag from each ProductTagDAO object.
     *
     * @param tagDAOs A list of ProductTagDAO objects.
     * @return A list of tag strings.
     */
    @Named("mapTags")
    default List<String> mapTags(List<ProductTagDAO> tagDAOs) {
        if (tagDAOs == null) return Collections.emptyList();
        return tagDAOs.stream()
                .map(ProductTagDAO::getTag)
                .collect(Collectors.toList());
    }

}
