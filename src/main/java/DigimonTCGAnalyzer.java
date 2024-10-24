import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import implementation.Mechanism;
import implementation.Util;
import model.*;

public class DigimonTCGAnalyzer {

    @lombok.SneakyThrows
    private static void setup() {
        Util util = new Util();

        String fileDeck1 = "sample/deck/ST1.json";
        String fileDeck2 = "sample/deck/ST2.json";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Deck player1Deck = mapper.readValue(util.printInputStream(util.getFileFromResourceAsStream(fileDeck1)), Deck.class);
        Deck player2Deck = mapper.readValue(util.printInputStream(util.getFileFromResourceAsStream(fileDeck2)), Deck.class);

        player1Deck.initialize("sample/card/ST1.json");
        player2Deck.initialize("sample/card/ST2.json");

        Player player1 = new Player("Player 1", player1Deck);
        Player player2 = new Player("Player 2", player2Deck);

        Mechanism mechanism = new Mechanism(player1, player2);
        mechanism.initializeGame();
        mechanism.mainGameFlow();
    }

    public static void main(String[] args) {
        DigimonTCGAnalyzer.setup();
    }
}
