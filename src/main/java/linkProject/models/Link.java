package linkProject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String shortUrl;

    @Column
    private String longUrl;

    @Column
    private int clickCount;

    @Column
    private int clickLimit;

    @Column (name = "expiration_time")
    private long expirationTime;

    @Column
    private long userId;

    public Link() {}

    public Link(String shortUrl, String longUrl, int clickCount, int clickLimit, long expirationTime, long userId) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.clickCount = clickCount;
        this.clickLimit = clickLimit;
        this.expirationTime = expirationTime;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public int getClickLimit() {
        return clickLimit;
    }

    public void setClickLimit(int clickLimit) {
        this.clickLimit = clickLimit;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", shortUrl='" + shortUrl + '\'' +
                ", longUrl='" + longUrl + '\'' +
                ", clickCount=" + clickCount +
                ", clickLimit=" + clickLimit +
                ", expirationTime=" + expirationTime +
                ", user_id=" + userId +
                '}';
    }
}
