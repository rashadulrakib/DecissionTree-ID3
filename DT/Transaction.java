package DT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;


/**
 * @author rakib
 * This class contains method to load transaction file into memory
 */
public class Transaction {
	
	/**
	 * @param transactionFilePath
	 * @return an entity for all transactions
	 */
	/**
	 * @param transactionFilePath
	 * @return the loaded transaction file
	 */
	public TransactionEntity loadTransactionFile(String transactionFilePath) {
		TransactionEntity objET = new TransactionEntity();
		try{
			ArrayList<ArrayList<String>> transactions= new ArrayList<ArrayList<String>>();
			LinkedHashMap<String, String> itemCodes = new LinkedHashMap<String, String>(); //0=0-> outlook=sunny
			
			BufferedReader br = new BufferedReader(new FileReader(transactionFilePath));
			
			String line="";
			
			line = br.readLine();
			String [] columnNames = line.split("\\s+");
			int totalColumns = columnNames.length;
			
			LinkedHashMap<String,LinkedHashMap<String,Integer>> hmColumnValueCodes = new LinkedHashMap<String, LinkedHashMap<String,Integer>>(); 
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if(line.isEmpty()){
					continue;
				}
				ArrayList<String> encodedValues = new ArrayList<String>();
				
				String [] items = line.split("\\s+");
				//check if there is any missing values
				if(totalColumns!=items.length){
					continue;
				}
				
				for(int i=0;i<items.length;i++){
					String columnName = columnNames[i];
					String value = items[i];
					
					if(!hmColumnValueCodes.containsKey(columnName)){
						LinkedHashMap<String,Integer> hmValueCodes = new LinkedHashMap<String, Integer>();
						hmValueCodes.put(value, 0);
						hmColumnValueCodes.put(columnName, hmValueCodes);
					}else{
						LinkedHashMap<String,Integer> hmValueCodes = hmColumnValueCodes.get(columnName);
						if(!hmValueCodes.containsKey(value)){
							int nextValueIndex = hmValueCodes.size();
							hmValueCodes.put(value,nextValueIndex);
							hmColumnValueCodes.put(columnName,hmValueCodes);
						}
					}
					
					String encodedValue =  i+":"+hmColumnValueCodes.get(columnName).get(value);
					//System.out.print(encodedValue+" ");
					
					itemCodes.put(encodedValue,columnName+":"+value);
					encodedValues.add(encodedValue);
				}
				
				transactions.add(encodedValues);
				//System.out.println();
			}
			
			br.close();
			
			objET.transactions = transactions;
			objET.itemCodes = itemCodes;
			objET.columnNames = new ArrayList<String>(Arrays.asList(columnNames));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return objET;
	}
}
