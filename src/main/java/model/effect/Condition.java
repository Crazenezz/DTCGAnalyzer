package model.effect;

import lombok.Getter;
import model.Player;
import model.card.Card;

import java.util.Objects;

@Getter
public class Condition {
    private ConditionType type;
    private SpecificConditionType specific;
    private int threshold;

    public Condition(ConditionType type) {
        this.type = type;
        this.threshold = 0;
    }

    public Condition(ConditionType type, int threshold) {
        this.type = type;
        this.threshold = threshold;
    }

    public Condition(ConditionType type, SpecificConditionType specific) {
        this.type = type;
        this.specific = specific;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return threshold == condition.threshold &&
                type == condition.type &&
                specific == condition.specific;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, specific, threshold);
    }

    @Override
    public String toString() {
        switch (type) {
            case ConditionType.DIGIVOLUTION_CARD_COUNT -> {
                return "For each " + threshold + " digivolution card(s) in this Digimon, DIGIVOLUTION_CARD_COUNT";
            }
            case ConditionType.DIGIMON_DP -> {
                if (specific == SpecificConditionType.ALL_YOUR_DIGIMON)
                    return "For All Digimon on Battle Area, DIGIMON_DP & ALL_YOUR_DIGIMON";
                else if (specific == SpecificConditionType.THIS_DIGIMON)
                    return "For this Digimon on Battle Area, DIGIMON_DP & THIS_DIGIMON";
            }
        }
        return super.toString();
    }

    public boolean evaluateCondition(Player player, Card card) {
        switch (type) {
            case ConditionType.DIGIVOLUTION_CARD_COUNT -> {
                return card.digivolutionCount() == threshold;
            }
            case ConditionType.DIGIMON_DP -> {
                return specific == SpecificConditionType.ALL_YOUR_DIGIMON || specific == SpecificConditionType.THIS_DIGIMON;
            }
        }
        return false;
    }
}
