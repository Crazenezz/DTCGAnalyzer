package implementation;

import implementation.util.Logger;
import lombok.Getter;
import lombok.Setter;
import model.Phase;
import model.Player;
import model.card.Card;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public class GameState {
    private Memory memory;
    private int currentPlayer;
    private boolean endGame;
    private boolean firstTurn;
    private int turn = 0;
    private Logger log;

    public GameState(Memory memory, int currentPlayer) {
        this.memory = memory;
        this.currentPlayer = currentPlayer;
        endGame = false;
        firstTurn = true;
        turn = 0;
        log = new Logger();
    }

    public void fromBattleAreaToTrash(Player player, @NotNull Card card) {
        if (card.previousDigivolution != null) {
            fromBattleAreaToTrash(player, card.previousDigivolution);
            card.previousDigivolution = null;
        }

        player.trash.add(card);
        log.logger(player, card, Phase.MAIN_TRASH, "Battle Area!", "Trash!");
    }
}
