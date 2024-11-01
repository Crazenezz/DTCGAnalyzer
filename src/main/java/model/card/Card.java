package model.card;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import model.effect.Effect;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = CardDeserializer.class)
public class Card extends Effect {
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
    @JsonProperty("card_colour")
    public Colour cardColour;
    @JsonProperty("card_type")
    @Getter
    public CardType cardType;
    @JsonProperty("attribute")
    public String attribute;
    @JsonProperty("form")
    public String form;
    @JsonProperty("type")
    public String type;
    @JsonProperty("digimon_power")
    @Getter
    public int digimonPower;

    // Not from DB
    @Getter
    private boolean suspended;

    @Getter
    private int securityAttack = 1;

    @Setter
    @Getter
    private int additionalDP;

    @Setter
    @Getter
    private boolean disoriented = true;

    @Getter
    public Trait trait;

    @JsonManagedReference
    public Card previousDigivolution;
    @JsonBackReference
    public Card nextDigivolution;

    public void suspend() {
        suspended = true;
    }

    public void unsuspend() {
        suspended = false;
    }

    public void setSecurityAttack(int securityAttack) {
        this.securityAttack += securityAttack;
    }

    public int getTotalDP() {
        return digimonPower + additionalDP;
    }

    public void resetAdditionalAttribute() {
        additionalDP = 0;
        securityAttack = 1;
    }

}
