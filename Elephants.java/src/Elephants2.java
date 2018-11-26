import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class OperationOnElephants2 {
	private Boolean error = false;
	private Scanner inputData = null;
	private int numberOfLines = 4;
	private String[] splitedLine;
	private int[] weight;
	private int[] firstStructure;
	private int[] secondStructure;
	private int[] permutation;
	private int numberOfElephants;
	private int numberOfCycles = 0; 
	private int globalMinWeight = 0;
	private List<List<Integer>> cycleList = new ArrayList<>();
	private List<Long> weightSumInCycleList = new ArrayList<>();
	private List<Integer> cycleWeightMin = new ArrayList<>();
	
	
	public OperationOnElephants2(Scanner inputData){
		this.inputData = inputData;
		convertLineToList();
		if(!error){
			setPermutation();
		}
	}
	
	public void convertLineToList(){
		for (int i = 0; i < numberOfLines; i++){
			if(inputData.hasNextLine()){
				try{
					createLists(i);
					
				}catch (NumberFormatException e) {
					error = true;
					return;
				}
				
			}else{
				error = true;
				return;
			}
		}
	}
	
	private void createLists(int i){
		splitedLine = inputData.nextLine().split(" ");
		if(i == 0 && !error){
			setNumberOfElephants();
		}if(i == 1 && !error){
			setWeightList();
		}if(i == 2 && !error){
			firstStructure = new int[numberOfElephants];
			splitedLineToList(firstStructure);
		}if(i == 3 && !error){
			secondStructure = new int[numberOfElephants];
			splitedLineToList(secondStructure);
		}	
	}
	
	private void splitedLineToList(int[] data){
		if(splitedLine.length == weight.length){
			for (int position = 0; position < numberOfElephants; position++){
				int elementData = Integer.parseInt(splitedLine[position]) - 1;
				data[position] = elementData;
				if(data[position] < 0){
					error = true;
					return;
				}
			}
		}else{
			error = true;
		}
	}
	
	private void setNumberOfElephants(){
		numberOfElephants = Integer.parseInt(splitedLine[0]);
		if(numberOfElephants <= 0 ){
			error = true;
		}
	}
	
	private void setWeightList(){
		weight = new int[numberOfElephants];
		if(splitedLine.length == weight.length){
			for (int position = 0; position < numberOfElephants; position++){
				weight[position] = Integer.parseInt(splitedLine[position]);
				if(weight[position] <= 0){
					error = true;
					return;
				}
			}
		}
		else{
			error = true;
		}
		
	}
	
	public void printResult(){
		if(!error){
			long result = getResult();
			if(result != 0 && !error){
				System.out.println(result);
			}
		}
	}
	
	private long getResult(){
		long result = 0;
		
		if(checkStateOfPreparingData()){
			for(int cycle = 0; cycle < numberOfCycles; cycle ++){
				final long firstMethod = weightSumInCycleList.get(cycle) + 
						(cycleList.get(cycle).size() - 2) * cycleWeightMin.get(cycle);
				
				final long secondMethod = weightSumInCycleList.get(cycle) + 
						cycleWeightMin.get(cycle) + (cycleList.get(cycle).size() + 1) * 
						globalMinWeight;
				
				result = result + Math.min(firstMethod, secondMethod);
			}
			return result;
		}
		
		error = true;
		return -1;
	}
	
	private Boolean checkStateOfPreparingData(){
		if(!error){
			createCycle();
			if(!error){
				setCyclesParameters();
				return true;
			}
		}
		
		error = true;
		return false;	
	}
	
	private void createCycle(){
		try{
			Boolean[] decidedArray = getDecidedArray();
			for(int decidedElem = 0; decidedElem < numberOfElephants; 
					decidedElem ++){
				if(!decidedArray[decidedElem]){
					numberOfCycles = numberOfCycles + 1;
					cycleList.add(new ArrayList<>());
					
					addElemToCycle(decidedArray, decidedElem);
				}
			}
		}catch(NullPointerException e){
			error = true;
		}	
	}
	
	private Boolean[] getDecidedArray(){
		Boolean[] decidedArray = new Boolean[numberOfElephants];
		for(int elem = 0; elem < numberOfElephants; elem ++){
			decidedArray[elem] = false;
		}
		return decidedArray;
	}
	
	private void addElemToCycle(Boolean[] decidedArray, int cycleElem){
		if(permutation[0] != -1){
			while(!decidedArray[cycleElem]){
				decidedArray[cycleElem] = true;
				cycleList.get(numberOfCycles - 1).add(cycleElem);
				cycleElem = permutation[cycleElem];
				if(cycleElem == -1){
					error = true;
					return;
				}
			}
			return;
		}
		error = true;
		return;	
	}
	
	private void setPermutation(){
		if(firstStructure[0] != -1 && secondStructure[0] != -1){
			permutation = new int[numberOfElephants];
			for (int numberOfElephant = 0; numberOfElephant < numberOfElephants; 
					numberOfElephant ++){
				if(secondStructure[numberOfElephant] < permutation.length){
					permutation[secondStructure[numberOfElephant]] = 
							firstStructure[numberOfElephant];
				}else{
					permutation[0] = -1;
					error = true;
				}
			}
		}else{
			permutation[0] = -1;
			error = true;
		}
	}
	
	private void setCyclesParameters(){
		for(int cycle = 0; cycle < numberOfCycles; cycle ++){
			long sum = 0; 
			int cycleMinWeight = 0;
			
			for(int elem = 0; elem < cycleList.get(cycle).size(); elem ++){
				sum = sum + weight[cycleList.get(cycle).get(elem)];
				
				if(cycleMinWeight != 0){
					cycleMinWeight = Math.min(cycleMinWeight, 
							weight[cycleList.get(cycle).get(elem)]);
				}else{
					cycleMinWeight = weight[cycleList.get(cycle).get(elem)];
				}
			}
			
			weightSumInCycleList.add(sum);
			cycleWeightMin.add(cycleMinWeight);
			
			if(globalMinWeight != 0){
				globalMinWeight = Math.min(cycleMinWeight, globalMinWeight);
			}else{
				globalMinWeight = cycleMinWeight;
			}
		}
	}
}

public class Elephants2 {
	public static void main(String[] args){
		Scanner inputData = new Scanner(System.in);
		
		OperationOnElephants2 operationOnElephants = new OperationOnElephants2(inputData);	
		operationOnElephants.printResult(); 
	}
}

