package com.store.backend.dao;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.store.backend.repository.ProductRepository;


public class ProductDAOServiceTest {
	@InjectMocks
	private ProductDAOService productDAOService;
	@Mock
	private ProductRepository productRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testSave_whenProductExists() {
		
		ProductDAO product = new ProductDAO();
		product.setId(1L);
		productDAOService.save(product);
		productDAOService.save(product);
		verify(productRepository, times(2)).save(product);
	}
	
}