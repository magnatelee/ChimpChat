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
        MonkeyControlOption option = new MonkeyControlOption();
        option.setADB("/Applications/Android//android-sdk-mac_x86/platform-tools/adb");
        option.setApplicationPackage("com.android.demo.notepad3");
        option.setMainActivity("com.android.demo.notepad3.Notepadv3");
        
        MonkeyControl controller = new MonkeyControlImp(option);
        MonkeyTeacher teacher = new MonkeyTeacher(controller);
        teacher.init();

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
}

class AppModel implements Model<Command, ViewState> { //TODO

    public void printModel(Writer w) {
    }
}

