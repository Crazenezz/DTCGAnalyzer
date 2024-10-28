package implementation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.DigivolutionObject;
import model.card.Card;
import model.card.CardType;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public int getCardWithLowestLevelCost(@NotNull List<Card> list, CardType type) {
        int index = -1;
        int level = 7;
        int playCost = 21;

        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card card = mapper.convertValue(object, Card.class);

            if (card.translateType() == CardType.DIGIMON && level > card.level && playCost > card.playCost)
                index = i;
            else if (card.translateType() == CardType.OPTION && playCost > card.playCost)
                index = i;
        }
        return index;
    }

    public List<Card> getDistinctObject(@NotNull List<Card> list) {
        return list.stream().distinct().collect(Collectors.toList());
    }

    public DigivolutionObject digivolveTo(List<Card> hand, @NotNull List<Card> area) {
        DigivolutionObject digivolutionObject = new DigivolutionObject();

        for (int indexFrom = 0; indexFrom < area.size(); indexFrom++) {
            Object object = area.get(indexFrom);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card cardOnBA = mapper.convertValue(object, Card.class);

            if (cardOnBA.translateType() == CardType.DIGIMON && cardOnBA.level < 7) {
                for (int indexTo = 0; indexTo < hand.size(); indexTo++) {
                    object = area.get(indexTo);
                    Card cardOnHand = mapper.convertValue(object, Card.class);

                    if (cardOnHand.translateType() == CardType.DIGIMON && cardOnHand.level == (cardOnBA.level + 1)) {
                        digivolutionObject.indexFrom = indexFrom;
                        digivolutionObject.indexTo = indexTo;

                        return digivolutionObject;
                    }
                }
            }
        }

        return digivolutionObject;
    }

    public int getAttackerDigimon(List<Card> list) {
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Card card = mapper.convertValue(object, Card.class);

            if (card.translateType() == CardType.DIGIMON && !card.isSuspended() && !card.isDisoriented())
                return i;
        }
        return -1;
    }

}