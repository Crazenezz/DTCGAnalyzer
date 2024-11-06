package implementation;

import lombok.Getter;

@Getter
public class Memory {

    // TODO: -10 till 1 is Player 1, 1 till 10 is Player 2, 0 is based on who's turn
    private int currentMemory;
    private final int left = -10;
    private final int right = 10;

    public Memory() {
        this.currentMemory = 0;
    }

    public void adjustMemory(int amount) {
        this.currentMemory += amount;
    }

    public void skipTurn(int currentPlayer) {
        if (currentPlayer == 1)
            this.currentMemory = 3;
        else
            this.currentMemory = -3;
    }

    public int remainingMemory(int amount, int currentPlayer) {
        if (currentPlayer == 1)
            return right - amount;
        else
            return left + amount;
    }
}

