package model.card;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CardSerializer extends JsonSerializer<Card> {
    @Override
    public void serialize(Card card, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeEndObject();
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
}
