package designbook.solutions5;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

// CÁC GIAO DIỆN VÀ LỚP PHỤ TRỢ
interface CardSource {
    Card draw();
    boolean isEmpty();
}

enum Rank11 {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

enum Suit11 {
    CLUBS, DIAMONDS, HEARTS, SPADES
}

class Card {
    private final Rank11 aRank;
    private final Suit11 aSuit;

    private Card(Rank11 pRank, Suit11 pSuit) {
        aRank = pRank;
        aSuit = pSuit;
    }

    public static Card get(Rank11 pRank, Suit11 pSuit) {
        return new Card(pRank, pSuit);
    }
}

// LỚP JUNIT TEST (GIẢI PHÁP BÀI 11)
public class Exercise11 {

    // Lớp Stub giả lập CardSource chỉ chứa tối đa 1 lá bài
    private static class CardSourceStub implements CardSource {
        private Optional<Card> aCard = Optional.empty();

        CardSourceStub() {}

        CardSourceStub(Card pCard) {
            aCard = Optional.of(pCard);
        }

        @Override
        public Card draw() {
            assert !isEmpty();
            Card card = aCard.get();
            aCard = Optional.empty();
            return card;
        }

        @Override
        public boolean isEmpty() {
            return !aCard.isPresent();
        }
    }

    private static final Card ACE = Card.get(Rank11.ACE, Suit11.CLUBS);
    private static final Card TWO = Card.get(Rank11.TWO, Suit11.CLUBS);

    private CardSource aEmpty = new CardSourceStub();
    private CardSource aContainsAce = new CardSourceStub(ACE);
    private CardSource aContainsTwo = new CardSourceStub(TWO);

    @Test
    void testEmpty_True() {
        assertTrue(new BiCardSource(aEmpty, aEmpty).isEmpty());
    }

    @Test
    void testEmpty_False_FirstEmpty() {
        assertFalse(new BiCardSource(aEmpty, aContainsAce).isEmpty());
    }

    @Test
    void testEmpty_False_SecondEmpty() {
        assertFalse(new BiCardSource(aContainsAce, aEmpty).isEmpty());
    }

    @Test
    void testEmpty_False_NeitherEmpty() {
        assertFalse(new BiCardSource(aContainsAce, aContainsTwo).isEmpty());
    }

    @Test
    void testDraw_FirstEmpty() {
        assertSame(ACE, new BiCardSource(aEmpty, aContainsAce).draw());
    }

    @Test
    void testDraw_SecondEmpty() {
        assertSame(ACE, new BiCardSource(aContainsAce, aEmpty).draw());
    }

    @Test
    void testDraw_NeitherEmpty() {
        assertSame(ACE, new BiCardSource(aContainsAce, aContainsTwo).draw());
    }
}

// LỚP CẦN KIỂM THỬ (UUT): BiCardSource
class BiCardSource implements CardSource {
    private final CardSource aSource1;
    private final CardSource aSource2;

    public BiCardSource(CardSource pSource1, CardSource pSource2) {
        aSource1 = pSource1;
        aSource2 = pSource2;
    }

    /* Draws from source 1 if not empty, and from source 2 if source 1 is empty. */
    @Override
    public Card draw() {
        if (!aSource1.isEmpty()) {
            // ĐÃ SỬA LỖI (Code gốc bị ngược là aSource2.draw() và aSource1.draw())
            return aSource1.draw();
        } else {
            return aSource2.draw();
        }
    }

    @Override
    public boolean isEmpty() {
        return aSource1.isEmpty() && aSource2.isEmpty();
    }
}