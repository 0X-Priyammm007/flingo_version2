package model;

public class Flashcard {
    private int id;
    private String question;
    private String answer;

    public Flashcard(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public Flashcard(String question, String answer) {
        this(-1, question, answer);
    }

    public int getId() { return id; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
}