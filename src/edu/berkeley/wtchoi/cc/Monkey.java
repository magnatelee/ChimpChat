package edu.berkeley.wtchoi.cc;

import java.io.*;
import java.util.TreeMap;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.PhysicalButton;
import com.android.chimpchat.core.TouchPressType;
//import com.android.chimpchat.core.IChimpView;

import java.lang.Thread;
import java.util.*;

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
	
	private java.io.ObjectInputStream ois;
	private java.io.ObjectOutputStream oos;

    private boolean cmd_sent = false;

    class ChannelInitiator extends Thread{
        private Monkey mMonkey;
        private boolean result = false;

        ChannelInitiator(Monkey m){
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

	public Monkey(){
		super();
		TreeMap<String,String> options = new TreeMap<String,String>();
		options.put("backend","adb");
		options.put("adbLocation",ADB);
		mChimpchat = ChimpChat.getInstance(options);
	}

    // Initiate application, connect chip, connect channel
	private void init(){
        //1. Initiate Communication Channel (Asynchronous)
        ChannelInitiator initiator = new ChannelInitiator(this);
        initiator.start();

		//2. Initiate Chimpcat Channel with a target device
		mOptions = new java.util.ArrayList<Option>();
		mDevice = mChimpchat.waitForConnection(TIMEOUT, ".*");
		if( mDevice == null){
			throw new RuntimeException("Couldn't connect.");
		}
		mDevice.wake();

        //3. Invoke target application
        String appPackage = "com.android.demo.notepad3";
        String activity = "com.android.demo.notepad3.Notepadv3";
        String runComponent = appPackage + '/' + activity;
        Collection<String> coll = new LinkedList<String>();
        Map<String,Object> extras = new HashMap<String,Object>();
        mDevice.startActivity(null, null, null, null, coll, extras, runComponent, 0);

		//4. Wait for communication channel initiation
        try{
            initiator.join();
            if(!initiator.getResult()){
                throw new RuntimeException("Communication channel cannot be initiated");
            }
        }
        catch(InterruptedException e){
            throw new RuntimeException("Communication initiator interrupted");
        }
	}

	private void shutdown(){
		mChimpchat.shutdown();
		mDevice = null;
	}
	
	private void getStatusReport(){
		//receiving data from application
		MonkeyView mv = getMonkeyViewReport();
		
		//Infer click points from view hierarchy
		TreeSet<Integer> grids_x = new TreeSet<Integer>();
		TreeSet<Integer> grids_y = new TreeSet<Integer>();
		mv.collectGrid(grids_x, grids_y);
		
		extendGrids(grids_x);
		extendGrids(grids_y);
		
		Map<MonkeyView,Pair<Integer,Integer>> map = generatePoints(mv, grids_x, grids_y);

	}
    
    private ViewState getViewStateReport(){
        MonkeyView mv = getMonkeyViewReport();
        return null; // TODO
    }

    private MonkeyView getMonkeyViewReport(){

        //receiving data from application
        //code fragment referring http://www.dreamincode.net/code/snippet1917.html
        MonkeyView mv;

        try{
            mv = (MonkeyView) ois.readObject();
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
        System.out.println(mv);

        return mv;
    }
    
     
	private static void extendGrids(TreeSet<Integer> grids){
		TreeSet<Integer> inter_grids = new TreeSet<Integer>();
		
		Integer prev = 0;
		for(Integer cur : grids){
			if(prev == 0 || prev+1 == cur){
				prev = cur;
				continue;
			}
			inter_grids.add((prev + cur)/2);
			prev = cur;
		}
		grids.addAll(inter_grids);
		
	}
	
	private static Map<MonkeyView,Pair<Integer,Integer>> generatePoints(MonkeyView mv, TreeSet<Integer> grids_x, TreeSet<Integer> grids_y){
		TreeMap<MonkeyView,Pair<Integer,Integer>> map = new TreeMap<MonkeyView,Pair<Integer,Integer>>();
		MonkeyView hit;
		for(Integer x: grids_x){
			for(Integer y: grids_y){
				hit = mv.project(x,y);
				if(hit != null) map.put(hit,new Pair<Integer,Integer>(x,y));
			}
		}
		return map;		
	}
	
	private void decide(){
		if(mOptions.isEmpty()){
			System.out.println("No Options!");
		}
		else{
			java.util.Random rand = new java.util.Random();
			int decision = rand.nextInt(mOptions.size());
			mOptions.get(decision).realize();
            cmd_sent = true;
		}
	}

    private void reset(){
        try{
            oos.writeObject(Packet.getReset());
        }
        catch(IOException e){
            throw new RuntimeException("Cannot send reset");
        }
    }
    
    private void ack(){
        if(!cmd_sent) return;

        try{
            oos.writeObject(Packet.getAck());
        }
        catch(IOException e){
            throw new RuntimeException("Cannot send ack");
        }
    }

    private void touch(Touch t){ //TODO
        mDevice.touch(0,0,TouchPressType.DOWN_AND_UP);
    }
    
    //Application Control Loop
	private List<ViewState> go(List<Touch> touchlist){
        List<ViewState> statelist = new Vector<ViewState>(touchlist.size());
		try{ 
            reset(); //TODO: reset is not considered in current protocol. have to extend it.
            ack();
			Thread.sleep(1000);
            ViewState state = getViewStateReport();
            
			for(Touch t: touchlist){
				touch(t);
                ack();
				Thread.sleep(1000);
                state = getViewStateReport();
                statelist.add(state);
			}
		}
		catch(Exception e){}
        return statelist;
	}
	
	//Main Loop with learning
	public static void main(String[] args){
		final Monkey monkey = new Monkey();
		monkey.init();
        
        LStarLearner<Touch,ViewState,ModelInstance> learner = null;    //TODO
        Oracle<Touch,ViewState,ModelInstance> oracle = null;           //TODO

        //Learning Loop
        while(true){
            Iterable<List<Touch>> questions = learner.getQuestions();
            for(List<Touch> question : questions){
                List<ViewState> answer = monkey.go(question);
                learner.learn(question, answer);
            }
            if(!learner.learnedHypothesis()){
                throw new RuntimeException("Somethings wrong with learner");
            }

            oracle.setModel(learner.getModel());
            if(oracle.isModelCorrect()) 
                break;
            
            Pair<List<Touch>,List<ViewState>> counterexample = oracle.getCounterexample();
            learner.learnCounterexample(counterexample);
        }
        Model m = learner.getModel();
        m.printModel(new BufferedWriter(new OutputStreamWriter(System.out)));

		monkey.shutdown();
	}
    
	public static IChimpDevice getDevice(){ return mDevice; }
}

class Touch{}         //TODO
class ViewState{}     //TODO
class ModelInstance implements Model{ //TODO
    public void printModel(Writer w){}
} 

interface Model{
    public void printModel(Writer w);
}

interface Oracle<I, O, M extends Model>{
    public void setModel(M model);
    public boolean isModelCorrect();
    public Pair<List<I>,List<O>> getCounterexample();
}

interface LStarLearner<I,O, M extends Model>{
    class ConflictingExample extends Exception{}    
    class UnresolvableExampleException extends Exception{}
    
    public boolean learnedHypothesis();
    public Iterable<List<I>> getQuestions();
    
    public void learn(List<I> i, List<O> o);
    public void learnCounterexample(Pair<List<I>, List<O>> ce);
    
    public List<O> calculateTransition(List<I> i);

    public M getModel();
}

