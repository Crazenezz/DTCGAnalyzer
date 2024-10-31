package model.effect;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Effect {
    private Map<EffectType, List<EffectAction>> effects;

    public void addEffect(EffectType type, EffectAction action) {
        effects.computeIfAbsent(type, _ -> new ArrayList<>()).add(action);
    }

    public void triggerEffect(EffectType type) {
        if (effects.containsKey(type)) {
            for (EffectAction action : effects.get(type)) {
                action.execute();
            }
        }
    }

    public void effectOnPlay() {

    }

    public void effectOnDeletion() {

    }

    public void effectYourTurn() {

    }
}
