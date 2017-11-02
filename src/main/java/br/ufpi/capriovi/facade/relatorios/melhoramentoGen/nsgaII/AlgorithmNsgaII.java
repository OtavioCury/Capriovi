package br.ufpi.capriovi.facade.relatorios.melhoramentoGen.nsgaII;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.problemas.AnimalProblemNsgaII;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.problemas.IndiceProblemNsgaII;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.AnimalSelectionSolution;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.InputNsgaII;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * 
 * @author thasciano
 *
 */
public class AlgorithmNsgaII {
	private InputNsgaII inputNsgaII;
	private ArrayList<Animal> animaisAptos;
	ArrayList<Animal> machos;
	ArrayList<Animal> femeas;
	private DecimalFormat fmt = new DecimalFormat("0.00");
	private Problem   problem;
	public static Logger      logger_ ;      // Logger object
	public static FileHandler fileHandler_ ; // FileHandler object
	/**
	 * Ganho Genetico;
	 * @param inputNsgaII
	 * @param animaisAptos
	 */
	public AlgorithmNsgaII(InputNsgaII inputNsgaII, List<Animal> machos, List<Animal> femeas) {
		super();
		this.inputNsgaII = inputNsgaII;
		
		this.machos = (ArrayList<Animal>) machos;
		this.femeas = (ArrayList<Animal>) femeas;
		
		this.problem = new AnimalProblemNsgaII(this.machos, this.femeas, this.inputNsgaII);;         // The problem to solve
	}

	public AlgorithmNsgaII(List<Animal> machos, List<Animal> femeas) {
		super();		
		this.machos = (ArrayList<Animal>)machos;
		this.femeas = (ArrayList<Animal>)femeas;
		
		this.problem = new IndiceProblemNsgaII(this.machos, this.femeas);         // The problem to solve
	}
	
	private SolutionSet initAlgorithmSettings() throws SecurityException, IOException, ClassNotFoundException, JMException {
	    Algorithm algorithm = null ;         // The algorithm to use
	    Operator  crossover ;         // Crossover operator
	    Operator  mutation  ;         // Mutation operator
	    Operator  selection ;         // Selection operator

	    HashMap<String, Object> parameters = new HashMap<String, Object>(); // Operator parameters

//		QualityIndicator indicators; //Object to get quality indicators

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

//		indicators = new QualityIndicator(problem, "DTLZ5.3D") ;;

		
		
		algorithm = new NSGAII(this.problem);

		// Algorithm parameters
	    algorithm.setInputParameter("populationSize",100);
	    algorithm.setInputParameter("maxEvaluations",25000);

	    // Mutation and Crossover for Real codification 
	    parameters = new HashMap<String, Object>();
	    parameters.put("probability",0.9);                   
	    parameters.put("distributionIndex",20.0);
	    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);                   
	    
	    parameters = new HashMap<String, Object>();
	    parameters.put("probability",1.0/this.problem.getNumberOfVariables());
	    parameters.put("distributionIndex",20.0);
	    mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);                    
	    
	    // Mutation and Crossover Binary codification
	    /*
	    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
	    crossover.setParameter("probability",0.9);                   
	    mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
	    mutation.setParameter("probability",1.0/199);
	     */

	    // Selection Operator 
	    parameters = null;
	    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;                           

	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover",crossover);
	    algorithm.addOperator("mutation",mutation);
	    algorithm.addOperator("selection",selection);


//		algorithm.setInputParameter("indicators", indicators);
		
		 // Execute the Algorithm
	    long initTime = System.currentTimeMillis();
	    SolutionSet population = algorithm.execute();
	    long estimatedTime = System.currentTimeMillis() - initTime;
	    
	    // Result messages 
	    logger_.info("Total execution time: "+estimatedTime + "ms");
//	    logger_.info("Variables values have been writen to file VAR");
//	    population.printVariablesToFile("VAR");    
//	    logger_.info("Objectives values have been writen to file FUN");
//	    population.printObjectivesToFile("FUN");
	  
//	    if (indicators != null) {
//	      logger_.info("Quality indicators") ;
//	      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
//	      logger_.info("GD         : " + indicators.getGD(population)) ;
//	      logger_.info("IGD        : " + indicators.getIGD(population)) ;
//	      logger_.info("Spread     : " + indicators.getSpread(population)) ;
//	      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;  
//	     
//	      int evaluations = ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
//	      logger_.info("Speed      : " + evaluations + " evaluations") ;      
//	    } // if
		
		return population;
	}
	
	public ArrayList<AnimalSelectionSolution> solveProblem() throws SecurityException, IOException {
		try {			
			
			/* Execute the Algorithm */
			SolutionSet solutionSet = this.initAlgorithmSettings();
			Iterator<Solution> iterator = solutionSet.iterator();
			
			AnimalSelectionSolution animalSelectionSolution = null;
			ArrayList<AnimalSelectionSolution> results = new ArrayList<AnimalSelectionSolution>();
			while (iterator.hasNext()) {
				Solution solution = (Solution) iterator.next();
				animalSelectionSolution = new AnimalSelectionSolution(
						formatResult(this.fmt,(-1 * solution.getObjective(0))), 
						formatResult(this.fmt,(solution.getObjective(1))));
				
				/* Get list of items selected */
				for(int i = 0; i < this.femeas.size(); i++){
					if(solution.getDecisionVariables()[i].getValue() >= 0){
						animalSelectionSolution.addAnimalSelecionado(
								this.machos.get((int) solution.getDecisionVariables()[i].getValue()));
						animalSelectionSolution.addAnimalSelecionado(this.femeas.get(i));
					}
				}
				//System.out.println(animalSelectionSolution);
				results.add(animalSelectionSolution);
			}
		
			Collections.sort(results, new Comparator<AnimalSelectionSolution>() {
				@Override
				public int compare(AnimalSelectionSolution o1, AnimalSelectionSolution o2) {
					return Double.valueOf(o2.getResultObjective()).compareTo(Double.valueOf(o1.getResultObjective()));
				}
			});
			
			return results;
		} catch (JMException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	/**
	 * @param fmt
	 * @param value
	 * @return
	 */
	private static double formatResult(DecimalFormat fmt, double value){
		return Double.valueOf(fmt.format(value).replaceAll(",", "."));
	}
	
}
