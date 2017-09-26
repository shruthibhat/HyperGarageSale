package com.example.shruthi.hypergaragesale;

/**
 * Created by shruthi on 10/17/16.
 */

public class Items {

    String _item_name;
    double _price;
    String _desc;
    String _imagePath;
    String _latitude;
    String _longitude;
    String _location;

    public Items() {

    }
//class was updated to support imagepath
    public Items(String item_name, double price, String desc, String imagePath) {

        this._item_name = item_name;
        this._price = price;
        this._desc = desc;
        this._imagePath=imagePath;
    }

    public Items(String item_name, double price, String desc, String imagePath,String latitude,String longitude,String loc) {

        this._item_name = item_name;
        this._price = price;
        this._desc = desc;
        this._imagePath=imagePath;
        this._latitude=latitude;
        this._longitude=longitude;
        this._location=loc;
    }

    public String get_item_name() {
        return _item_name;
    }

    public void set_item_name(String _item_name) {
        this._item_name = _item_name;
    }

    public double get_price() {
        return _price;
    }

    public void set_price(double _price) {
        this._price = _price;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public String get_imagePath() {return _imagePath;}

    public void set_imagePath(String _imagePath) {this._imagePath = _imagePath;}

    public String get_latitude() {return _latitude;}

    public void set_latitude(String _latitude) {this._latitude = _latitude;}

    public String get_longitude() {return _longitude;}

    public void set_longitude(String _longitude) {this._longitude = _longitude;}

    public String get_location() {return _location;}

    public void set_location(String _location) {this._location = _location;}
}
