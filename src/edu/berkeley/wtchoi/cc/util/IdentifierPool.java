package edu.berkeley.wtchoi.cc.util;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/26/12
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class IdentifierPool {
    private static int intSeed = 0;
    
    public static Integer getFreshInteger(){
        return new Integer(intSeed++);
    }
}
