package com.cognizant.ormlearn.model.quiz;

import javax.persistence.*;
import java.util.List;

/**
 * AttemptQuestion entity — maps to the 'attempt_question' table.
 * Links one Attempt to one Question; contains many AttemptOptions.
 * Hands-on 3.
 */
@Entity
@Table(name = "attempt_question")
public class AttemptQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aq_id")
    private int id;

    /** Many attempt questions belong to one attempt. */
    @ManyToOne
    @JoinColumn(name = "aq_at_id")
    private Attempt attempt;

    /**
     * Many attempt questions reference one question.
     * EAGER so question text/score is loaded with the attempt question.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aq_qu_id")
    private Question question;

    /**
     * One attempt question has many attempt options.
     * EAGER so all options are loaded in one fetch.
     */
    @OneToMany(mappedBy = "attemptQuestion", fetch = FetchType.EAGER)
    private List<AttemptOption> attemptOptions;

    public AttemptQuestion() {}

    public int getId()                                          { return id; }
    public void setId(int id)                                   { this.id = id; }
    public Attempt getAttempt()                                 { return attempt; }
    public void setAttempt(Attempt attempt)                     { this.attempt = attempt; }
    public Question getQuestion()                               { return question; }
    public void setQuestion(Question question)                  { this.question = question; }
    public List<AttemptOption> getAttemptOptions()              { return attemptOptions; }
    public void setAttemptOptions(List<AttemptOption> options)  { this.attemptOptions = options; }

    @Override
    public String toString() {
        return "AttemptQuestion{id=" + id + ", question=" + question + "}";
    }
}
