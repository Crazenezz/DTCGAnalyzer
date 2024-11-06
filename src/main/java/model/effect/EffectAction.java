package model.effect;

import implementation.EffectParser;
import lombok.Getter;

@Getter
public class EffectAction {
    private Condition condition;
    private Action action;
    private EffectParser effectParser;

    public EffectAction(String effectText) {
        effectParser = new EffectParser();

        condition = effectParser.parseCondition(effectText);
        action = effectParser.parseAction(effectText);
    }
}
