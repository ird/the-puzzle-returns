package uk.org.ird;

public interface Puzzle {
    void generate(Integer token);
    String toString();
    boolean verify(String answer, Integer token);
    Integer getTimeRemaining();
}
