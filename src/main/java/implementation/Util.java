package implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.card.Card;
import model.card.CardType;
import model.Phase;
import model.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Util {
    public InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public String printInputStream(InputStream is) {
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            return "{}";
        }
    }

    public Card getCardFromNumber(@NotNull List<Card> list, String value) {
        for (Object object : list.toArray()) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card card = mapper.convertValue(object, Card.class);

            if (card.number.equalsIgnoreCase(value))
                return card;
        }

        return null;
    }

    public List<Card> getListCardFromLevel(@NotNull List<Card> list, int level) {
        List<Card> result = new ArrayList<>();

        for (Object object : list.toArray()) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card card = mapper.convertValue(object, Card.class);

            if (card.level == level)
                result.add(card);
        }

        return result;
    }

    public int getCardFromLevel(@NotNull List<Card> list, int level) {

        for (int i = 0; i < list.size(); i++) {

            Object object = list.get(i);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card card = mapper.convertValue(object, Card.class);

            if (card.level == level)
                return i;
        }
        return -1;
    }

    public int getCardFromType(@NotNull List<Card> list, CardType type) {
        for (int i = 0; i < list.size(); i++) {

            Object object = list.get(i);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card card = mapper.convertValue(object, Card.class);

            if (card.translateType() == type)
                return i;
        }
        return -1;
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
        if (Objects.requireNonNull(phase) == Phase.DRAW)
            System.out.println("Player: " + player.name + ", draw " + card.translateType() + " card, " + card.name + " (" + card.number + ") to hand!");
        else if (Objects.requireNonNull(phase) == Phase.UNSUSPEND)
            System.out.println("Player: " + player.name + ", set " + card.name + " (" + card.number + ") as active!");
        else if (Objects.requireNonNull(phase) == Phase.BREEDING)
            System.out.println("Player: " + player.name + ", hatch " + card.name + " (" + card.number + ") on Breeding Area!");
        else if (Objects.requireNonNull(phase) == Phase.BREEDING_MOVE)
            System.out.println("Player: " + player.name + ", move " + card.name + " (" + card.number + ") to Battle Area!");
        else if (Objects.requireNonNull(phase) == Phase.MAIN_PLAY)
            System.out.println("Player: " + player.name + ", play a " + card.translateType() + " card, " + card.name + " (" + card.number + ") to Battle Area!");
        else if (Objects.requireNonNull(phase) == Phase.MAIN_DIGIVOLVE)
            System.out.println("Player: " + player.name + ", digivolve " + card.previousDigivolution.name + " (" + card.previousDigivolution.number + ") to " + card.name + " (" + card.number + ")" + " on Breeding Area!");
    }

    public void logger(Player player, int turn, Phase phase) {
        if (Objects.requireNonNull(phase) == Phase.START_TURN)
            System.out.println("Start turn: " + turn + ", by Player: " + player.name);
        else
            System.out.println("End turn: " + turn + ", by Player: " + player.name + "\n");
    }
}
