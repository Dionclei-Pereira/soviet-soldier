package me.dionclei.soviet_soldier.domain.enums;

public enum Card {
    // Hearts
    H_4(1, Suit.HEARTS), H_5(2, Suit.HEARTS), H_6(3, Suit.HEARTS),
    H_7(4, Suit.HEARTS), H_Q(5, Suit.HEARTS), H_J(6, Suit.HEARTS),
    H_K(7, Suit.HEARTS), H_A(8, Suit.HEARTS), O_2(9, Suit.HEARTS), H_3(10, Suit.HEARTS),

    // Clubs
    C_4(1, Suit.CLUBS), C_5(2, Suit.CLUBS), C_6(3, Suit.CLUBS),
    C_7(4, Suit.CLUBS), C_Q(5, Suit.CLUBS), C_J(6, Suit.CLUBS),
    C_K(7, Suit.CLUBS), C_A(8, Suit.CLUBS), C_2(9, Suit.CLUBS), C_3(10, Suit.CLUBS),

    // Spades
    S_4(1, Suit.SPADES), S_5(2, Suit.SPADES), S_6(3, Suit.SPADES),
    S_7(4, Suit.SPADES), S_Q(5, Suit.SPADES), S_J(6, Suit.SPADES),
    S_K(7, Suit.SPADES), S_A(8, Suit.SPADES), S_2(9, Suit.SPADES), S_3(10, Suit.SPADES),

    // Diamonds
    D_4(1, Suit.DIAMONDS), D_5(2, Suit.DIAMONDS), D_6(3, Suit.DIAMONDS),
    D_7(4, Suit.DIAMONDS), D_Q(5, Suit.DIAMONDS), D_J(6, Suit.DIAMONDS),
    D_K(7, Suit.DIAMONDS), D_A(8, Suit.DIAMONDS), D_2(9, Suit.DIAMONDS), D_3(10, Suit.DIAMONDS);

    private final int power;
    private final Suit suit;

    Card(int power, Suit suit) {
        this.power = power;
        this.suit = suit;
    }

    public int getPower() { return power; }
    public Suit getSuit() { return suit; }
}
