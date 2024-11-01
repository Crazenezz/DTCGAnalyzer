package model.card;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class CardUtil {
    @Contract(pure = true)
    CardType getType(@NotNull String type) {
        return switch (type) {
            case "Digimon" -> CardType.DIGIMON;
            case "Digi-Egg" -> CardType.DIGIEGG;
            case "Tamer" -> CardType.TAMER;
            case "Option" -> CardType.OPTION;
            default -> CardType.UNKNOWN;
        };
    }

    @Contract(pure = true)
    Colour getColour(@NotNull String colour) {
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
    Trait getTrait(String attribute, String form, String type) {
        Trait trait = new Trait();
        trait.attribute = attribute;
        trait.form = form;
        trait.type = type;

        return trait;
    }

    int getDigimonPower(@NotNull String digimonPower) {
        try {
            return Integer.parseInt(digimonPower.replace(" DP", ""));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
