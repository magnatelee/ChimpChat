package edu.berkeley.wtchoi.cc.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Comparing {
    static public <T extends Comparable<T>> int iterrableCollection(Collection<T> a, Collection<T> b) {
        int temp1 = a.size();
        int temp2 = b.size();

        if (temp1 != temp2)
            return Integer.valueOf(temp1).compareTo(temp2);

        Iterator<T> it1 = a.iterator();
        Iterator<T> it2 = b.iterator();

        while (true) {
            if (it1.hasNext()) {
                if (it2.hasNext()) {
                    int temp = it1.next().compareTo(it2.next());
                    if (temp == 0) continue;
                    return temp;
                } else {
                    return 1;
                }
            } else {
                return -1;
            }
        }
    }
}