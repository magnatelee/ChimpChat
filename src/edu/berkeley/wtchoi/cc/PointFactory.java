package edu.berkeley.wtchoi.cc;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/24/12
 * Time: 9:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PointFactory<T> {
    public T get(int x, int y);
}

class TouchFactory implements PointFactory<Touch>{
    private static TouchFactory instance;

    public Touch get(int x, int y){
        return new Touch(x,y);
    }
    
    static TouchFactory getInstance(){
        return instance;
    }
}
