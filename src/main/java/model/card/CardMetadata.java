package model.card;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardMetadata {
    @JsonProperty("card_number")
    public String cardNumber;
    public String quantity;
}
