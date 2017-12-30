package uk.org.ird;

public interface Puzzle {
    String generate(Integer token);
    Response verify(String answer, Integer token);
    Integer getTimeRemaining(Integer token);
}
