package edu.berkeley.wtchoi.cc.util;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/25/12
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */

public interface CList<T extends Comparable<T>> extends Comparable<CList<T>>, List<T> {
}

