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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService mockService;

    @MockitoBean
    private ObjectMapper objectMapper;

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

}