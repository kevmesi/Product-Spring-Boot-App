package com.project.demo.share;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;

@Data
public class Currency {

    @SerializedName("broj_tecajnice")
    private String exchangeRateCode;
    @SerializedName("datum_primjene")
    private Date dateFrom;
    @SerializedName("drzava")
    private String country;
    @SerializedName("drzava_iso")
    private String countryCode;
    @SerializedName("kupovni_tecaj")
    private String buyingRate;
    @SerializedName("prodajni_tecaj")
    private String sellingRate;
    @SerializedName("sifra_valute")
    private String currencyCode;
    @SerializedName("srednji_tecaj")
    private String averageRate;
    @SerializedName("valuta")
    private String currency;

}
