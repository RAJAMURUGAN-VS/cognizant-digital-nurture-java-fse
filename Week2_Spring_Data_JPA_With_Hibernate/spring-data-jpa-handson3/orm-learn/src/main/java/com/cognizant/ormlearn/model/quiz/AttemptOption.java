package com.cognizant.ormlearn.model.quiz;

import javax.persistence.*;

/**
 * AttemptOption entity — maps to the 'attempt_option' table.
 * Records which option was presented and whether the user selected it.
 * Hands-on 3.
 */
@Entity
@Table(name = "attempt_option")
public class AttemptOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ao_id")
    private int id;

    /**
     * Many attempt options belong to one attempt question.
     */
    @ManyToOne
    @JoinColumn(name = "ao_aq_id")
    private AttemptQuestion attemptQuestion;

    /**
     * Many attempt options reference one option.
     * EAGER so option text and isCorrect flag are loaded immediately.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ao_op_id")
    private Option option;

    /** Whether the user selected this option. */
    @Column(name = "ao_selected")
    private boolean selected;

    public AttemptOption() {}

    public int getId()                                          { return id; }
    public void setId(int id)                                   { this.id = id; }
    public AttemptQuestion getAttemptQuestion()                 { return attemptQuestion; }
    public void setAttemptQuestion(AttemptQuestion aq)          { this.attemptQuestion = aq; }
    public Option getOption()                                   { return option; }
    public void setOption(Option option)                        { this.option = option; }
    public boolean isSelected()                                 { return selected; }
    public void setSelected(boolean selected)                   { this.selected = selected; }

    @Override
    public String toString() {
        return "AttemptOption{option=" + option + ", selected=" + selected + "}";
    }
}
