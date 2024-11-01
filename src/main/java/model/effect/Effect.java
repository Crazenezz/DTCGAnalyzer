package model.effect;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Effect {
    private Map<EffectType, List<EffectAction>> allEffects;

    public void addEffect(EffectType type, EffectAction action) {
        allEffects.computeIfAbsent(type, _ -> new ArrayList<>()).add(action);
    }

    public void triggerEffect(EffectType type) {
        if (allEffects.containsKey(type)) {
            if (type == EffectType.YOUR_TURN)
                effectYourTurn(allEffects.get(type));
        }
    }

    public void effectOnPlay() {

    }

    public void effectOnDeletion() {

    }

    public void effectYourTurn(List<EffectAction> effects) {
        // TODO: Get the String effect, after that need to breakdown to Condition and Action
        for (EffectAction action : effects) {

        }
    }
}
