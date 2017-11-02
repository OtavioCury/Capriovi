package br.ufpi.capriovi.facade.relatorios.reproducao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.dao.controleAnimal.ManejoReprodutivoDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.facade.relatorios.GenericRelFacade;
import br.ufpi.capriovi.suporte.relatorios.ResumoAniEstatistico;
import br.ufpi.capriovi.suporte.relatorios.ResumoAnimalReprodutivo;
import br.ufpi.capriovi.suporte.relatorios.ResumoPartos;

@Stateless
public class RelReproducaoFacade extends GenericRelFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9050178316588186759L;

	@Inject
	private AnimalDAO animalDAO;

	@Inject
	private ManejoReprodutivoDAO manejoReprodutivoDAO;

	/**
	 * Relatório CPM.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<ResumoPartos> relDePartos(List<Long> idsReb, 
			Date inicio, Date fim) {

		List<Animal> result = animalDAO.relNumeroDePartos(idsReb, 
				inicio, fim);
		List<ResumoPartos> res = new ArrayList<ResumoPartos>();
		/* intervalo de geracoes */
		List<Double> intervalo = new ArrayList<Double>();
		GregorianCalendar dataAvaliacaoGC = new GregorianCalendar();
		ResumoPartos x;
		double mediaMeses;
		int next = 0;
		int quant;
		for (Animal animal : result) {
			next = 0;
			for (ResumoPartos ani : res) {
				if(animal.getId() == ani.getAnimal().getId()){
					next = 1;
				}
			}
			if(next == 0){
				x = new ResumoPartos(animal);
				int sim = 0, dup = 0, tri = 0, qua = 0;
				//			ArrayList<ManejoReprodutivo> mrm = (ArrayList<ManejoReprodutivo>) animal.getManejoReprodutivoMatriz();
				List<ManejoReprodutivo> manejos = ordenaDataManejos(animal.getManejoReprodutivoMatriz());

				x.setIdadePrimeiroParto(Years.yearsBetween(
						new DateTime(animal.getNascimento()),
						new DateTime( manejos.get(0).getDataParto() ) ).getYears());

				mediaMeses = 0;
				quant = animal.getManejoReprodutivoMatriz().size();

				if(quant >= 2){//media cio pos parto
					List<ManejoReprodutivo> manejo = ((List<ManejoReprodutivo>) animal.getManejoReprodutivoMatriz());
					for (int i = 0; i < quant-1; i++) {
						mediaMeses += Months.monthsBetween(
								new DateTime(manejo.get(i).getDataParto()),
								new DateTime(manejo.get(i+1).getDataParto()) ).getMonths();
					}
					mediaMeses = mediaMeses / quant;
					x.setMediaCioPosParto(mediaMeses);
				}// fim media cio pos parto

				intervalo.clear();
				for (Animal animal2 : result) {//intervalo de geracao

					if (animal2.getPai() != null && animal2.getMae() != null) {
						if ((animal2.getPai().getCodigo().equals(animal.getCodigo()))
								|| (animal2.getMae().getCodigo().equals(animal.getCodigo())) ) {

							double days = Days.daysBetween(
									new DateTime(animal.getNascimento() ),
									new DateTime(animal2.getNascimento()  ) ).getDays();

							double media = days /360;

							intervalo.add(media);

						}
					}

				}
				if (!intervalo.isEmpty()) {
					double aux = 0.0;
					for (int i = 0; i < intervalo.size(); i++) {
						aux += intervalo.get(i);
					}
					x.setIntervaloPartos( aux / intervalo.size());
				} else {
					double days = Days.daysBetween(
							new DateTime(animal.getNascimento() ),
							new DateTime(dataAvaliacaoGC.getTime()  ) ).getDays();

					double media = days /360;

					x.setIntervaloPartos(media);
				}//Fim intervalo de geracao

				for (ManejoReprodutivo mr : manejos) {//contagem de partos
					if(mr.getParto() != null){
						if(mr.getParto().getCodigo() == 1){
							sim++;
						}else if(mr.getParto().getCodigo() == 2){
							dup++;
						}else if(mr.getParto().getCodigo() == 3){
							tri++;
						}else if(mr.getParto().getCodigo() == 4){
							qua++;
						}
					}
				}
				x.setPartoSimples(sim);
				x.setPartoDuplo(dup);
				x.setPartoTriplo(tri);
				x.setPartoQuadr(qua);
				x.setTotal( (sim + dup + tri + qua) );

				res.add(x);
			}
		}
		return res;
	}

	/**
	 * Relatório Femeas em idade reprodutiva.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<ResumoAnimalReprodutivo> relFemeasIdadeReprodutiva(List<Long> idsReb, Integer idadeEmMeses) {
		List<Animal> result = animalDAO.relFemeasIdadeReprodutiva(idsReb);

		List<ResumoAnimalReprodutivo> res = new ArrayList<ResumoAnimalReprodutivo>();
		int idade;
		for (Animal animal : result) {

			idade = Months.monthsBetween(
					new DateTime(animal.getNascimento()),
					new DateTime(new Date())).getMonths();

			if(idade >= idadeEmMeses ){
				ResumoAnimalReprodutivo resumo = new ResumoAnimalReprodutivo(animal);
				resumo.setIdadeMeses(idade);
				resumo.setIdadeAnos(idade/12);
				res.add(resumo);
			}
		}
		return res;
	}

	/**
	 * Relatório média cio Pos parto.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<ResumoAniEstatistico> relMediaCioPosParto(List<Rebanho> idsReb) {

		List<Animal> result = animalDAO.relMediaCioPosParto(getListIdsRebanho(idsReb));

		List<ResumoAniEstatistico> res = new ArrayList<ResumoAniEstatistico>();
		double mediaMeses;
		int quant;
		for (Animal animal : result) {
			mediaMeses = 0;
			quant = animal.getManejoReprodutivoMatriz().size();

			if(quant >= 2){
				List<ManejoReprodutivo> manejo = ((List<ManejoReprodutivo>) animal.getManejoReprodutivoMatriz());
				for (int i = 0; i < quant-1; i++) {
					mediaMeses += Months.monthsBetween(
							new DateTime(manejo.get(i).getDataParto()),
							new DateTime(manejo.get(i+1).getDataParto()) ).getMonths();
				}
				mediaMeses = mediaMeses / quant;
				ResumoAniEstatistico resumo = new ResumoAniEstatistico(animal);
				resumo.getQuantIdade().add(quant);
				resumo.getTotal().add(mediaMeses);
				res.add(resumo);
			}

		}
		return res;
	}

	/**
	 * Relatório cobertura do reprodutor.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<ResumoAnimalReprodutivo> relCoberturaReprodutor(List<Rebanho> idsReb, Date inicio, Date fim) {

		List<Animal> result = animalDAO.relCoberturaReprodutor(getListIdsRebanho(idsReb), inicio, fim);

		List<ResumoAnimalReprodutivo> res = new ArrayList<ResumoAnimalReprodutivo>();
		int aux = 0;
		for (Animal animal : result) {
			for (int i = 0; i < res.size(); i++) {
				aux = 0;
				if(res.get(i).getAnimal().getId() == animal.getId()){
					aux = 1;
					continue;

				}
			}
			if(aux == 0){
				ResumoAnimalReprodutivo resumo = new ResumoAnimalReprodutivo(animal);
				resumo.setSoma(animal.getManejoReprodutivo().size());
				res.add(resumo);
			}

		}
		return res;
	}

	/**
	 * Relatório cobertura do reprodutor.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<ManejoReprodutivo> relPerimetroEscrotal(List<Rebanho> rebanhos) {

		List<ManejoReprodutivo> manejos = manejoReprodutivoDAO.manejosRebs(getListIdsRebanho(rebanhos));
		List<ManejoReprodutivo> manejosRetornados = new ArrayList<ManejoReprodutivo>();
		List<Long> reprodutoresAnalisados = new ArrayList<Long>();					

		if (manejos != null) {
			for (int i = 0; i < manejos.size(); i++) {
				if (manejos.get(i).getPerimetroEscrotalReprodutor() != null) {

					boolean analisado = false;

					for (Long id : reprodutoresAnalisados) {
						if (manejos.get(i).getReprodutor().getId() == id) {
							analisado = true;
						}
					}

					if (analisado == false) {
						double soma = manejos.get(i).getPerimetroEscrotalReprodutor();
						double quantidade = 1;
						ManejoReprodutivo manejoReprodutivo = manejos.get(i);
						for (int j = 0; j < manejos.size(); j++) {
							if ((j != i) && (manejos.get(i).getReprodutor().getId() == manejos.get(j).getReprodutor().getId()) 
									&& (manejos.get(j).getPerimetroEscrotalReprodutor() != null)) {
								soma = soma + manejos.get(j).getPerimetroEscrotalReprodutor();
								quantidade++;
								if (manejos.get(j).getDataDaCobertura().after(manejoReprodutivo.getDataDaCobertura())) {
									manejoReprodutivo = manejos.get(j);
								}
							}
						}
						manejoReprodutivo.setMedia(soma/quantidade);
						manejoReprodutivo.setSoma(quantidade);
						manejosRetornados.add(manejoReprodutivo);
						reprodutoresAnalisados.add(manejos.get(i).getReprodutor().getId());
					}

				}

			}
		}		

		return manejosRetornados;

	}

	public List<ManejoReprodutivo> relPrevPartos(List<Rebanho> rebanhosMarcados) {
		List<ManejoReprodutivo> manejos = new ArrayList<ManejoReprodutivo>();
		manejos = manejoReprodutivoDAO.manejosRebs(getListIdsRebanho(rebanhosMarcados));

		for (ManejoReprodutivo manejoReprodutivo : manejos) {
			Calendar c = Calendar.getInstance(); 
			c.setTime(manejoReprodutivo.getDataDaCobertura()); 
			c.add(Calendar.DATE, 152);									
			manejoReprodutivo.setPrevisaoParto(c.getTime());
		}			

		return manejos;
	}
}
