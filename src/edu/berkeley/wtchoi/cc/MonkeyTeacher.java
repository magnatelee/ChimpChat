package edu.berkeley.wtchoi.cc;

import edu.berkeley.wtchoi.cc.interfaces.MonkeyControl;
import edu.berkeley.wtchoi.cc.interfaces.PointFactory;
import edu.berkeley.wtchoi.cc.interfaces.TeacherP;
import edu.berkeley.wtchoi.cc.util.CList;
import edu.berkeley.wtchoi.cc.util.CSet;
import edu.berkeley.wtchoi.cc.util.CVector;
import edu.berkeley.wtchoi.cc.util.Pair;

import java.util.TreeMap;


/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/24/12
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class MonkeyTeacher implements TeacherP<Touch, ViewState, AppModel> {

    private MonkeyControl controller;
    private TreeMap<CList<Touch>,CSet<Touch>> paletteTable;
    
    private PointFactory<Touch> pointFactory;

    public MonkeyTeacher(MonkeyControl imp){
        controller = imp;
        pointFactory = TouchFactory.getInstance();
    }

    public Pair<CList<Touch>,CList<ViewState>> getCounterExample(AppModel model) {
        // TODO: implement getCounterExample query
        return null;
    }

    //This implementation only put last palette to palette table.
    public CList<ViewState> checkMembership(CList<Touch> input) {
        if(input == null) return null;
        if(input.size() == 0) return null;

        CVector<ViewState> output = new CVector<ViewState>(input.size());
        CSet<Touch> palette = null;

        for(Touch t: input){
            if(!controller.go(t))
                return null;

            palette = controller.getView().getRepresentativePoints(pointFactory);
            if(palette == null)
                return null;

            ViewState state = new ViewState(palette);
            output.add(state);
        }
        paletteTable.put(input,palette);
        
        return output;
    }

    public CSet<Touch> getPalette(CList<Touch> input){
        CSet<Touch> palette = paletteTable.get(input);
        
        if(palette == null){// If null, run application to reach desired state and acquire palette
            boolean result = controller.go(input);
            if(result){
                MonkeyView view = controller.getView();
                if(view != null) return null;
                
                palette = view.getRepresentativePoints(pointFactory);
                paletteTable.put(input,palette);
            }
            else{
                return null;
            }
        }

        return palette;
    }

    public boolean init(){
        //1. Initiate connection with application
        if(! controller.connectToDevice()) return false;
        if(! controller.initiateApp()) return false;

        //2. Warming palette table for initial state
        MonkeyView view = controller.getView();
        if(view != null) return false;
        
        CSet<Touch> initialPalette = view.getRepresentativePoints(pointFactory);
        paletteTable.put(new CVector<Touch>(), initialPalette);

        return true;
    }
}