package edu.berkeley.wtchoi.cc;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.lang.Comparable;

/**
 * Created by IntelliJ IDEA.
 * User: cusgadmin
 * Date: 3/25/12
 * Time: 1:30 AM
 * To change this template use File | Settings | File Templates.
 */


//Comparable Set
class CSet<T extends Comparable<T>> extends TreeSet<T> implements Set<T>, Comparable<CSet<T>> {
    public int compareTo(CSet<T> target){
        return Comparing.iterCollection(this, target) ;
    }
    
    public CSet(Collection<T> collection){
        super(collection);
    }
}

interface CList<T extends Comparable<T>> extends Comparable<CList<T>>, List<T>{}

//Comparable Vector
class CVector<T extends Comparable<T>> extends Vector<T> implements CList<T>{
    public int compareTo(CList<T> target){
        return Comparing.iterCollection(this, target);
    }

    public CVector(int size){ super(size); }

    public CVector(){ super(); }
}

class Comparing{
    static public <T extends Comparable<T>> int iterCollection(Collection<T> a, Collection<T> b){
        int temp1 = a.size();
        int temp2 = b.size();
        
        if(temp1 != temp2)
            return Integer.valueOf(temp1).compareTo(temp2);
        
        Iterator<T> it1 = a.iterator();
        Iterator<T> it2 = b.iterator();

        while(true){
            if(it1.hasNext()){
                if(it2.hasNext()){
                    int temp = it1.next().compareTo(it2.next());
                    if(temp == 0) continue;
                    return temp;
                }
                else{
                    return 1;
                }
            }
            else{
                return -1;
            }
        }
    }
}