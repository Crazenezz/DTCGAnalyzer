package model.effect;

import model.card.Card;

public class Condition {
    private ConditionType type;
    private int threshold;
    private boolean appliesToOpponent;

    public Condition(ConditionType type, int threshold, boolean appliesToOpponent) {
        this.type = type;
        this.threshold = threshold;
        this.appliesToOpponent = appliesToOpponent;
    }
}
