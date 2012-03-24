package edu.berkeley.wtchoi.cc;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Learner<I extends Alphabet, O extends Alphabet, LI extends List<I>, LO extends List<O>, M extends Model<I,O>>{
    public boolean learnedHypothesis();
    public LI getQuestion();

    public void learn(LI i, LO o);
    public void learnCounterExample(Pair<LI, LO> ce);

    public LO calculateTransition(LI i);

    public M getModel();
}
