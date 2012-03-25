package edu.berkeley.wtchoi.cc;

import android.view.SurfaceHolder;
import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/24/12
 * Time: 4:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Command{
    public void sendCommand(IChimpDevice target) throws RuntimeException;
}


//Concrete Touch Position. We are going to use this as an input character
class Touch implements Command, Alphabet{//TODO
    private Integer x;
    private Integer y;

    public int compareTo(Alphabet target){
        if(target instanceof Touch){
            Touch t = (Touch)target;
            
            int c1 = x.compareTo(t.getX());
            if(c1 == 0){
                return y.compareTo(t.getY());
            }
            return c1;
        }
        else{
            throw new RuntimeException("Type Error!");
        }
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
