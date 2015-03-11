package DT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;


/**
 * @author rakib
 * This class contains core methods for ID3 Decision Tree 
 */
public class Predict {
	
	TransactionEntity objET;
	Stack<ArrayList<String>> stLabels = new Stack<ArrayList<String>>();
	
	/**
	 * @param objET
	 * Create an obejct
	 */
	public Predict(TransactionEntity objET){
		this.objET = objET;
	}
	
	/**
	 * @param transactions
	 * @param rules
	 * The is the core function to generate Decision Tree using Depth First Search
	 */
	public void GenerateDT(ArrayList<ArrayList<String>> transactions, ArrayList<String> rules) {
		try{
			
			if(transactions.size()<=0){
				return;
			}
			
			ArrayList<String> targetLabels = getTargetClassLabels(transactions);
			double entropy = computeEntropyOnTheSamples(targetLabels);
			if(entropy==0.0){
				if(targetLabels.size()>0){
					String label = targetLabels.get(0);
					printRule(label);
				}
				return;
			}
			
			if(outOfAttributes(transactions)){
				String majorityLabel = getMajorityLabel(stLabels);
				if(!majorityLabel.trim().isEmpty()){
					printRule(majorityLabel);
				}
				return;
			}
			
			int splitAttributeIndex = getSplitAttributeIndex(transactions, entropy);
			if(splitAttributeIndex==-1){
				if(targetLabels.size()>0){
					printRule(targetLabels.get(0));
				}
				return;
			}
			
			LinkedHashMap<String, ArrayList<ArrayList<String>>> hmSubsets = getSubsets(splitAttributeIndex,transactions);
			for(String key: hmSubsets.keySet()){
				stLabels.push(targetLabels);
				rules.add(key);
				printRule(key, rules);
				GenerateDT(hmSubsets.get(key),rules);
				stLabels.pop();
				rules.remove(key);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param targetLabels
	 * @return the Entropy using the target/class labels of the samples
	 */
	private double computeEntropyOnTheSamples(ArrayList<String> targetLabels) {
		double entropy=0;
		try{
			int totalSamples = targetLabels.size();
			LinkedHashMap<String, Integer> hmLabelCount = getLabelCount(targetLabels);
			for(String label: hmLabelCount.keySet()){
				int labelCount = hmLabelCount.get(label);
				double probability = (double)labelCount/(double)totalSamples;
				entropy = entropy + -1.0*probability*Math.log(probability)/Math.log(2.0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return entropy;
	}
	
	/**
	 * @param transactions
	 * @return the target class labels
	 */
	private ArrayList<String> getTargetClassLabels(ArrayList<ArrayList<String>> transactions) {
		ArrayList<String> targetClassLabels = new ArrayList<String>();
		try{
			int targetColIndex=transactions.get(0).size()-1;
			for(ArrayList<String> transaction: transactions){
				targetClassLabels.add(transaction.get(targetColIndex));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return targetClassLabels;
	}
	
	/**
	 * @param targetLabels
	 * @return the count of each label
	 */
	private LinkedHashMap<String, Integer> getLabelCount (ArrayList<String> targetLabels){
		LinkedHashMap<String, Integer> hmLabelCount = new LinkedHashMap<String, Integer>();
		try{
			for(String label: targetLabels){
				if(!hmLabelCount.containsKey(label)){
					hmLabelCount.put(label, 1);
				}else{
					hmLabelCount.put(label, hmLabelCount.get(label)+ 1);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return hmLabelCount;
	}
	
	/**
	 * @param transactions
	 * @param entropy
	 * @return the split attribute index 
	 */
	private int getSplitAttributeIndex(ArrayList<ArrayList<String>> transactions, double entropy) {
		int index=-1;
		try{
			int rows=transactions.size();
			int cols =transactions.get(0).size();
			double maxGain =0;
			for(int j=0;j<cols-1;j++){
				LinkedHashMap<String,ArrayList<String>> hmAttrValueToClassValues = new LinkedHashMap<String, ArrayList<String>>();
				for(int i=0;i<rows;i++){
					String attrValue = transactions.get(i).get(j);
					if(!hmAttrValueToClassValues.containsKey(attrValue)){
						ArrayList<String> alClassValues = new ArrayList<String>();
						alClassValues.add(transactions.get(i).get(cols-1));
						hmAttrValueToClassValues.put(attrValue, alClassValues);
					}else{
						ArrayList<String> alClassValues = hmAttrValueToClassValues.get(attrValue);
						alClassValues.add(transactions.get(i).get(cols-1));
						hmAttrValueToClassValues.put(attrValue, alClassValues);
					}
				}
				double informationGain = computeGainOnEachAttribute(hmAttrValueToClassValues,entropy,rows);
				if(informationGain>maxGain){
					maxGain=informationGain;
					index=j;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return index;
	}

	/**
	 * @param hmAttrValueToClassValues
	 * @param entropy
	 * @param totalSamples
	 * @return the information gain on each attribute
	 */
	private double computeGainOnEachAttribute(LinkedHashMap<String, ArrayList<String>> hmAttrValueToClassValues, double entropy, int totalSamples) {
		double gain=entropy;
		try{
			for(String attrValue: hmAttrValueToClassValues.keySet()){
				ArrayList<String> alClassValues = hmAttrValueToClassValues.get(attrValue);
				double entropyForGain = computeEntropyOnTheSamples(alClassValues);
				gain = gain-(double)alClassValues.size()/(double)totalSamples*entropyForGain;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return gain;
	}
	
	/**
	 * @param splitAttributeIndex
	 * @param transactions
	 * @return the subset of samples
	 */
	private LinkedHashMap<String, ArrayList<ArrayList<String>>> getSubsets(int splitAttributeIndex, ArrayList<ArrayList<String>> transactions) {
		LinkedHashMap<String, ArrayList<ArrayList<String>>> hmSubsets = new LinkedHashMap<String, ArrayList<ArrayList<String>>>();
		try{
			int rows=transactions.size();
			int cols =transactions.get(0).size();
			
			for(int i=0;i<rows;i++){
				String key="";
				ArrayList<String> subTransaction = new ArrayList<String>();
				for(int j=0;j<cols;j++){
					if(j==splitAttributeIndex){
						key=transactions.get(i).get(splitAttributeIndex);
					}else{
						subTransaction.add(transactions.get(i).get(j));
					}
				}
				
				if(!hmSubsets.containsKey(key)){
					ArrayList<ArrayList<String>> subTransactions = new ArrayList<ArrayList<String>>();
					subTransactions.add(subTransaction);
					hmSubsets.put(key, subTransactions);
				}else{
					ArrayList<ArrayList<String>> subTransactions = hmSubsets.get(key);
					subTransactions.add(subTransaction);
					hmSubsets.put(key, subTransactions);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return hmSubsets;
	}
		
	/**
	 * @param transactions
	 * @return true if all attributes are used. 
	 */
	private boolean outOfAttributes(ArrayList<ArrayList<String>> transactions) {
		boolean flag=false;
		try{
			if(transactions.get(0).size()<=1){
				flag=true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @param stLabels
	 * @return the majority class label
	 */
	private String getMajorityLabel(Stack<ArrayList<String>> stLabels){
		String label="";
		try{
			Iterator<ArrayList<String>> itr =  stLabels.iterator();
			ArrayList<ArrayList<String>> alStack = new ArrayList<ArrayList<String>>();
			while(itr.hasNext()){
				alStack.add(itr.next());
			}
			
			for(int i=alStack.size()-1;i>=0;i--){
				ArrayList<String> alLabels =  alStack.get(i);
				
				LinkedHashMap<String, Integer> hmCount = new LinkedHashMap<String, Integer>();
				for(String key: alLabels){
					if(!hmCount.containsKey(key)){
						hmCount.put(key, 1);
					}else{
						hmCount.put(key, hmCount.get(key) +1);
					}
				}
				
				if(hmCount.size()>1){
					Map<String,Integer> sortedMap = sortHashMap(hmCount);
					String keyArr [] = (String []) sortedMap.keySet().toArray(new String[sortedMap.size()]);
					
					if(sortedMap.size()>1){
						if(sortedMap.get(keyArr[0])>sortedMap.get(keyArr[1])){
							label = keyArr[0];
							break;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return label;
	}
	
	/**
	 * @param hmCount
	 * @return the sorted hashmap
	 */
	private Map<String, Integer> sortHashMap(LinkedHashMap<String, Integer> hmCount) {
		Map<String, Integer> sortedMap = new TreeMap<String, Integer>();
		try{
			ComparatorDescending comp=new ComparatorDescending(hmCount);
			sortedMap = new TreeMap<String, Integer>(comp);
			sortedMap.putAll(hmCount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sortedMap;
	}
	
	/**
	 * @param key
	 * @param rules
	 * Print non leafs
	 */
	private void printRule(String key, ArrayList<String> rules) {
		try{
			System.out.println();
			for(int i=0;i<rules.size()-1;i++){
				System.out.print("\t");
			}
			System.out.print("if "+objET.itemCodes.get(key).replace(":", " is ")+", then");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param key
	 * print leaf
	 */
	private void printRule(String key) {
		try{
			System.out.print(" "+objET.itemCodes.get(key).replace(":", " is ")+".");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
}
