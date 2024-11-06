package implementation;

import model.card.Card;
import model.effect.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EffectParser {
    public Condition parseCondition(@NotNull String effectText) {
        if (effectText.contains("blocked")) {
            return new Condition(ConditionType.BLOCKED);
        }

        if (effectText.contains("digivolution cards")) {
            Pattern pattern = Pattern.compile("for every (\\d+) digivolution cards", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(effectText.toLowerCase());
            if (matcher.find()) {
                int threshold = Integer.parseInt(matcher.group(1));
                return new Condition(ConditionType.DIGIVOLUTION_CARD_COUNT, threshold);
            }
            if (effectText.contains("4 or more digivolution cards")) {
                return new Condition(ConditionType.DIGIVOLUTION_CARD_COUNT, 4);
            }
        }

        if (effectText.contains("no digivolution cards")) {
            return new Condition(ConditionType.DIGIVOLUTION_CARD_COUNT);
        }

        if (effectText.contains("All of your Digimon"))
            return new Condition(ConditionType.DIGIMON_DP, SpecificConditionType.ALL_YOUR_DIGIMON);
        if (effectText.contains("This Digimon"))
            return new Condition(ConditionType.DIGIMON_DP, SpecificConditionType.THIS_DIGIMON);

        // TODO: Need to revise, this one supposedly the Action not Condition
        String regex = "\\b(Delete)\\s+((?:up\\s+to\\s+)?\\d+)\\s+of\\s+your\\s+opponent's\\s+Digimon(?:\\s+with\\s+(\\d+\\s+DP\\s+or\\s+less))?";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int quantity = Integer.parseInt(matcher.group(2));
            String condition = matcher.group(3); // TODO: Need to implement as additional condition such as "4000 DP or less"
        }

        return new Condition(ConditionType.DEFAULT);
    }

    public Action parseAction(@NotNull String effectText) {
        Pattern pattern = Pattern.compile("gain (\\d+) memory", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int threshold = Integer.parseInt(matcher.group(1));
            return new Action(ActionType.GAIN_MEMORY, threshold);
        }

        pattern = Pattern.compile("gain(s)? *[＜<]Security Attack \\+(\\d)[＞>]", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int threshold = Integer.parseInt(matcher.group(2));
            return new Action(ActionType.SECURITY_ATTACK, threshold);
        }

        pattern = Pattern.compile("get(s)? \\+(\\d+) DP", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int threshold = Integer.parseInt(matcher.group(2));
            return new Action(ActionType.INCREASE_DP, threshold);
        }

        // TODO: Need to revise, this one supposedly the Action not Condition
        String regex = "\\b(Delete)\\s+((?:up\\s+to\\s+)?\\d+)\\s+of\\s+your\\s+opponent's\\s+Digimon(?:\\s+with\\s+(\\d+\\s+DP\\s+or\\s+less))?";
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int quantity = Integer.parseInt(matcher.group(2));

            if (quantity == 1)
                return new Action(ActionType.DELETE_ONE_DIGIMON, 1);
            else if (quantity == 2)
                return new Action(ActionType.DELETE_TWO_DIGIMON, 2);
        }

        return new Action(ActionType.DEFAULT);
    }

    public Map<Condition, Action> parseEffect(String effectText) {
        Map<Condition, Action> conditionAction = new HashMap<>();
        conditionAction.put(parseCondition(effectText), parseAction(effectText));
        return conditionAction;
    }

    public Map<Condition, Action> parseEffect(String effectText, Card card) {
        Map<Condition, Action> conditionAction = new HashMap<>();

        Pattern pattern = Pattern.compile("\\b(Activate)\\b.*?(\\[[^\\]]+\\])", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(effectText);
        if (matcher.find())
            conditionAction.put(parseCondition(card.effect), parseAction(card.effect));
        else
            conditionAction.put(new Condition(ConditionType.DEFAULT), new Action(ActionType.DEFAULT));

        return conditionAction;
    }
}
