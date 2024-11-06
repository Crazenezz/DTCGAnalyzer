package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import implementation.util.Util;
import lombok.SneakyThrows;
import model.card.Card;
import model.card.CardMetadata;

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

    @SneakyThrows
    private void setDigiEggDeck(String fileDeck) throws JsonProcessingException, ClassNotFoundException {
        digiEggDeck = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        List<Card> cardList = mapper.readValue(util.printInputStream(util.getFileFromResourceAsStream(fileDeck)), List.class);

        for (CardMetadata data : digiEggDeckMetaData) {
            for (int i = 0; i < Integer.parseInt(data.quantity); i++) {
                Card card = util.getCardFromNumber(cardList, data.cardNumber);
                digiEggDeck.add(card);
            }
        }
    }

    @SneakyThrows
    private void setMainDeck(String fileDeck) throws JsonProcessingException, ClassNotFoundException {
        mainDeck = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        List<Card> cardList = mapper.readValue(util.printInputStream(util.getFileFromResourceAsStream(fileDeck)), List.class);

        for (CardMetadata data : mainDeckMetaData) {
            for (int i = 0; i < Integer.parseInt(data.quantity); i++) {
                Card card = util.getCardFromNumber(cardList, data.cardNumber);
                mainDeck.add(card);
            }
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
