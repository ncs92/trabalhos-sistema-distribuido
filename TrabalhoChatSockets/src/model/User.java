package model;

import java.net.InetAddress;

public class User {
    
    public String nick;
    
    public InetAddress address;
    public int port;

    @Override
    public String toString() {
        return nick;
    }
}
