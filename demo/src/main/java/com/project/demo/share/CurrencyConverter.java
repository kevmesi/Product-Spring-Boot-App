package com.project.demo.share;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Client for the <a href="https://api.hnb.hr/tecajn-eur/v3?valuta=USD">HNB API</a>.
 *
 * <p>Covered by CurrencyConverterTests.
 */
@Component
public class CurrencyConverter {

    private final RestClient restClient;

    public CurrencyConverter(RestClient.Builder restClientBuilder,
                             @Value("${hnb.api.url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Returns price in USD converted from EUR using the average conversion rate from HNB API.
     * @param priceEUR price in EUR to be converted in USD
     */
    public BigDecimal convertEURtoUSD(BigDecimal priceEUR) {
        BigDecimal averageRate = new BigDecimal(getUSDData().getAverageRate().replace(",", "."));
        return priceEUR.multiply(averageRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns USD currency data from the HNB API.
     * @throws IllegalStateException if the API returns no rate for USD
     */
    public CurrencyData getUSDData() {

        List<CurrencyData> usdDataList = restClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("valuta", "USD").build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (usdDataList == null || usdDataList.isEmpty()) {
            throw new IllegalStateException("HNB API returned no exchange rate for USD");
        }

        return usdDataList.getFirst();
    }

}
