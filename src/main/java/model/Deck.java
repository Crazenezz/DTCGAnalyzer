package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import implementation.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    @JsonProperty("digiegg_deck")
    public List<CardMetadata> digiEggDeckMetaData = new ArrayList<>();

    @JsonProperty("deck")
    public List<CardMetadata> mainDeckMetaData = new ArrayList<>();

    @JsonProperty("digiEggDeck")
    public List<Card> digiEggDeck;

    @JsonProperty("mainDeck")
    public List<Card> mainDeck;

    private Util util;

    public void initialize(String fileDeck) {
        util = new Util();
        try {
            setDigiEggDeck(fileDeck);
            setMainDeck(fileDeck);
        } catch (JsonProcessingException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        shuffleDigiEggDeck();
        shuffleMainDeck();
    }

    private void setDigiEggDeck(String fileDeck) throws JsonProcessingException, ClassNotFoundException {
        digiEggDeck = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        List<Card> cardList = mapper.readValue(util.printInputStream(util.getFileFromResourceAsStream(fileDeck)), List.class);

        for (CardMetadata data : digiEggDeckMetaData) {
            Card card = util.getCardFromNumber(cardList, data.cardNumber);

            for (int i = 0; i < Integer.parseInt(data.quantity); i++)
                digiEggDeck.add(card);
        }
    }

    private void setMainDeck(String fileDeck) throws JsonProcessingException, ClassNotFoundException {
        mainDeck = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        List<Card> cardList = mapper.readValue(util.printInputStream(util.getFileFromResourceAsStream(fileDeck)), List.class);

        for (CardMetadata data : mainDeckMetaData) {
            Card card = util.getCardFromNumber(cardList, data.cardNumber);

            for (int i = 0; i < Integer.parseInt(data.quantity); i++)
                mainDeck.add(card);
        }
    }

    public void shuffleDigiEggDeck() {
        Collections.shuffle(digiEggDeck);
    }

    public void shuffleMainDeck() {
        Collections.shuffle(mainDeck);
    }

    public Card draw() {
        return mainDeck.isEmpty() ? null : mainDeck.removeFirst();
    }

    public boolean isMainDeckEmpty() {
        return mainDeck.isEmpty();
    }

    public boolean isDigiEggDeckEmpty() {
        return digiEggDeck.isEmpty();
    }
}
