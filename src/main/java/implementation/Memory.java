package implementation;

import lombok.Getter;

@Getter
public class Memory {
    // Get current memory value
    private int memory;

    public Memory() {
        this.memory = 0;  // Game starts with 0 memory
    }

    // Adjust memory by a certain amount
    public void adjustMemory(int amount) {
        this.memory += amount;

        // Ensure memory doesn't exceed limits
        // Max memory a player can have
        int maxMemory = 10;
        if (this.memory > maxMemory) {
            this.memory = maxMemory;
        } else if (this.memory < -maxMemory) {
            this.memory = -maxMemory;
        }
    }

    // Reset memory (at start of new turn)
    public void reset() {
        this.memory = 0;
    }
}

