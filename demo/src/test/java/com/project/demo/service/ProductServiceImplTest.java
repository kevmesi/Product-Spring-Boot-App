package com.project.demo.service;

import com.project.demo.dao.ProductDAO;
import com.project.demo.entity.Product;
import com.project.demo.share.CurrencyConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;
    @Mock
    ProductDAO productDao;

    @Test
    void findAll_SuccessfulCall() {

        // Mocking a list of products
        List<Product> productList = new ArrayList<>();
        Product mockProduct = mock(Product.class);
        productList.add(mockProduct);

        when(productDao.findAll()).thenReturn(productList);

        // Calling service method
        List<Product> products = productService.findAll();

        // verifications
        verify(productDao, times(1)).findAll();
        assertEquals(productList, products);

    }

    @Test
    void findAvailable_SuccessfulCall() {

        // Mocking a list of products
        List<Product> productList = new ArrayList<>();
        Product mockProduct = mock(Product.class);
        productList.add(mockProduct);

        when(productDao.findAvailable()).thenReturn(productList);

        // Calling service method
        List<Product> products = productService.findAvailable();

        // verifications
        verify(productDao, times(1)).findAvailable();
        assertEquals(productList, products);

    }

    @Test
    void findAvailable_NoAvailableProducts() {

        // List of products should be empty
        when(productDao.findAvailable()).thenReturn(new ArrayList<>());

        // Calling service method
        List<Product> products = productService.findAvailable();

        // verifications
        verify(productDao, times(1)).findAvailable();
        assertEquals(new ArrayList<>(), products);

    }

    @Test
    void findByCode_SuccessfulCall() {

        // mock product
        Product mockProduct = mock(Product.class);
        String mockCode = "code";
        when(productDao.findByCode(mockCode)).thenReturn(mockProduct);

        // Calling service method
        Product product = productService.findByCode(mockCode);

        // verifications
        verify(productDao, times(1)).findByCode(mockCode);
        assertEquals(mockProduct, product);

    }

    @Test
    void findByCode_UnsuccessfulCall() {

        // mock product
        String mockCode = "code";
        when(productDao.findByCode(mockCode)).thenReturn(null);

        // Calling service method
        Product product = productService.findByCode(mockCode);

        // verifications
        verify(productDao, times(1)).findByCode(mockCode);
        assertNull(product);

    }

    @Test
    void save_addsNewProductSuccessfullyAndPopulatesPriceUSD() {

        // Mock values
        Product mockProduct = mock(Product.class);
        BigDecimal mockPriceUSD = mock(BigDecimal.class);

        // Expected product values
        Product expectedProduct = new Product();
        expectedProduct.setPriceUSD((mockPriceUSD));

        try (MockedStatic<CurrencyConverter> mockedStatic = mockStatic(CurrencyConverter.class, RETURNS_MOCKS)) {
            mockedStatic.when(() -> CurrencyConverter.convertEURtoUSD(mockProduct.getPriceEUR()))
                    .thenReturn(mockPriceUSD);
            when(productDao.save(mockProduct)).thenReturn(expectedProduct);

            Product addedProduct = productService.save(mockProduct);
            assertNotNull(addedProduct.getPriceUSD());
            assertNotEquals(addedProduct.getPriceUSD(), mockProduct.getPriceUSD());
        }

    }
}