package br.ufpi.capriovi.facade.relatorios.melhoramentoGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.ejml.simple.SimpleMatrix;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.facade.relatorios.GenericRelFacade;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.nsgaII.AlgorithmNsgaII;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.AnimalSelectionSolution;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.Endogamia;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.InputNsgaII;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.relatorios.ResumoAnimalIndice;
import br.ufpi.capriovi.suporte.relatorios.ResumoNsgaII;
import br.ufpi.capriovi.suporte.tiposEnum.TipoDesenvolvimentoPonderal;

@Stateless
public class MelhorGeneticoFacade extends GenericRelFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9087240260321620120L;

	@Inject
	private AnimalDAO animalDAO;

	ArrayList<ResumoAnimalIndice> animaisAptos;

	List<Animal> animais;
	ArrayList<Integer> idAnimaisAptos = new ArrayList<Integer>();
	ArrayList<Animal> animaisAptosSIM = new ArrayList<Animal>();

	/**
	 * retorna os pais e as maes com as deps calculadas.
	 * 
	 * @param inputNsgaII
	 * @param idsReb
	 * @return
	 * @throws MensagensExceptions 
	 */
	public HashMap<String, ArrayList<Animal> > selecaoIndividualMassal(InputNsgaII inputNsgaII, List<Rebanho> idsReb) throws MensagensExceptions{
		animais = animalDAO.relNsgaII(getListIdsRebanho(idsReb));

		/* Get a DescriptiveStatistics instance */
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int qntMachos = 0;
		int qntFemeas = 0;
		int ind=0, posAnt = 0;
		Double pesoAjustado;
		/* intervalo de geracoes */
		List<Double> intervalo = new ArrayList<Double>();
		GregorianCalendar dataAvaliacaoGC = new GregorianCalendar();

		Endogamia end = new Endogamia();

		try {
			end.prepareInputFortran(animais);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread p = new Thread(end);
		p.start();

		/**
		 * intervalo de geracao;
		 * add em animais aptos;
		 */
		for (Animal animal : animais) { 

			intervalo.clear();
			for (Animal animal2 : animais) {//intervalo de geracao

				if (animal2.getPai() != null && animal2.getMae() != null) {
					if ((animal2.getPai().getCodigo().equals(animal.getCodigo()))
							|| (animal2.getMae().getCodigo().equals(animal.getCodigo())) ) {

						double days = Days.daysBetween(
								new DateTime(animal.getNascimento() ),
								new DateTime(animal2.getNascimento()  ) ).getDays();
						double x = days /360;
						intervalo.add(x);
					}
				}

			}
			if (!intervalo.isEmpty()) {
				double aux = 0.0;
				for (int i = 0; i < intervalo.size(); i++) {
					aux += intervalo.get(i);
				}
				animal.setIntervaloGeracao(aux / intervalo.size());
			} else {
				double days = Days.daysBetween(
						new DateTime(animal.getNascimento() ),
						new DateTime(dataAvaliacaoGC.getTime()  ) ).getDays();

				double x = days /360;

				animal.setIntervaloGeracao(x);
			}//Fim intervalo de geracao

			if(inputNsgaII.getDiasPesagem() == 60){
				pesoAjustado = animal.AjustaPeso60D();
			}else if(inputNsgaII.getDiasPesagem() == 120){
				pesoAjustado = animal.AjustaPeso120D();
			}else{
				pesoAjustado = animal.AjustaPeso180D();
			}

			animal.setIdadeAtualAnos(Years.yearsBetween(
					new DateTime(animal.getNascimento()),
					new DateTime(dataAvaliacaoGC.getTime())).getYears() );

			double idade = Months.monthsBetween(
					new DateTime(animal.getNascimento()),
					new DateTime(dataAvaliacaoGC.getTime())).getMonths();/*idade em meses*/


			if (animal.getSexo().getCodigo().equals(1)//macho
					&& (idade >= inputNsgaII.getIdadeAcasalamentoMachos())
					&& animal.getStatus().getCodigo().equals(1)//vivo
					&& pesoAjustado >= 0) {
				stats.addValue(pesoAjustado);
				qntMachos++;
				animal.setPesoAjustado(pesoAjustado);
				animal.setPosAnt(ind);
				animal.setPosAtual(posAnt);
				animaisAptosSIM.add(animal);
				idAnimaisAptos.add(ind);
				posAnt++;
			} else if (animal.getSexo().getCodigo().equals(2)//femea
					&& (idade >= inputNsgaII.getIdadeAcasalamentoFemeas())
					&& animal.getStatus().getCodigo().equals(1)//vivo
					&& pesoAjustado >= 0) {
				stats.addValue(pesoAjustado);
				qntFemeas++;
				animal.setPesoAjustado(pesoAjustado);
				animal.setPosAnt(ind);
				animal.setPosAtual(posAnt);
				animaisAptosSIM.add(animal);
				idAnimaisAptos.add(ind);
				posAnt++;
			}
			ind++;
		}

		// separa em grupos
		HashMap<String, ArrayList<Animal>> grupos = new HashMap<String, ArrayList<Animal>>();
		for (Animal animal : animaisAptosSIM) {
			if (!grupos.containsKey(animal.getGrupo())) {
				grupos.put(animal.getGrupo(), new ArrayList<Animal>());
			}
			grupos.get(animal.getGrupo()).add(animal);
		}

		// seta o peso corrigido do animal e DEP
		double mediaGrupo = 0;
		Collection<ArrayList<Animal>> aux = grupos.values();
		for (ArrayList<Animal> GrupoAnimais : aux) {
			for (Animal animal : GrupoAnimais) {
				mediaGrupo += animal.getPesoAjustado();
			}
			mediaGrupo = mediaGrupo / GrupoAnimais.size();

			for (Animal animal : GrupoAnimais) {
				animal.setMediaGrupo(mediaGrupo);
				animal.setPesoCorrigido(animal.getPesoAjustado() - mediaGrupo);
				animal.setDiferencialEsperado(((animal.getPesoCorrigido() * inputNsgaII.getHerdabilidade()) / 2));
			}
			mediaGrupo = 0;
		}

		/* Calculando a media e desvio padrao da caracteristica (peso) */
		inputNsgaII.setMediaCaracteristica(stats.getMean());
		inputNsgaII.setDesvioPadraoCaracteristica(stats.getStandardDeviation());

		try {
			p.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		ArrayList<Double[]> matrizA = null;
		try {
			matrizA = end.getMatrizA(animais.size(), idAnimaisAptos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Animal> machos = new ArrayList<Animal>();
		ArrayList<Animal> femeas = new ArrayList<Animal>();

		/**
		 * set endogamia de cada animal.(diagonal)
		 */
		for (Animal ani : animaisAptosSIM) {
			ani.setLinhaMatrizA(matrizA.get(ani.getPosAtual()));
			ani.setEndogamia(ani.getLinhaMatrizA()[ani.getPosAnt()]);
			ani.setGanhoGenetico(inputNsgaII.getMediaCaracteristica() + (inputNsgaII.getHerdabilidade() * ani.getPesoCorrigido()));
			if(ani.getSexo().getCodigo().equals(1)){
				machos.add(ani);
			}else{
				femeas.add(ani);
			}
		}

		machos = ordenaDEP(machos);
		femeas = ordenaDEP(femeas);


		HashMap<String, ArrayList<Animal>> MachosEFemeas = new HashMap<String, ArrayList<Animal>>();
		MachosEFemeas.put("machos", machos);
		MachosEFemeas.put("femeas", femeas);
		return MachosEFemeas;
	}

	/**
	 * Algoritmo NSGAII com framework Jmetal para melhoramento genetico animal com uma caracteristica.
	 *   
	 * @param inputNsgaII
	 * @param idsReb
	 * @return
	 * @throws MensagensExceptions 
	 */
	public ResumoNsgaII NSGAII(List<Animal> machos, List<Animal> femeas, InputNsgaII inputNsgaII) throws MensagensExceptions{		
		if(machos.size() == 0 && femeas.size() == 0){
			throw new MensagensExceptions(MensagensExceptions.MachoseFemeasInsuficienteException);
		}else{ 
			if(femeas.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.FemeasInsuficienteException);
			}
			if(machos.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.MachosInsuficienteException);
			}
		}

		AlgorithmNsgaII algorithm = new AlgorithmNsgaII(inputNsgaII, machos, femeas);
		ArrayList<AnimalSelectionSolution> results = null;
		try {
			results = algorithm.solveProblem();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}//magic

		for (Animal ani : results.get(0).getAnimaisSelecionados()) {
			ani.setGanhoGenetico(inputNsgaII.getMediaCaracteristica() + (inputNsgaII.getHerdabilidade() * ani.getPesoCorrigido()));
		}
		ArrayList<Animal> filhos = new ArrayList<Animal>();
		for (int i = 0 ; i < (results.get(0).getAnimaisSelecionados().size()/2) ; i = i + 2) {
			Animal a = new Animal(
					(results.get(0).getAnimaisSelecionados().get(i).getDiferencialEsperado() + 
							results.get(0).getAnimaisSelecionados().get(i+1).getDiferencialEsperado())/2, 
					(results.get(0).getAnimaisSelecionados().get(i).getGanhoGenetico() + 
							results.get(0).getAnimaisSelecionados().get(i+1).getGanhoGenetico())/2,
					(results.get(0).getAnimaisSelecionados().get(i+1).getLinhaMatrizA()[results.get(0).getAnimaisSelecionados().get(i).getPosAnt()] / 2));
			filhos.add(a);
		}

		Collections.sort(filhos, new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return Double.valueOf(o2.getGanhoGenetico()).compareTo(Double.valueOf(o1.getGanhoGenetico()));
			}
		});
		ResumoNsgaII resumo = new ResumoNsgaII(results.get(0).getResultObjective(), 
				results.get(0).getResultObjective2(), results.get(0).getResultObjective3(), filhos);
		return resumo;
	}


	public ArrayList<Animal> algGenIndices(List<Animal> machos, List<Animal> femeas) throws MensagensExceptions{
		if(machos.size() == 0 && femeas.size() == 0){
			throw new MensagensExceptions(MensagensExceptions.MachoseFemeasInsuficienteException);
		}else{
			if(femeas.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.FemeasInsuficienteException);
			}
			if(machos.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.MachosInsuficienteException);
			}
		}

		AlgorithmNsgaII algorithm = new AlgorithmNsgaII(machos, femeas);
		ArrayList<AnimalSelectionSolution> results = null;
		try {
			results = algorithm.solveProblem();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}//magic

		ArrayList<Animal> filhos = new ArrayList<Animal>();
		for (int i = 0 ; i < results.get(0).getAnimaisSelecionados().size(); i = i + 2) {
			Animal a = new Animal(
					(results.get(0).getAnimaisSelecionados().get(i).getIndice() + 
							results.get(0).getAnimaisSelecionados().get(i+1).getIndice())/2, 
					(results.get(0).getAnimaisSelecionados().get(i+1).getLinhaMatrizA()[results.get(0).getAnimaisSelecionados().get(i).getPosAnt()] / 2));
			filhos.add(a);
		}
		// não precisa mais do Fuzzy
		//Fuzzy f = new Fuzzy();
		filhos = ordenaIndice(filhos);
		//filhos = f.fuzzyIndicePB(filhos, filhos.get(0).getIndice(), filhos.get(filhos.size()-1).getIndice());

		return filhos;

	}

	/** 
	 * Para 4 caracteristicas.
	 * Metodo principal para os calculos dos indices.
	 *  Em ordem: Peso, IPP, PTCN, PTCD
	 * @param inputNsgaII
	 * @param idsReb
	 * @return
	 * @throws MensagensExceptions 
	 */
	public HashMap<String, ArrayList<Animal>> selecaoIndices(InputNsgaII inputNsgaII, List<Rebanho> idsReb) throws MensagensExceptions{

		List<Animal> animais = animalDAO.relNsgaII(getListIdsRebanho(idsReb));
		animaisAptos = new ArrayList<ResumoAnimalIndice>();
		ArrayList<Integer> idAnimaisAptos = new ArrayList<Integer>();
		ArrayList<Animal> filhos = null;
		int qntMachos = 0;
		int qntFemeas = 0;
		int ind=0, posAnt = 0;

		inputNsgaII.setQuantCarac(4);
		int quantCarc = inputNsgaII.getQuantCarac();

		/* Get a DescriptiveStatistics instance */
		DescriptiveStatistics[] stats = new DescriptiveStatistics[quantCarc];
		for (int i = 0; i < quantCarc; i++) {
			stats[i] = new DescriptiveStatistics();
		}
		Double[] variancia = new Double[quantCarc];
		Double[] media = new Double[quantCarc];
		double[][] matrizG = new double[quantCarc][quantCarc];
		double[] ganhosDesejados = new double[quantCarc];
		//Herdabilidade para o tcc do Thasciano
		//double[] herdabilidae = {0.15, 0.19, 0.22, 0.09};
		//Herdabilidade para o mestrado do Lailsson

		double[] herdabilidae = {0.14, 0.21, 0.08, 0.14};

		//Matriz de relação genetica dpara o tcc Thasciano
		//		double[][] relGen = {	{0		, 0.25	, -0.13	, -0.3},
		//								{0.25	, 0		, 0.29	, 0.99},
		//								{-0.13	,  0.29	, 0		, 0.93},
		//								{-0.3	, 0.99	, 0.93	, 0}
		//		};
		// matriz de relação genetica lailsson
		double[][] relGen = {{0		, 0.09 	, 0.72  , 0.59},
				{0.09  	, 0		, 0.61	, 0.39},
				{0.72	, 0.61	, 0		, 0.64},
				{0.59	, 0.39	, 0.64	, 0}
		};

		Double pesoAjustado = null, PTCN = null, PTCD = null, IPP = null;
		double mediaPGeral = 0.0, mediaIPPGeral = 0.0, mediaPTCNGeral = 0.0, mediaPTCDGeral = 0.0;

		Endogamia end = new Endogamia();
		// preparando o fortran
		try {
			end.prepareInputFortran(animais);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread p = new Thread(end);
		p.start();

		GregorianCalendar dataAvaliacaoGC = new GregorianCalendar();
		dataAvaliacaoGC.setTime(new Date());

		for (Animal animal : animais) { 
			filhos = new ArrayList<Animal>();
			for (Animal animal2 : animais) {
				if (animal2.getPai() != null && animal2.getPai().getId().equals(animal.getId())) {
					filhos.add(animal2);
				} else if( animal2.getMae() != null && animal2.getMae().getId().equals(animal.getId())){
					filhos.add(animal2);
				}
			}
			animal.setFilhos(filhos);
		}

		/**
		 * seleção, Ajustesde de pesos.
		 */
		for (Animal animal : animais) { 

			animal.setIdadeAtualAnos(Years.yearsBetween(
					new DateTime(animal.getNascimento()),
					new DateTime(dataAvaliacaoGC.getTime())).getYears() );

			double idade = Months.monthsBetween(
					new DateTime(animal.getNascimento()),
					new DateTime(dataAvaliacaoGC.getTime())).getMonths();/*idade em meses*/

			if(idsReb.get(0).getId() == 006 ){
				pesoAjustado = animal.getPeso120();
				IPP = animal.getIpp();
				PTCN = animal.getPtcn();
				PTCD = animal.getPtcd();
			}else{
				if(inputNsgaII.getDiasPesagem() == 60){
					pesoAjustado = animal.AjustaPeso60D();
				}else if(inputNsgaII.getDiasPesagem() == 120){
					pesoAjustado = animal.AjustaPeso120D();
				}else{
					pesoAjustado = animal.AjustaPeso180D();
				}
				IPP = buscaIdadPP(animal);
				PTCN = buscaPTCN(animal);
				PTCD = buscaPTCD(animal);
			}

			System.out.println(animal.getNomeNumero()+": " + pesoAjustado +"; "+ IPP + "; " +PTCN + "; "+ PTCD);

			if (animal.getSexo().getCodigo().equals(1)//macho
					&& (idade >= inputNsgaII.getIdadeAcasalamentoMachos())
					&& animal.getStatus().getCodigo().equals(1)//vivo
					&& pesoAjustado >= 0 
					&& IPP != null && PTCN != null && PTCD != null) {
				qntMachos++;
				mediaPGeral += pesoAjustado;
				mediaIPPGeral += IPP;
				mediaPTCNGeral += PTCN;
				mediaPTCDGeral += PTCD;
				animal.setPosAnt(ind);
				animal.setPosAtual(posAnt);
				animaisAptos.add(new ResumoAnimalIndice(animal, pesoAjustado, 
						IPP, PTCN, PTCD) );
				idAnimaisAptos.add(ind);
				posAnt++;
			} else if (animal.getSexo().getCodigo().equals(2)//femea
					&& (idade >= inputNsgaII.getIdadeAcasalamentoFemeas())
					&& animal.getStatus().getCodigo().equals(1)//vivo
					&& pesoAjustado >= 0
					&& IPP != null && PTCN != null && PTCD != null) {
				qntFemeas++;
				mediaPGeral += pesoAjustado;
				mediaIPPGeral += IPP;
				mediaPTCNGeral += PTCN;
				mediaPTCDGeral += PTCD;
				animal.setPosAnt(ind);
				animal.setPosAtual(posAnt);
				animaisAptos.add(new ResumoAnimalIndice(animal, pesoAjustado, 
						IPP, PTCN, PTCD) );
				idAnimaisAptos.add(ind);
				posAnt++;
			}
			ind++;
		}
		mediaPGeral = mediaPGeral / animaisAptos.size();
		mediaIPPGeral = mediaIPPGeral / animaisAptos.size();
		mediaPTCNGeral = mediaPTCNGeral / animaisAptos.size();
		mediaPTCDGeral = mediaPTCDGeral / animaisAptos.size();

		System.out.println("Media Geral(P,IPP,PTCN,PTCD): " + mediaPGeral +"; "+ mediaIPPGeral + "; " +mediaPTCNGeral + "; "+ mediaPTCDGeral);

		/******************************* GRUPOS DE COMTEMPORANEOS***************************************************/
		HashMap<String, ArrayList<ResumoAnimalIndice>> grupos = new HashMap<String, ArrayList<ResumoAnimalIndice>>();
		for (ResumoAnimalIndice resumo : animaisAptos) {
			if (!grupos.containsKey(resumo.getAnimal().getGrupo())) {
				grupos.put(resumo.getAnimal().getGrupo(), new ArrayList<ResumoAnimalIndice>());
			}
			grupos.get(resumo.getAnimal().getGrupo()).add(resumo);
		}

		// seta o peso corrigido do animal e DEP
		double mPeso = 0, mIPP = 0, mPTCN = 0, mPTCD = 0;
		Collection<ArrayList<ResumoAnimalIndice>> aux = grupos.values();
		for (ArrayList<ResumoAnimalIndice> GrupoAnimais : aux) {
			for (ResumoAnimalIndice animal : GrupoAnimais) {
				mPeso += animal.getPesoAjustado();
				mIPP += animal.getIdadePrimParto(); 
				mPTCN += animal.getPesoTotCriasNasc(); 
				mPTCD += animal.getPesoTotCriasDesm();
			}
			mPeso = mPeso / GrupoAnimais.size();
			mIPP = mIPP / GrupoAnimais.size(); 
			mPTCN = mPTCN / GrupoAnimais.size(); 
			mPTCD = mPTCD / GrupoAnimais.size();

			for (ResumoAnimalIndice animal : GrupoAnimais) {
				animal.setMediaPeso(mPeso);
				animal.setMediaIPP(mIPP);
				animal.setMediaPTCN(mPTCN);
				animal.setMediaPTCD(mPTCD);

				animal.setPesoCorrigido(animal.getPesoAjustado() - mPeso);
				animal.setIppCorrigido(animal.getIdadePrimParto() - mIPP);
				animal.setPtcnCorrigido(animal.getPesoTotCriasNasc() - mPTCN);
				animal.setPtcdCorrigido(animal.getPesoTotCriasDesm() - mPTCD);
				System.out.println(animal.toString());
			}
			mPeso = 0; mIPP = 0; mPTCN = 0; mPTCD = 0;
		}
		/*******************************FIM GRUPOS DE COMTEMPORANEOS***************************************************/

		//media, var e desvio Padrão das caractersiticas
		for (ResumoAnimalIndice resumo : animaisAptos) {
			stats[0].addValue(resumo.getPesoCorrigido() + mediaPGeral);
			stats[1].addValue(resumo.getIppCorrigido() + mediaIPPGeral);
			stats[2].addValue(resumo.getPtcnCorrigido() + mediaPTCNGeral);
			stats[3].addValue(resumo.getPtcdCorrigido() + mediaPTCDGeral);
		}

		/* popula vetor de variancias e ganhos geneticos desejados das respectivas caracteristicas */
		for (int i = 0; i < quantCarc; i++) {
			variancia[i] = (Double) stats[i].getVariance();
			media[i] = (Double) stats[i].getMean();
			if(i == 1){
				ganhosDesejados[i] =  -(media[i] * 0.1);
			}else{
				ganhosDesejados[i] = media[i] * 0.1;
			}
		}
		//calcula diagonal principal
		for (int i = 0; i < quantCarc; i++) {
			matrizG[i][i] = variancia[i] * herdabilidae[i];
		}

		//calcular vertor de ganhos desejados
		//montar matriz G, inverter e multiplicar pelos ganhos desejados
		for (int i = 0; i < quantCarc; i++) {
			for (int j = 0; j < quantCarc; j++) {
				if(i!=j){
					matrizG[i][j] = relGen[i][j] * Math.sqrt((matrizG[i][i] * matrizG[j][j] ));
				}
			}
		}

		SimpleMatrix G = new SimpleMatrix(matrizG);
		SimpleMatrix I = G.invert();

		double[] indices = new double[quantCarc];

		//multiplicação
		for (int i = 0; i < quantCarc; i++) {
			for (int j = 0; j < quantCarc; j++) {
				indices[i] += I.get(i, j) * ganhosDesejados[j];
			}
		}
		System.out.println("Indices: "+indices[0]+" " + indices[1]+ " "
				+ indices[2] +" "+ indices[3] + " ");
		//calcular indices de cada animal
		double indIndividual = 0;
		for (ResumoAnimalIndice resumo : animaisAptos) {
			indIndividual = 0;
			indIndividual += (resumo.getPesoCorrigido() * indices[0]);
			indIndividual += (resumo.getIppCorrigido() * indices[1]);
			indIndividual += (resumo.getPtcnCorrigido() * indices[2]);
			indIndividual += (resumo.getPtcdCorrigido() * indices[3]);

			resumo.getAnimal().setIndice(indIndividual);
		}

		try {
			p.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		ArrayList<Double[]> matrizA = null;
		try {
			matrizA = end.getMatrizA(animais.size(), idAnimaisAptos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Animal> machos = new ArrayList<Animal>();
		ArrayList<Animal> femeas = new ArrayList<Animal>();
		/**
		 * set endogamia de cada animal.(diagonal)
		 */
		for (ResumoAnimalIndice ani : animaisAptos) {
			ani.getAnimal().setLinhaMatrizA(matrizA.get(ani.getAnimal().getPosAtual()));
			ani.getAnimal().setEndogamia(ani.getAnimal().getLinhaMatrizA()[ani.getAnimal().getPosAnt()]);
			if(ani.getAnimal().getSexo().getCodigo().equals(1)){
				machos.add(ani.getAnimal());
			}else{
				femeas.add(ani.getAnimal());
			}
		}

		if(machos.size() == 0 && femeas.size() == 0){
			throw new MensagensExceptions(MensagensExceptions.MachoseFemeasInsuficienteException);
		}else{ 
			if(femeas.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.FemeasInsuficienteException);
			}
			if(machos.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.MachosInsuficienteException);
			}
		}
		machos = ordenaIndice(machos);
		femeas = ordenaIndice(femeas);

		HashMap<String, ArrayList<Animal>> MachosEFemeas = new HashMap<String, ArrayList<Animal>>();
		MachosEFemeas.put("machos", machos);
		MachosEFemeas.put("femeas", femeas);

		return MachosEFemeas;
	}


	public ArrayList<Animal> ordenaDEP(ArrayList<Animal> list){
		Collections.sort(list, new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o2.getDiferencialEsperado().compareTo(o1.getDiferencialEsperado());
			}
		});
		return list;
	}

	public ArrayList<Animal> ordenaNota(ArrayList<Animal> list){
		Collections.sort(list, new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o2.getNotaCasalIndice().compareTo(o1.getNotaCasalIndice());
			}
		});
		return list;
	}
	/**
	 * 
	 * @param Animal
	 */
	public List<Long> getListIdsRebanho(List<Rebanho> rebanhos){
		List<Long> aux = new ArrayList<Long>();
		for (Rebanho r : rebanhos) {
			aux.add(r.getId());
		}
		return aux;
	}
	/**
	 * 
	 * @param a
	 * @return
	 */
	public int buscaIndFemea(Animal a){
		ArrayList<Animal> filhos= a.getFilhos();

		for (int i = 0; i < filhos.size(); i++) {
			if(filhos.get(i).getSexo().getCodigo() == 2){
				return i;
			}
		}
		return -1;
	}

	public Double mediaIPP(Animal a){
		ArrayList<Animal> filhos = a.getFilhos();
		ArrayList<Animal> filhosAux;
		Double soma = 0.0;
		int cont = 0, ind;
		for (int i = 0; i < filhos.size(); i++) {
			if(filhos.get(i).getSexo().getCodigo() == 2){
				Animal a1 = filhos.get(i);

				filhosAux = a1.getFilhos();
				if(filhosAux.size()>0){
					cont++;
					Collections.sort(filhosAux, new Comparator<Animal>() {
						@Override
						public int compare(Animal o1, Animal o2) {
							return o1.getNascimento().compareTo(o2.getNascimento());
						}
					});

					Animal a2 = filhosAux.get(filhosAux.size() - 1);
					soma += Days.daysBetween(new DateTime(a1.getNascimento()), new DateTime(a2.getNascimento() )).getDays();
				}
			}
		}
		if(cont>0){
			return soma/cont;
		}else{
			return -1.0;
		}
	}

	public Double buscaIdadPP(Animal a){
		ArrayList<Animal> filhos = new ArrayList<Animal>();
		int flag = 0;
		double mediaIPP;
		if(a.getSexo().getCodigo() == 1){//macho
			if(a.getMae() != null && a.getMae().getFilhos().size() == 0){// filhas da mae
				return mediaIPP(a.getMae());
			}else{//olhar para as filhas do pai.
				return mediaIPP(a);
			}
		}else{// femea
			filhos = a.getFilhos();
			flag=3;
			if(filhos.size() == 0 && a.getMae() != null){
				filhos =  a.getMae().getFilhos();
				flag=1;
			}
		}

		if(filhos.size() > 0){
			Collections.sort(filhos, new Comparator<Animal>() {
				@Override
				public int compare(Animal o1, Animal o2) {
					return o1.getNascimento().compareTo(o2.getNascimento());
				}
			});

			if(filhos.size() > 0){
				Animal a2 = filhos.get(0);
				//filhos da femea
				return (double) Days.daysBetween(new DateTime(a.getNascimento()), new DateTime(a2.getNascimento())).getDays();

			}
		}

		return null;
	}

	public Double mediaPTCN(Animal a){
		ArrayList<Animal> filhos = a.getFilhos();
		ArrayList<Animal> filhosAux;
		Double soma = 0.0;
		int cont = 0, ind;
		for (int i = 0; i < filhos.size(); i++) {
			if(filhos.get(i).getSexo().getCodigo() == 2){
				Animal a1 = filhos.get(i);
				filhosAux = a1.getFilhos();
				if(filhosAux.size()>0){
					cont++;
					Collections.sort(filhosAux, new Comparator<Animal>() {
						@Override
						public int compare(Animal o1, Animal o2) {
							return o1.getNascimento().compareTo(o2.getNascimento());
						}
					});

					double peso = 0;
					ArrayList<Animal> irmaosGemeos = new ArrayList<Animal>();
					List<DesenvolvimentoPonderal> dp = null;
					irmaosGemeos.add(filhosAux.get(0));
					for (int j = 1; j < filhosAux.size(); j++) {
						if(irmaosGemeos.get(0).getNascimento().equals(filhosAux.get(j).getNascimento())){
							irmaosGemeos.add(filhosAux.get(j));
						}
					}
					for (Animal aux : irmaosGemeos) {
						dp = aux.getDesenvolvimentoPonderal();
						for (DesenvolvimentoPonderal desenvolvimentoPonderal : dp) {
							if(desenvolvimentoPonderal.getTipoDesenvolvimento().getCodigo() == 1){
								peso += desenvolvimentoPonderal.getPeso();
								break;
							}
						}
					}
					soma += peso/irmaosGemeos.size();
				}

			}//if

		}//for

		if(cont>0){
			return soma/cont;
		}else{
			return -1.0;
		}
	}

	public Double buscaPTCN(Animal a){
		ArrayList<Animal> filhos = new ArrayList<Animal>();
		if(a.getSexo().getCodigo() == 1){//macho
			if(a.getMae() != null && a.getMae().getFilhos().size() == 0){// mae do macho
				return mediaPTCN(a.getMae());
			}
			if(filhos.size() == 0){//filhas do macho
				return mediaPTCN(a);
			}
		}else{//femeas
			filhos = a.getFilhos();
			if(filhos.size() == 0 && a.getMae() != null){
				filhos =  a.getMae().getFilhos();
			}
		}

		if(filhos.size() > 0){
			Collections.sort(filhos, new Comparator<Animal>() {
				@Override
				public int compare(Animal o1, Animal o2) {
					return o1.getNascimento().compareTo(o2.getNascimento());
				}
			});
			if(filhos.size() > 0){
				double peso = 0;
				ArrayList<Animal> irmaosGemeos = new ArrayList<Animal>();
				List<DesenvolvimentoPonderal> dp = null;
				irmaosGemeos.add(filhos.get(0));
				for (int i = 1; i < filhos.size(); i++) {
					if(irmaosGemeos.get(0).getNascimento().equals(filhos.get(i).getNascimento())){
						irmaosGemeos.add(filhos.get(i));
					}
				}
				for (Animal i : irmaosGemeos) {
					dp = i.getDesenvolvimentoPonderal();
					for (DesenvolvimentoPonderal desenvolvimentoPonderal : dp) {
						if(desenvolvimentoPonderal.getTipoDesenvolvimento().getCodigo() == 1){
							peso += desenvolvimentoPonderal.getPeso();
							break;
						}
					}
				}
				return peso/irmaosGemeos.size();
			}

		}

		return null;
	}

	public Double mediaPTCD(Animal a){
		ArrayList<Animal> filhos = a.getFilhos();
		ArrayList<Animal> filhosAux;
		Double soma = 0.0;
		int cont = 0;
		for (int i = 0; i < filhos.size(); i++) {
			if(filhos.get(i).getSexo().getCodigo() == 2){
				Animal a1 = filhos.get(i);
				filhosAux = a1.getFilhos();
				if(filhosAux.size()>0){
					cont++;
					//					Collections.sort(filhosAux, new Comparator<Animal>() {
					//						@Override
					//						public int compare(Animal o1, Animal o2) {							
					//							return o1.getDesmame().compareTo(o2.getDesmame());														
					//						}
					//					});

					double peso = 0;
					ArrayList<Animal> irmaosGemeos = new ArrayList<Animal>();
					List<DesenvolvimentoPonderal> dp = null;
					irmaosGemeos.add(filhosAux.get(0));
					//					for (int j = 1; j < filhosAux.size(); j++) {
					//						if(irmaosGemeos.get(0).getDesmame().equals(filhosAux.get(j).getDesmame())){
					//							irmaosGemeos.add(filhosAux.get(j));
					//						}
					//					}
					for (Animal aux : irmaosGemeos) {
						dp = aux.getDesenvolvimentoPonderal();
						for (DesenvolvimentoPonderal desenvolvimentoPonderal : dp) {
							if(desenvolvimentoPonderal.getTipoDesenvolvimento().getCodigo() == 1){
								peso += desenvolvimentoPonderal.getPeso();
								break;
							}
						}
					}
					soma += peso/irmaosGemeos.size();
				}

			}//if

		}//for

		if(cont>0){
			return soma/cont;
		}else{
			return -1.0;
		}
	}


	public Double buscaPTCD(Animal a){
		ArrayList<Animal> filhos = new ArrayList<Animal>();
		if(a.getSexo().getCodigo() == 1){//macho
			if(a.getMae() != null && a.getMae().getFilhos().size() == 0){// mae do macho
				return mediaPTCD(a.getMae());
			}
			if(filhos.size() == 0){//filhas do macho
				return mediaPTCD(a);
			}
		}else{//femeas
			filhos = a.getFilhos();
			if(filhos.size() == 0 && a.getMae() != null){
				filhos =  a.getMae().getFilhos();
			}
		}

		if(filhos.size() > 0){
			//			Collections.sort(filhos, new Comparator<Animal>() {
			//				@Override
			//				public int compare(Animal o1, Animal o2) {
			//					return o1.getDesmame().compareTo(o2.getDesmame());
			//				}
			//			});
			if(filhos.size() > 0){
				double peso = 0;
				ArrayList<Animal> irmaosGemeos = new ArrayList<Animal>();
				List<DesenvolvimentoPonderal> dp = null;
				irmaosGemeos.add(filhos.get(0));
				//				for (int i = 1; i < filhos.size(); i++) {
				//					if(irmaosGemeos.get(0).getDesmame().equals(filhos.get(i).getDesmame())){
				//						irmaosGemeos.add(filhos.get(i));
				//					}
				//				}
				for (Animal i : irmaosGemeos) {
					dp = i.getDesenvolvimentoPonderal();
					for (DesenvolvimentoPonderal desenvolvimentoPonderal : dp) {
						if(desenvolvimentoPonderal.getTipoDesenvolvimento().getCodigo() == 1){
							peso += desenvolvimentoPonderal.getPeso();
							break;
						}
					}
				}
				return peso/irmaosGemeos.size();
			}
		}
		return null;

	}

	public Integer buscaIndiceDesmame(List<DesenvolvimentoPonderal> desnP){
		//		Collections.sort(desnP, new Comparator<DesenvolvimentoPonderal>() {
		//			@Override
		//			public int compare(DesenvolvimentoPonderal o1, DesenvolvimentoPonderal o2) {
		//				return o1.getData().compareTo(o2.getData());
		//			}
		//		});
		if(desnP.size() > 0){
			for (int i = 0; i < desnP.size(); i++) {
				if(desnP.get(i).getTipoDesenvolvimento().equals(TipoDesenvolvimentoPonderal.TIPO_AODESMAME) ){
					return i;
				}
			}
		}
		return null;
	}

	public ArrayList<ResumoAnimalIndice> getAnimaisAptos() {
		return animaisAptos;
	}

	public void setAnimaisAptos(ArrayList<ResumoAnimalIndice> animaisAptos) {
		this.animaisAptos = animaisAptos;
	}

}
