package model.effect;

import implementation.GameState;
import implementation.Memory;
import lombok.Getter;
import model.Player;
import model.card.Card;
import model.card.CardType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
public class Action {
    private ActionType type;
    private int threshold;

    public Action(ActionType type) {
        this.type = type;
        this.threshold = 0;
    }

    public Action(ActionType type, int threshold) {
        this.type = type;
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return threshold == action.threshold &&
                type == action.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, threshold);
    }

    @Override
    public String toString() {
        switch (type) {
            case ActionType.INCREASE_DP -> {
                return "Increase +" + threshold + " DP, INCREASE_DP";
            }
            case ActionType.SECURITY_ATTACK -> {
                return "Increase +" + threshold + " <Security Attack>, SECURITY_ATTACK";
            }
            case ActionType.GAIN_MEMORY -> {
                return "Get " + threshold + " memory";
            }
        }
        return super.toString();
    }

    public void executeAction(Player player, GameState gameState) {

    }

    public void executeAction(Player player, Card card, Condition condition, GameState gameState) {
        switch (type) {
            case ActionType.INCREASE_DP -> {
                if (condition.getSpecific() == SpecificConditionType.ALL_YOUR_DIGIMON) {
                    for (Card cardOnBA : player.battleArea) {
                        if (cardOnBA.getCardType() == CardType.DIGIMON)
                            increaseDP(cardOnBA, threshold);
                    }
                } else {
                    increaseDP(card, threshold);
                }
            }
            case ActionType.SECURITY_ATTACK -> increaseSecurityAttack(card, threshold);
            case ActionType.GAIN_MEMORY -> gainMemory(gameState.getCurrentPlayer(), gameState.getMemory(), threshold);
            case ActionType.DELETE_ONE_DIGIMON -> deleteOpponentDigimon(player, threshold, gameState);
        }
    }

    private void increaseSecurityAttack(@NotNull Card card, int securityAttack) {
        card.setSecurityAttack(securityAttack);
    }

    private void gainMemory(int currentPlayer, Memory memory, int memoryGain) {
        if (currentPlayer == 1)
            memory.adjustMemory(memoryGain);
        else
            memory.adjustMemory(-memoryGain);
    }

    private void increaseDP(@NotNull Card card, int additionalDP) {
        card.setAdditionalDP(additionalDP);
    }

    private void deleteOpponentDigimon(@NotNull Player player, int totalDigimon, GameState gameState) {
        int index = 0;
        for (Card card : player.battleArea) {
            if (card.getCardType() == CardType.DIGIMON) {
                gameState.fromBattleAreaToTrash(player, player.battleArea.get(index));
                player.battleArea.remove(index);

                if (totalDigimon == 0)
                    break;
                else
                    totalDigimon--;
            }
            index++;
        }
    }
}