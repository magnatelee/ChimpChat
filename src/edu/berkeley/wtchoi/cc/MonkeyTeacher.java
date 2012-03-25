package edu.berkeley.wtchoi.cc;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/24/12
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class MonkeyTeacher implements TeacherP<Touch, ViewState, AppModel>{

    private MonkeyControl controler;
    private TreeMap<List<Touch>,Collection<Touch>> paletteTable;
    
    private PointFactory<Touch> pointFactory;

    public MonkeyTeacher(MonkeyControl imp){
        controler = imp;
        pointFactory = TouchFactory.getInstance();
    }

    public Pair<List<Touch>,List<ViewState>> getCounterExample(AppModel model) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Vector<ViewState> checkMembership(List<Touch> input) {
        Vector<ViewState> output = new Vector<ViewState>(input.size());
        for(Touch t: input){
            controler.go(t); //TODO: Error handling
            Collection<Touch> palette = controler.getView().getRepresentativePoints(pointFactory);
            ViewState state = new ViewState(palette);
            output.add(state);
        }

        return output;
        //TODO: properly comparing everything
    }

    public Collection<Touch> getPalette(List<Touch> input){
        Collection<Touch> palette = paletteTable.get(input);
        
        if(palette == null){// If null, run application to reach desired state and acquire palette
            boolean result = controler.go(input);
            if(result){
                MonkeyView view = controler.getView();
                if(view != null) return null;
                
                palette = view.getRepresentativePoints(pointFactory);
                paletteTable.put(input,palette);

                //TODO: filling palette table
            }
            else{
                return null;
            }
        }

        return palette;
    }

    public boolean init(){
        //1. Initiate connection with application
        if(! controler.connectToDevice()) return false;
        if(! controler.initiateApp()) return false;

        //2. Warming palette table for initial state
        MonkeyView view = controler.getView();
        if(view != null) return false;
        
        Collection<Touch> initialPalette = view.getRepresentativePoints(pointFactory);
        paletteTable.put(new Vector<Touch>(),initialPalette);

        return true;
    }
}