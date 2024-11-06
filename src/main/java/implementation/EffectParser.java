package implementation;

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
            Pattern pattern = Pattern.compile("for every (\\d+) digivolution cards");
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
        return null;
    }

    public Action parseAction(@NotNull String effectText) {
        Pattern pattern = Pattern.compile("gain (\\d+) memory");
        Matcher matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int threshold = Integer.parseInt(matcher.group(1));
            return new Action(ActionType.GAIN_MEMORY, threshold);
        }

        pattern = Pattern.compile("gain(s)? *[＜<]Security Attack \\+(\\d)[＞>]");
        matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int threshold = Integer.parseInt(matcher.group(2));
            return new Action(ActionType.SECURITY_ATTACK, threshold);
        }

        pattern = Pattern.compile("get(s)? \\+(\\d+) DP");
        matcher = pattern.matcher(effectText);
        if (matcher.find()) {
            int threshold = Integer.parseInt(matcher.group(2));
            return new Action(ActionType.INCREASE_DP, threshold);
        }
        return null;
    }

    public Map<Condition, Action> parseEffect(String effectText) {
        Map<Condition, Action> conditionAction = new HashMap<>();
        conditionAction.put(parseCondition(effectText), parseAction(effectText));
        return conditionAction;
    }
}
