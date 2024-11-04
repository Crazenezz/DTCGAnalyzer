package model.card;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardDeserializer extends JsonDeserializer<Card> {
    @Override
    public Card deserialize(@NotNull JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = mapper.readTree(parser);

        Card card = new Card();

        // Deserialize each field manually
        card.number = node.get("card_number").asText();
        card.name = node.get("card_name_english").asText();
        card.effect = node.get("card_effect").asText();
        card.inheritEffect = node.get("inherit_effect").asText();
        card.securityEffect = node.get("security_effect").asText();
        card.playCost = node.get("play_cost").asInt();

        // Assuming `Digivolution` is a custom type, deserialize as a list
        JsonNode digivolutionsNode = node.get("digivolution");
        List<Digivolution> digivolutions = new ArrayList<>();
        if (digivolutionsNode != null && digivolutionsNode.isArray()) {
            for (JsonNode digivolutionNode : digivolutionsNode) {
                Digivolution digivolution = mapper.treeToValue(digivolutionNode, Digivolution.class);
                digivolutions.add(digivolution);
            }
        }
        card.digivolutions = digivolutions;

        card.level = node.get("level").asInt();
        card.cardColour = getColour(node.get("card_colour").asText());
        card.cardType = getType(node.get("card_type").asText());
        card.digimonPower = getDigimonPower(node.get("digimon_power").asText());

        card.attribute = node.get("attribute").asText();
        card.form = node.get("form").asText();
        card.type = node.get("type").asText();
        card.trait = getTrait(card.attribute, card.form, card.type);

        return card;
    }

    @Contract(pure = true)
    private CardType getType(@NotNull String type) {
        return switch (type) {
            case "Digimon" -> CardType.DIGIMON;
            case "Digi-Egg" -> CardType.DIGIEGG;
            case "Tamer" -> CardType.TAMER;
            case "Option" -> CardType.OPTION;
            default -> CardType.UNKNOWN;
        };
    }

    @Contract(pure = true)
    private Colour getColour(@NotNull String colour) {
        return switch (colour) {
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

    @NotNull
    private Trait getTrait(String attribute, String form, String type) {
        Trait trait = new Trait();
        trait.attribute = attribute;
        trait.form = form;
        trait.type = type;

        return trait;
    }

    private int getDigimonPower(@NotNull String digimonPower) {
        try {
            return Integer.parseInt(digimonPower.replace(" DP", ""));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
