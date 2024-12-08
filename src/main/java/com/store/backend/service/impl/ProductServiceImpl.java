package com.store.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.store.backend.dao.ProductDAO;
import com.store.backend.dao.ProductDAOService;
import com.store.backend.dao.ProductImageDAO;
import com.store.backend.dao.ProductTagDAO;
import com.store.backend.dao.ReviewDAO;
import com.store.backend.dto.ProductDTO;
import com.store.backend.exception.ProductNotFoundException;
import com.store.backend.exception.ProductServiceException;
import com.store.backend.mapper.ProductMapper;
import com.store.backend.repository.ProductImageRepository;
import com.store.backend.repository.ProductRepository;
import com.store.backend.repository.ProductReviewRepository;
import com.store.backend.repository.ProductTagRepository;
import com.store.backend.service.ProductService;
import com.store.backend.util.ProductsLoadingHelper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of ProductService
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductReviewRepository productReviewRepository;
	@Autowired
	private ProductImageRepository productImageRepository;
	@Autowired
	private ProductTagRepository productTagRepository;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ProductsLoadingHelper productsLoadingHelper;
	@Autowired
	private ProductDAOService productDAOService;

	private static final String ASC = "asc";
	private static final String PRICE = "price";
	private static final String PLACEHOLDER = "{}";

	private static final String RETRIEVED_PRODUCT = "Retrieved Product: ";
	private static final String RETRIEVED_PRODUCT_ID = "Retrieved Product by ID: ";
	private static final String RETRIEVED_PRODUCT_SKU = "Retrieved Product by SKU: ";
	private static final String RETRIEVED_PRODUCTS_BY_CATEGORY = "Retrieved Products by Category: ";
	private static final String RETRIEVED_PRODUCTS_SORTED_BY_PRICE = "Retrieved Products sorted by Price: ";
	private static final String PRODUCTS_LOADED_SUCCESSFULLY = "Products loaded successfully";

	private static final String ERROR_RETRIEVING_PRODUCTS = "Error retrieving Products:";
	private static final String ERROR_RETRIEVING_PRODUCT_BY_ID = "Error retrieving Product by ID: ";
	private static final String ERROR_RETRIEVING_PRODUCT_BY_SKU = "Error retrieving Product by SKU: ";
	private static final String ERROR_RETRIEVING_PRODUCTS_BY_CATEGORY = "Error retrieving Products by Category: ";
	private static final String ERROR_RETRIEVING_PRODUCTS_SORTED_BY_PRICE = "Error retrieving Products sorted by Price";
	private static final String ERROR_LOADING_PRODUCTS = "Error loading products";

	private static final String NO_PRODUCTS_FOUND_BY_CATEGORY = "No products found in category ";
	private static final String PRODUCT_DOES_NOT_EXIST_BY_ID = "product does not exist with id: ";
	private static final String PRODUCT_DOES_NOT_EXIST_BY_SKU = "product does not exist with sku: ";

	/**
	 * Get all products
	 * 
	 * @return A Flux of ProductDTO containing all products
	 */
	@Override
	public Flux<ProductDTO> getProducts() {
		return productRepository.findAll().flatMap(this::loadProductComponents)
				.doOnNext(product -> log.info(RETRIEVED_PRODUCT, product))
				.onErrorResume(e -> Flux.error(new ProductServiceException(ERROR_RETRIEVING_PRODUCTS, e)));
	}

	/**
	 * Get product by id
	 * 
	 * @param id The ID of the product to be retrieved
	 * @return A Mono of ProductDTO containing the requested product
	 */
	@Override
	public Mono<ProductDTO> getProductById(Long id) {
		return productRepository.findById(id).flatMap(this::loadProductComponents)
				.switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_DOES_NOT_EXIST_BY_ID + id)))
				.doOnSuccess(product -> log.info(RETRIEVED_PRODUCT_ID + PLACEHOLDER, id))
				.onErrorResume(e -> Mono.error(e instanceof ProductNotFoundException ? e
						: new ProductServiceException(ERROR_RETRIEVING_PRODUCT_BY_ID + id, e)));
	}

	/**
	 * Get product by sku
	 * 
	 * @param sku The SKU of the product to be retrieved
	 * @return A Mono of ProductDTO containing the requested product
	 */
	@Override
	public Mono<ProductDTO> getProductBySku(String sku) {
		return productRepository.findBySku(sku).flatMap(this::loadProductComponents)
				.switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_DOES_NOT_EXIST_BY_SKU + sku)))
				.doOnSuccess(product -> log.info(RETRIEVED_PRODUCT_SKU + PLACEHOLDER, sku))
				.onErrorResume(e -> Mono.error(e instanceof ProductNotFoundException ? e
						: new ProductServiceException(ERROR_RETRIEVING_PRODUCT_BY_SKU + sku, e)));
	}

	/**
	 * Get products by category
	 * 
	 * @param category The category of products to be retrieved
	 * @return A Flux of ProductDTO containing products of the specified category
	 */
	@Override
	public Flux<ProductDTO> getProductsByCategory(String category) {
		return productRepository.findByCategory(category).flatMap(this::loadProductComponents)
				.doOnNext(product -> log.info(RETRIEVED_PRODUCTS_BY_CATEGORY, category))
				.switchIfEmpty(Flux.error(new ProductNotFoundException(NO_PRODUCTS_FOUND_BY_CATEGORY + category)))
				.onErrorResume(e -> Flux.error(e instanceof ProductNotFoundException ? e
						: new ProductServiceException(ERROR_RETRIEVING_PRODUCTS_BY_CATEGORY + category, e)));
	}

	/**
	 * Get products sorted by price
	 * 
	 * @param order The order in which the products should be sorted (asc or desc)
	 * @return A Flux of ProductDTO containing products sorted by price
	 */
	@Override
	public Flux<ProductDTO> getProductsSortedByPrice(String order) {
		Sort sort = order.equalsIgnoreCase(ASC) ? Sort.by(Sort.Order.asc(PRICE)) : Sort.by(Sort.Order.desc(PRICE));
		return productRepository.findAll(sort).flatMap(this::loadProductComponents)
				.doOnNext(product -> log.info(RETRIEVED_PRODUCTS_SORTED_BY_PRICE, order)).onErrorResume(e -> Flux
						.error(new ProductServiceException(ERROR_RETRIEVING_PRODUCTS_SORTED_BY_PRICE + order, e)));
	}

	/**
	 * Load products
	 * 
	 * @return A Mono indicating the completion of the product loading process
	 */
	@Override
	public Mono<String> loadProducts() {
		log.info("Loading products");
		return productsLoadingHelper
				.loadProducts()
				.flatMap(this::saveProduct).then()
				.doOnSuccess(unused -> log.info(PRODUCTS_LOADED_SUCCESSFULLY))
				.onErrorResume(e -> {
					log.error(ERROR_LOADING_PRODUCTS, e);
					return Mono.error(new ProductServiceException(ERROR_LOADING_PRODUCTS, e));
				}).thenReturn(PRODUCTS_LOADED_SUCCESSFULLY);

	}

	/**
	 * Save a product
	 * 
	 * @return A Mono of ProductDTO containing the saved product
	 */
	public Mono<ProductDTO> saveProduct(ProductDTO productDTO) {
		ProductDAO productDAO = productMapper.toDAO(productDTO);
		log.info("Saving product: {}", productDAO);

		return productDAOService.save(productDAO).flatMap(savedProduct -> {
			Flux<ReviewDAO> reviews = Flux.fromIterable(productMapper.toReviewDAOList(productDTO))
					.flatMap(productReviewRepository::save);
			Flux<ProductImageDAO> images = Flux.fromIterable(productMapper.toProductImageDaos(productDTO))
					.flatMap(productImageRepository::save);
			Flux<ProductTagDAO> tags = Flux.fromIterable(productMapper.toProductTagDaos(productDTO))
					.flatMap(productTagRepository::save);
			return Flux.merge(reviews, images, tags).then(Mono.just(savedProduct));
		}).flatMap(this::loadProductComponents);

	}

	/**
	 * Load all components of a product
	 * 
	 * @param productDAO The ProductDAO object whose components are to be loaded
	 * @return A Mono of ProductDTO containing the product with its components (reviews, images, tags)
	 */
	public Mono<ProductDTO> loadProductComponents(ProductDAO productDAO) {
		Mono<List<ReviewDAO>> reviews = productReviewRepository.findByProductId(productDAO.getId()).collectList();
		Mono<List<ProductImageDAO>> images = productImageRepository.findByProductId(productDAO.getId()).collectList();
		Mono<List<ProductTagDAO>> tags = productTagRepository.findByProductId(productDAO.getId()).collectList();

		return Mono.zip(reviews, images, tags)
				.map(tuple -> productMapper.toDTO(productDAO, tuple.getT1(), tuple.getT2(), tuple.getT3()));
	}

}
