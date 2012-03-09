package edu.berkeley.wtchoi.cc;

public class AckPacket {
    private static int id_next = 0;
    private int id;
    private AckPacket(){}
    
    
    public static AckPacket generate(){
        AckPacket ap = new AckPacket();
        ap.id = AckPacket.id_next++;
        return ap;
    }
    
    public int getId(){
        return id;
    }
}
