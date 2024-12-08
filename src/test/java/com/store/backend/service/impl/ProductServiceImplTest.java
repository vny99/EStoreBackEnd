package com.store.backend.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import com.store.backend.dao.ProductDAO;
import com.store.backend.dao.ProductImageDAO;
import com.store.backend.dao.ProductTagDAO;
import com.store.backend.dao.ReviewDAO;
import com.store.backend.dto.DimensionsDTO;
import com.store.backend.dto.MetadataDTO;
import com.store.backend.dto.ProductDTO;
import com.store.backend.dto.ReviewDTO;
import com.store.backend.exception.ProductNotFoundException;
import com.store.backend.mapper.ProductMapper;
import com.store.backend.repository.ProductImageRepository;
import com.store.backend.repository.ProductRepository;
import com.store.backend.repository.ProductReviewRepository;
import com.store.backend.repository.ProductTagRepository;
import com.store.backend.util.ProductsLoadingHelper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ProductServiceImplTest {

	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private ProductRepository productRepository;
	@Mock
	private ProductReviewRepository productReviewRepository;
	@Mock
	private ProductImageRepository productImageRepository;
	@Mock
	private ProductTagRepository productTagRepository;
	@Mock
	private ProductMapper productMapper;
	@Mock
	private ProductsLoadingHelper productsLoadingHelper;

	private ProductDTO sampleProductDTO;
	private ProductDAO sampleProductDAO;
	private ReviewDTO sampleReviewDTO;
	private DimensionsDTO sampleDimensionsDTO;
	private MetadataDTO sampleMetadataDTO;
	private ReviewDAO sampleReviewDAO;
	private ProductImageDAO sampleProductImageDAO;
	private ProductTagDAO sampleProductTagDAO;

	@Before
	public void setUp() {
		
		MockitoAnnotations.openMocks(this);
		
		sampleDimensionsDTO = new DimensionsDTO(10.0, 20.0, 30.0);
		sampleMetadataDTO = new MetadataDTO("2023-01-01", "2023-01-02", "123456789", "http://example.com/qr");
		sampleReviewDTO = new ReviewDTO(4.5, "Great product!", "2023-01-05", "John Doe", "john.doe@example.com");
		sampleReviewDAO = new ReviewDAO(1l,4.5, "Great product!", Instant.now(), "John Doe", "john.doe@example.com");
		sampleProductDTO = new ProductDTO(1L, "Product A", "Description A", "Category A", 100.0, 10.0, 4.0, 50,
				Arrays.asList("tag1", "tag2"), "Brand A", "SKU123", 200, sampleDimensionsDTO, "Warranty info",
				"Shipping info", "In Stock", Arrays.asList(sampleReviewDTO), "Return policy", 1, sampleMetadataDTO,
				Arrays.asList("image1.jpg", "image2.jpg"), "thumbnail.jpg");

		sampleProductDAO = new ProductDAO(1L, "Product A", "Description A", "Category A", 100.0, 10.0, 4.0, 50,
				"Brand A", "SKU123", 200.0, 10.0, 20.0, 30.0, "Warranty info", "Shipping info", "In Stock",
				"Return policy", 1, Instant.now(), Instant.now(), "123456789", "http://example.com/qr", "thumbnail.jpg",
				true);
		sampleProductImageDAO = new ProductImageDAO(1L, "image1.jpg");
		sampleProductTagDAO = new ProductTagDAO(1L, "tag1");

		when(productImageRepository.findByProductId(anyLong())).thenReturn(Flux.just(sampleProductImageDAO));
		when(productTagRepository.findByProductId(anyLong())).thenReturn(Flux.just(sampleProductTagDAO));		
		when(productReviewRepository.findByProductId(anyLong())).thenReturn(Flux.just(sampleReviewDAO));
		
	}

	@Test
	public void testGetProducts_success() {
		when(productRepository.findAll()).thenReturn(Flux.just(sampleProductDAO));
		when(productMapper.toDTO(any(ProductDAO.class), anyList(), anyList(), anyList())).thenReturn(sampleProductDTO);

		Flux<ProductDTO> result = productService.getProducts();

		StepVerifier.create(result).expectNext(sampleProductDTO).verifyComplete();
	}

	@Test
	public void testGetProductById_success() {

		when(productRepository.findById(1L)).thenReturn(Mono.just(sampleProductDAO));
		when(productMapper.toDTO(any(ProductDAO.class), anyList(), anyList(), anyList())).thenReturn(sampleProductDTO);

		Mono<ProductDTO> result = productService.getProductById(1L);

		StepVerifier.create(result).expectNext(sampleProductDTO).verifyComplete();
	}
	
	@Test
	public void testGetProductBySku_success() {

		when(productRepository.findBySku(anyString())).thenReturn(Mono.just(sampleProductDAO));
		when(productMapper.toDTO(any(ProductDAO.class), anyList(), anyList(), anyList())).thenReturn(sampleProductDTO);

		Mono<ProductDTO> result = productService.getProductBySku("SKU123");

		StepVerifier.create(result).expectNext(sampleProductDTO).verifyComplete();
	}
	
	@Test
	public void testGetProductBySku_notFound() {

        when(productRepository.findBySku("SKU123")).thenReturn(Mono.empty());

        Mono<ProductDTO> result = productService.getProductBySku("SKU123");

        StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException
                && throwable.getMessage().equals("product does not exist with sku: SKU123")).verify();
        }
	
	@Test 
	public void testGetProductsByCategory_success() {
		when(productRepository.findByCategory(anyString())).thenReturn(Flux.just(sampleProductDAO));
		when(productMapper.toDTO(any(ProductDAO.class), anyList(), anyList(), anyList())).thenReturn(sampleProductDTO);

		Flux<ProductDTO> result = productService.getProductsByCategory("Category A");

		StepVerifier.create(result).expectNext(sampleProductDTO).verifyComplete();
	}
	
	@Test
	public void testGetProductsByCategory_notFound() {

		when(productRepository.findByCategory(anyString())).thenReturn(Flux.empty());

		Flux<ProductDTO> result = productService.getProductsByCategory("Category A");

		 StepVerifier.create(result)
         .expectError(ProductNotFoundException.class)
         .verify();
	}
	
	@Test
	public void testGetProductsSortedByPrice_success() {
		when(productRepository.findAll(any(Sort.class))).thenReturn(Flux.just(sampleProductDAO));
		when(productMapper.toDTO(any(ProductDAO.class), anyList(), anyList(), anyList())).thenReturn(sampleProductDTO);

		Flux<ProductDTO> result = productService.getProductsSortedByPrice("asc");

		StepVerifier.create(result).expectNext(sampleProductDTO).verifyComplete();
	}

	@Test
	public void testGetProductById_notFound() {

		when(productRepository.findById(1L)).thenReturn(Mono.empty());

		Mono<ProductDTO> result = productService.getProductById(1L);

		StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException
				&& throwable.getMessage().equals("product does not exist with id: 1")).verify();
	}

	@Test
	public void testSaveProduct_success() {
		when(productMapper.toDAO(any(ProductDTO.class))).thenReturn(sampleProductDAO);
		when(productRepository.save(any(ProductDAO.class))).thenReturn(Mono.just(sampleProductDAO));
		when(productMapper.toDTO(any(ProductDAO.class), anyList(), anyList(), anyList())).thenReturn(sampleProductDTO);

		Mono<ProductDTO> result = productService.saveProduct(sampleProductDTO);

		StepVerifier.create(result).expectNext(sampleProductDTO).verifyComplete();
	}

	@Test
	public void testLoadProducts_success() {

		when(productsLoadingHelper.loadProducts()).thenReturn(Flux.just(sampleProductDTO));
		when(productMapper.toDAO(any(ProductDTO.class))).thenReturn(sampleProductDAO);
		when(productRepository.save(any(ProductDAO.class))).thenReturn(Mono.just(sampleProductDAO));
		when(productMapper.toReviewDAOList(any(ProductDTO.class))).thenReturn(Arrays.asList(sampleReviewDAO));
		when(productMapper.toProductImageDaos(any(ProductDTO.class))).thenReturn(Arrays.asList(sampleProductImageDAO));
		when(productMapper.toProductTagDaos(any(ProductDTO.class))).thenReturn(Arrays.asList(sampleProductTagDAO));
		when(productRepository.findAll()).thenReturn(Flux.just(sampleProductDAO));
		when(productMapper.toDTO(any(ProductDAO.class), anyList(), anyList(), anyList())).thenReturn(sampleProductDTO);
		when(productReviewRepository.save(any(ReviewDAO.class))).thenReturn(Mono.just(sampleReviewDAO));
		when(productImageRepository.save(any(ProductImageDAO.class))).thenReturn(Mono.just(sampleProductImageDAO));
		when(productTagRepository.save(any(ProductTagDAO.class))).thenReturn(Mono.just(sampleProductTagDAO));

		Mono<String> result = productService.loadProducts();

		StepVerifier.create(result).expectNext("Products loaded successfully").verifyComplete();
	}
}
