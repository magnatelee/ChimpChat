package edu.berkeley.wtchoi.cc;

public class Packet {

    public enum PacketType {
        Ack, Reset,
        // Packet from MonkeyControl
        AckCommand, RequestView,

        // Packet from Application
        AckStable
    }

    private static int id_next = 0;
    private int id;
    private PacketType type;

    private Packet(PacketType t) {
        id = id_next++;
        type = t;
    }


    public static Packet getAck() {
        return new Packet(PacketType.Ack);
    }

    public static Packet getReset() {
        return new Packet(PacketType.Reset);
    }

    public static Packet getRequestView() {
        return new Packet(PacketType.RequestView);
    }

    public static Packet getAckCommand() {
        return new Packet(PacketType.AckCommand);
    }

    public static Packet getAckStable() {
        return new Packet(PacketType.AckStable);
    }

    public int getId() {
        return id;
    }

    public PacketType getType() {
        return type;
    }
}
