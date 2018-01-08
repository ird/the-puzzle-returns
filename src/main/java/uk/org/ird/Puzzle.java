package uk.org.ird;

import java.io.IOException;
import java.math.BigInteger;

public interface Puzzle {
    void generate() throws IOException;
    boolean verify(String answer);
    String toString();
    int decreaseAndGetRoundsRemaining();
    void setRoundsRemaining(int r);
    Long getTimeAllowed();
    boolean inTime();
    BigInteger getToken();
}
