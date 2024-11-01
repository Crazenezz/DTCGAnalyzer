package implementation;

import lombok.Getter;

@Getter
public class Memory {

    // TODO: -10 till 1 is Player 1, 1 till 10 is Player 2, 0 is based on who's turn
    private int memory;
    private final int left = -10;
    private final int right = 10;

    public Memory() {
        this.memory = 0;
    }

    public void adjustMemory(int amount) {
        this.memory += amount;
    }

    public void skipTurn(int currentPlayer) {
        if (currentPlayer == 1)
            this.memory = 3;
        else
            this.memory = -3;
    }
}

