package model;

public class MulticastMessage {
    
    public String from;
    public String text;

    @Override
    public String toString() {
        return "MSG|" + from + "|" + text;
    }
}
