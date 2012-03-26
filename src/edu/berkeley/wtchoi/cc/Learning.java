package edu.berkeley.wtchoi.cc;


import edu.berkeley.wtchoi.cc.interfaces.Learner;
import edu.berkeley.wtchoi.cc.interfaces.Model;
import edu.berkeley.wtchoi.cc.interfaces.Teacher;
import edu.berkeley.wtchoi.cc.util.CList;
import edu.berkeley.wtchoi.cc.util.Pair;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class Learning<I extends Comparable<I>, O extends Comparable<O>, M extends Model<I, O>> {
    private Learner<I, O, M> learner;
    private Teacher<I, O, M> teacher;
    private M model;

    public Learning(Learner<I, O, M> l, Teacher<I, O, M> t) {
        learner = l;
        teacher = t;
    }

    public void run() {
        while (true) {
            //1. Do hypothesis generation, if not finished
            if (!learner.learnedHypothesis()) {
                try {
                    System.out.println("Stroke any key to go");
                    System.in.read();
                } catch (Exception e) {
                }

                CList<I> question = learner.getQuestion();
                CList<O> answer = teacher.checkMembership(question);
                learner.learn(question, answer);
                continue;
            }

            //2. Hypothesis is learned. Check Equivalence
            Pair<CList<I>, CList<O>> ce = teacher.getCounterExample(learner.getModel());

            //There is no counter example. Thus, we can conclude that learner learned target machine!
            if (ce == null) return;

            //If there is a counter example, teacher learner!
            learner.learnCounterExample(ce);
        }
    }
}