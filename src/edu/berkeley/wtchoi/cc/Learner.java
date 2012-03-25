package edu.berkeley.wtchoi.cc;


/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Learner<I extends Comparable<I>, O extends Comparable<O>,  M extends Model<I,O>>{
    public boolean learnedHypothesis();
    public CList<I> getQuestion();

    public void learn(CList<I> i, CList<O> o);
    public void learnCounterExample(Pair<CList<I>, CList<O>> ce);

    public CList<O> calculateTransition(CList<I> i);

    public M getModel();
}