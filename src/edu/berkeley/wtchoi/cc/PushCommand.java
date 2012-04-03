package edu.berkeley.wtchoi.cc;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.PhysicalButton;
import com.android.chimpchat.core.TouchPressType;

import edu.berkeley.wtchoi.cc.interfaces.Command;
import edu.berkeley.wtchoi.cc.util.IdentifierPool;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/26/12
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PushCommand implements Command{

    public enum Type{
        MENU;
    }

    private static final Integer typeint = IdentifierPool.getFreshInteger();
    private Type type;

    public void sendCommand(IChimpDevice device){
        switch(this.type){
            case MENU:
                //Code fragment for push MENU button
                device.press(PhysicalButton.MENU, TouchPressType.DOWN_AND_UP);
        }
    }

    private PushCommand(Type t){
        type = t;
    }

    public static PushCommand getMenu(){
        return new PushCommand(Type.MENU);
    }

    public int compareTo(Command target) {
        int c1 = typeint.compareTo(target.typeint());
        if(c1 != 0) return c1;

        PushCommand t = (PushCommand) target;
        return this.compareTo(t);
    }
    
    public int compareTo(PushCommand target){
        return type.compareTo(target.type);
    }
    
    public Integer typeint(){
        return typeint;
    }

    public String toString(){
        return "MENU";
    }
}
