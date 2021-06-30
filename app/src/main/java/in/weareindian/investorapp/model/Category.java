package in.weareindian.investorapp.model;

public class Category {
    String images, time;

    public Category() {
    }

    public Category(String images, String time) {
        this.images = images;
        this.time = time;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

