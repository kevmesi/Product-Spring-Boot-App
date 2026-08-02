package com.project.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name="product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private int id;

    @Column(name="code", unique=true, nullable=false)
    @NotNull(message = "Code must not be null")
    @Pattern(regexp="^[a-zA-Z0-9]{10}", message="Code must be exactly 10 characters long")
    private String code;

    @Column(name="name")
    private String name;

    @Column(name="price_eur", nullable=false)
    @JsonProperty(value="price_eur")
    @NotNull(message = "Price in EUR must not be null")
    @PositiveOrZero(message = "Price in EUR must not be negative")
    private BigDecimal priceEUR;

    @Column(name="price_usd")
    @JsonProperty(value="price_usd")
    @PositiveOrZero
    private BigDecimal priceUSD;

    @Column(name="is_available")
    @JsonProperty(value="is_available")
    private boolean isAvailable;

    // Getters and setters for boolean variables
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
