package com.cognizant.ormlearn.model.quiz;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Attempt entity — maps to the 'attempt' table.
 * One attempt belongs to one User and contains many AttemptQuestions.
 * Hands-on 3.
 */
@Entity
@Table(name = "attempt")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "at_id")
    private int id;

    @Column(name = "at_attempted_on")
    private LocalDateTime attemptedOn;

    /** Many-to-One: many attempts can belong to one user. EAGER by JPA default for ManyToOne. */
    @ManyToOne
    @JoinColumn(name = "at_us_id")
    private User user;

    /**
     * One-to-Many: one attempt has many attempt questions.
     * fetch = EAGER needed so attempt questions load within the HQL fetch chain.
     */
    @OneToMany(mappedBy = "attempt", fetch = FetchType.EAGER)
    private List<AttemptQuestion> attemptQuestions;

    public Attempt() {}

    public int getId()                                          { return id; }
    public void setId(int id)                                   { this.id = id; }
    public LocalDateTime getAttemptedOn()                       { return attemptedOn; }
    public void setAttemptedOn(LocalDateTime at)                { this.attemptedOn = at; }
    public User getUser()                                       { return user; }
    public void setUser(User user)                              { this.user = user; }
    public List<AttemptQuestion> getAttemptQuestions()          { return attemptQuestions; }
    public void setAttemptQuestions(List<AttemptQuestion> aqs)  { this.attemptQuestions = aqs; }

    @Override
    public String toString() {
        return "Attempt{id=" + id + ", user=" + user + ", on=" + attemptedOn + "}";
    }
}
