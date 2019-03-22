package com.ruirong.chefang.bean;

import java.util.List;

/**
 * Created by BX on 2018/3/1.
 */

public class ReserveTitleBean {


    /**
     * hotel : {"id":"50","dao_time":["18:00-23:00","23:00-7:00"],"description":"房间整晚保留，06:00前到店可能需要等房"}
     * house : {"id":"1","name":"豪华主题房","specif":["上网","WiFi和宽带","卫浴独立"],"num":"1","price":"288","cover":"/Uploads/Picture/2018-04-10/5acc73428fa01.png"}
     * username : 福粉4233
     * mobile : 15300266275
     */

    private HotelBean hotel;
    private HouseBean house;
    private String username;
    private String mobile;

    public HotelBean getHotel() {
        return hotel;
    }

    public void setHotel(HotelBean hotel) {
        this.hotel = hotel;
    }

    public HouseBean getHouse() {
        return house;
    }

    public void setHouse(HouseBean house) {
        this.house = house;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public static class HotelBean {
        /**
         * id : 50
         * dao_time : ["18:00-23:00","23:00-7:00"]
         * description : 房间整晚保留，06:00前到店可能需要等房
         */

        private String id;
        private String description;
        private List<String> dao_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getDao_time() {
            return dao_time;
        }

        public void setDao_time(List<String> dao_time) {
            this.dao_time = dao_time;
        }
    }

    public static class HouseBean {
        /**
         * id : 1
         * name : 豪华主题房
         * specif : ["上网","WiFi和宽带","卫浴独立"]
         * num : 1
         * price : 288
         * cover : /Uploads/Picture/2018-04-10/5acc73428fa01.png
         */

        private String id;
        private String name;
        private String num;
        private String price;
        private String cover;
        private List<String> specif;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public List<String> getSpecif() {
            return specif;
        }

        public void setSpecif(List<String> specif) {
            this.specif = specif;
        }
    }
}
