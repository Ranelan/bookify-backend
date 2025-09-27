package com.booklify.dto;

import com.booklify.domain.Address;

public class AddressDto {

    private Long id;
    private String street;
    private String suburb;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private Long userId;

    public AddressDto() {
    }

    public AddressDto(Long id, String street, String suburb, String city,
                      String province, String country, String postalCode, Long userId) {
        this.id = id;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
        this.userId = userId;
    }


    public static AddressDto fromEntity(Address address) {
        AddressDto dto = new AddressDto();
        dto.id = address.getId();
        dto.street = address.getStreet();
        dto.suburb = address.getSuburb();
        dto.city = address.getCity();
        dto.province = address.getProvince();
        dto.country = address.getCountry();
        dto.postalCode = address.getPostalCode();
        return dto;
    }

    public static Address toEntity(AddressDto dto) {
        return new Address.Builder()
                .setStreet(dto.getStreet())
                .setSuburb(dto.getSuburb())
                .setCity(dto.getCity())
                .setProvince(dto.getProvince())
                .setCountry(dto.getCountry())
                .setPostalCode(dto.getPostalCode())
                .build();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AddressDto{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", suburb='" + suburb + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", userId=" + userId +
                '}';
    }
}