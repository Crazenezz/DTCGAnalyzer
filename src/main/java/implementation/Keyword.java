package implementation;

import model.Player;
import model.card.Card;

import java.util.List;

public class Keyword {
    void blocker(Card thisDigimon, Card attackingDigimon) {
        if (thisDigimon.isSuspended()) {
            // Redirect the attack to this Digimon
        }
    }

    void securityAttackPlusX(Card attackingDigimon, int X) {
        for (int i = 0; i < X; i++) {
            // Check and resolve each security card
        }
    }

    void securityAttackMinusX(Card attackingDigimon, int X) {
        // Implementation of Security Attack -X
    }

    void recoveryPlusX(Player player, int X) {
        for (int i = 0; i < X; i++) {
//            player.securityStack.push(player.deck.remove(player.deck.size() - 1));
        }
    }

    void piercing(Card attackingDigimon, Card defendingDigimon) {
        if (defendingDigimon.translateDP() <= 0) {
            // Check security if attacking Digimon survives
        }
    }

    void drawX(Player player, int X) {
        for (int i = 0; i < X; i++) {
//            player.hand.add(player.deck.remove(player.deck.size() - 1));
        }
    }

    void jamming(Card attackingDigimon) {
        // Can't be deleted in battles against Security Digimon
    }

    void digisorptionX(Card digivolvingDigimon, Player player, int X) {
        // Suspend 1 of player's Digimon to reduce digivolve cost by X
    }

    void reboot(Card digimon) {
        // Unsuspend this Digimon during opponent's unsuspend phase
    }

    void deDigivolveX(Card targetDigimon, int X) {
        for (int i = 0; i < X; i++) {
            if (!targetDigimon.digivolutions.isEmpty()) {
                targetDigimon.digivolutions.remove(targetDigimon.digivolutions.size() - 1);
            }
        }
    }

    void retaliation(Card thisDigimon, Card opposingDigimon) {
        if (thisDigimon.translateDP() <= 0) {
            // Delete the opposing Digimon
        }
    }

    void digiBurstX(Card digimon, int X) {
        for (int i = 0; i < X; i++) {
            if (!digimon.digivolutions.isEmpty()) {
                digimon.digivolutions.remove(digimon.digivolutions.size() - 1);
            }
        }
        // Activate the effect
    }

    void rush(Card digimon) {
        // Can attack the turn it comes into play
    }

    void blitz(Card digimon, int opponentMemory) {
        if (opponentMemory >= 1) {
            // Can attack
        }
    }

    void delay() {
        // Trash this card in battle area to activate effect next turn
    }

    void decoy(Card decoyDigimon, Card targetDigimon) {
        // Delete the decoy Digimon to prevent deletion of target Digimon
    }

    void armorPurge(Card digimon) {
        if (!digimon.digivolutions.isEmpty()) {
            digimon.digivolutions.remove(digimon.digivolutions.size() - 1);
            // Prevent deletion
        }
    }

    void save(Card digimon, Card tamer) {
        // Place this card under one of your tamers
    }

    void materialSaveX(Card digimon, Card tamer, int X) {
        for (int i = 0; i < X; i++) {
            if (!digimon.digivolutions.isEmpty()) {
                tamer.previousDigivolution = digimon.previousDigivolution;
            }
        }
    }

    void evade(Card digimon) {
        // Suspend to prevent deletion
    }

    void raid(Card attackingDigimon, Card highestDpDigimon) {
        // Switch target to opponent's unsuspended Digimon with highest DP
    }

    void alliance(Card attackingDigimon, Card supportingDigimon) {
        attackingDigimon.addDP(supportingDigimon.translateDP());
        // Gain Security Attack +1
    }

    void barrier(Card digimon, Player player) {
        if (!player.securityStack.isEmpty()) {
            player.securityStack.pop();
            // Prevent deletion
        }
    }

    void blastDigivolve(Card digimon) {
        // Digivolve into specified card without paying cost
    }

    void mindLink(Card tamer, Card digimon) {
        // Place tamer under one of your Digimon without a Tamer
    }

    void fortitude(Card digimon) {
        // When deleted, play without cost
    }

    void partition(Card digimon) {
        // When would leave battle area, play specified cards without cost
    }

    void collision(Card attackingDigimon) {
        // Opponent's Digimon gain Blocker and must Block if possible
    }

    void scapegoat(Card digimon, Card sacrificialDigimon) {
        // Delete sacrificial Digimon to prevent deletion
    }

    void blastDNADigivolve(Card digimon) {
        // DNA digivolve using specified Digimon from hand
    }

    void vortex(Card digimon) {
        // At end of turn, may attack opponent's Digimon
    }

    void overclock(Card digimon, List<Card> tokenOrPuppetDigimon) {
        // At end of turn, delete Token or [Puppet] trait Digimon to attack player without suspending
        for (Card tokenOrPuppet : tokenOrPuppetDigimon) {
            if (tokenOrPuppet.getTrait().type.equals("Puppet")) {
                // Delete the token or puppet Digimon
                tokenOrPuppetDigimon.remove(tokenOrPuppet);
                // Attack player without suspending
                // (Logic for attacking player)
                break;
            }
        }
    }

    void iceArmor(Card attackingDigimon, Card defendingDigimon) {
        // Compares digivolution cards instead of DP in battles
        int attackingDigivolutionCount = attackingDigimon.digivolutions.size();
        int defendingDigivolutionCount = defendingDigimon.digivolutions.size();
        if (attackingDigivolutionCount > defendingDigivolutionCount) {
            // Attacking Digimon wins the battle
        } else if (attackingDigivolutionCount < defendingDigivolutionCount) {
            // Defending Digimon wins the battle
        } else {
            // It's a tie (Handle tie scenario)
        }
    }
}
