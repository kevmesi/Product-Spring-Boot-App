package com.project.demo.share;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

public class CurrencyConverter {

    private static final String HNB_API_URL = "https://api.hnb.hr/tecajn-eur/v3?valuta=USD";

    /**
     * Returns price in USD converted from EUR using the average conversion rate from HNB API.
     * @param priceEUR price in EUR to be converted in USD
     */
    public static BigDecimal convertEURtoUSD(BigDecimal priceEUR) {
        CurrencyData usdData = getUSDData();
        BigDecimal averageRate = new BigDecimal(usdData.getAverageRate().replace(",", "."));
        return priceEUR.multiply(averageRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns USD currency data from the <a href="https://api.hnb.hr/tecajn-eur/v3?valuta=USD">HNB API</a>.
     *
     */
    public static CurrencyData getUSDData() {

        HttpRequest getRequest = HttpRequest.newBuilder(URI.create(HNB_API_URL)).build();
        HttpResponse<String> getResponse;

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        Type currencyListType = new TypeToken<List<CurrencyData>>() {}.getType();
        List<CurrencyData> usdDataList = gson.fromJson(getResponse.body(), currencyListType);

        return usdDataList.getFirst();
    }

}
