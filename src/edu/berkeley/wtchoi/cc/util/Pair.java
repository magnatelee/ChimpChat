package edu.berkeley.wtchoi.cc.util;

//generic pair
//code from http://www.factsandpeople.com/facts-mainmenu-5/8-java/10-java-pair-class?start=1
public class Pair<A, B>{

    public A fst;
    public B snd;

    public Pair(A fst, B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public A getFirst() { return fst; }
    public B getSecond() { return snd; }

    public void setFirst(A v) { fst = v; }
    public void setSecond(B v) { snd = v; }

    public String toString() {
        return "Pair[" + fst + "," + snd + "]";
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    public boolean equals(Object other) {
        return
                other instanceof Pair &&
                        equals(fst, ((Pair)other).fst) &&
                        equals(snd, ((Pair)other).snd);
    }

    public int hashCode() {
        if (fst == null) return (snd == null) ? 0 : snd.hashCode() + 1;
        else if (snd == null) return fst.hashCode() + 2;
        else return fst.hashCode() * 17 + snd.hashCode();
    }

    public static <A,B> Pair<A,B> of(A a, B b) {
        return new Pair<A,B>(a,b);
    }
}

class CPair<A extends Comparable<A>,B extends Comparable<B>> extends Pair<A,B> implements Comparable<CPair<A,B>>{
    public int compareTo(CPair<A,B> target){
        int temp = this.fst.compareTo(target.fst);
        if(temp !=0) return temp;
        return this.snd.compareTo(target.snd);
    }
    
    public CPair(A fst, B snd){ super(fst,snd); }
}

