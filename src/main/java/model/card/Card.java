package model.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    @JsonProperty("card_number")
    public String number;
    @JsonProperty("card_name_english")
    public String name;
    @JsonProperty("card_effect")
    public String effect;
    @JsonProperty("inherit_effect")
    public String inheritEffect;
    @JsonProperty("security_effect")
    public String securityEffect;
    @JsonProperty("play_cost")
    public int playCost;
    @JsonProperty("digivolution")
    public List<Digivolution> digivolutions;
    @JsonProperty("level")
    public int level;
    public int dp;
    public Card previousDigivolution;
    public Card nextDigivolution;
    @JsonProperty("card_colour")
    private String cardColour;
    @JsonProperty("card_type")
    private String cardType;
    @JsonProperty("attribute")
    private String attribute;
    @JsonProperty("form")
    private String form;
    @JsonProperty("type")
    private String type;
    @JsonProperty("digimon_power")
    private String digimonPower;
    // Not from DB
    @Getter
    private boolean suspended;
    // TODO: Not used for now
    private Price price;
    private String image;
    private String deckNumber;
    private String rarity;

    public void suspend() {
        suspended = true;
    }

    public void unsuspend() {
        suspended = false;
    }

    public Colour translateColour() {
        return switch (cardColour) {
            case "Red" -> Colour.RED;
            case "Blue" -> Colour.BLUE;
            case "Yellow" -> Colour.YELLOW;
            case "Green" -> Colour.GREEN;
            case "Black" -> Colour.BLACK;
            case "Purple" -> Colour.PURPLE;
            case "White" -> Colour.WHITE;
            default -> Colour.ALL;
        };
    }

    public CardType translateType() {
        return switch (cardType) {
            case "Digimon" -> CardType.DIGIMON;
            case "Digi-Egg" -> CardType.DIGIEGG;
            case "Tamer" -> CardType.TAMER;
            case "Option" -> CardType.OPTION;
            default -> CardType.UNKNOWN;
        };
    }

    public int translateDP() {
        try {
            return Integer.parseInt(digimonPower.replace(" DP", ""));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public int addDP(int dp) {
        return this.dp += dp;
    }

    public Trait getTrait() {
        Trait trait = new Trait();
        trait.attribute = attribute;
        trait.form = form;
        trait.type = type;

        return trait;
    }
}
