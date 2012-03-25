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
interface Teacher<I extends Alphabet, O extends Alphabet, M extends Model<I,O>>{
    // Used to implement equivalence query.
    // Should return null if model and target are equivalent
    public Pair<List<I>,List<O>> getCounterExample(M model);
    public List<O> checkMembership(List<I> input);
}

interface TeacherP<I extends Alphabet, O extends Alphabet, M extends Model<I,O>> extends Teacher<I,O,M>{
    public Collection<I> getPalette(List<I> input);
}

