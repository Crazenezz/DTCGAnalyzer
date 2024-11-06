package implementation;

import model.effect.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class EffectTest {
    private final String[] conditions = {
        "[Your Turn] For every 2 digivolution cards this Digimon has, it gains ＜Security Attack +1＞ (This Digimon checks 1 additional security card).",
        "[Your Turn] When this Digimon is blocked, gain 3 memory.",
        "[Your Turn] All of your Digimon get +1000 DP.",
        "[Your Turn] This Digimon gets +1000 DP.",
        "[Your Turn] While this Digimon has 4 or more digivolution cards, it gets +1000 DP.",
        "[Your Turn] While your opponent has a Digimon with no digivolution cards, this Digimon gains ＜Security Attack +1＞ (This Digimon checks 1 additional security card).",
        "[Your Turn] This Digimon gets +1000 DP when battling an opponent's Digimon that has no digivolution cards."
    };

    private EffectParser effectParser;

    @BeforeEach
    void setUp() {
        effectParser = new EffectParser();
    }

    @Test
    public void testParseEffectForEveryTwoDigivolutionCardsGainsSecurityAttack() {
        String effectText = conditions[0];
        Map<Condition, Action> result = effectParser.parseEffect(effectText);

        Condition condition = new Condition(ConditionType.DIGIVOLUTION_CARD_COUNT, 2);
        Action action = new Action(ActionType.SECURITY_ATTACK, 1);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }

    @Test
    public void testParseEffectWhenBlockedGainMemory() {
        String effectText = conditions[1];
        Map<Condition, Action> result = effectParser.parseEffect(effectText);

        Condition condition = new Condition(ConditionType.BLOCKED);
        Action action = new Action(ActionType.GAIN_MEMORY, 3);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }

    @Test
    public void testParseEffectAllOfYourDigimonGetDP() {
        String effectText = conditions[2];
        Map<Condition, Action> result = effectParser.parseEffect(effectText);

        Condition condition = new Condition(ConditionType.DIGIMON_DP, SpecificConditionType.ALL_YOUR_DIGIMON);
        Action action = new Action(ActionType.INCREASE_DP, 1000);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }

    @Test
    public void testParseEffectThisDigimonGetsDP() {
        String effectText = conditions[3];
        Map<Condition, Action> result = effectParser.parseEffect(effectText);

        Condition condition = new Condition(ConditionType.DIGIMON_DP, SpecificConditionType.THIS_DIGIMON);
        Action action = new Action(ActionType.INCREASE_DP, 1000);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }

    @Test
    public void testParseEffectWhileThisDigimonHasFourOrMoreDigivolutionCards() {
        String effectText = conditions[4];
        Map<Condition, Action> result = effectParser.parseEffect(effectText);

        Condition condition = new Condition(ConditionType.DIGIVOLUTION_CARD_COUNT, 4);
        Action action = new Action(ActionType.INCREASE_DP, 1000);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }

    @Test
    public void testParseEffectOpponentHasNoDigivolutionCardsThisDigimonGainsSecurityAttack() {
        String effectText = conditions[5];
        Map<Condition, Action> result = effectParser.parseEffect(effectText);

        Condition condition = new Condition(ConditionType.DIGIVOLUTION_CARD_COUNT);
        Action action = new Action(ActionType.SECURITY_ATTACK, 1);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }

    @Test
    public void testParseEffectThisDigimonGetsDPWhenBattlingOpponentWithNoDigivolutionCards() {
        String effectText = conditions[6];
        Map<Condition, Action> result = effectParser.parseEffect(effectText);

        Condition condition = new Condition(ConditionType.DIGIVOLUTION_CARD_COUNT);
        Action action = new Action(ActionType.INCREASE_DP, 1000);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }
}
