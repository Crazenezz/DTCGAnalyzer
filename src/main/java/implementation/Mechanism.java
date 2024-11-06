package implementation;

import implementation.util.Logger;
import implementation.util.Util;
import model.Deck;
import model.DigivolutionObject;
import model.Phase;
import model.Player;
import model.card.Card;
import model.card.CardType;
import model.effect.Action;
import model.effect.Condition;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Map;

import static java.lang.System.exit;

public class Mechanism {
    private final Player player1;
    private final Player player2;
    private final Util util;
    private final Logger log;
    private int turn = 0;
    private EffectParser effectParser;
    private GameState gameState;

    public Mechanism(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        util = new Util();
        log = new Logger();
        Memory memory = new Memory();
        effectParser = new EffectParser();
        gameState = new GameState(memory, 0);
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
        gameState.setCurrentPlayer(Math.random() < 0.5 ? 1 : 2);
        System.out.println("Player " + gameState.getCurrentPlayer() + " goes first.");
        if (gameState.getCurrentPlayer() == 1) {
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
        while (!gameState.isEndGame()) {
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
        turn++;
        log.logger(player, turn, Phase.START_TURN);
        log.logger(player, gameState.getMemory().getCurrentMemory());
        log.logger(player.hand, "Hand");
        log.logger(player.battleArea, "Battle Area");

        // Check if any card on battle area, with card_effect [Start of Your Turn], trigger the effect
        if (!player.battleArea.isEmpty()) {
            for (Card card : player.battleArea) {
                Map<Condition, Action> effects = effectParser.parseEffect(card.effect);
                for (Map.Entry<Condition, Action> entry : effects.entrySet()) {
                    Condition condition = entry.getKey();
                    Action action = entry.getValue();

                    if (condition == null || action == null)
                        continue;

                    if (condition.evaluateCondition(player, card)) {  // Checks if the condition is met
                        action.executeAction(player, card, condition, gameState);            // Executes the action if condition is true
                        log.logger(condition, action, card, Phase.START_TURN);
                    }
                }
            }
        }
    }

    private void unsuspendPhase(@NotNull Player player) {
        player.unsuspendAll();
    }

    private void drawPhase(Player player) {
        if (player1.isTurn && player1.deck.isMainDeckEmpty()) {
            System.out.println("Player 1 has no more cards in deck. Player 2 wins!");
            gameState.setEndGame(true);
            exit(0);
        } else if (player2.isTurn && player2.deck.isMainDeckEmpty()) {
            System.out.println("Player 2 has no more cards in deck. Player 1 wins!");
            gameState.setEndGame(true);
            exit(0);
        }

        if (!gameState.isFirstTurn()) {
            drawCards(player, 1);
        } else
            gameState.setFirstTurn(false);
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
        log.logger(player, gameState.getMemory().getCurrentMemory());

        // TODO: Need to implementation check from hand to do efficient action

        int index;
        while ((gameState.getCurrentPlayer() == 1 && gameState.getMemory().getCurrentMemory() < 1) || (gameState.getCurrentPlayer() == 2 && gameState.getMemory().getCurrentMemory() > -1)) {
            if (player.hand.isEmpty()) {
                gameState.getMemory().skipTurn(gameState.getCurrentPlayer());

                log.logger(player, Phase.SKIP_MAIN);
            }

            // TODO: Move to first priority, if there are any Digimon on Battle Area, next need to implement Summoning Sickness
            index = util.getAttackerDigimon(player.battleArea);
            if (index != -1) {
                // TODO: Implementation Digimon Attack to Security Stack (compare DP if both DIGIMON for the card type)
                Card attackerDigimon = player.battleArea.get(index);
                attackerDigimon.suspend();
                int attackerDP = attackerDigimon.getDigimonPower();

                // TODO: Only 1 check to Security Stack
                Player opponent;
                if (gameState.getCurrentPlayer() == 1)
                    opponent = player2;
                else
                    opponent = player1;

                try {
                    Card securityCard = opponent.securityStack.pop();
                    log.logger(player, opponent, attackerDigimon, securityCard, Phase.MAIN_ATTACKING);

                    if (securityCard.getCardType() == CardType.DIGIMON) {
                        if (attackerDP > securityCard.getDigimonPower()) {
                            opponent.trash.add(securityCard);
                            log.logger(opponent, securityCard, Phase.MAIN_TRASH, "Security Stack!", "Trash!");
                        } else {
                            opponent.trash.add(securityCard);
                            log.logger(opponent, securityCard, Phase.MAIN_TRASH, "Security Stack!", "Trash!");

                            // TODO: Need to breakdown the each of digivolution card to trash
                            fromBattleAreaToTrash(player, player.battleArea.get(index));
                            player.battleArea.remove(index);
                        }
                    } else if (securityCard.getCardType() == CardType.TAMER) {
                        // TODO: Implement [Security Effect] of a Tamer Card (assume, Play Free to Battle Area)
                        opponent.battleArea.add(securityCard);
                        log.logger(opponent, securityCard, Phase.MAIN_TRASH, "Security Stack!", "Battle Area!");
                    } else {
                        // TODO: Implement [Security Effect] of an Option Card
                    }


                    // TODO: Need to trigger [When Attacking] Effect
                } catch (EmptyStackException ex) {
                    System.out.println(opponent.name + " has no more Security Cards in Stack. Direct Attack, " + player.name + " wins!");
                    gameState.setEndGame(true);
                    exit(0);
                }

                continue;
            }

            if (!player.breedingArea.isEmpty() && player.breedingArea.getLast().level >= 2) {
                index = util.getCardFromLevel(player.hand, player.breedingArea.getLast().level + 1);
                if (index != -1) {

                    player.breedingArea.set(0, digivolve(player, player.breedingArea.getLast(), player.hand.get(index)));
                    player.hand.remove(index);
                    memoryMarker(player, player.breedingArea.getLast().digivolutions.getFirst().cost);
                    continue;
                }
            }

            DigivolutionObject digivolutionObject = util.digivolveTo(player.hand, player.battleArea);

            if (digivolutionObject != null) {
                if (digivolutionObject.indexTo != -1 &&
                        digivolutionObject.indexFrom != -1 &&
                        digivolutionObject.indexFrom < player.battleArea.size() &&
                        digivolutionObject.indexTo < player.hand.size() &&
                        player.battleArea.get(digivolutionObject.indexFrom).getCardType() == CardType.DIGIMON &&
                        player.hand.get(digivolutionObject.indexTo).getCardType() == CardType.DIGIMON) {

                    player.battleArea.set(digivolutionObject.indexFrom,
                            digivolve(player,
                                    player.battleArea.get(digivolutionObject.indexFrom),
                                    player.hand.get(digivolutionObject.indexTo)));
                    player.hand.remove(digivolutionObject.indexTo);

                    memoryMarker(player, player.battleArea.get(digivolutionObject.indexFrom).digivolutions.getFirst().cost);
                    continue;
                }
            }

            index = util.getCardFromType(player.hand, CardType.TAMER);
            if (index != -1) {
                player.battleArea.add(player.hand.get(index));
                player.hand.remove(index);
                log.logger(player, player.battleArea.getLast(), Phase.MAIN_PLAY, "Battle Area!");

                // TODO: Need to trigger [On Play] Effect

                memoryMarker(player, player.battleArea.getLast().playCost);

                continue;
            }

            index = util.getCardWithLowestLevelCost(player.hand, gameState.getMemory(), gameState.getCurrentPlayer());
            if (index != -1) {
                player.battleArea.add(player.hand.get(index));
                player.hand.remove(index);
                log.logger(player, player.battleArea.getLast(), Phase.MAIN_PLAY, "Battle Area!");

                // TODO: Need to trigger [On Play] Effect

                memoryMarker(player, player.battleArea.getLast().playCost);
            } else {
                gameState.getMemory().skipTurn(gameState.getCurrentPlayer());
                log.logger(player, Phase.SKIP_MAIN);
            }
        }
    }

    private void endTurn(@NotNull Player player) {
        log.logger(player.hand, "Hand");
        log.logger(player.battleArea, "Battle Area");
        log.logger(player.trash, "Trash");
        log.logger(player, turn, Phase.END_TURN);

        for (Card card : player.battleArea) {
            card.resetAdditionalAttribute();
        }

        switchTurn();
    }

    private void switchTurn() {
        if (player1.isTurn) {
            player1.isTurn = false;
            player2.isTurn = true;

            gameState.setCurrentPlayer(2);
        } else {
            player1.isTurn = true;
            player2.isTurn = false;

            gameState.setCurrentPlayer(1);
        }
    }

    private void memoryMarker(Player player, int memoryCost) {
        if (gameState.getCurrentPlayer() == 1)
            gameState.getMemory().adjustMemory(memoryCost);
        else
            gameState.getMemory().adjustMemory(-memoryCost);

        log.logger(player, gameState.getMemory().getCurrentMemory());
    }

    @NotNull
    @Contract("_, _, _ -> param3")
    private Card digivolve(@NotNull Player player, @NotNull Card digivolution, @NotNull Card digimon) {
        digimon.previousDigivolution = digivolution;
        digivolution.nextDigivolution = digimon;

        // TODO: Trigger or Register Effect?

        log.logger(player, digimon, Phase.MAIN_DIGIVOLVE);

        if (!player.deck.isMainDeckEmpty())
            drawCards(player, 1);

        return digimon;
    }

    private void fromBattleAreaToTrash(Player player, @NotNull Card card) {
        if (card.previousDigivolution != null) {
            fromBattleAreaToTrash(player, card.previousDigivolution);
            card.previousDigivolution = null;
        }

        player.trash.add(card);
        log.logger(player, card, Phase.MAIN_TRASH, "Battle Area!", "Trash!");
    }
}
