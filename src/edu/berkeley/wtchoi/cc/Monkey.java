package edu.berkeley.wtchoi.cc;

import java.util.TreeMap;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.PhysicalButton;
import com.android.chimpchat.core.TouchPressType;
//import com.android.chimpchat.core.IChimpView;

import java.lang.Thread;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

interface Option{
	public void realize();
}

enum KeyOption implements Option{
	MENU;
		
	public void realize(){
		switch (this){
		case MENU:
			Monkey.getDevice().press(PhysicalButton.MENU, TouchPressType.DOWN_AND_UP);
			System.out.println("Press Menu");
			break;
		}
	}
}

class TouchOption implements Option{
	TouchOption(){
		/* XXX */
	}
	
	private void findPoint(int point[]){
		point[0] = 10;
		point[1] = 10; /* XXX */
	}
	
	public void realize(){
		int point[] = new int[2];
		findPoint(point);
		Monkey.getDevice().touch(point[0], point[1] , TouchPressType.DOWN_AND_UP);
		System.out.println("Touch");
	}
}


public class Monkey {
	private static final String ADB = "/Applications/Android//android-sdk-mac_x86/platform-tools/adb";
	private static final long TIMEOUT = 5000;
	private ChimpChat mChimpchat;
	private static IChimpDevice mDevice;
	private List<Option> mOptions;
	
	public Monkey(){
		super();
		TreeMap<String,String> options = new TreeMap<String,String>();
		options.put("backend","adb");
		options.put("adbLocation",ADB);
		mChimpchat = ChimpChat.getInstance(options);
	}
	
	private void init(){
		mOptions = new java.util.ArrayList<Option>();
		mDevice = mChimpchat.waitForConnection(TIMEOUT, ".*");
		if( mDevice == null){
			throw new RuntimeException("Couldn't connect.");
		}
		mDevice.wake();
	}
	
	private void shutdown(){
		mChimpchat.shutdown();
		mDevice = null;
	}
	
	private void getStatusReport(){
		//code fragment refering http://www.dreamincode.net/code/snippet1917.html
		java.net.ServerSocket serverSocket;
		java.net.Socket socket;
		java.io.ObjectInputStream ois;
		MonkeyView mv;
		
		try{
			serverSocket = new java.net.ServerSocket(13339,0,java.net.InetAddress.getByName("128.32.45.173"));
			System.out.println("wait");
			socket = serverSocket.accept();
			System.out.println("go");
			
			ois = new java.io.ObjectInputStream(socket.getInputStream());
			mv = (MonkeyView) ois.readObject();
			
			ois.close();
			socket.close();
			serverSocket.close();
		}
		catch(java.io.IOException e){
			System.out.println("Exception on new ServerSocket");
			return;
		}
		catch(java.lang.ClassNotFoundException e){
			System.out.println("Cannot read an object");
			return;
		}
		
		System.out.println(mv);
		
		Z3Config config = Z3.mk_config();
		Z3Context context = Z3.mk_context(config);
		Z3.del_config(config);
		
	}
	
	private void decide(){
		if(mOptions.isEmpty()){
			System.out.println("No Options!");
		}
		else{
			java.util.Random rand = new java.util.Random();
			int decision = rand.nextInt(mOptions.size());
			mOptions.get(decision).realize();
		}
	}
	
	private void go(){
		String appPackage = "com.android.demo.notepad3";
		String activity = "com.android.demo.notepad3.Notepadv3";
		String runComponent = appPackage + '/' + activity;
		Collection<String> coll = new LinkedList<String>();
		Map<String,Object> extras = new HashMap<String,Object>();
		mDevice.startActivity(null, null, null, null, coll, extras, runComponent, 0);
		try{ 
			Thread.sleep(1000);
			while(true){
				getStatusReport();
				decide();
				Thread.sleep(1000);
				
				/*System.out.println("Iterate!");
				
				System.out.println("progress");
				for(IChimpView c : root.getChildren()){
					System.out.println(c.getLocation().getHeight());
				}
				mDevice.press(PhysicalButton.MENU, TouchPressType.DOWN_AND_UP);
				Thread.sleep(1000);*/
			}
		}
		catch(Exception e){}
	}
	
	
	public static void main(String[] args){
		final Monkey monkey = new Monkey();
		monkey.init();
		monkey.go();
		monkey.shutdown();
	}
	
	public static IChimpDevice getDevice(){ return mDevice; }
}
