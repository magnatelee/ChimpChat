package edu.berkeley.wtchoi.cc;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import edu.berkeley.wtchoi.cc.interfaces.Command;
import edu.berkeley.wtchoi.cc.util.IdentifierPool;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/26/12
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
//Concrete Command Position. We are going to use this as an input character
public class TouchCommand implements Command{//TODO
    private Integer x;
    private Integer y;

    //All implementation of command should obtain integer identifier from
    private static final Integer tint = IdentifierPool.getFreshInteger();

    public int compareTo(Command target) {
        int c1 = tint.compareTo(target.typeint());
        if(c1 != 0) return c1;

        TouchCommand t = (TouchCommand) target;
        return this.compareTo(t);
    }
    
    public int compareTo(TouchCommand target){
        int c1 = x.compareTo(target.x);
        if (c1 == 0) {
            return y.compareTo(target.y);
        }
        return c1;
    }

    public TouchCommand(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void sendCommand(IChimpDevice target) {
        target.touch(x, y, TouchPressType.DOWN_AND_UP);
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer typeint(){
        return tint;
    }
    
    public String toString(){
        return ("(" + x + "," + y + ")");
    }
}
