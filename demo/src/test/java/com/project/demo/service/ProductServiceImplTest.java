package com.project.demo.service;

import com.project.demo.dao.ProductDAO;
import com.project.demo.entity.Product;
import com.project.demo.share.CurrencyConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;
    @Mock
    ProductDAO productDao;
    @Mock
    CurrencyConverter currencyConverter;

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

        // Product as it arrives from the request, without a USD price
        Product product = new Product();
        product.setCode("ABCD123456");
        product.setPriceEUR(new BigDecimal("100.00"));

        when(currencyConverter.convertEURtoUSD(new BigDecimal("100.00")))
                .thenReturn(new BigDecimal("116.08"));

        // Asserting inside the stub proves the USD price is set before the product is persisted
        when(productDao.save(any(Product.class))).thenAnswer(invocation -> {
            Product productToSave = invocation.getArgument(0);
            assertEquals(new BigDecimal("116.08"), productToSave.getPriceUSD());
            return productToSave;
        });

        // Calling service method
        Product savedProduct = productService.save(product);

        // verifications
        assertEquals(new BigDecimal("116.08"), savedProduct.getPriceUSD());
        assertEquals(new BigDecimal("100.00"), savedProduct.getPriceEUR());
        verify(currencyConverter, times(1)).convertEURtoUSD(new BigDecimal("100.00"));
        verify(productDao, times(1)).save(product);

    }

    @Test
    void save_PropagatesFailureFromCurrencyConverter() {

        Product product = new Product();
        product.setCode("ABCD123456");
        product.setPriceEUR(new BigDecimal("100.00"));

        when(currencyConverter.convertEURtoUSD(any(BigDecimal.class)))
                .thenThrow(new IllegalStateException("HNB API returned no exchange rate for USD"));

        assertThrows(IllegalStateException.class, () -> productService.save(product));

        // Nothing should be persisted if the conversion fails
        verify(productDao, never()).save(any(Product.class));

    }
}
