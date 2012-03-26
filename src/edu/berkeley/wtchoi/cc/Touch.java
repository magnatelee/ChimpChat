package edu.berkeley.wtchoi.cc;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import edu.berkeley.wtchoi.cc.interfaces.Command;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */

//Concrete Touch Position. We are going to use this as an input character
public class Touch implements Command, Comparable<Touch> {//TODO
    private Integer x;
    private Integer y;

    public int compareTo(Touch target){
        int c1 = x.compareTo(target.x);
        if(c1 == 0){
            return y.compareTo(target.y);
        }
        return c1;
    }

    public Touch(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public void sendCommand(IChimpDevice target){
        target.touch(x,y, TouchPressType.DOWN_AND_UP);
    }

    public Integer getX(){return x;}
    public Integer getY(){return y;}
}
