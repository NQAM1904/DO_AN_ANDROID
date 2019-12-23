package vn.example.do_an.model;

import java.io.Serializable;

public class Products implements Serializable {

    private String image;
    private String imageUrl;
    private String nameProduct;
    private String content;
    private String loai;
    private int gia;
    private boolean isChoose;
    private String formatCode;
    public String base64;
    public String username;
    public boolean chon;

    public Products() {

    }

    public Products(String image,String imageUrl, String nameProduct, String content, String loai, int gia, boolean isChoose, String formatCode) {

        this.image = image;
        this.imageUrl = imageUrl;
        this.nameProduct = nameProduct;
        this.content = content;
        this.loai = loai;
        this.gia = gia;
        this.isChoose = isChoose;
        this.formatCode = formatCode;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }
    public boolean isChon() {
        return chon;
    }

    public void setChon(boolean chon) {
        this.chon = chon;
    }


}
