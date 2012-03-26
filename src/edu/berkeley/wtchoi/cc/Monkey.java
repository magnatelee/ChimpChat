package edu.berkeley.wtchoi.cc;

import java.io.*;

import edu.berkeley.wtchoi.cc.interfaces.*;
import edu.berkeley.wtchoi.cc.util.CSet;
import edu.berkeley.wtchoi.cc.interfaces.Command;
//import com.android.chimpchat.core.IChimpView;


//Code fragment for push MENU button
//MonkeyControlImp.getDevice().press(PhysicalButton.MENU, TouchPressType.DOWN_AND_UP);


public class Monkey {
    public void main(String args) {
        MonkeyControl controller = new MonkeyControlImp();
        TeacherP<Command, ViewState, AppModel> teacher = new MonkeyTeacher(controller);
        Learner<Command, ViewState, AppModel> learner = new LearnerFoo(teacher);// = new PaletteLearnerImp(teacher);

        Learning<Command, ViewState, AppModel> learning = new Learning<Command, ViewState, AppModel>(learner, teacher);
        learning.run();

        Model m = learner.getModel();
    }
}

//View State Information. We are going to use it as output character
class ViewState implements Comparable<ViewState> {
    private CSet<Command> palette;

    public int compareTo(ViewState target) {
        ViewState t = (ViewState) target;
        return palette.compareTo(t.palette);
    }

    public ViewState(CSet<Command> palette) {
        this.palette = palette;
    }
}

class AppModel implements Model<Command, ViewState> { //TODO

    public void printModel(Writer w) {
    }
}

