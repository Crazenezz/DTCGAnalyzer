package model;

import java.util.List;

public class Tamer extends Card {
    public List<Digimon> digivolutionCards;

    public void add(Digimon card) {
        digivolutionCards.add(card);
    }
}
