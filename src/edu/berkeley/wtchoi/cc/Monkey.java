package edu.berkeley.wtchoi.cc;

import java.io.*;

import com.android.chimpchat.core.PhysicalButton;
import com.android.chimpchat.core.TouchPressType;
//import com.android.chimpchat.core.IChimpView;


//Code fragment for push MENU button
//MonkeyControlImp.getDevice().press(PhysicalButton.MENU, TouchPressType.DOWN_AND_UP);


public class Monkey{
    public void main(String args){
        MonkeyControl controler = new MonkeyControlImp();
        TeacherP<Touch,ViewState,AppModel> teacher = new MonkeyTeacher(controler);
        Learner<Touch,ViewState,AppModel> learner = null;// = new PaletteLearnerImp(teacher);
        
        Learning<Touch,ViewState,AppModel> learning = new Learning<Touch, ViewState, AppModel>(learner, teacher);
        learning.run();

        Model m = learner.getModel();
    }
}

//View State Information. We are going to use it as output character
class ViewState implements Comparable<ViewState>{
    private CSet<Touch> palette;
    
    public int compareTo(ViewState target){
        ViewState t = (ViewState) target;
        return palette.compareTo(t.palette);
    }
    
    public ViewState(CSet<Touch> palette){
        this.palette = palette;
    }
}

class AppModel implements Model<Touch,ViewState>{ //TODO
    public void printModel(Writer w){}
}

interface Model<I extends Comparable<I>, O extends Comparable<O>>{
    public void printModel(Writer w);
}
