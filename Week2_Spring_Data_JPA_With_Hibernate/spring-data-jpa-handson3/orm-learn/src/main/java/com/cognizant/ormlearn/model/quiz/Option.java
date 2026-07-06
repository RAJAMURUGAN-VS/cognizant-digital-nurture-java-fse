package com.cognizant.ormlearn.model.quiz;

import javax.persistence.*;

/**
 * Option entity — maps to the 'options' table.
 * Each option belongs to one Question; isCorrect flags the correct answer.
 * Hands-on 3.
 */
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "op_id")
    private int id;

    @Column(name = "op_text")
    private String text;

    @Column(name = "op_is_correct")
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "op_qu_id")
    private Question question;

    public Option() {}

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getText()                 { return text; }
    public void setText(String text)        { this.text = text; }
    public boolean isCorrect()              { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
    public Question getQuestion()           { return question; }
    public void setQuestion(Question q)     { this.question = q; }

    @Override
    public String toString() {
        return "Option{id=" + id + ", text='" + text + "', correct=" + correct + "}";
    }
}
