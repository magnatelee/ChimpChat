package edu.berkeley.wtchoi.cc;

public class Packet {

    public enum PacketType {
        Ack, Reset,
        // Packet from MonkeyControl
        AckCommand, RequestView,

        // Packet from Application
        AckStable;
    }

    private static int id_next = 0;
    private int id;
    private PacketType type;

    private Packet(PacketType t) {
        id = id_next++;
        type = t;
    }


    public static Packet getAck() {
        Packet ap = new Packet(PacketType.Ack);
        return ap;
    }

    public static Packet getReset() {
        Packet ap = new Packet(PacketType.Reset);
        return ap;
    }

    public static Packet getRequestView() {
        Packet ap = new Packet(PacketType.RequestView);
        return ap;
    }

    public static Packet getAckCommand() {
        Packet ap = new Packet(PacketType.AckCommand);
        return ap;
    }

    public static Packet getAckStable() {
        Packet ap = new Packet(PacketType.AckStable);
        return ap;
    }

    public int getId() {
        return id;
    }

    public PacketType getType() {
        return type;
    }
}
