package edu.berkeley.wtchoi.cc.util;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collection;
import java.lang.Comparable;

/**
 * Created by IntelliJ IDEA.
 * User: cusgadmin
 * Date: 3/25/12
 * Time: 1:30 AM
 * To change this template use File | Settings | File Templates.
 */


//Comparable Set
public class CSet<T extends Comparable<T>> extends TreeSet<T> implements Set<T>, Comparable<CSet<T>> {
    public int compareTo(CSet<T> target) {
        return Comparing.iterrableCollection(this, target);
    }

    public CSet(Collection<T> collection) {
        super(collection);
    }
}