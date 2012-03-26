package edu.berkeley.wtchoi.cc.util;

import java.util.Collection;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
//Comparable Vector
public class CVector<T extends Comparable<T>> extends Vector<T> implements CList<T> {
    public int compareTo(CList<T> target) {
        return Comparing.iterrableCollection(this, target);
    }

    public CVector(int size) {
        super(size);
    }

    public CVector() {
        super();
    }

    public CVector(Collection<T> collection) {
        super(collection);
    }
}
