package edu.berkeley.wtchoi.cc.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: wtchoi
 * Date: 3/26/12
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */

public class CollectionUtil {
    static public <T extends Comparable<T>> int compare(Collection<T> a, Collection<T> b) {
        int temp1 = a.size();
        int temp2 = b.size();

        if (temp1 != temp2)
            return Integer.valueOf(temp1).compareTo(temp2);

        Iterator<T> it1 = a.iterator();
        Iterator<T> it2 = b.iterator();

        while (true) {
            if (it1.hasNext()) {
                if (it2.hasNext()) {
                    int temp = it1.next().compareTo(it2.next());
                    if (temp == 0) continue;
                    return temp;
                } else {
                    return 1;
                }
            } else {
                return -1;
            }
        }
    }

    static public <E,T> void writeTo(Collection<E> c, String opener, String separator, String closer, Writer writer)
        throws IOException {
        
        Iterator<E> iterator = c.iterator();

        if(!iterator.hasNext()){
            writer.write(opener);
            writer.write(closer);
            return;
        }

        writer.write(opener);
        do{
            E element = iterator.next();
            writer.write(element.toString());

            if(!iterator.hasNext()) break;
            writer.write(separator);
        }
        while(true);
        writer.write(closer);
    }
    
    static public <T> String stringOf(Collection<T> collection, String opener, String separator, String closer){
        StringWriter writer = new StringWriter();
        try {
            CollectionUtil.writeTo(collection, opener, separator, closer, writer);
            return writer.toString();
        }
        catch(IOException e){
            //Failed to generate string...
            return null;
        }
    }
}
