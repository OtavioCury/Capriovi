package br.ufpi.capriovi.facade.relatorios.melhoramentoGen.problemas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.util.JMException;

public class IndiceProblemNsgaII extends Problem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7439429354862641539L;
	
	private ArrayList<Animal> machos;
	private ArrayList<Animal> femeas;

	private int qntMachos;
	
	public IndiceProblemNsgaII(ArrayList<Animal> machos,
			ArrayList<Animal> femeas) {
		super();
		this.problemName_ = "Knapsack MultiObjective Problem";
		this.machos = machos;
		this.femeas = femeas;
		
		/* List of Variables */
		this.numberOfVariables_ = this.femeas.size();
		
		/* The number of objectives: Gain */
		this.numberOfObjectives_ = 2;
		/* The number of constraints: knapsack restriction */
		this.numberOfConstraints_ = 0;//1
		
		/* Define a binary solution */
		this.lowerLimit_ = new double[numberOfVariables_];
		this.upperLimit_ = new double[numberOfVariables_];

		for (int i = 0; i < this.numberOfVariables_; i++) {
			this.lowerLimit_[i] = (double) 0;
			this.upperLimit_[i] = (double) this.machos.size() - 1;
		}
		this.solutionType_ = new IntSolutionType(this);
	}


	@Override
	public void evaluate(Solution solution) throws JMException {
		
		double[] x = new double[this.numberOfVariables_];
		double endogamiaMedia = 0.0, indiceMedio = 0.0;
		setQntMachos(0);
		HashMap<String, Integer> hashMachos = new HashMap<String, Integer>();
		for(int i = 0; i < this.numberOfVariables_; i++){
			
			x[i] = solution.getDecisionVariables()[i].getValue();
			if(x[i] >= 0){		
				
				
				if (!hashMachos.containsKey(""+x[i])) {
					hashMachos.put(""+x[i], new Integer(0));
				}
				hashMachos.put(""+x[i], (hashMachos.get(""+x[i]) +1));
				
				//min endogamia
				endogamiaMedia += (	this.femeas.get(i).getLinhaMatrizA()[
				                   	this.machos.get((int) x[i]).getPosAnt() ] ) / 2;
				//max indice 
				indiceMedio += (femeas.get(i).getIndice() + this.machos.get((int) x[i]).getIndice()) / 2;
			}
		}
		//proporção de machos
		Set<String> chaves = hashMachos.keySet();
		int metade = this.femeas.size()/2;
		Boolean restricaoAtiva = false;
		for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();){
			String chave = iterator.next();
			if(hashMachos.get(chave) > metade ){
				restricaoAtiva = true;
			}
		}
		indiceMedio = indiceMedio / this.femeas.size();
		endogamiaMedia = endogamiaMedia / this.femeas.size();

		if( (hashMachos.size() == machos.size() ) && ( !restricaoAtiva ) ){
		
			System.out.println("			===========SOLUCAO===========");
			System.out.println("	Solucao Gerada		: " + Arrays.toString(x));
			System.out.println("	Indice Medio		: " + indiceMedio);
			System.out.println("	Endogamia Media		: " + endogamiaMedia);
	
			solution.setObjective(0, -1 * indiceMedio);//max
			solution.setObjective(1, endogamiaMedia);//min
		}
//		else{
//			solution.setObjective(0, -1 * indiceMedio*(-1000000));//max
//			solution.setObjective(1, endogamiaMedia*1000000);//min
//		}
		
	}//evaluate


	public int getQntMachos() {
		return qntMachos;
	}


	public void setQntMachos(int qntMachos) {
		this.qntMachos = qntMachos;
	}
}
