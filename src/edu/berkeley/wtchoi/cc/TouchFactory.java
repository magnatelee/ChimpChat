package edu.berkeley.wtchoi.cc;

import edu.berkeley.wtchoi.cc.interfaces.PointFactory;
import edu.berkeley.wtchoi.cc.interfaces.Command;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TouchFactory implements PointFactory<Command> {
    private static TouchFactory instance;

    public Command get(int x, int y) {
        return new TouchCommand(x, y) ;
    }

    static TouchFactory getInstance() {
        return instance;
    }
}
