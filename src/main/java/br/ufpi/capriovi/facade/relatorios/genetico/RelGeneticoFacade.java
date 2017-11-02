package br.ufpi.capriovi.facade.relatorios.genetico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.joda.time.DateTime;
import org.joda.time.Days;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.relatorios.GenericRelFacade;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.Endogamia;
import br.ufpi.capriovi.suporte.relatorios.ResumoGanhoGeneticoEsp;

@Stateless
public class RelGeneticoFacade extends GenericRelFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6667355379213517391L;

	@Inject
	private AnimalDAO animalDAO;

	private ArrayList<Double[]> matrizA;

	/**
	 * Relatório intervalo geração
	 * @param idsReb
	 * @return
	 */
	public List<Animal> relIntervaloGeracoes(Rebanho rebanho) {

		List<Animal> result = animalDAO.relIntervaloGeracoesReb(rebanho.getId());
		/* intervalo de geracoes */
		List<Double> intervalo = new ArrayList<Double>();
		GregorianCalendar dataAvaliacaoGC = new GregorianCalendar();

		Date dt = new Date();
		dataAvaliacaoGC.setTime(dt);


		for (Animal animal : result) { 

			intervalo.clear();
			for (Animal animal2 : result) {//intervalo de geracao

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
		}
		return result;
	}

	/**
	 * Relatório Endogamia.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	public List<Animal> relEndogamia(List<Rebanho> idsReb) throws IOException {

		List<Animal> result = animalDAO.relIntervaloGeracoes(getListIdsRebanho(idsReb));

		Endogamia end = new Endogamia();

		try {
			end.prepareInputFortran(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread p = new Thread(end);
		p.start();

		try {
			p.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		matrizA = end.getAllMatrizA(result.size());

		/**
		 * set endogamia de cada animal.(diagonal)
		 */
		for (int i = 0; i < result.size(); i++) {
			result.get(i).setEndogamia(matrizA.get(i)[i]);
		}

		return result;
	}

	/**
	 * Relatório Ganho Genetico esperado.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	public ResumoGanhoGeneticoEsp relGanhoGeneticoEsperado(Long idsReb, ResumoGanhoGeneticoEsp resumo) {

		List<Animal> result = animalDAO.animaisVivosRebanho(idsReb);		
		resumo.setTotalAniVivosReb(result.size());
		DescriptiveStatistics stats = new DescriptiveStatistics();
		ArrayList<Animal> machos = new ArrayList<Animal>();
		ArrayList<Animal> femeas = new ArrayList<Animal>();
		Double pesoAjustado;
		for (Animal animal : result) {
			if(resumo.getPesoEscolhido() == 60){
				pesoAjustado = animal.AjustaPeso60D();
			}else if(resumo.getPesoEscolhido() == 120){
				pesoAjustado = animal.AjustaPeso120D();
			}else{
				pesoAjustado = animal.AjustaPeso180D();
			}
			if(pesoAjustado >= 0){
				animal.setPesoAjustado(pesoAjustado);
				System.out.println(animal.getSexo().getCodigo());
				if(animal.getSexo().getCodigo().equals(1)){
					machos.add(animal);
				}else{
					femeas.add(animal);
				}
			}
		}

		machos = ordenaPesoAjustado(machos);
		femeas = ordenaPesoAjustado(femeas);

		resumo.setTotalAnimaisAptos(machos.size() + femeas.size());

		int aux = (int) Math.round( (machos.size()*resumo.getPropMachos()) );
		if (aux < 1) {
			aux = 1;
		}
		resumo.setMachosSel(aux);
		if (machos.size() > 0) {
			for (int i = 0; i < aux; i++) {
				stats.addValue(machos.get(i).getPesoAjustado());
			}
		}
		aux = (int) Math.round( (femeas.size()*resumo.getPropFemeas()) );
		if(aux<1){
			aux = 1;
		}
		resumo.setFemeasSel(aux);
		if (femeas.size() > 0) {
			for (int i = 0; i < aux; i++) {
				stats.addValue(femeas.get(i).getPesoAjustado());
			}
		}
		resumo.setDesvioPadraoCarac(stats.getStandardDeviation());
		resumo.setDiferencialSel( (resumo.getIntensidadeMacho() + resumo.getIntensidadeFemea()) * resumo.getDesvioPadraoCarac() );
		resumo.setGanhoGenEsp(resumo.getHerdabilidade() * resumo.getDiferencialSel());

		return resumo;
	}

	public void relTamPopulacao(List<Rebanho> rebanhos){

		for (Rebanho rebanho : rebanhos) {

			List<Long> id = new ArrayList<Long>();
			id.add(rebanho.getId());

			List<Animal> machos = new ArrayList<Animal>();
			List<Animal> femeas = new ArrayList<Animal>();

			machos = animalDAO.animaisSexo(id, 1);
			femeas = animalDAO.animaisSexo(id, 2);

			if ((machos.size() == 0) || (femeas.size() == 0)) {
				rebanho.setTamanhoEfetivo(0);
			}else{
				rebanho.setTamanhoEfetivo((4 * (double) (machos.size() * femeas.size())) /
						(double) (machos.size() + femeas.size()));
			}			
		}

	}

	public ArrayList<Double[]> getMatrizA() {
		return matrizA;
	}

	public void setMatrizA(ArrayList<Double[]> matrizA) {
		this.matrizA = matrizA;
	}
}
