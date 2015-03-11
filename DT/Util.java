package DT;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * @author rakib
 * This class contains utility functions
 *
 */
public class Util {

	/**
	 * @param objET
	 * @return the Attributes for prediction
	 */
	public LinkedHashMap<String,Integer> getBinaryAttributes(TransactionEntity objET){
		LinkedHashMap<String,Integer> battributes = new LinkedHashMap<String,Integer>();
		try{
			ArrayList<ArrayList<String>> transactions = objET.transactions;
			ArrayList<String> columnNames = objET.columnNames; 
			int rows = transactions.size();
			int columns = columnNames.size();
			
			for(int j=0;j<columns;j++){
				LinkedHashMap<String,Integer> hmItemcodes = new LinkedHashMap<String,Integer>();
				for(int i=0;i<rows;i++){
					hmItemcodes.put(transactions.get(i).get(j), 0);
				}
				
				if(hmItemcodes.size()==2){
					battributes.put(columnNames.get(j), j);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return battributes;
	}
	
	/**
	 * @param objET
	 * @param targetIndex
	 * @return reordered transaction file based on the prediction attribute
	 */
	public TransactionEntity reorderTransactions(TransactionEntity objET, int targetIndex){
		TransactionEntity obj = new TransactionEntity();
		try{
			ArrayList<ArrayList<String>> transactions = objET.transactions;
			ArrayList<String> columnNames = objET.columnNames; 
			int rows = transactions.size();
			int columns = columnNames.size();
			
			ArrayList<ArrayList<String>> newTransactions = new ArrayList<ArrayList<String>>();
			ArrayList<String> newColumnNames = new ArrayList<String>();
			ArrayList<String> alTargetRows = new ArrayList<String>();

			for(int i=0;i<rows;i++){
				alTargetRows.add(transactions.get(i).get(targetIndex));
			}
			
			for(int j=0;j<columns;j++){
				if(j==targetIndex){
					continue;
				}
				newColumnNames.add(columnNames.get(j));
			}
			newColumnNames.add(columnNames.get(targetIndex));
			
			for(int i=0;i<rows;i++){
				ArrayList<String> newTrans = new ArrayList<String>();
				for(int j=0;j<columns;j++){
					if(j==targetIndex){
						continue;
					}
					newTrans.add(transactions.get(i).get(j));
				}
				newTrans.add(alTargetRows.get(i));
				newTransactions.add(newTrans);
			}
			
			obj.transactions = newTransactions;
			obj.itemCodes = objET.itemCodes;
			obj.columnNames = newColumnNames;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
}
