package edu.berkeley.wtchoi.cc;

import edu.berkeley.wtchoi.cc.interfaces.Learner;
import edu.berkeley.wtchoi.cc.interfaces.TeacherP;
import edu.berkeley.wtchoi.cc.util.CList;
import edu.berkeley.wtchoi.cc.util.CSet;
import edu.berkeley.wtchoi.cc.util.CVector;
import edu.berkeley.wtchoi.cc.util.Pair;
import edu.berkeley.wtchoi.cc.interfaces.Command;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class LearnerFoo implements Learner<Command, ViewState, AppModel> {
    private TeacherP<Command, ViewState, AppModel> teacher;
    private Map<CList<Command>, CList<ViewState>> iomap;
    private Set<CList<Command>> nexts;

    public LearnerFoo(TeacherP<Command, ViewState, AppModel> teacher) {
        this.teacher = teacher;
        iomap = new TreeMap<CList<Command>, CList<ViewState>>();
        nexts = new TreeSet<CList<Command>>();
        CSet<Command> initialPalette = teacher.getPalette(null);
        nexts.addAll(makeInputs(null, initialPalette));
    }

    private Collection<CList<Command>> makeInputs(CList<Command> prefix, CSet<Command> alphabet) {
        if (prefix == null) {
            prefix = new CVector<Command>();
        }

        Collection<CList<Command>> set = new TreeSet<CList<Command>>();

        for (Command t : alphabet) {
            CList<Command> new_input = new CVector<Command>(prefix);
            new_input.add(t);
            set.add(new_input);
        }

        return set;
    }

    public boolean learnedHypothesis() {
        return false;  // Do nothing
    }

    public CList<Command> getQuestion() {
        if (nexts.isEmpty()) return null;
        return nexts.iterator().next();
    }

    public void learn(CList<Command> input, CList<ViewState> output) {
        if (!iomap.containsKey(input)) {
            CSet<Command> palette = teacher.getPalette(input);
            nexts.addAll(makeInputs(input, palette));
        }
        iomap.put(input, output);
    }

    public void learnCounterExample(Pair<CList<Command>, CList<ViewState>> ce) {
    } // Do nothing

    public CList<ViewState> calculateTransition(CList<Command> input) {
        CList<ViewState> result = iomap.get(input);
        return result;
    }

    public AppModel getModel() {
        return null;
    }
}