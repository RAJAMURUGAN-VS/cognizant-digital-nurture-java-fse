package com.cognizant.ormlearn.model.quiz;

import javax.persistence.*;
import java.util.List;

/**
 * User entity — maps to the 'user' table.
 * Hands-on 3: Quiz attempt details.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "us_id")
    private int id;

    @Column(name = "us_username")
    private String username;

    @Column(name = "us_email")
    private String email;

    /** One user can have many attempts. */
    @OneToMany(mappedBy = "user")
    private List<Attempt> attempts;

    public User() {}

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public String getUsername()                 { return username; }
    public void setUsername(String username)    { this.username = username; }
    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }
    public List<Attempt> getAttempts()          { return attempts; }
    public void setAttempts(List<Attempt> a)    { this.attempts = a; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}
