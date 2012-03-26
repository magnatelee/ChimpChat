package edu.berkeley.wtchoi.cc;

import edu.berkeley.wtchoi.cc.interfaces.Learner;
import edu.berkeley.wtchoi.cc.interfaces.TeacherP;
import edu.berkeley.wtchoi.cc.util.CList;
import edu.berkeley.wtchoi.cc.util.CSet;
import edu.berkeley.wtchoi.cc.util.CVector;
import edu.berkeley.wtchoi.cc.util.Pair;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class LearnerFoo implements Learner<Touch, ViewState, AppModel> {
    private TeacherP<Touch, ViewState, AppModel> teacher;
    private Map<CList<Touch>, CList<ViewState>> iomap;
    private Set<CList<Touch>> nexts;

    public LearnerFoo(TeacherP<Touch, ViewState, AppModel> teacher) {
        this.teacher = teacher;
        iomap = new TreeMap<CList<Touch>, CList<ViewState>>();
        nexts = new TreeSet<CList<Touch>>();
        CSet<Touch> initialPalette = teacher.getPalette(null);
        nexts.addAll(makeInputs(null, initialPalette));
    }

    private Collection<CList<Touch>> makeInputs(CList<Touch> prefix, CSet<Touch> alphabet) {
        if (prefix == null) {
            prefix = new CVector<Touch>();
        }

        Collection<CList<Touch>> set = new TreeSet<CList<Touch>>();

        for (Touch t : alphabet) {
            CList<Touch> new_input = new CVector<Touch>(prefix);
            new_input.add(t);
            set.add(new_input);
        }

        return set;
    }

    public boolean learnedHypothesis() {
        return false;  // Do nothing
    }

    public CList<Touch> getQuestion() {
        if (nexts.isEmpty()) return null;
        return nexts.iterator().next();
    }

    public void learn(CList<Touch> input, CList<ViewState> output) {
        if (!iomap.containsKey(input)) {
            CSet<Touch> palette = teacher.getPalette(input);
            nexts.addAll(makeInputs(input, palette));
        }
        iomap.put(input, output);
    }

    public void learnCounterExample(Pair<CList<Touch>, CList<ViewState>> ce) {
    } // Do nothing

    public CList<ViewState> calculateTransition(CList<Touch> input) {
        CList<ViewState> result = iomap.get(input);
        return result;
    }

    public AppModel getModel() {
        return null;
    }
}