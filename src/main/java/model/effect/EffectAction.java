package model.effect;

public interface EffectAction extends Condition, Action {
    void execute();
}
