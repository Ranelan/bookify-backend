package com.booklify.domain;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class Address{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = true)
    protected String street ;

    @Column(nullable = true)
    protected String suburb;

    @Column(nullable = false)
    protected String city;

    @Column(nullable = false)
    protected String province;

    @Column(nullable = false)
    protected String country ;

    @Column(nullable = false)
    protected String postalCode;

    @OneToOne
    private RegularUser user;

    @OneToMany
    @JoinColumn(name = "shipping_address_id")
    private List<Order> orders;


    protected Address(){

    }


    private Address(Builder builder){
        this.street = builder.street;
        this.suburb= builder.suburb;
        this.city=builder.city;
        this.province=builder.province;
        this.country=builder.country;
        this.postalCode=builder.postalCode;
        this.user = builder.user;
        this.orders = builder.orders;

    }

    public Address(String street, String city,String suburb,String province,String country,String postalCode, RegularUser user, List<Order> orders) {
        this.street = street;
        this.suburb= suburb;
        this.city=city;
        this.province=province;
        this.country=country;
        this.postalCode=postalCode;
        this.user = user;
        this.orders = orders;
    }



    public String getStreet() {
        return street;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public RegularUser getUser() {
        return user;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", suburb='" + suburb + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", Country='" + country + '\'' +
                ", postalCode=" + postalCode +
                '}';
    }
    public static class Builder {
        protected String street ;
        protected String suburb;
        protected String province;
        protected String city;
        protected String country ;
        protected String postalCode;
        protected RegularUser user;
        protected List<Order> orders;


        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }
        public Builder setSuburb(String suburb) {
            this.suburb = suburb;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setProvince(String province) {
            this.province = province;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setUser(RegularUser user) {
            this.user = user;
            return this;
        }

        public Builder setOrder(List<Order> orders) {
            this.orders = orders;
            return this;
        }

        public Builder copy(Address address){
            this.street = address.street;
            this.suburb= address.suburb;
            this.city=address.city;
            this.province=address.province;
            this.country=address.country;
            this.postalCode= address.postalCode;
            this.orders= address.orders;
            this.user= address.user;
            return this;

        }

        public Address build(){return new Address(this);}

    }
}
