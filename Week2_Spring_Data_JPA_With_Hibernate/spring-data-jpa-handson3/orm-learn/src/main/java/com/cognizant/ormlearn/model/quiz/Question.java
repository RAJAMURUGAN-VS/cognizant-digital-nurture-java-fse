package com.cognizant.ormlearn.model.quiz;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Question entity — maps to the 'question' table.
 * Hands-on 3.
 */
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qu_id")
    private int id;

    @Column(name = "qu_text")
    private String text;

    @Column(name = "qu_score")
    private BigDecimal score;

    /**
     * One question has many options.
     * fetch = EAGER so options are loaded with the question inside HQL fetch chain.
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<Option> options;

    public Question() {}

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getText()                 { return text; }
    public void setText(String text)        { this.text = text; }
    public BigDecimal getScore()            { return score; }
    public void setScore(BigDecimal score)  { this.score = score; }
    public List<Option> getOptions()        { return options; }
    public void setOptions(List<Option> o)  { this.options = o; }

    @Override
    public String toString() {
        return "Question{id=" + id + ", text='" + text + "', score=" + score + "}";
    }
}
