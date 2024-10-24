package model;

import java.util.List;

public class Digimon extends Card {
    public int dp;
    public List<Digimon> digivolutionCards;
    public int digivolutionCost;

    public Digimon() {

    }

    public String getEffect() {
        return effect;
    }

//    public boolean isSuspended() {
//        return suspended;
//    }
//
//    public void suspend() {
//        suspended = true;
//    }
//
//    public void unsuspend() {
//        suspended = false;
//    }

    public String getName() {
        return name;
    }
}
