package implementation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameState {
    private Memory memory;
    private int currentPlayer;
    private boolean endGame;
    private boolean firstTurn;
    private int turn = 0;

    public GameState(Memory memory, int currentPlayer) {
        this.memory = memory;
        this.currentPlayer = currentPlayer;
        endGame = false;
        firstTurn = true;
        turn = 0;
    }
}
