package edu.berkeley.wtchoi.cc;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/24/12
 * Time: 4:29 AM
 * To change this template use File | Settings | File Templates.
 */

public interface MonkeyControl {
    public boolean connectToDevice();
    public boolean initiateApp();

    //public boolean resetData();

    public boolean restartApp();
    public boolean go(List<? extends Command> input);
    public boolean go(Command input);
    public MonkeyView getView();

    public void shutdown();
}

class MonkeyControlImp implements MonkeyControl{
    private static final String ADB = "/Applications/Android//android-sdk-mac_x86/platform-tools/adb";
    private static final long TIMEOUT = 5000;
    private ChimpChat mChimpchat;
    private static IChimpDevice mDevice;

    private java.io.ObjectInputStream ois;
    private java.io.ObjectOutputStream oos;

    private boolean app_stability = false;

    private class ChannelInitiator extends Thread{
        private MonkeyControlImp mMonkey;
        private boolean result = false;

        ChannelInitiator(MonkeyControlImp m){
            mMonkey = m;
        }

        public void run(){
            //Initiate augmented channel with a target application
            java.net.ServerSocket serverSocket;
            java.net.Socket socket;

            try{
                serverSocket = new java.net.ServerSocket(13339);
                System.out.println("wait");
                socket = serverSocket.accept();
                System.out.println("go");
                serverSocket.close();

                mMonkey.ois = new java.io.ObjectInputStream(socket.getInputStream());
                mMonkey.oos = new java.io.ObjectOutputStream(socket.getOutputStream());
                result = true;
            }
            catch(java.io.IOException e){
                System.out.println("Exception on new ServerSocket");
            }
        }

        public boolean getResult(){
            return result;
        }
    }

    public MonkeyControlImp(){
        TreeMap<String,String> options = new TreeMap<String,String>();
        options.put("backend","adb");
        options.put("adbLocation",ADB);
        mChimpchat = ChimpChat.getInstance(options);
    }

    // Initiate application, connect chip, connect channel
    public boolean connectToDevice(){
        //1. Initiate Chimpcat Channel with a target device
        mDevice = mChimpchat.waitForConnection(TIMEOUT, ".*");
        if( mDevice == null){
            //throw new RuntimeException("Couldn't connect.");
            return false;
        }
        mDevice.wake();
        return true;
    }

    public boolean initiateApp(){
        //1. Initiate Communication Channel (Asynchronous)
        ChannelInitiator initiator = new ChannelInitiator(this);
        initiator.start();

        //2. Invoke target application
        String appPackage = "com.android.demo.notepad3";
        String activity = "com.android.demo.notepad3.Notepadv3";
        String runComponent = appPackage + '/' + activity;
        Collection<String> coll = new LinkedList<String>();
        Map<String,Object> extras = new HashMap<String,Object>();
        mDevice.startActivity(null, null, null, null, coll, extras, runComponent, 0);

        //3. Wait for communication channel initiation
        try{
            initiator.join();
            if(!initiator.getResult()){
                //throw new RuntimeException("Communication channel cannot be initiated");
                return false;
            }
        }
        catch(InterruptedException e){
            //throw new RuntimeException("Communication initiator interrupted");
            return false;
        }

        //4. Wait for application to be ready for command
        try{
            Packet packet = (Packet) this.ois.readObject();
            if(packet.getType() != Packet.PacketType.AckStable){
                //throw new RuntimeException("Application sent wrong packet. AckStable expected");
                return false;
            }
            this.app_stability = true;
        }
        catch(Exception e){
            //throw new RuntimeException("Cannot get packet from Application");
            return false;
        }

        return true;
        //NOTE: At this moment, we expect application to erase all user data when ever it starts.
        //Therefore, our protocol doesn't have anythings about resetting application data.
        //However, we may need more complex protocol to fine control an application.
    }


    public boolean restartApp(){
        return initiateApp();
        //TODO: redesign protocol to include separate restart packet!
    }


    public void shutdown(){
        mChimpchat.shutdown();
        mDevice = null;
    }


    public MonkeyView getView(){
        MonkeyView mv;

        try{
            //0. Assume that application is waiting for command
            if(!app_stability) return null;

            //1. Send request view packet to application
            Packet packet = Packet.getRequestView();
            oos.writeObject(packet);

            //2. Wait and get a View information from application
            mv = (MonkeyView) ois.readObject();

            //Since RequestView command does not alter application state,
            //we don't have to change app_stability flag.
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot read an object");
            return null;
        }
        catch(java.lang.ClassNotFoundException e){
            System.out.println("Cannot read an object");
            return null;
        }
        //DEBUG PRINT : whether received information is correct or not
        //System.out.println(mv);
        return mv;
    }



    public boolean go(List<? extends Command> clist){
        try{
            //1. Send commands
            for(Command c:clist){
                if(!go(c)) return false;
            }
        }
        catch(Exception e){
            return false;
        }
        return true;
    }


    public boolean go(Command c){
        //0. Assume application is waiting for command
        if(!app_stability) return false;

        try{
            //1.1 Send command through ChimpChat.
            c.sendCommand(mDevice);
            app_stability = false;

            //1.2 Send command acknowledgement to App Supervisor
            Packet ack = Packet.getAckCommand();
            oos.writeObject(ack);

            //1.3 Wait for App Supervisor response
            Packet receivingPacket = (Packet) ois.readObject();
            if(receivingPacket.getType() != Packet.PacketType.AckStable){
                //throw new RuntimeException(Application Execution is not guided correctly);
                return false;
            }
            app_stability = true;
        }
        catch(Exception e){
            return false;
        }
        return true;
    }


    public static IChimpDevice getDevice(){ return mDevice; }
}