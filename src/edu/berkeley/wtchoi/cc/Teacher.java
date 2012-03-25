package edu.berkeley.wtchoi.cc;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
interface Teacher<I extends Comparable<I>, O extends Comparable<O>, M extends Model<I,O>>{
    // Used to implement equivalence query.
    // Should return null if model and target are equivalent
    public Pair<CList<I>,CList<O>> getCounterExample(M model);
    public CList<O> checkMembership(CList<I> input);
}

interface TeacherP<I extends Comparable<I>, O extends Comparable<O>, M extends Model<I,O>> extends Teacher<I,O,M>{
    public CSet<I> getPalette(CList<I> input);
}

