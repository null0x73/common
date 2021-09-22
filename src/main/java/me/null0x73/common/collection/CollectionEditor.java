package me.null0x73.common.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionEditor {

    public static class MapEditor<K,V> {
        private Map<K, V> map;
        public MapEditor(Map<K, V> map) {
            this.map = map!=null ? map : new HashMap<>();
        }
        public MapEditor<K,V> put(K key, V value){
            map.put(key, value);
            return this;
        }
        public <T> MapEditor<K,V> put(List<T> sourceList, Function<T,K> keyProvider, Function<T,V> valueProvider){
            sourceList.forEach(sourceObject -> map.put(keyProvider.apply(sourceObject), valueProvider.apply(sourceObject)));
            return this;
        }
        public <T> MapEditor<K,V> put(List<T> sourceList, Predicate<T> blacklistFilter, Function<T,K> keyProvider, Function<T,V> valueProvider){
            sourceList.stream().filter(blacklistFilter).forEach(sourceObject -> map.put(keyProvider.apply(sourceObject), valueProvider.apply(sourceObject)));
            return this;
        }
        public Map<K,V> save(){
            return new HashMap<>(map);
        }
    }

    public static <K,V> MapEditor<K,V> forMap(Map<K, V> map){
        return new MapEditor<K,V>(map);
    }

    public static <K,V> MapEditor<K,V> newMap(Class<K> keyClass, Class<V> valueClass){
        return new MapEditor<K,V>(new HashMap<K,V>());
    }

    public static MapEditor<String, String> newMap(){
        return new MapEditor<String, String>(new HashMap<>());
    }

}
