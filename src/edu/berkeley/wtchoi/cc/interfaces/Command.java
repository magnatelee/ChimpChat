package edu.berkeley.wtchoi.cc.interfaces;

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