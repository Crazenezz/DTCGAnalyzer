package implementation;

public class Memory {
    private int memory;
    private final int maxMemory = 10;  // Max memory a player can have

    public Memory() {
        this.memory = 0;  // Game starts with 0 memory
    }

    // Adjust memory by a certain amount
    public void adjustMemory(int amount) {
        this.memory += amount;

        // Ensure memory doesn't exceed limits
        if (this.memory > maxMemory) {
            this.memory = maxMemory;
        } else if (this.memory < -maxMemory) {
            this.memory = -maxMemory;
        }
    }

    // Check if memory has crossed to the opponent's side
    public boolean isOpponentTurn() {
        return memory < 0;
    }

    // Get current memory value
    public int getMemory() {
        return memory;
    }

    // Reset memory (at start of new turn)
    public void reset() {
        this.memory = 0;
    }

    @Override
    public String toString() {
        return "Memory: " + memory;
    }
}

