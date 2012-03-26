package edu.berkeley.wtchoi.cc.interfaces;

import edu.berkeley.wtchoi.cc.util.CList;
import edu.berkeley.wtchoi.cc.util.CSet;
import edu.berkeley.wtchoi.cc.util.Pair;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Teacher<I extends Comparable<I>, O extends Comparable<O>, M extends Model<I,O>>{
    // Used to implement equivalence query.
    // Should return null if model and target are equivalent
    public Pair<CList<I>,CList<O>> getCounterExample(M model);
    public CList<O> checkMembership(CList<I> input);
}
