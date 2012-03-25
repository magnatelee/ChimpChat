package edu.berkeley.wtchoi.cc;

import java.io.*;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.PhysicalButton;
import com.android.chimpchat.core.TouchPressType;
//import com.android.chimpchat.core.IChimpView;

import java.util.*;

interface Option{
	public void realize();
}

enum KeyOption implements Option{
	MENU;
		
	public void realize(){
		switch (this){
		case MENU:
			MonkeyControlImp.getDevice().press(PhysicalButton.MENU, TouchPressType.DOWN_AND_UP);
			System.out.println("Press Menu");
			break;
		}
	}
}

public class Monkey{
    public void main(String args){
        MonkeyControl controler = new MonkeyControlImp();
        TeacherP<Touch,ViewState,AppModel> teacher = new MonkeyTeacher(controler);
        Learner<Touch,ViewState,AppModel> learner = null;// = new MonkeyLearner();
        
        Learning<Touch,ViewState,AppModel> learning = new Learning<Touch, ViewState, AppModel>(learner, teacher);
        learning.run();

        Model m = learner.getModel();
    }
}

//View State Information. We are going to use it as output character
class ViewState implements Alphabet{
    private Collection<Touch> palette;
    
    public int compareTo(Alphabet target){
        if(target instanceof ViewState){
            ViewState t = (ViewState) target;
            return palette.compareTo(t.palette);
        }
        else{
            throw new RuntimeException("Type Error");
        }
    }
    
    public ViewState(Collection<Touch> palette){
        this.palette = palette;
    }
}

class AppModel implements Model<Touch,ViewState>{ //TODO
    public void printModel(Writer w){}
}

interface Model<I extends Alphabet, O extends Alphabet>{
    public void printModel(Writer w);
}
