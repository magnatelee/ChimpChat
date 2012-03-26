package edu.berkeley.wtchoi.cc;

import edu.berkeley.wtchoi.cc.interfaces.PointFactory;
import edu.berkeley.wtchoi.cc.util.CSet;

import java.lang.Comparable;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collection;
import java.io.BufferedWriter;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;


public class MonkeyView implements Serializable, Comparable {
    /**
     *
     */
    private static final long serialVersionUID = -5186309675577891457L;

    private int x;
    private int y;
    private int width;
    private int height;
    private LinkedList<MonkeyView> children;

    @SuppressWarnings("unchecked")
    public MonkeyView(int ix, int iy, int iw, int ih, LinkedList<MonkeyView> ic) {
        x = ix;
        y = iy;
        width = iw;
        height = ih;

        if (ic != null) children = (LinkedList<MonkeyView>) ic.clone();
        else children = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }


    public String toString() {
        java.io.StringWriter sw = new java.io.StringWriter();
        BufferedWriter buffer = new BufferedWriter(sw);
        try {
            MonkeyView.toString(buffer, this, 0);
            buffer.flush();
        } catch (Exception e) {
            System.out.println("Error occur!");
        }

        return sw.toString();
    }

    private static void toString(BufferedWriter buffer, MonkeyView mv, int depth) throws java.io.IOException {
        for (int i = 0; i < depth; i++) {
            buffer.write("  ");
        }

        buffer.write("<");
        buffer.write(Integer.toString(mv.x));
        buffer.write(",");
        buffer.write(Integer.toString(mv.y));
        buffer.write(",");
        buffer.write(Integer.toString(mv.width));
        buffer.write(",");
        buffer.write(Integer.toString(mv.height));
        buffer.write(">");
        buffer.newLine();

        if (mv.children == null) return;

        for (MonkeyView child : mv.children) {
            toString(buffer, child, depth + 1);
        }
    }


    //Function to collect representative click positions for each equivalence class
    public <T extends Comparable<T>> CSet<T> getRepresentativePoints(PointFactory<T> factory) {
        //Infer click points from view hierarchy
        TreeSet<Integer> grids_x = new TreeSet<Integer>();
        TreeSet<Integer> grids_y = new TreeSet<Integer>();
        this.collectGrid(grids_x, grids_y);

        extendGrids(grids_x);
        extendGrids(grids_y);

        Collection<T> values = generatePoints(grids_x, grids_y, factory).values();
        return new CSet<T>(values);
    }

    private void collectGrid(Collection<Integer> grids_x, Collection<Integer> grids_y) {
        collectGrid(grids_x, grids_y, 0, 0);
    }

    private void collectGrid(Collection<Integer> grids_x, Collection<Integer> grids_y, int px, int py) {
        int my_x = px + this.x;
        int my_y = py + this.y;

        //System.out.println("!!!" + this.y);
        //System.out.println("!!!" + py);

        grids_x.add(my_x);
        grids_x.add(my_x + this.width);
        grids_y.add(my_y);
        grids_y.add(my_y + this.height);

        if (this.children == null) return;
        for (MonkeyView child : this.children) {
            child.collectGrid(grids_x, grids_y, my_x, my_y);
        }
    }


    public MonkeyView project(Integer x, Integer y) {
        try {
            return this.project(x, y, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private MonkeyView project(Integer x, Integer y, int px, int py) {
        int my_x = this.x + px;
        int my_y = this.y + py;

        //Assumption : parent include children
        if (my_x > x || my_x + this.width <= x)
            if (my_y > y || my_y + this.height <= y)
                return null;

        //If there is no children, just return myself
        if (children == null) return this;

        //Assumption : children never intersect
        MonkeyView projected_child;
        for (MonkeyView child : children) {
            projected_child = child.project(x, y, my_x, my_y);
            if (projected_child != null)
                return projected_child;
        }

        //The point hit no child.
        //Thus return my self.
        return this;
    }

    private static void extendGrids(TreeSet<Integer> grids) {
        TreeSet<Integer> inter_grids = new TreeSet<Integer>();

        Integer prev = 0;
        for (Integer cur : grids) {
            if (prev == 0 || prev + 1 == cur) {
                prev = cur;
                continue;
            }
            inter_grids.add((prev + cur) / 2);
            prev = cur;
        }
        grids.addAll(inter_grids);

    }

    private <T> Map<MonkeyView, T> generatePoints(TreeSet<Integer> grids_x, TreeSet<Integer> grids_y, PointFactory<T> factory) {
        TreeMap<MonkeyView, T> map = new TreeMap<MonkeyView, T>();
        MonkeyView hit;
        for (Integer x : grids_x) {
            for (Integer y : grids_y) {
                hit = this.project(x, y);
                if (hit != null) map.put(hit, factory.get(x, y));
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    LinkedList<MonkeyView> getChildren() {
        return (LinkedList<MonkeyView>) children.clone();
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
