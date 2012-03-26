package edu.berkeley.wtchoi.cc.interfaces;

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