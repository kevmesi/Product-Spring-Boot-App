package com.project.demo.share;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * One row of the HNB exchange rate list. Rates are returned as strings using a comma
 * as the decimal separator, so they are kept as String here and parsed where used.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyData {

    @JsonProperty("broj_tecajnice")
    private String exchangeRateCode;

    @JsonProperty("datum_primjene")
    private LocalDate dateFrom;

    @JsonProperty("drzava")
    private String country;

    @JsonProperty("drzava_iso")
    private String countryCode;

    @JsonProperty("kupovni_tecaj")
    private String buyingRate;

    @JsonProperty("prodajni_tecaj")
    private String sellingRate;

    @JsonProperty("sifra_valute")
    private String currencyCode;

    @JsonProperty("srednji_tecaj")
    private String averageRate;

    @JsonProperty("valuta")
    private String currency;

}
