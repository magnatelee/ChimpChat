package edu.berkeley.wtchoi.cc.interfaces;

import edu.berkeley.wtchoi.cc.MonkeyView;

import java.util.List;

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
