package br.ufpi.capriovi.facade.relatorios.melhoramentoGen.nsgaII;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
/**
 * This class implements the NSGA−II algorithm .
 * @author thasciano
 *
 */
public class NSGAII extends Algorithm{
	/**
	 * 
	 */
	private static final long serialVersionUID = -684241864040485870L;

	/**
	 * Construtor
	 * @param problem Problem to solve.
	 */
	public NSGAII(Problem problem) {
		super(problem);
	}

	/**
	 * Runs the NSGA-II algorithm
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;
		
		QualityIndicator indicators;
		int requiredEvaluations;
		
		
		SolutionSet population;
		SolutionSet offspringPopulation;
		SolutionSet union;
		
		Operator    mutationOperator  ;
	    Operator    crossoverOperator ;
	    Operator    selectionOperator ;
	    
	    Distance distance = new Distance();
		
	    // Read the params
	    populationSize = ((Integer)this.getInputParameter("populationSize")).intValue();
	    maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();
	    indicators = (QualityIndicator) getInputParameter("indicators");
	    
	    //initialize the variables
	    population          = new SolutionSet(populationSize);   
	    evaluations = 0 ;
	    
	    requiredEvaluations = 0;                

	    // Read the operators
	    mutationOperator  = this.operators_.get("mutation");
	    crossoverOperator = this.operators_.get("crossover");
	    selectionOperator = this.operators_.get("selection");
	    
	    // Create the initial population
	    Solution newSolution;
	    for (int i = 0; i < populationSize; i++) {
	      newSolution = new Solution(problem_);                    
	      problem_.evaluate(newSolution);
	      problem_.evaluateConstraints(newSolution);
	      evaluations++;
	      population.add(newSolution);
	    } //for
		
	    //Generations
	    while (evaluations < maxEvaluations) {
	        //Creat the offSpring solutionSet
	    	if ((evaluations % 100) == 0) {
		    	 System.out.println("Evaluations: " + evaluations ) ;
		     }
	    	offspringPopulation = new SolutionSet(populationSize);
	    	Solution[] parents = new Solution[2];
	    	for (int i = 0 ; i < (populationSize / 2) ; i ++) {
	    		if(evaluations < maxEvaluations){
					//obtain parents	          	
					parents[0] = (Solution)selectionOperator.execute(population);
					parents[1] = (Solution)selectionOperator.execute(population);
					  
					// Crossover
					Solution [] offspring = (Solution []) crossoverOperator.execute(parents);                
					  
					// Mutation
					mutationOperator.execute(offspring[0]);
					mutationOperator.execute(offspring[1]);
					
					// Evaluation of the new individual
					problem_.evaluate(offspring[0]); 
					problem_.evaluateConstraints(offspring[0]);
					
					problem_.evaluate(offspring[1]);            
					problem_.evaluateConstraints(offspring[1]);
					  
					offspringPopulation.add(offspring[0]);
					offspringPopulation.add(offspring[1]);
					  
					evaluations +=2;
	
	    		}//if
	        } // for
	
	        // Create the solutionSet union of solutionSet and offSpring
	        union = ((SolutionSet)population).union(offspringPopulation);              
	
	        // Ranking the union
	        Ranking ranking = new Ranking(union);                        
	
	        int remain = populationSize;
	        int index  = 0;
	        SolutionSet front = null;
	        population.clear();
	
	        // Obtain the next front
	        front = ranking.getSubfront(index);
	
	        while ((remain > 0) && (remain >= front.size())){                
				//Assign crowding distance to individuals
				distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());                
				//Add the individuals of this front
				for (int k = 0; k < front.size(); k++ ) {
					population.add(front.get(k));
				} // for
				 
				//Decrement remain
				remain = remain - front.size();
				
				//Obtain the next front
				index++;
				if (remain > 0) {
					front = ranking.getSubfront(index);
				} // if        
	        } // while
	        
	        // Remain is less than front(index).size, insert only the best one
	        if (remain > 0) {  // front contains individuals to insert                        
	        	distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());
	        	front.sort(new CrowdingComparator());
	        	for (int k = 0; k < remain; k++) {
	        		population.add(front.get(k));
	        	} // for
	          
	        	remain = 0; 
	        } // if                                        
	         
	        // This piece of code shows how to use the indicator object into the code
	        // of NSGA-II. In particular, it finds the number of evaluations required
	        // by the algorithm to obtain a Pareto front with a hypervolume higher
	        // than the hypervolume of the true Pareto front.
	        if ((indicators != null) && 
	            (requiredEvaluations == 0)) {
	        	double HV = indicators.getHypervolume(population) ;
	        	if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
	        		requiredEvaluations = evaluations ;
	        	} // if
	        } // if
	     } // while
	               
	     // Return as output parameter the required evaluations
	     setOutputParameter("evaluations", requiredEvaluations);
	     if ((evaluations % 10) == 0) {
	    	 System.out.println("Evaluations: " + evaluations ) ;
	     }
	     // Return the first non-dominated front
	     Ranking ranking = new Ranking(population);        
	     return ranking.getSubfront(0);
		
	}//execute

}//nsgaII
