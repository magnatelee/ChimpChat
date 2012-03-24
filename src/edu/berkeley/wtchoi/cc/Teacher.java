package edu.berkeley.wtchoi.cc;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
interface Teacher<I extends Alphabet, O extends Alphabet, LI extends List<I>, LO extends List<O>, M extends Model<I,O>>{
    // Used to implement equivalence query.
    // Should return null if model and target are equivalent
    public Pair<LI,LO> getCounterExample(M model);
    public LO checkMembership(LI input);
}

interface TeacherP<I extends Alphabet, O extends Alphabet, L extends List, LI extends List<I>, LO extends List<O>, M extends Model<I,O>> extends Teacher{
    public Collection<I> getPalette(LI input);
}

