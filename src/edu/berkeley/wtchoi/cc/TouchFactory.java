package edu.berkeley.wtchoi.cc;

import edu.berkeley.wtchoi.cc.interfaces.PointFactory;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TouchFactory implements PointFactory<Touch> {
    private static TouchFactory instance;

    public Touch get(int x, int y){
        return new Touch(x,y);
    }

    static TouchFactory getInstance(){
        return instance;
    }
}
