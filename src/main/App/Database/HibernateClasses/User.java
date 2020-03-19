package Database.HibernateClasses;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id",unique = true)
    private int id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "salt", nullable = false)
    private String salt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Album> albums = new ArrayList<>();

    public User() {
    }

    public User(String username, String email, String hash, String salt) {
        this.username = username;
        this.email = email;
        this.hash = hash;
        this.salt = salt;
    }


    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }


}
