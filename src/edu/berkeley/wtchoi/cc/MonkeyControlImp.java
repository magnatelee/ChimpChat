package edu.berkeley.wtchoi.cc;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import edu.berkeley.wtchoi.cc.interfaces.Command;
import edu.berkeley.wtchoi.cc.interfaces.MonkeyControl;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */

class MonkeyControlImp implements MonkeyControl {
   
    private MonkeyControlOption option;
        
    private ChimpChat mChimpchat;
    private IChimpDevice mDevice;
    

    private java.io.ObjectInputStream ois;
    private java.io.ObjectOutputStream oos;

    private class ChannelInitiator extends Thread {
        private MonkeyControlImp mMonkey;
        private boolean result = false;

        ChannelInitiator(MonkeyControlImp m) {
            mMonkey = m;
        }

        public void run() {
            //Initiate augmented channel with a target application
            java.net.ServerSocket serverSocket;
            java.net.Socket socket;

            try {
                serverSocket = new java.net.ServerSocket(13339);
                System.out.println("wait");
                socket = serverSocket.accept();
                System.out.println("go");
                serverSocket.close();

                mMonkey.ois = new java.io.ObjectInputStream(socket.getInputStream());
                mMonkey.oos = new java.io.ObjectOutputStream(socket.getOutputStream());
                result = true;
            } catch (java.io.IOException e) {
                System.out.println("Exception on new ServerSocket");
            }
        }

        public boolean getInitiateResult() {
            return result;
        }
    }

    public MonkeyControlImp(MonkeyControlOption option) {
        super();
        this.option = option;
    }

    // Initiate application, connect chip, connect channel
    public boolean connectToDevice() {
        if(!option.isComplete()) return false;
        
        //1. Boot ChimpChat Instance
        TreeMap<String, String> options = new TreeMap<String, String>();
        options.put("backend", "adb");
        options.put("adbLocation", option.getADB());
        mChimpchat = ChimpChat.getInstance(options);

        //2. Initiate ChimpChat Channel with a target device
        mDevice = mChimpchat.waitForConnection(option.getTimeout(), ".*");
        if (mDevice == null) {
            //throw new RuntimeException("Couldn't connect.");
            return false;
        }
        mDevice.wake();
        return true;
    }

    public boolean initiateApp() {
        //1. Initiate Communication Channel (Asynchronous)
        ChannelInitiator initiator = new ChannelInitiator(this);
        initiator.start();
        
        String runComponent = option.getRunComponent();
        Collection<String> coll = new LinkedList<String>();
        Map<String, Object> extras = new HashMap<String, Object>();
        mDevice.startActivity(null, null, null, null, coll, extras, runComponent, 0);

        //3. Wait for communication channel initiation
        try {
            initiator.join();
            if (!initiator.getInitiateResult()) {
                //throw new RuntimeException("Communication channel cannot be initiated");
                return false;
            }
        } catch (InterruptedException e) {
            //throw new RuntimeException("Communication initiator interrupted");
            return false;
        }

        //4. Wait for application to be ready for command
        try {
            Packet packet = (Packet) this.ois.readObject();
            if (packet.getType() != Packet.Type.AckStable) {
                //throw new RuntimeException("Application sent wrong packet. AckStable expected");
                return false;
            }
        } catch (Exception e) {
            //throw new RuntimeException("Cannot get packet from Application");
            return false;
        }

        return true;
        //NOTE: At this moment, we expect application to erase all user data when ever it starts.
        //Therefore, our protocol doesn't have anythings about resetting application data.
        //However, we may need more complex protocol to fine control an application.
    }

    public boolean restartApp() {
        return initiateApp();
        //TODO: redesign protocol to include separate restart packet!
    }

    public void shutdown() {
        mChimpchat.shutdown();
        mDevice = null;
    }


    public MonkeyView getView() {
        MonkeyView mv;

        try {
            //0. Assume that application is waiting for command
            //1. Send request view packet to application
            Packet packet = Packet.getRequestView();
            oos.writeObject(packet);

            //2. Wait and get a View information from application
            mv = (MonkeyView) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot read an object");
            return null;
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Cannot read an object");
            return null;
        }
        //DEBUG PRINT : whether received information is correct or not
        //System.out.println(mv);
        return mv;
    }

    public boolean go(List<? extends Command> clist) {
        try {
            //1. Send commands
            for (Command c : clist) {
                if (!go(c)) return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean go(Command c) {
        //0. Assume application is waiting for command
        try {
            //1.1 Send command through ChimpChat.
            c.sendCommand(mDevice);

            //1.2 Send command acknowledgement to App Supervisor
            Packet ack = Packet.getAckCommand();
            oos.writeObject(ack);

            //1.3 Wait for App Supervisor response
            Packet receivingPacket = (Packet) ois.readObject();
            if (receivingPacket.getType() != Packet.Type.AckStable) {
                //throw new RuntimeException(Application Execution is not guided correctly);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
