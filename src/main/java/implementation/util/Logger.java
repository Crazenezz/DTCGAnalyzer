package implementation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Phase;
import model.Player;
import model.card.Card;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Logger {

    public void logger(List<Card> list) {
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card card = mapper.convertValue(object, Card.class);

            System.out.println("On Hand: " + i + ", " + card.translateType() + " card: " + card.name + " (" + card.number + ")");
        }
    }

    public void logger(Player player, Phase phase) {
        if (Objects.requireNonNull(phase) == Phase.BREEDING)
            System.out.println("Player: " + player.name + ", Skip Breeding Phase!");
        else if (Objects.requireNonNull(phase) == Phase.SKIP_MAIN)
            System.out.println("Player: " + player.name + ", Skip Main Phase!");
        else if (Objects.requireNonNull(phase) == Phase.START_MAIN)
            System.out.println("Player: " + player.name + ", Start Main Phase!");
    }

    public void logger(Player player, Card card, Phase phase) {
        logger(player, card, phase, "Breeding Area!");
    }

    public void logger(Player player, Card card, Phase phase, String area) {
        if (Objects.requireNonNull(phase) == Phase.DRAW)
            System.out.println("Player: " + player.name + ", draw " + card.translateType() + " card, " + card.name + " (" + card.number + ") to hand!");
        else if (Objects.requireNonNull(phase) == Phase.UNSUSPEND)
            System.out.println("Player: " + player.name + ", set " + card.name + " (" + card.number + ") as active!");
        else if (Objects.requireNonNull(phase) == Phase.BREEDING)
            System.out.println("Player: " + player.name + ", hatch " + card.name + " (" + card.number + ") on Breeding Area!");
        else if (Objects.requireNonNull(phase) == Phase.BREEDING_MOVE)
            System.out.println("Player: " + player.name + ", move " + card.name + " (" + card.number + ") to Battle Area!");
        else if (Objects.requireNonNull(phase) == Phase.MAIN_PLAY)
            System.out.println("Player: " + player.name + ", play a " + card.translateType() + " card, " + card.name + " (" + card.number + ") pay " + card.playCost + " memory, to Battle Area!");
        else if (Objects.requireNonNull(phase) == Phase.MAIN_DIGIVOLVE)
            System.out.println("Player: " + player.name + ", digivolve " + card.previousDigivolution.name + " (" + card.previousDigivolution.number + ") to " + card.name + " (" + card.number + ") pay " + card.digivolutions.getFirst().cost + " memory, on " + area);
    }

    public void logger(Player player, Card card, Phase phase, String fromArea, String toArea) {
        if (Objects.requireNonNull(phase) == Phase.MAIN_TRASH)
            System.out.println("Player: " + player.name + ", Trash card: " + card.name + " (" + card.number + "), from " + fromArea + " to " + toArea);
    }

    public void logger(Player player, Player opponent, Card attacker, Card security, Phase phase) {
        if (Objects.requireNonNull(phase) == Phase.MAIN_ATTACKING)
            System.out.println("Player: " + player.name + ", Attacking with: " + attacker.name + " (" + attacker.number + ") to " + opponent.name + " Security Stack, check with: " + security.name + " (" + security.number + ")");
    }

    public void logger(Player player, int turn, Phase phase) {
        if (Objects.requireNonNull(phase) == Phase.START_TURN)
            System.out.println("Start turn: " + turn + ", by Player: " + player.name);
        else
            System.out.println("End turn: " + turn + ", by Player: " + player.name + "\n");
    }

    public void logger(@NotNull Player player, int memory) {
        System.out.println("Player: " + player.name + ", current memory: " + memory);
    }
}