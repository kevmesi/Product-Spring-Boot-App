package com.project.demo.rest;

import com.project.demo.entity.Product;
import com.project.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService mockService;

    @Test
    void findAllProducts_SuccessfulCall() throws Exception {
        mockMvc.perform(get("/api/products")).andExpect(status().isOk());
    }

    @Test
    void findProductByCode_ProductFound() throws Exception {

        // Mocking product to return
        Product mockProduct = mock(Product.class);
        when(mockService.findByCode(anyString())).thenReturn(mockProduct);

        mockMvc.perform(get("/api/products/firstcode1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findProductByCode_ProductNotFound() throws Exception {

        // Mocking return no product
        when(mockService.findByCode(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/products/firstcode1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllAvailableProducts_SuccessfulCall() throws Exception {
        mockMvc.perform(get("/api/products/available")).andExpect(status().isOk());
    }

    @Test
    void save_SuccessfulCall() throws Exception {

        // Mocks
        Product mockProduct = new Product();
        when(mockService.save(any(Product.class))).thenReturn(mockProduct);

        String mockContent = """
                {
                    "code": "firstcode1",
                    "name": "some name",
                    "price_eur": 1.00,
                    "is_available": false
                }""";

        BindingResult mockResult = mock(BindingResult.class);
        when(mockResult.hasErrors()).thenReturn(false);

        // Calling service
        mockMvc.perform(post("/api/products")
                        .content(mockContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void save_InvalidCodeIsRejectedBeforeReachingTheService() throws Exception {

        // "short" is not the required 10 alphanumeric characters
        String invalidContent = """
                {
                    "code": "short",
                    "name": "some name",
                    "price_eur": 1.00,
                    "is_available": false
                }""";

        mockMvc.perform(post("/api/products")
                        .content(invalidContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // An invalid product must never be persisted
        verify(mockService, never()).save(any(Product.class));

    }

    @Test
    void save_MissingPriceEurIsRejectedBeforeReachingTheService() throws Exception {

        String invalidContent = """
                {
                    "code": "firstcode1",
                    "name": "some name",
                    "is_available": false
                }""";

        mockMvc.perform(post("/api/products")
                        .content(invalidContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(mockService, never()).save(any(Product.class));

    }

}