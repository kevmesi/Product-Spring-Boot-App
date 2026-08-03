package com.project.demo.share;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * The HNB response is stubbed, so these tests exercise the real JSON mapping,
 * the real comma-to-dot rate handling, and the real rounding without a network call.
 */
class CurrencyConverterTests {

    private static final String BASE_URL = "https://api.hnb.hr/tecajn-eur/v3";
    private static final String EXPECTED_URL = BASE_URL + "?valuta=USD";

    // Data fetched from the HNB API on date 2026-08-02
    private static final String HNB_USD_RESPONSE = """
            [{
              "broj_tecajnice": "148",
              "datum_primjene": "2026-08-02",
              "drzava": "SAD",
              "drzava_iso": "USA",
              "kupovni_tecaj": "1,159300",
              "prodajni_tecaj": "1,162200",
              "sifra_valute": "840",
              "srednji_tecaj": "1,160750",
              "valuta": "USD"
            }]""";

    private MockRestServiceServer mockServer;
    private CurrencyConverter currencyConverter;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        currencyConverter = new CurrencyConverter(builder, BASE_URL);
    }

    @Test
    void getUSDData_ReturnsCurrencyData() {

        mockServer.expect(requestTo(EXPECTED_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(HNB_USD_RESPONSE, MediaType.APPLICATION_JSON));

        CurrencyData usdData = currencyConverter.getUSDData();

        assertEquals("USD", usdData.getCurrency());
        assertEquals("840", usdData.getCurrencyCode());
        assertEquals("SAD", usdData.getCountry());
        assertEquals("USA", usdData.getCountryCode());
        assertEquals("1,160750", usdData.getAverageRate());
        mockServer.verify();
    }

    @Test
    void convertEURtoUSD_ReturnsConvertedPrice() {

        mockServer.expect(requestTo(EXPECTED_URL))
                .andRespond(withSuccess(HNB_USD_RESPONSE, MediaType.APPLICATION_JSON));

        // 100.00 * 1.160750 = 116.0750, to be rounded HALF_UP to two decimals
        assertEquals(new BigDecimal("116.08"),
                currencyConverter.convertEURtoUSD(new BigDecimal("100.00")));
        mockServer.verify();
    }

    @Test
    void convertEURtoUSD_RoundsHalfUpToTwoDecimals() {

        mockServer.expect(requestTo(EXPECTED_URL))
                .andRespond(withSuccess(HNB_USD_RESPONSE, MediaType.APPLICATION_JSON));

        // 0.01 * 1.160750 = 0.01160750, to be rounded HALF_UP to two decimals
        assertEquals(new BigDecimal("0.01"),
                currencyConverter.convertEURtoUSD(new BigDecimal("0.01")));
    }

    @Test
    void convertEURtoUSD_ZeroStaysZero() {

        mockServer.expect(requestTo(EXPECTED_URL))
                .andRespond(withSuccess(HNB_USD_RESPONSE, MediaType.APPLICATION_JSON));

        assertEquals(new BigDecimal("0.00"),
                currencyConverter.convertEURtoUSD(BigDecimal.ZERO));
    }

    @Test
    void getUSDData_ThrowsWhenApiReturnsEmptyList() {

        mockServer.expect(requestTo(EXPECTED_URL))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertThrows(IllegalStateException.class, () -> currencyConverter.getUSDData());
    }

    @Test
    void getUSDData_PropagatesApiFailure() {

        mockServer.expect(requestTo(EXPECTED_URL)).andRespond(withServerError());

        assertThrows(RuntimeException.class, () -> currencyConverter.getUSDData());
    }

}
