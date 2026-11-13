
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	List<T> rules;
	Map<T, HashSet<T>> mainMap = new HashMap<>();
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		this.rules = rules;
		T toAddKey = null;
		for(T ruleItem: rules){
			if(toAddKey!=null && ruleItem!=null){
				mainMap.get(toAddKey).add(ruleItem);
			}
			toAddKey = ruleItem;
			if(!mainMap.keySet().contains(toAddKey)){
				mainMap.put(toAddKey, new HashSet<>());
			}
		}


	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		Set<T> result = new HashSet<>();
		if(mainMap.containsKey(elem)){
			result = mainMap.get(elem);
		}else{
			return Collections.emptySet();
		}
		return result;

	}

	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		Iterator<T> myIterator = list.iterator();
		T toCheck= null;
		while(myIterator.hasNext()){
			T item = myIterator.next();
			if(toCheck!=null && item!=null){
				if(mainMap.keySet().contains(toCheck) && mainMap.get(toCheck).contains((item))){
					myIterator.remove();
				}else{
					toCheck = item;
				}
			}else{
				toCheck = item;
			}
		}

	}
}
