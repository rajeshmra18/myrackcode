package com.example.aravind.offerlisting.Model;

import android.graphics.Typeface;

public class OfferModel {

    String heading, Details, image, id, startdate, enddate, price, quantity;
    Typeface montserrat_Medium;

    public OfferModel(String id, String heading, String description, String special_price, String quantity, String offer_start_date, String offer_last_date, String image, Typeface montserrat_Medium) {
        this.id = id;
        this.Details = description;
        this.heading = heading;
        this.image = image;
        this.startdate = offer_start_date;
        this.enddate = offer_last_date;
        this.price = special_price;
        this.quantity = quantity;
        this.montserrat_Medium = montserrat_Medium;

    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Typeface getMontserrat_Medium() {
        return montserrat_Medium;
    }

    public void setMontserrat_Medium(Typeface montserrat_Medium) {
        this.montserrat_Medium = montserrat_Medium;
    }
}
