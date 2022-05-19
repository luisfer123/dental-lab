package com.dental.lab.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 * A MapTree Wrapper class. It allows a single Key to be map to multiple values.<br>
 * The class contains a field of type:<br>
 * 
 * <br>
 *  {@literal TreeMap<K, Set<V>>}
 * <br><br>
 * 
 *  So multiple values for the same key are stored in a Set. This way we simulate that
 *  multiple values can correspond to a single key.<br>
 * 
 * @author Luis Fernando Martinez Oritz
 *
 * @param <K> - key
 * @param <V> - value
 */
public class MultiTreeMap<K, V> {
	
	/**
	 * 
	 */
	private TreeMap<K, Set<V>> multiTreeMap;
	
	public MultiTreeMap() {
		multiTreeMap = new TreeMap<K, Set<V>>();
	}
	
	public MultiTreeMap(TreeMap<K, V> treeMap) {
		TreeMap<K, Set<V>> multiTreeMap = new TreeMap<>();
		
		treeMap.keySet().forEach(key -> {
			// TreeMap<K, V> does not allows multiple values for same key, no need to check it.
			multiTreeMap.put(key, Collections.singleton(treeMap.get(key)));
		});
		
		this.multiTreeMap = multiTreeMap;
	}
	
	public void put(K key, V value) {
		if(multiTreeMap.containsKey(key)) {
			multiTreeMap.get(key).add(value);
		} else {
			Set<V> tempSet = new HashSet<>();
			tempSet.add(value);
			multiTreeMap.put(key, tempSet);
		}
	}

	public Set<V> get(K key) {
		return multiTreeMap.get(key);
	}
	
	public boolean containsKey(K key) {
		return multiTreeMap.containsKey(key);
	}
	
	public boolean isEmpty() {
		return multiTreeMap.isEmpty();
	}
	
	public K firstKey() {
		return multiTreeMap.firstKey();
	}
	
	public Set<V> remove(K key) {
		return multiTreeMap.remove(key);
	}

}
