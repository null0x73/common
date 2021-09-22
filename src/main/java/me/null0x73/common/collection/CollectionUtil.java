package me.null0x73.common.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class CollectionUtil {

    public static <K,V> void process(Map<K,V> map, Consumer<Map.Entry<K,V>> actionForEach){
        processInternal(map.entrySet(), actionForEach, actionForEach, actionForEach);
    }

    public static <K,V> void process(Map<K,V> map, Consumer<Map.Entry<K,V>> actionForFirst, Consumer<Map.Entry<K,V>> actionForMiddle, Consumer<Map.Entry<K,V>> actionForLast){
        processInternal(map.entrySet(), actionForFirst, actionForMiddle, actionForLast);
    }

    private static <T> void processInternal(Collection<T> collection, Consumer<T> actionForFirst, Consumer<T> actionForMiddle, Consumer<T> actionForLast){
        int counter = 0;
        Iterator<T> iterator = collection.iterator();
        while(iterator.hasNext()){
            if (counter == 0) {
                actionForFirst.accept(iterator.next());
            } else if(counter == collection.size()-1){
                actionForLast.accept(iterator.next());
            } else {
                actionForMiddle.accept(iterator.next());
            }
            counter++;
        }
    }

}
