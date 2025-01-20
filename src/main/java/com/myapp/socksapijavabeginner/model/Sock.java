package com.myapp.socksapijavabeginner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sock_table")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Sock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message = "color can not be null")
    private String color;

    @Column
    @NotNull(message = "cottonPercentage can not be null")
    @Min(value = 0, message = "cottonPercentage can not be less 0")
    @Max(value = 100, message = "cottonPercentage can not be more 100")
    private int cottonPercentage;

    @Column
    @NotNull(message = "quantity can not be null")
    @Min(value = 0, message = "quantity can not be less 0")
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "color can not be null") String getColor() {
        return color;
    }

    public void setColor(@NotNull(message = "color can not be null") String color) {
        this.color = color;
    }

    @NotNull(message = "cottonPercentage can not be null")
    @Min(value = 0, message = "cottonPercentage can not be less 0")
    @Max(value = 100, message = "cottonPercentage can not be more 100")
    public int getCottonPercentage() {
        return cottonPercentage;
    }

    public void setCottonPercentage(@NotNull(message = "cottonPercentage can not be null") @Min(value = 0, message = "cottonPercentage can not be less 0") @Max(value = 100, message = "cottonPercentage can not be more 100") int cottonPercentage) {
        this.cottonPercentage = cottonPercentage;
    }

    @NotNull(message = "quantity can not be null")
    @Min(value = 0, message = "quantity can not be less 0")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "quantity can not be null") @Min(value = 0, message = "quantity can not be less 0") int quantity) {
        this.quantity = quantity;
    }
}
