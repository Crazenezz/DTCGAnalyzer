package model;

import implementation.Util;
import model.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Player {
    private final Util util;
    public Deck deck;
    public List<Card> hand;
    public Stack<Card> securityStack;
    public List<Card> trash;
    public List<Card> battleArea;
    public List<Card> breedingArea;
    public boolean isTurn;
    public String name;

    public Player(String name, Deck deck) {
        this.hand = new ArrayList<>();
        this.securityStack = new Stack<>();
        this.trash = new ArrayList<>();
        this.battleArea = new ArrayList<>();
        this.breedingArea = new ArrayList<>();
        this.isTurn = false;

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
