package com.daily.news.location;

import java.io.Serializable;

/**
 * ip 定位 返回的数据
 *
 * @author a_liYa
 * @date 2017/9/4 10:40.
 */
public class DataLocation implements Serializable {

    private String location;
    private Address address;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static class Address implements Serializable{
        private String country;
        private String province;
        private String city;


        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

}
