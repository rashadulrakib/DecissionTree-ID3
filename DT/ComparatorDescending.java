package DT;

import java.util.Comparator;
import java.util.Map;


/**
 * @author rakib
 * This class contains the comperator for sorting 
 *
 */
public class ComparatorDescending implements Comparator<Object> {
	Map<String, Integer> map;

	/**
	 * @param map
	 * Create an object
	 */
	public ComparatorDescending(Map<String, Integer> map) {
	    this.map = map;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
	    return ((Integer) map.get(o2)).compareTo((Integer) map.get(o1));
	}
}
