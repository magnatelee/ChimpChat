package edu.berkeley.wtchoi.cc;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Learner<I extends Alphabet, O extends Alphabet,  M extends Model<I,O>>{
    public boolean learnedHypothesis();
    public List<I> getQuestion();

    public void learn(List<I> i, List<O> o);
    public void learnCounterExample(Pair<List<I>, List<O>> ce);

    public List<O> calculateTransition(List<I> i);

    public M getModel();
}