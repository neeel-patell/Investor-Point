package in.weareindian.investorapp.model;

public class Home {
    String id, images, blog, time;

    public Home() {
    }

    public Home(String id, String images, String blog, String time) {
        this.id = id;
        this.images = images;
        this.blog = blog;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }
}
