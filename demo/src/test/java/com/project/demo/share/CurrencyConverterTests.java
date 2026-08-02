package com.project.demo.share;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CurrencyConverterTests {

    @Test
    void convertEURtoUSD_ReturnsConvertedPrice() {

        BigDecimal mockedPriceEUR = mock(BigDecimal.class);
        CurrencyData mockedCurrencyData = mock(CurrencyData.class);

        try (MockedStatic<CurrencyConverter> mockedStatic = mockStatic(CurrencyConverter.class, RETURNS_MOCKS)) {
            mockedStatic.when(CurrencyConverter::getUSDData).thenReturn(mockedCurrencyData);

            BigDecimal priceUSD = CurrencyConverter.convertEURtoUSD(mockedPriceEUR);
            assertNotNull(priceUSD);
            assertNotEquals(priceUSD, mockedPriceEUR);
        }

    }

    @Test
    void getUSDData_ReturnsCurrencyData(){

        // Expected data
        String currency = "USD";
        String currencyCode = "840";
        String country = "SAD";
        String countryCode = "USA";

        // Real data
        CurrencyData USDData = CurrencyConverter.getUSDData();

        // Assertions
        assertEquals(currency, USDData.getCurrency());
        assertEquals(currencyCode, USDData.getCurrencyCode());
        assertEquals(country, USDData.getCountry());
        assertEquals(countryCode, USDData.getCountryCode());

    }

}
