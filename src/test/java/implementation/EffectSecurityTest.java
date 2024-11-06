package implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import model.card.Card;
import model.effect.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class EffectSecurityTest {
    private final String[] conditions = {
        "[Security] Activate this card's [Main] effect.",
        "[Security] All of your Digimon gain ＜Security Attack +1＞ (This Digimon checks 1 additional security card) until the end of your next turn.",
        "[Security] All of your Security Digimon get +7000 DP for the turn.",
        "[Security] Play this card without paying its memory cost.",
        "[Security] Gain 2 memory.",
        "[Security] Choose 1 of your opponent's Digimon with no digivolution cards. That Digimon can't attack or block until the end of your next turn."
    };

    private final String st1_16 = "{\"_id\":{\"$oid\":\"64410e745aefbfff8f76ede7\"},\"card_number\":\"ST1-16\",\"card_name\":\"ガイアフォース\",\"card_name_romaji\":\"Gaia Force\",\"card_price_jpy\":\"120\",\"card_price_idr\":\"13326.94\",\"card_image\":\"https://en.digimoncard.com/images/cardlist/card/ST1-16.png\",\"deck_number\":\"ST1\",\"attribute\":\"\",\"card_colour\":\"Red\",\"card_effect\":\"[Main] Delete 1 of your opponent's Digimon.\",\"card_name_english\":\"Gaia Force\",\"card_type\":\"Option\",\"digimon_power\":\"\",\"digivolution\":[],\"form\":\"\",\"inherit_effect\":\"\",\"level\":\"\",\"play_cost\":\"8\",\"rarity\":\"Uncommon\",\"type\":\"\",\"security_effect\":\"[Security] Activate this card's [Main] effect.\"  }";

    private ObjectMapper mapper;
    private EffectParser effectParser;

    @BeforeEach
    void setUp() {
        effectParser = new EffectParser();
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @SneakyThrows
    @Test
    public void testParseEffectForActivateMainEffect() {
        String effectText = conditions[0];
        Card card = mapper.readValue(st1_16, Card.class);

        Map<Condition, Action> result = effectParser.parseEffect(effectText, card);

        Condition condition = new Condition(ConditionType.DEFAULT);
        Action action = new Action(ActionType.DELETE_ONE_DIGIMON, 1);

        Assertions.assertNotNull(result.get(condition));
        Assertions.assertEquals(action, result.get(condition));
    }
}
