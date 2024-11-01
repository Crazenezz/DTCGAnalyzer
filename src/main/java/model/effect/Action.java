package model.effect;

import implementation.Memory;
import model.card.Card;
import org.jetbrains.annotations.NotNull;

public class Action {

    public void increaseSecurityAttack(@NotNull Card card, int securityAttack) {
        card.setSecurityAttack(securityAttack);
    }

    public void gainMemory(int currentPlayer, Memory memory, int memoryGain) {
        if (currentPlayer == 1)
            memory.adjustMemory(memoryGain);
        else
            memory.adjustMemory(-memoryGain);
    }

    public void increaseDP(@NotNull Card card, int additionalDP) {
        card.setAdditionalDP(additionalDP);
    }
}