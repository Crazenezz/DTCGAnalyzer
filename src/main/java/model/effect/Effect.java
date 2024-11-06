package model.effect;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Effect {
    private Map<EffectType, List<EffectAction>> allEffects;

    public void registerEffect(EffectType type, EffectAction action) {
        allEffects.computeIfAbsent(type, _ -> new ArrayList<>()).add(action);
    }

    public void unregisterEffect(EffectType type, EffectAction action) {
        allEffects.remove(type, action);
    }

    public void triggerEffect(EffectType type) {
        if (allEffects.containsKey(type)) {
            if (type == EffectType.YOUR_TURN)
                effectYourTurn(allEffects.get(EffectType.YOUR_TURN));
        }
    }

    public void effectOnPlay() {

    }

    public void effectOnDeletion() {

    }

    public void effectYourTurn(@NotNull List<EffectAction> effects) {
        // TODO: Get the String effect, after that need to breakdown to Condition and Action
        for (EffectAction effectAction : effects) {

        }
    }
}
