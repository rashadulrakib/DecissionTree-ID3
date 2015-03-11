package DT;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * @author rakib
 * This class contains the properties of transactions
 */
public class TransactionEntity {
	
	ArrayList<ArrayList<String>> transactions;
	LinkedHashMap<String, String> itemCodes;
	ArrayList<String> columnNames;
	
	/**
	 * Create an object
	 */
	public TransactionEntity(){
		transactions = new ArrayList<ArrayList<String>>();
		itemCodes = new LinkedHashMap<String, String>();
		columnNames = new ArrayList<String>();
	}	
}
