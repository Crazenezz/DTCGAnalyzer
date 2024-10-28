package implementation;

import implementation.util.Logger;
import implementation.util.Util;
import model.Deck;
import model.DigivolutionObject;
import model.Phase;
import model.Player;
import model.card.Card;
import model.card.CardType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

import static java.lang.System.exit;

public class Mechanism {
    private final Player player1;
    private final Player player2;
    private final Util util;
    private final Logger log;
    private int currentPlayer;
    private boolean endGame = false;
    private boolean firstTurn = true;
    private int turn = 0;
    private Memory memory;

    public Mechanism(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = 0;
        util = new Util();
        log = new Logger();
        memory = new Memory();
    }

    public void drawCards(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.hand.add(player.deck.draw());
            log.logger(player, player.hand.getLast(), Phase.DRAW);
        }
    }

    public void initializeGame() {
        shuffleDeck(player1.deck);
        shuffleDeck(player2.deck);
        determineStartingPlayer();
        drawInitialHands();
        placeSecurityStack(player1);
        placeSecurityStack(player2);
    }

    public void shuffleDeck(@NotNull Deck deck) {
        Collections.shuffle(deck.digiEggDeck);
        Collections.shuffle(deck.mainDeck);
    }

    public void determineStartingPlayer() {
        currentPlayer = Math.random() < 0.5 ? 1 : 2;
        System.out.println("Player " + currentPlayer + " goes first.");
        if (currentPlayer == 1) {
            player1.isTurn = true;
        } else {
            player2.isTurn = true;
        }
    }

    public void drawInitialHands() {
        drawCards(player1, 5);
        drawCards(player2, 5);
        System.out.print("\n");
    }

    public void placeSecurityStack(Player player) {
        for (int i = 0; i < 5; i++) {
            player.securityStack.push(player.deck.draw());
        }
    }

    public void mainGameFlow() {
        while (!endGame) {
            if (player1.isTurn)
                executePlayerTurn(player1);
            else
                executePlayerTurn(player2);
        }
    }

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
        log.logger(player, turn, Phase.START_TURN);
        log.logger(player, memory.getMemory());
        log.logger(player.hand);


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
     * <p>
     * Send to the trash any Digimon with no DP in the Battle Area.
     * Send to the trash any Option cards in the Battle Area (that were not placed in the Battle Area by an effect specifying to place it in the Battle Area)
     * Deletes all Digimon with 0 DP.
     * Effects still follow the Triggered Effects procedure, Rules Processing is a step within that procedure:
     * <p>
     * An effect has activated.
     * Effects met their Trigger Condition from the previous effect trigger.
     * Rules Processing removes elements that should not exist in the Battle Area.
     * Effects met their Trigger Condition from rules processing trigger.
     * Players then proceed with Triggered Effects procedures again.
     * This can continue indefinitely, until all effects have been triggered and activated.
     */
    private void rulesProcessing() {
        // Implementation for Rules Processing
    }

    /**
     * Pending Activation
     * Pending Activation (発揮待ち Hakki-machi?) is an official term used by the Detailed Rulebook that denotes when an effect is waiting for its player to activate it. An effect that has triggered enters "Pending Activation" status. The status is removed after an effect fails activation or is successfully activated.
     * <p>
     * Priority
     * This is an unofficial term, and used only to illustrate the order of effects in this page.
     * <p>
     * An effect that has met its trigger conditions becomes pending activation with a priority to effects triggered before it. Any effects triggered by the same action or effect are considered to have triggered at the same time and as such have the same priority level assigned. In this page, effects will be assigned a priority number.
     * <p>
     * If a player has an effect with a higher priority, they must activate that effect first. If effects have the same priority, the turn player will activate first, before the opponent activates an effect. If a player has multiple effects with the same priority, the player chooses any of those effects to activate first. The order of effects with the same priority are not determined when they are triggered, and only when a player decides which effect to activate.
     * <p>
     * Procedure
     * 1. All effects that met their trigger condition enter pending activation state with the same priority.
     * 2. Repeat the following until all effects are no longer "Pending Activation".
     * 2.1. The turn player chooses an effect with the highest priority. If the opposing turn player has an effect with a higher priority, the opponent chooses instead.
     * 2.1.1. If the effect is optional, the player may choose to skip the following steps (2.1.2 to 2.1.5) and the effect is no longer "Pending Activation".
     * 2.1.2. Check if the effect meets its activation conditions, if it fails to meet its activation conditions skip the following steps (2.1.3 to 2.1.5) and the effect is no longer marked "Pending Activation".
     * 2.1.3. Effect is activated, and if it has [Once Per Turn] it cannot be activated again during the rest of this turn regardless of whether the actions in Step 5 are carried out or not.
     * 2.1.4. Apply the actions listed in the effect.
     * 2.1.4.1. If an Option was used by the card effect, instantly apply the Option's effect.
     * 2.1.4.2. If an effect is activated by the card effect, instantly apply the actions in that effect.
     * 2.1.4.3. If a Digimon leaves the Battle Area, instantly place its digivolution cards in trash.
     * 2.2. All effects that met their trigger conditions in Step 2.1. are now triggered and become "pending activation" with a priority +1 to the current highest priority.
     * 2.3. Perform Rules Processing:
     * 2.3.1. Do the following simultaneously:
     * 2.3.1.1. Send to the trash any Level 2 Digi-Eggs on the field.
     * 2.3.1.2. Send to the trash any Option cards in the Battle Area (that were not placed in the Battle Area by an effect specifying to place it in the Battle Area)
     * 2.3.1.3. Delete all Digimon with 0 DP on the field.
     * 2.3.2. All effects that met their trigger conditions in Step 3.1. are now triggered and become "pending activation" with a priority +1 to the current highest priority.
     * 2.3.2.1. This includes effects triggered in Step 2.2., if effects triggered at both Step 2.2, and Step 2.3.2. Effects triggered at Step 2.3.2. will have a higher priority than those triggered in Step 2.2.
     * 3. Repeat Step 2 until there are no more effects marked as "Pending Activation".
     */
    private void pendingActivation() {
        // Implementation for Pending Activation

        // Implementation for Priority

        // Implementation for Procedure

    }

    private void unsuspendPhase(@NotNull Player player) {
        player.unsuspendAll();
    }

    private void drawPhase(Player player) {
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

    private void breedingPhase(@NotNull Player player) {
        if (player.breedingArea.isEmpty() && !player.deck.digiEggDeck.isEmpty()) {
            player.breedingArea.add(player.deck.digiEggDeck.removeLast());
            log.logger(player, player.breedingArea.getLast(), Phase.BREEDING);
        } else if (!player.breedingArea.isEmpty() && player.breedingArea.getLast().level != 2 && player.breedingArea.getLast().previousDigivolution != null) {
            // TODO: Need to set the priority, when to move and when to stay (Skip Breeding Phase)
            player.battleArea.add(player.breedingArea.removeLast());
            log.logger(player, player.battleArea.getLast(), Phase.BREEDING_MOVE);
        } else {
            log.logger(player, Phase.BREEDING);
        }
    }

    /**
     * Implementation List of Action based on General Rules, such as:
     * 1. Skip Turn
     * 2. Digivolve on Breeding Area
     * 3. Digivolve on Battle Area
     * 4. Play Tamer
     * 5. Play Option
     * 6. Play Digimon
     * 7. Attacking with Digimon (on Battle Area) to Security
     * <p>
     * Priority based on first action that match with condition
     * @param player Object Player, to get attributes such as hand, security, deck, area (breeding/battle/trash)
     */
    private void mainPhase(Player player) {
        log.logger(player, Phase.START_MAIN);
        log.logger(player, memory.getMemory());

        // TODO: Need to implementation check from hand to do efficient action

        int index;
        while ((currentPlayer == 1 && memory.getMemory() < 1) || (currentPlayer == 2 && memory.getMemory() > -1)) {
            if (player.hand.isEmpty()) {
                memory.skipTurn(currentPlayer);

                log.logger(player, Phase.SKIP_MAIN);
            }

            // TODO: Move to first priority, if there are any Digimon on Battle Area, next need to implement Summoning Sickness
            index = util.getAttackerDigimon(player.battleArea);
            if (index != -1) {
                // TODO: Implementation Digimon Attack to Security Stack (compare DP if both DIGIMON for the card type)
                Card attackerDigimon = player.battleArea.get(index);
                attackerDigimon.suspend();
                int attackerDP = attackerDigimon.translateDP();

                // TODO: Only 1 check to Security Stack
                Player opponent;
                if (currentPlayer == 1)
                    opponent = player2;
                else
                    opponent = player1;

                try {
                    Card securityCard = opponent.securityStack.pop();
                    log.logger(player, opponent, attackerDigimon, securityCard, Phase.MAIN_ATTACKING);

                    if (securityCard.translateType() == CardType.DIGIMON) {
                        if (attackerDP > securityCard.translateDP()) {
                            opponent.trash.add(securityCard);
                            log.logger(opponent, securityCard, Phase.MAIN_TRASH, "Security Stack!", "Trash!");
                        } else {
                            opponent.trash.add(securityCard);
                            log.logger(opponent, securityCard, Phase.MAIN_TRASH, "Security Stack!", "Trash!");

                            // TODO: Need to breakdown the each of digivolution card to trash
                            fromBattleAreaToTrash(player, player.battleArea.remove(index));
                        }
                    } else if (securityCard.translateType() == CardType.TAMER) {
                        // TODO: Implement [Security Effect] of a Tamer Card (assume, Play Free to Battle Area)
                        opponent.battleArea.add(securityCard);
                        log.logger(opponent, securityCard, Phase.MAIN_TRASH, "Security Stack!", "Battle Area!");
                    } else {
                        // TODO: Implement [Security Effect] of an Option Card
                    }


                    // TODO: Need to trigger [When Attacking] Effect
                } catch (EmptyStackException ex) {
                    System.out.println(opponent.name + " has no more Security Cards in Stack. Direct Attack, " + player.name + " wins!");
                    endGame = true;
                    exit(0);
                }
            }

            if (!player.breedingArea.isEmpty() && player.breedingArea.getLast().level == 2) {
                index = util.getCardFromLevel(player.hand, 3);
                if (index != -1) {
                    player.breedingArea.set(0, digivolve(player, player.breedingArea.getLast(), player.hand.remove(index)));

                    memoryMarker(player.breedingArea.getLast().digivolutions.getFirst().cost);
                    continue;
                }
            } else if (!player.breedingArea.isEmpty() && player.breedingArea.getLast().level == 3) {
                index = util.getCardFromLevel(player.hand, 4);
                if (index != -1) {
                    player.breedingArea.set(0, digivolve(player, player.breedingArea.getLast(), player.hand.remove(index)));

                    memoryMarker(player.breedingArea.getLast().digivolutions.getFirst().cost);
                    continue;
                }
            }

            DigivolutionObject digivolutionObject;
            try {
                // TODO: Need to check what makes IndexOutofBoundsException & IllegalArgumentException
                digivolutionObject = util.digivolveTo(player.hand, player.battleArea);

                if (digivolutionObject.indexTo != -1 && digivolutionObject.indexFrom != -1 && player.battleArea.get(digivolutionObject.indexFrom).translateType() == CardType.DIGIMON && player.hand.get(digivolutionObject.indexFrom).translateType() == CardType.DIGIMON) {
                    player.battleArea.set(digivolutionObject.indexTo, digivolve(player, player.battleArea.get(digivolutionObject.indexFrom), player.hand.remove(digivolutionObject.indexFrom)));

                    memoryMarker(player.battleArea.get(digivolutionObject.indexTo).digivolutions.getFirst().cost);
                    continue;
                }
            } catch (IndexOutOfBoundsException ex) {
                memory.skipTurn(currentPlayer);

                log.logger(player, Phase.SKIP_MAIN);
                continue;
            }


            index = util.getCardFromType(player.hand, CardType.TAMER);
            if (index != -1) {
                player.battleArea.add(player.hand.remove(index));
                log.logger(player, player.battleArea.getLast(), Phase.MAIN_PLAY, "Battle Area!");

                // TODO: Need to trigger [On Play] Effect

                memoryMarker(player.battleArea.getLast().playCost);

                continue;
            }

            index = util.getCardWithLowestLevelCost(player.hand, CardType.DIGIMON);
            if (index != -1) {
                player.battleArea.add(player.hand.remove(index));
                log.logger(player, player.battleArea.getLast(), Phase.MAIN_PLAY, "Battle Area!");

                // TODO: Need to trigger [On Play] Effect

                memoryMarker(player.battleArea.getLast().playCost);

                continue;
            }

            index = util.getCardWithLowestLevelCost(player.hand, CardType.OPTION);
            if (index != -1) {
                player.battleArea.add(player.hand.remove(index));
                log.logger(player, player.battleArea.getLast(), Phase.MAIN_PLAY, "Battle Area!");

                // TODO: Need to trigger [On Play] Effect

                memoryMarker(player.battleArea.getLast().playCost);

                continue;
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

        log.logger(player.hand);
        log.logger(player, turn, Phase.END_TURN);
        switchTurn();
    }

    private void switchTurn() {
        if (player1.isTurn) {
            player1.isTurn = false;
            player2.isTurn = true;

            currentPlayer = 2;
        } else {
            player1.isTurn = true;
            player2.isTurn = false;

            currentPlayer = 1;
        }
    }

    private void memoryMarker(int memoryCost) {
        if (currentPlayer == 1)
            memory.adjustMemory(memoryCost);
        else
            memory.adjustMemory(-memoryCost);
    }

    @NotNull
    @Contract("_, _, _ -> param3")
    private Card digivolve(@NotNull Player player, Card digivolution, @NotNull Card digimon) {
        digimon.previousDigivolution = digivolution;

        log.logger(player, digimon, Phase.MAIN_DIGIVOLVE);

        drawCards(player, 1);
        return digimon;
    }

    private void attack(Player player, List<Card> security) {

    }

    private void fromBattleAreaToTrash(Player player, Card card) {
        if (card.previousDigivolution != null)
            fromBattleAreaToTrash(player, card.previousDigivolution);

        player.trash.add(card);
        log.logger(player, card, Phase.MAIN_TRASH, "Battle Area!", "Trash!");
        card.previousDigivolution = null;
    }
}
