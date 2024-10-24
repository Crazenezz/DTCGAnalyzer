package model;

import implementation.Memory;
import implementation.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Player {
    // Player's deck contains max 50 cards
    // Player's Digi-Egg deck contains max 5 cards
    // Player's hand draw initial 5 cards
    // Player's security stack draw initial 5 cards

    public Deck deck;
    public List<Card> hand;
    public List<Card> digiEggDeck;
    public Stack<Card> securityStack;
    public List<Card> trash;
    public List<Card> battleArea;
    public List<Card> breedingArea;
    public Memory memory;
    public boolean isTurn;
    public String name;
    private final Util util;

    public Player(String name, Deck deck) {
        this.hand = new ArrayList<>();
        this.digiEggDeck = new ArrayList<>();
        this.securityStack = new Stack<>();
        this.trash = new ArrayList<>();
        this.battleArea = new ArrayList<>();
        this.breedingArea = new ArrayList<>();
        this.isTurn = false;
        this.memory = new Memory();

        this.name = name;
        this.deck = deck;

        util = new Util();
    }

    public void unsuspendAll() {
        for (Card card : battleArea) {
            if (card.isSuspended()) {
                card.unsuspend();
                util.logger(this, card, Phase.UNSUSPEND);
            }
        }
    }
}
