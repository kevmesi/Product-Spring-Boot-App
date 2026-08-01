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
import java.util.zip.DataFormatException;

public class CurrencyConverter {

    private static final String HNB_API_URL = "https://api.hnb.hr/tecajn-eur/v3?valuta=USD";

    public BigDecimal convertEURtoUSD(BigDecimal priceEUR) throws DataFormatException {
        Currency usdData = getUSDData();
        BigDecimal averageRate = new BigDecimal(usdData.getAverageRate().replace(",", "."));
        return priceEUR.multiply(averageRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns USD currency data from the HNB_API_URL.
     * @return Currency object with data from the HNB_API_URL
     */
    private static Currency getUSDData() {

        HttpRequest getRequest = HttpRequest.newBuilder(URI.create(HNB_API_URL)).build();
        HttpResponse<String> getResponse;

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        Type currencyListType = new TypeToken<List<Currency>>() {}.getType();
        List<Currency> usdDataList = gson.fromJson(getResponse.body(), currencyListType);

        return usdDataList.getFirst();
    }

}
