package DT;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * @author rakib
 * This class contains methods to call ID3 DT
 */
public class ID3 {

	/**
	 * @param args
	 * @throws IOException
	 * Entry point of the program
	 */
	public static void main(String[] args) throws IOException {

		ID3 obj = new ID3();
		Util objU = new Util();
		
		BufferedReader br= new BufferedReader( new InputStreamReader(System.in));
		String inputFile= obj.getInputFile(br);
		
		Transaction objLT = new Transaction();
		TransactionEntity objET = objLT.loadTransactionFile(inputFile);
		
		LinkedHashMap<String,Integer> bAttributeNameIndex = objU.getBinaryAttributes(objET);
		String predictionColumn = obj.getPredictionColumn(bAttributeNameIndex, br);
		br.close();
		
		if(!predictionColumn.isEmpty()){
			TransactionEntity objNewTE = objU.reorderTransactions(objET, bAttributeNameIndex.get(predictionColumn));
			Predict objP = new Predict(objNewTE);
			objP.GenerateDT(objNewTE.transactions,new ArrayList<String>());
			System.out.println();
		}
	}
	
	/**
	 * @param br
	 * @return the input file
	 */
	private String getInputFile(BufferedReader br){
		String inputFile="";
		try{
			while(true){	
				System.out.println("What is the name of the file containing your data?");
				inputFile = br.readLine();
								
				if(inputFile.isEmpty()){
					System.out.println("File name cannot be empty.\n");
					continue;
				}else{
					File f = new File(inputFile);
					if(!f.exists()){
						System.out.println("The file does not exist.\n");
						continue;
					}else{
						break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return inputFile;
	}
	
	/**
	 * @param bAttributeNameIndex
	 * @param br
	 * @return the prediction attribute
	 */
	private String getPredictionColumn(LinkedHashMap<String,Integer> bAttributeNameIndex, BufferedReader br) {
		int index=-1;
		String[] keys = (String [])bAttributeNameIndex.keySet().toArray(new String[bAttributeNameIndex.size()]);
		try{
			System.out.println("Please choose an attribute (by number):");
			int c=1;
			
			for(String column: keys){
				System.out.println("\t"+c+". "+column);
				c++;
			}
			System.out.print("Attribute: ");
			try{
				index = Integer.parseInt(br.readLine().trim());
				if(index<1 || index>=c){
					index=-1;
					System.out.println("Invalid attribute number...");
					return "";
				}
				
			}catch(Exception formatEx){
				System.out.println("Invalid format of attribute number...");
				return "";
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		System.out.println("Target attribute is: "+keys[index-1]);
		return keys[index-1];
	}

}
