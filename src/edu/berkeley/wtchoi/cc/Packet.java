package edu.berkeley.wtchoi.cc;

public class Packet {
    
    public enum PacketType{
        Ack, Reset;
    }
    
    private static int id_next = 0;
    private int id;
    private PacketType type;
    private Packet(PacketType t){
        id = id_next++;
        type = t;                 
    }
    
    
    public static Packet getAck(){
        Packet ap = new Packet(PacketType.Ack);
        return ap;
    }
    
    public static Packet getReset(){
        Packet ap = new Packet(PacketType.Reset);
        return ap;
    }
    
    public int getId(){
        return id;
    }
    
    public PacketType getType(){
        return type;
    }
}
