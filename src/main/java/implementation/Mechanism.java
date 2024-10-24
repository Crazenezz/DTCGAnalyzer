package implementation;

import model.*;

import java.util.Collections;

import static java.lang.System.exit;

public class Mechanism {
    private final Player player1;
    private final Player player2;
    private int currentPlayer;
    private boolean endGame = false;
    private boolean firstTurn = true;
    private int turn = 0;
    private final Util util;

    public Mechanism(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = 0;
        util = new Util();
    }

    public void drawCards(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.hand.add(player.deck.draw());
            util.logger(player, player.hand.getLast(), Phase.DRAW);
        }
    }



    /**
     * Function to initialize the game, with following steps:
     * 1. Shuffle the deck of both players
     * 2. Shuffle the Digi-Egg deck of both players
     * 3. Determine the starting player
     * 4. Draw initial hands for both players
     * 5. Place security stack for both players
     */
    public void initializeGame() {
        shuffleDeck(player1.deck);
        shuffleDeck(player2.deck);
        determineStartingPlayer();
        drawInitialHands();
        placeSecurityStack(player1);
        placeSecurityStack(player2);
    }

    /**
     * Function to shuffle the deck of a player, with following steps:
     * 1. A deck contains 50 cards
     * 2. Shuffle the deck
     * <p>
     * Additional notes, for Digi-Egg deck, it only contains 5 cards
     */
    public void shuffleDeck(Deck deck) {
        Collections.shuffle(deck.digiEggDeck);
        Collections.shuffle(deck.mainDeck);
    }

    /**
     * Function to determine the starting player
     */
    public void determineStartingPlayer() {
        currentPlayer = Math.random() < 0.5 ? 1 : 2;
        System.out.println("Player " + currentPlayer + " goes first.");
        if (currentPlayer == 1) {
            player1.isTurn = true;
        } else {
            player2.isTurn = true;
        }
    }

    /**
     * Function to draw initial hands for both players
     */
    public void drawInitialHands() {
        drawCards(player1, 5);
        drawCards(player2, 5);
    }

    /**
     * Function to place security stack for a player, with rules:
     * 1. Initial security stack contains 5 cards
     * 2. Draw the cards from the deck
     * 3. First to draw from main deck is the last card in the security stack
     */
    public void placeSecurityStack(Player player) {
        for (int i = 0; i < 5; i++) {
            player.securityStack.push(player.deck.draw());
        }
    }

    // Main Game Flow
    public void mainGameFlow() {
        while (!endGame) {
            if (player1.isTurn)
                executePlayerTurn(player1);
            else
                executePlayerTurn(player2);
        }
    }

    /**
     * Execute player's turn, with following steps:
     * 1. Start of Your Turn Phase
     * 2. Unsuspend Phase
     * 3. Draw Phase
     * 4. Breeding Phase
     * 5. Main Phase
     * 6. End of Your Turn Phase
     * <p>
     * Additional notes:
     * 1. Player's turn is determined by the boolean isTurn in Player class
     * 2. Player's turn is switched at the end of the turn
     * 3. Player's turn is checked at the beginning of the loop
     * 4. Game ends when checkForGameEndConditions() returns true
     * 5. On draw phase, player draws 1 card from the deck, if the deck is empty, player loses
     */
    public void executePlayerTurn(Player player) {
        startTurn(player);
        unsuspendPhase(player);
        drawPhase(player);
        breedingPhase(player);
        mainPhase(player);
        endTurn(player);
    }

    private void startTurn(Player player) {
        /**
         * Implementation for Start of Your Turn Phase
         *
         * Check if there are any effects that trigger at the start of the turn, with following keywords:
         * 1. Perform Rules Processing
         * 2. Start of Your Turn
         * 3. Start of Opponent's Turn
         * 4. Start of All Turns
         * 5. After that, proceed to the next phase, unsuspend phase
         */
        turn++;
        util.logger(player, turn, Phase.START_TURN);


        // Check if any card on battle area, with card_effect [Start of Your Turn], trigger the effect
        if (!player.battleArea.isEmpty()) {
            for (Card card : player.battleArea) {
                if (card.effect.contains("[Start of Your Turn]")) {
                    // TODO: Trigger the effect, how to read and how to trigger
                }
            }
        }
    }

    /**
     * We know as Rules Processing the process by which the rules remove elements that should not exist in the battle area:
     *
     * Send to the trash any Digimon with no DP in the Battle Area.
     * Send to the trash any Option cards in the Battle Area (that were not placed in the Battle Area by an effect specifying to place it in the Battle Area)
     * Deletes all Digimon with 0 DP.
     * Effects still follow the Triggered Effects procedure, Rules Processing is a step within that procedure:
     *
     * An effect has activated.
     * Effects met their Trigger Condition from the previous effect trigger.
     * Rules Processing removes elements that should not exist in the Battle Area.
     * Effects met their Trigger Condition from rules processing trigger.
     * Players then proceed with Triggered Effects procedures again.
     * This can continue indefinitely, until all effects have been triggered and activated.
     *
     */
    private void rulesProcessing() {
        // Implementation for Rules Processing
    }

    /**
     * Pending Activation
     * Pending Activation (発揮待ち Hakki-machi?) is an official term used by the Detailed Rulebook that denotes when an effect is waiting for its player to activate it. An effect that has triggered enters "Pending Activation" status. The status is removed after an effect fails activation or is successfully activated.
     *
     * Priority
     * This is an unofficial term, and used only to illustrate the order of effects in this page.
     *
     * An effect that has met its trigger conditions becomes pending activation with a priority to effects triggered before it. Any effects triggered by the same action or effect are considered to have triggered at the same time and as such have the same priority level assigned. In this page, effects will be assigned a priority number.
     *
     * If a player has an effect with a higher priority, they must activate that effect first. If effects have the same priority, the turn player will activate first, before the opponent activates an effect. If a player has multiple effects with the same priority, the player chooses any of those effects to activate first. The order of effects with the same priority are not determined when they are triggered, and only when a player decides which effect to activate.
     *
     * Procedure
     * 1. All effects that met their trigger condition enter pending activation state with the same priority.
     * 2. Repeat the following until all effects are no longer "Pending Activation".
     *  2.1. The turn player chooses an effect with the highest priority. If the opposing turn player has an effect with a higher priority, the opponent chooses instead.
     *      2.1.1. If the effect is optional, the player may choose to skip the following steps (2.1.2 to 2.1.5) and the effect is no longer "Pending Activation".
     *      2.1.2. Check if the effect meets its activation conditions, if it fails to meet its activation conditions skip the following steps (2.1.3 to 2.1.5) and the effect is no longer marked "Pending Activation".
     *      2.1.3. Effect is activated, and if it has [Once Per Turn] it cannot be activated again during the rest of this turn regardless of whether the actions in Step 5 are carried out or not.
     *      2.1.4. Apply the actions listed in the effect.
     *          2.1.4.1. If an Option was used by the card effect, instantly apply the Option's effect.
     *          2.1.4.2. If an effect is activated by the card effect, instantly apply the actions in that effect.
     *          2.1.4.3. If a Digimon leaves the Battle Area, instantly place its digivolution cards in trash.
     *  2.2. All effects that met their trigger conditions in Step 2.1. are now triggered and become "pending activation" with a priority +1 to the current highest priority.
     *  2.3. Perform Rules Processing:
     *      2.3.1. Do the following simultaneously:
     *          2.3.1.1. Send to the trash any Level 2 Digi-Eggs on the field.
     *          2.3.1.2. Send to the trash any Option cards in the Battle Area (that were not placed in the Battle Area by an effect specifying to place it in the Battle Area)
     *          2.3.1.3. Delete all Digimon with 0 DP on the field.
     *      2.3.2. All effects that met their trigger conditions in Step 3.1. are now triggered and become "pending activation" with a priority +1 to the current highest priority.
     *          2.3.2.1. This includes effects triggered in Step 2.2., if effects triggered at both Step 2.2, and Step 2.3.2. Effects triggered at Step 2.3.2. will have a higher priority than those triggered in Step 2.2.
     * 3. Repeat Step 2 until there are no more effects marked as "Pending Activation".
     */
    private void pendingActivation() {
        // Implementation for Pending Activation

        // Implementation for Priority

        // Implementation for Procedure

    }

    private void unsuspendPhase(Player player) {
        /**
         * Implementation for Unsuspend Phase
         *
         * Unsuspend all suspended Digimon
         */
        player.unsuspendAll();
    }

    private void drawPhase(Player player) {
        /**
         * Implementation for Draw Phase
         *
         * Draw 1 card from the deck
         */

        if (player1.isTurn && player1.deck.isMainDeckEmpty()) {
            System.out.println("Player 1 has no more cards in deck. Player 2 wins!");
            endGame = true;
            exit(0);
        } else if (player2.isTurn && player2.deck.isMainDeckEmpty()) {
            System.out.println("Player 2 has no more cards in deck. Player 1 wins!");
            endGame = true;
            exit(0);
        }

        if (!firstTurn) {
            drawCards(player, 1);
        } else
            firstTurn = false;
    }

    private void breedingPhase(Player player) {
        /**
         * Implementation for Breeding Phase
         * <p>
         * Breeding Phase is the phase where player can hatch the digi-egg
         * <p>
         * 1. Hatch a Digi-Egg
         * 2. Move Digimon to Battle Area
         * 3. Do Nothing (need to set priority, to trigger this event)
         */

        if (player.breedingArea.isEmpty()) {
            player.breedingArea.add(player.deck.digiEggDeck.removeLast());
            util.logger(player, player.breedingArea.getLast(), Phase.BREEDING);
        } else if (player.breedingArea.getLast().level == 2 && player.breedingArea.getLast().nextDigivolution != null) {
            player.battleArea.add(player.breedingArea.removeLast());
            util.logger(player, player.battleArea.getLast(), Phase.BREEDING_MOVE);
        } else {
            util.logger(player, Phase.BREEDING);
        }
    }

    private void mainPhase(Player player) {
        /**
         * Implementation for Main Phase
         *
         * A. Playing Digimon
         * Players can play Digimon cards from their hand to their battle area.
         * First, place the Digimon card you want to play in the battle area unsuspended.
         * Next, pay the play cost of that Digimon card. The Digimon is now played in the battle area.
         * Digimon can't attack on the turn they were played. There's no limit to how many Digimon can be placed in the battle area.
         *
         * B. Digivolving
         * Players can digivolve the Digimon in their battle area or breeding area.
         * Check the digivolution conditions listed on a card in your hand. If you have a Digimon in play that matches the required colour and level, it can digivolve into the Digimon card you have in your hand. If a card has multiple digivolution conditions, a Digimon must only satisfy one of those conditions to digivolve into it.
         * Place the digivolved Digimon card from your hand on top of the card that meets its digivolution conditions. Stack them so that any inherited effects of the card below it are visible. Next, pay the digivolve cost written on the card. Once digivolution is complete, draw 1 card as a digivolution bonus.
         *  - The cards below a digivolved Digimon become digivolution cards.
         *  - The inherited effects of those cards can be used after digivolving with them.
         *  - The digivolution card and digivolved Digimon are treated as a single Digimon.
         *  - If that Digimon is deleted, all of its digivolution cards are placed in the trash.
         *
         *  C. Playing Tamers
         * Players can play Tamer cards from their hand to their battle area. There is no limit to how many Tamers can be placed in the battle area. Tamers can't attack or block.
         *  - Place the Tamer card you want to play in the battle area unsuspended.
         *  - Pay the play cost of the Tamer card. The Tamer is now played to the battle area.
         *
         *  D. Using Option Cards
         * Players can activate the main effect of Option cards from their hand. To use an Option card, you must have at least one Digimon or Tamer of the same colour as the Option card in your battle area or breeding area.
         *
         * E. Attacking
         * Digimon in the battle area can make attacks.
         *  - Suspend an unsuspended Digimon you want to attack with and declare your attack.
         *  - Choose the target of your attack. You can either target one of your opponent's suspended Digimon in the battle area, or the opposing player.
         *  - Any [When Attacking] and "when suspended" effects are activated at this point.
         *
         * Additional Notes on Main Phase:
         * Attacking the Opponent's Digimon
         * - The attacking Digimon and the target Digimon battle each other.
         * - The winner of the battle is determined by which Digimon has the higher DP.
         * - The defeated Digimon is deleted and gets placed in its owner's trash.
         * - If both Digimon have equal DP, the battle is a draw, and both Digimon are deleted.
         *
         * Attacking the Opposing Player
         * - If the opposing player has at least 1 security card in their security stack, flip over their top security card. Flipping a security card face up during an attack is called checking.
         * - If the checked card has a security effect, that effect is activated.
         * - You don't need to pay any memory cost to activate a security effect, and security effects on Option cards ignore normal colour restrictions for Option cards. Proceed after the security effect has been resolved, or if the card has no security effect. Cards are resolved as follows, depending on what type was turned over.
         */

        util.logger(player, Phase.START_MAIN);

        /** TODO:
         * Search from hand for specific case
         *
         * 1. Check if breeding area is level 2, and check if there is level 3 on hand, digivolve on breeding area
         * 2. Check if there is a Tamer on hand, play it directly to battle area
         */
        if (!player.breedingArea.isEmpty() && player.breedingArea.getLast().level == 2) {
            if (!util.getListCardFromLevel(player.hand, 3).isEmpty()) {
                // TODO: Digivolve Digi-Egg to level 3
            }

            // TODO: Play Tamer
            int index = util.getCardFromType(player.hand, CardType.TAMER);
            if (index != -1) {
                player.battleArea.add(player.hand.remove(index));
                util.logger(player, player.battleArea.getLast(), Phase.MAIN_PLAY);
            }
        }
    }

    private void endTurn(Player player) {
        /**
         * Implementation for End of Your Turn Phase
         *
         * Check if there are any effects that trigger at the end of the turn, with following keywords:
         * 1. End of Your Turn
         * 2. End of Opponent's Turn
         * 3. End of All Turns
         * 4. After that, switch the turn to the opponent
         */

        util.logger(player, turn, Phase.END_TURN);
        switchTurn();
    }

    private void switchTurn() {
        if (player1.isTurn) {
            player1.isTurn = false;
            player2.isTurn = true;
        } else {
            player1.isTurn = true;
            player2.isTurn = false;
        }
    }

    /**
     * Function for implementation memory marker,
     *
     * Memory (メモリー Memorī?) is the resource system used by the Digimon Card Game. It is used to pay costs in order to play cards. It is also used in order to determine when a turn will swap over. Both players are meant to know what the memory gauge is currently.
     *
     */
    private void memoryMarker() {
        /**
         * Start of game, memory is set to 0
         *
         * On main phase, refer to card's Play Cost or Digivolve Cost (if digivolving)
         *
         * Both player's refer to same memory gauge
         *
         * Player 1 memory = 1, Player 2 memory = -1
         * When current player's turn and memory = 0, still can play cards
         * If current player's memory < 0, switch turn
         */

        // Will get back to this part later
//        if (player1.isTurn) {
//            if (player1.memory < 0)
//                switchTurn();
//        } else {
//            if (player2.memory < 0)
//                switchTurn();
//        }
    }
}
