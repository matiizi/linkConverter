package linkProject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String UUID;

    @Column
    private int deletedLinksCount = 0;

    public User() {}

    public User(String UUID) {
        this.UUID = UUID;
    }

    public Long getId() {
        return id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getDeletedLinksCount() {
        return deletedLinksCount;
    }

    public void setDeletedLinksCount(int deletedLinksCount) {
        this.deletedLinksCount = deletedLinksCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", UUID='" + UUID + '\'' +
                '}';
    }
}
