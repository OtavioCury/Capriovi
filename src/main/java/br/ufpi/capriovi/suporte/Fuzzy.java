package br.ufpi.capriovi.suporte;

import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.relatorio.RelatorioVermifugação;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Fuzzy {
	public void fuzzyVermifuga(Animal animal, Integer famacha, Double escore, Integer opg) {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("vermifuga.fcl");
		//String fileName = "vermifuga.fcl";
		FIS fis = FIS.load(input, true);

		//Erro ao carregar o arquivo
		if(fis == null){
			System.err.println("Não foi possivel carregar o arquivo!!");
			return;
		}

		FunctionBlock functionBlock = fis.getFunctionBlock(null);

		/**
		 * Seta as variáveis
		 */
		fis.setVariable("FAMACHA", famacha);
		fis.setVariable("ECC", escore);
		fis.setVariable("OPG", opg);

		/**
		 * Executa
		 */
		fis.evaluate();

		/**
		 * Obtem o resultado
		 */
		Variable nota = functionBlock.getVariable("NOTA");
		if (nota.getValue() < 0) {						
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(0.0, new Date(), 3);
			checaData(animal, resumoVermifugação);
		}else if (nota.getValue() >= 0 && nota.getValue() < 3.4) {				
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), new Date(), 3);
			checaData(animal, resumoVermifugação);	
		}else if (nota.getValue() == 3.4) {					
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), new Date(), 5);
			checaData(animal, resumoVermifugação);
		}else if (nota.getValue() > 3.4 && nota.getValue() < 6.6) {						
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), new Date(), 2);
			checaData(animal, resumoVermifugação);
		}else if (nota.getValue() == 6.6) {
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), new Date(), 4);
			checaData(animal, resumoVermifugação);
		}else if (nota.getValue() > 6.6 && nota.getValue() <= 10) {			
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), new Date(), 1);
			checaData(animal, resumoVermifugação);
		}else{			
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(10.0, new Date(), 1);
			checaData(animal, resumoVermifugação);
		}
	}

	public void checaData(Animal animal, RelatorioVermifugação resumoVermifugação){
		boolean presente = false;
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("#.####");

		for (RelatorioVermifugação relatorio : animal.getListaNotas()) {			
			cal1.setTime(relatorio.getData());
			cal2.setTime(new Date());

			df.setRoundingMode(RoundingMode.CEILING);			

			if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
					cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && 
					df.format(resumoVermifugação.getNotaVermifugacao()).equals(df.format(relatorio.getNotaVermifugacao()))) {
				presente = true;
				break;
			}
		}

		if (presente == false) {
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);
		}
	}


	public void fuzzyVermifuga(Animal animal, Integer famacha, Double escore, Integer opg, Date data) {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("vermifuga.fcl");
		//String fileName = "vermifuga.fcl";
		FIS fis = FIS.load(input, true);

		//Erro ao carregar o arquivo
		if(fis == null){
			System.err.println("Não foi possivel carregar o arquivo!!");
			return;
		}

		FunctionBlock functionBlock = fis.getFunctionBlock(null);

		/**
		 * Seta as variáveis
		 */
		fis.setVariable("FAMACHA", famacha);
		fis.setVariable("ECC", escore);
		fis.setVariable("OPG", opg);

		/**
		 * Executa
		 */
		fis.evaluate();

		/**
		 * Obtem o resultado
		 */
		Variable nota = functionBlock.getVariable("NOTA");
		if (nota.getValue() < 0) {			
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(0.0, data, 3);
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);
		}else if (nota.getValue() >= 0 && nota.getValue() < 3.75) {			
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), data, 3);
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);	
		}else if (nota.getValue() == 3.75) {			
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), data, 5);
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);
		}else if (nota.getValue() > 3.75 && nota.getValue() < 6.25) {					
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), data, 2);
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);
		}else if (nota.getValue() == 6.25) {			
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), data, 4);
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);
		}else if (nota.getValue() > 6.25 && nota.getValue() <= 10) {				
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(nota.getValue(), data, 1);
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);
		}else{					
			RelatorioVermifugação resumoVermifugação = new RelatorioVermifugação(10.0, data, 1);
			animal.getListaNotas().add(resumoVermifugação);
			resumoVermifugação.setAnimal(animal);
		}
	}


	public ArrayList<Animal> fuzzyIndicePB(ArrayList<Animal> listAnimal, Double maiorIndice, Double menorIndice) {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("indiceEndogamia.fcl");
		FIS fis = FIS.load(input, true);

		//Erro ao carregar o arquivo
		if(fis == null){
			System.err.println("Não foi possivel carregar o arquivo!!");
			return null;
		}
		double percent =( ( (menorIndice < 0) ? menorIndice*(-1) : menorIndice) + 
				((maiorIndice < 0) ? maiorIndice*(-1) : maiorIndice) ) /100;

		FunctionBlock functionBlock = fis.getFunctionBlock(null);
		fis.setVariable("baixoUm", menorIndice);
		fis.setVariable("baixoDois", menorIndice + (percent*25));
		fis.setVariable("medioUm", menorIndice + (percent*20));
		fis.setVariable("medioDois", menorIndice + (percent*50));
		fis.setVariable("medioTres", menorIndice + (percent*75));
		fis.setVariable("altoUm", menorIndice + (percent*70));
		fis.setVariable("altoDois", maiorIndice);

		for (Animal animal : listAnimal) {		
			/**
			 * Seta as variáveis
			 */
			fis.setVariable("indice", animal.getIndice());
			fis.setVariable("endogamia", animal.getEndogamia());	
			/**
			 * Executa
			 */
			fis.evaluate();
			/**
			 * Obtem o resultado
			 */
			Variable nota = functionBlock.getVariable("nota");
			if (nota.getValue() < 0) {
				animal.setNotaCasalIndice(0.0);
			}else if (nota.getValue() >= 0 && nota.getValue() < 4) {
				animal.setNotaCasalIndice(nota.getValue());
			}else if (nota.getValue() == 4) {
				animal.setNotaCasalIndice(nota.getValue());
			}else if (nota.getValue() > 4 && nota.getValue() < 6) {
				animal.setNotaCasalIndice(nota.getValue());
			}else if (nota.getValue() == 6) {
				animal.setNotaCasalIndice(nota.getValue());
			}else if (nota.getValue() > 6 && nota.getValue() <= 10) {
				animal.setNotaCasalIndice(nota.getValue());
			}else{
				animal.setNotaCasalIndice(10.0);
			}
		}
		return listAnimal;
	}

	public static void main(String[] args) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("vermifuga.fcl");
		//String fileName = "vermifuga.fcl";
		FIS fis = FIS.load(input, true);

		//Erro ao carregar o arquivo
		if(fis == null){
			System.err.println("Não foi possivel carregar o arquivo!!");
			return;
		}

		FunctionBlock functionBlock = fis.getFunctionBlock(null);
		JFuzzyChart.get().chart(functionBlock);
	}
}
