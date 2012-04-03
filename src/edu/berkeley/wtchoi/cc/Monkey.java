package edu.berkeley.wtchoi.cc;

import java.io.*;

import edu.berkeley.wtchoi.cc.interfaces.*;
import edu.berkeley.wtchoi.cc.util.CSet;
import edu.berkeley.wtchoi.cc.interfaces.Command;
//import com.android.chimpchat.core.IChimpView;


public class Monkey {
    public static void main(String args[]) {

        MonkeyControlOption option = new MonkeyControlOption();
        option.fillFromEnvironmentVariables();
        
        MonkeyControl controller = new MonkeyControlImp(option);
        MonkeyTeacher teacher = new MonkeyTeacher(controller);

        if(!teacher.init()) throw new RuntimeException("Cannot initialize teacher");

        Learner<Command, ViewState, AppModel> learner = new LearnerFoo(teacher);// = new PaletteLearnerImp(teacher);

        Learning<Command, ViewState, AppModel> learning = new Learning<Command, ViewState, AppModel>(learner, teacher);
        learning.run();

        Model m = learner.getModel();
        m.printModel(new BufferedWriter(new OutputStreamWriter(System.out)));
    }
}

//View State Information. We are going to use it as output character
class ViewState implements Comparable<ViewState> {
    private CSet<Command> palette;

    public int compareTo(ViewState target) {
        return palette.compareTo(target.palette);
    }

    public ViewState(CSet<Command> palette) {
        this.palette = palette;
    }

    public String toString(){
        return palette.toString();
    }
}

class AppModel implements Model<Command, ViewState> { //TODO

    public void printModel(Writer w) {
    }
}

