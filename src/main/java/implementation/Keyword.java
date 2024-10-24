package implementation;

import model.Digimon;
import model.Player;
import model.Tamer;

import java.util.List;

public class Keyword {
    void blocker(Digimon thisDigimon, Digimon attackingDigimon) {
        if (thisDigimon.isSuspended()) {
            // Redirect the attack to this Digimon
        }
    }

    void securityAttackPlusX(Digimon attackingDigimon, int X) {
        for (int i = 0; i < X; i++) {
            // Check and resolve each security card
        }
    }

    void securityAttackMinusX(Digimon attackingDigimon, int X) {
        // Implementation of Security Attack -X
    }

    void recoveryPlusX(Player player, int X) {
        for (int i = 0; i < X; i++) {
//            player.securityStack.push(player.deck.remove(player.deck.size() - 1));
        }
    }

    void piercing(Digimon attackingDigimon, Digimon defendingDigimon) {
        if (defendingDigimon.dp <= 0) {
            // Check security if attacking Digimon survives
        }
    }

    void drawX(Player player, int X) {
        for (int i = 0; i < X; i++) {
//            player.hand.add(player.deck.remove(player.deck.size() - 1));
        }
    }

    void jamming(Digimon attackingDigimon) {
        // Can't be deleted in battles against Security Digimon
    }

    void digisorptionX(Digimon digivolvingDigimon, Player player, int X) {
        // Suspend 1 of player's Digimon to reduce digivolve cost by X
    }

    void reboot(Digimon digimon) {
        // Unsuspend this Digimon during opponent's unsuspend phase
    }

    void deDigivolveX(Digimon targetDigimon, int X) {
        for (int i = 0; i < X; i++) {
            if (!targetDigimon.digivolutionCards.isEmpty()) {
                targetDigimon.digivolutionCards.remove(targetDigimon.digivolutionCards.size() - 1);
            }
        }
    }

    void retaliation(Digimon thisDigimon, Digimon opposingDigimon) {
        if (thisDigimon.dp <= 0) {
            // Delete the opposing Digimon
        }
    }

    void digiBurstX(Digimon digimon, int X) {
        for (int i = 0; i < X; i++) {
            if (!digimon.digivolutionCards.isEmpty()) {
                digimon.digivolutionCards.remove(digimon.digivolutionCards.size() - 1);
            }
        }
        // Activate the effect
    }

    void rush(Digimon digimon) {
        // Can attack the turn it comes into play
    }

    void blitz(Digimon digimon, int opponentMemory) {
        if (opponentMemory >= 1) {
            // Can attack
        }
    }

    void delay() {
        // Trash this card in battle area to activate effect next turn
    }

    void decoy(Digimon decoyDigimon, Digimon targetDigimon) {
        // Delete the decoy Digimon to prevent deletion of target Digimon
    }

    void armorPurge(Digimon digimon) {
        if (!digimon.digivolutionCards.isEmpty()) {
            digimon.digivolutionCards.remove(digimon.digivolutionCards.size() - 1);
            // Prevent deletion
        }
    }

    void save(Digimon digimon, Tamer tamer) {
        // Place this card under one of your tamers
    }

    void materialSaveX(Digimon digimon, Tamer tamer, int X) {
        for (int i = 0; i < X; i++) {
            if (!digimon.digivolutionCards.isEmpty()) {
                tamer.add(digimon.digivolutionCards.remove(digimon.digivolutionCards.size() - 1));
            }
        }
    }

    void evade(Digimon digimon) {
        // Suspend to prevent deletion
    }

    void raid(Digimon attackingDigimon, Digimon highestDpDigimon) {
        // Switch target to opponent's unsuspended Digimon with highest DP
    }

    void alliance(Digimon attackingDigimon, Digimon supportingDigimon) {
        attackingDigimon.dp += supportingDigimon.dp;
        // Gain Security Attack +1
    }

    void barrier(Digimon digimon, Player player) {
        if (!player.securityStack.isEmpty()) {
            player.securityStack.pop();
            // Prevent deletion
        }
    }

    void blastDigivolve(Digimon digimon) {
        // Digivolve into specified card without paying cost
    }

    void mindLink(Tamer tamer, Digimon digimon) {
        // Place tamer under one of your Digimon without a Tamer
    }

    void fortitude(Digimon digimon) {
        // When deleted, play without cost
    }

    void partition(Digimon digimon) {
        // When would leave battle area, play specified cards without cost
    }

    void collision(Digimon attackingDigimon) {
        // Opponent's Digimon gain Blocker and must Block if possible
    }

    void scapegoat(Digimon digimon, Digimon sacrificialDigimon) {
        // Delete sacrificial Digimon to prevent deletion
    }

    void blastDNADigivolve(Digimon digimon) {
        // DNA digivolve using specified Digimon from hand
    }

    void vortex(Digimon digimon) {
        // At end of turn, may attack opponent's Digimon
    }

    void overclock(Digimon digimon, List<Digimon> tokenOrPuppetDigimon) {
        // At end of turn, delete Token or [Puppet] trait Digimon to attack player without suspending
        for (Digimon tokenOrPuppet : tokenOrPuppetDigimon) {
            if (tokenOrPuppet.getTrait().type.equals("Puppet")) {
                // Delete the token or puppet Digimon
                tokenOrPuppetDigimon.remove(tokenOrPuppet);
                // Attack player without suspending
                // (Logic for attacking player)
                break;
            }
        }
    }

    void iceArmor(Digimon attackingDigimon, Digimon defendingDigimon) {
        // Compares digivolution cards instead of DP in battles
        int attackingDigivolutionCount = attackingDigimon.digivolutionCards.size();
        int defendingDigivolutionCount = defendingDigimon.digivolutionCards.size();
        if (attackingDigivolutionCount > defendingDigivolutionCount) {
            // Attacking Digimon wins the battle
        } else if (attackingDigivolutionCount < defendingDigivolutionCount) {
            // Defending Digimon wins the battle
        } else {
            // It's a tie (Handle tie scenario)
        }
    }
}
