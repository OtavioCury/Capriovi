package br.ufpi.capriovi.facade.relatorios.melhoramentoGen.problemas;

import java.util.ArrayList;
import java.util.HashMap;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.util.JMException;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.InputNsgaII;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.IntensidadeDeSelecao;
import br.ufpi.capriovi.suporte.tiposEnum.TipoSexoEnum;

/**
 * 
 * 
 * @author thasciano
 *
 */
public class AnimalProblemNsgaII extends Problem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7439429354862641539L;
	
	private ArrayList<Animal> machos;
	private ArrayList<Animal> femeas;
	private double intensidadeSelecaoTotal;
	private InputNsgaII inputNsgaII;

	private int qntMachos;
	
	public AnimalProblemNsgaII(ArrayList<Animal> machos,
			ArrayList<Animal> femeas, InputNsgaII inputNsgaII) {
		super();
		this.problemName_ = "Knapsack MultiObjective Problem";
		this.machos = machos;
		this.femeas = femeas;
		this.inputNsgaII = inputNsgaII;
		
		/* List of Variables */
		this.numberOfVariables_ = this.femeas.size();
		
		/* The number of objectives: Gain */
		this.numberOfObjectives_ = 3;
		/* The number of constraints: knapsack restriction */
		this.numberOfConstraints_ = 0;//1
		
		/* Define a binary solution */
		this.lowerLimit_ = new double[numberOfVariables_];
		this.upperLimit_ = new double[numberOfVariables_];

		for (int i = 0; i < this.numberOfVariables_; i++) {
			this.lowerLimit_[i] = (double) -1;
			this.upperLimit_[i] = (double) this.machos.size() - 1;
		}
		this.solutionType_ = new IntSolutionType(this);
	}


	@Override
	public void evaluate(Solution solution) throws JMException {
		
		double[] x = new double[this.numberOfVariables_];
		double endogamiaMedia = 0.0, ganhoGeneticoAnual = 0.0, depMedia = 0.0;
		setQntMachos(0);
		int qntFemeas = 0;
		double intervaloGeracaoM = 0.0,intervaloGeracaoF = 0.0;
		HashMap<String, Integer> hashMachos = new HashMap<String, Integer>();
		for(int i = 0; i < this.numberOfVariables_; i++){
			
			x[i] = solution.getDecisionVariables()[i].getValue();
			if(x[i] >= 0){
				
				intervaloGeracaoF += this.femeas.get(i).getIntervaloGeracao();
				qntFemeas++;
				
				// separa em grupos
				
				if (!hashMachos.containsKey(""+x[i])) {
					hashMachos.put(""+x[i], new Integer(0));
					intervaloGeracaoM += this.machos.get((int) x[i]).getIntervaloGeracao();
					//qntMachos++;
				}
				hashMachos.put(""+x[i], (hashMachos.get(""+x[i]) +1));
				
				//min endogamia
				endogamiaMedia += (	this.femeas.get(i).getLinhaMatrizA()[
				                   	this.machos.get((int) x[i]).getPosAnt() ] ) / 2;
				/*
				System.out.println();
				System.out.println(this.femeas.get(i).getNomeNumero()+ " " + this.femeas.get(i).getDiferencialEsperado());
				System.out.println(this.machos.get((int) x[i]).getNomeNumero()+ " " + this.machos.get((int) x[i]).getDiferencialEsperado());
				System.out.println("" + (this.femeas.get(i).getDiferencialEsperado() + this.machos.get((int) x[i]).getDiferencialEsperado())/2);
				*/
				//max DEP
				depMedia += (this.femeas.get(i).getDiferencialEsperado() + this.machos.get((int) x[i]).getDiferencialEsperado())/2;
			}
		}
		if((hashMachos.size() < this.machos.size()) && (hashMachos.size() > 0) && (qntFemeas > 0) && (qntFemeas < this.femeas.size())){
			IntensidadeDeSelecao intensidade = new IntensidadeDeSelecao();
			this.intensidadeSelecaoTotal = 0.0;
			intensidade.calcIntensidadeSelecao(((double) hashMachos.size() / this.machos.size()), TipoSexoEnum.SEXO_MACHO);
			intensidade.calcIntensidadeSelecao(((double) qntFemeas / this.femeas.size()), TipoSexoEnum.SEXO_FEMEA);
			this.intensidadeSelecaoTotal = intensidade.getIntensidadeSelecaoMachos() + intensidade.getIntensidadeSelecaoFemeas();
			/*
			System.out.println("intensidade Macho: "+intensidade.getIntensidadeSelecaoMachos());
			System.out.println("intensidade Femea: "+intensidade.getIntensidadeSelecaoFemeas());
			System.out.println("intervalo Macho: "+intensidade.getIntensidadeSelecaoMachos());
			System.out.println("intervalo Femea: "+intensidade.getIntensidadeSelecaoFemeas());
			*/
			intervaloGeracaoM = intervaloGeracaoM/hashMachos.size();
			intervaloGeracaoF = intervaloGeracaoF/qntFemeas;
			/*
			System.out.println("intervalo Macho: "+intervaloGeracaoM);
			System.out.println("intervalo Femea: "+intervaloGeracaoF);
			*/
	
			ganhoGeneticoAnual = (this.intensidadeSelecaoTotal/(intervaloGeracaoM + intervaloGeracaoF)) * inputNsgaII.getHerdabilidade() * inputNsgaII.getDesvioPadraoCaracteristica();
			endogamiaMedia = endogamiaMedia / qntFemeas;
			/*
			System.out.println("			===========SOLUCAO===========");
			System.out.println("	Solucao Gerada		: " + Arrays.toString(x));
			System.out.println("	Int. Sel. Total		: " + this.intensidadeSelecaoTotal);
			System.out.println("	Herdabilidade		: " + model.getHerdabilidade());
			System.out.println("	Desvio Padrao		: " + model.getDesvioPadraoCaracteristica());
			System.out.println("			=============================");
			System.out.println("	Quant. Ani. Aptos	: " + (this.machos.size() + this.femeas.size()));
			System.out.println("	Quant. Femeas Aptas	: " + (this.femeas.size()));
			System.out.println("	Quant. Machos Aptos	: " + (this.machos.size()));
			System.out.println("			=============================");
			System.out.println("	Quant. Ani. Sel		: " + (hashMachos.size() + qntFemeas));
			System.out.println("	Quant. Machos Sel	: " + hashMachos.size());
			System.out.println("	Quant. Femeas Sel	: " + qntFemeas);
			System.out.println("			=============================");
			System.out.println("	Endogamia			: " + endogamiaMedia);
			System.out.println("	Ganho Genetico		: " + ganhoGeneticoAnual);
			System.out.println("	DEPO Media			: " + (depMedia/qntFemeas));
			*/
			solution.setObjective(0, -1 * ganhoGeneticoAnual);//max
			solution.setObjective(1, endogamiaMedia);//min
			//solution.setObjective(2, -1 * (depMedia/qntFemeas));//max
		}//if
	}//evaluate


	public int getQntMachos() {
		return qntMachos;
	}


	public void setQntMachos(int qntMachos) {
		this.qntMachos = qntMachos;
	}
}
