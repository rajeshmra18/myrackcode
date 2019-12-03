package com.example.aravind.models;

public class CartInfos {
    private int tax;
    private String promocode1;
    private String promocode2;
    private String promocode3;

    public CartInfos() {

    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public String getPromocode1() {
        return promocode1;
    }

    public void setPromocode1(String promocode1) {
        this.promocode1 = promocode1;
    }

    public String getPromocode2() {
        return promocode2;
    }

    public void setPromocode2(String promocode2) {
        this.promocode2 = promocode2;
    }

    public String getPromocode3() {
        return promocode3;
    }

    public void setPromocode3(String promocode3) {
        this.promocode3 = promocode3;
    }
}