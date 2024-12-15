package pl.edu.agh.to.cinemanager.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "screening_type")
public class ScreeningType {

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 32)
    private String name;
    private BigDecimal basePrice;
    private BigDecimal discountPrice;

    public ScreeningType() {}

    public ScreeningType(String name, BigDecimal basePrice, BigDecimal discountPrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.discountPrice = discountPrice;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }
}
