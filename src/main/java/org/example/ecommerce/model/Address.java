package org.example.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5,message = "Street Name should be atleast 5 characters long")
    private String street;

    @NotBlank
    @Size(min = 5,message = "Building Name should be atleast 5 characters long")
    private String buildingName;

    @NotBlank
    @Size(min = 5,message = "City Name should be atleast 5 characters long")
    private String city;

    @NotBlank
    @Size(min = 5,message = "State Name should be atleast 5 characters long")
    private String state;

    @NotBlank
    @Size(min = 5,message = "Country Name should be atleast 5 characters long")
    private String country;

    @NotBlank
    @Size(min = 5,message = "PinCode should be atleast 5 characters long")
    private String pinCode;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Address() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
