package model;

public class DirectMessage {
    
    public String from;
    public String to;
    public String text;

    @Override
    public String toString() {
        return "MSGIDV FROM|" + from + "|TO|" + to + "|" + text;
    }
}
