package edu.berkeley.wtchoi.cc;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/23/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class Learning<I extends Alphabet, O extends Alphabet,  M extends Model<I,O>>{
        Learner<I,O,M> learner;
        Teacher<I,O,M> teacher;
        M model;

        public Learning(Learner<I,O,M> l, Teacher<I,O,M> t){
            learner = l;
            teacher = t;
        }

        public void run(){
            while(true){
                //1. Do hypothesis generation, if not finished
                if(!learner.learnedHypothesis()){
                    List<I> question = learner.getQuestion();
                    List<O> answer = teacher.checkMembership(question);
                    learner.learn(question,answer);
                    continue;
                }

                //2. Hypothesis is learned. Check Equivalence
                Pair<List<I>,List<O>> ce = teacher.getCounterExample(learner.getModel());

                //There is no counter example. Thus, we can conclude that learner learned target machine!
                if(ce == null) return;

                //If there is a counter example, teacher learner!
                learner.learnCounterExample(ce);
            }
        }
}

