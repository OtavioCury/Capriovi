package br.ufpi.capriovi.recursos.relatorios;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.dao.cadastros.RebanhoDAO;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.facade.relatorios.reproducao.RelReproducaoFacade;
import br.ufpi.capriovi.suporte.relatorios.ResumoAnimalReprodutivo;
import br.ufpi.capriovi.suporte.relatorios.ResumoPartos;

@Path("relReproducao")
@Produces(MediaType.APPLICATION_JSON)
public class RelReproducaoResource {

	@Inject
	private RelReproducaoFacade relReproducaoFacade;

	@Inject
	private RebanhoDAO rebanhoDAO;

	@Inject
	private AnimalDAO animalDAO;

	/**
	 * Retornos manejos do relatório de previsão de partos
	 * @param ids
	 * @return
	 */
	@GET
	@Path("previsaoPartos")	
	public List<ManejoReprodutivo> previsaoDePartos(@QueryParam(value = "ids") List<Long> ids){
		List<Rebanho> rebanhos = new ArrayList<Rebanho>();
		List<ManejoReprodutivo> manejos = new ArrayList<ManejoReprodutivo>();
		rebanhos = rebanhoDAO.listaRebanhosIds(ids);
		manejos = relReproducaoFacade.relPrevPartos(rebanhos);
		return manejos;
	}

	/**
	 * Retornos manejos do relatório de corbertua por reprodutor
	 * @param ids
	 * @return
	 */
	@GET
	@Path("coberturaReprodutor")	
	public List<ResumoAnimalReprodutivo> coberturaReprodutor(@QueryParam(value = "ids") List<Long> ids){
		List<Rebanho> rebanhos = new ArrayList<Rebanho>();
		List<ResumoAnimalReprodutivo> animais = new ArrayList<ResumoAnimalReprodutivo>();
		rebanhos = rebanhoDAO.listaRebanhosIds(ids);
		animais = relReproducaoFacade.relCoberturaReprodutor(rebanhos, null, null);
		return animais;
	}

	@GET
	@Path("femeasIdadeReprodutiva")
	public List<ResumoAnimalReprodutivo> femeasIdadeReprodutiva(@QueryParam(value = "ids") List<Long> ids){
		List<ResumoAnimalReprodutivo> animais = new ArrayList<ResumoAnimalReprodutivo>();
		animais = relReproducaoFacade.relFemeasIdadeReprodutiva(ids,(Integer) 0);
		for (ResumoAnimalReprodutivo animal : animais) {
			animal.getAnimal().setListaNotas(animalDAO.relatorioVermifugaAnimal(animal.getAnimal().getId()));
		}
		return animais;
	}

	@GET
	@Path("partos")
	public List<ResumoPartos> partos(@QueryParam(value = "ids") List<Long> ids){
		List<ResumoPartos> animais = new ArrayList<ResumoPartos>();
		animais = relReproducaoFacade.relDePartos(ids, null, null);
		for (ResumoPartos animal : animais) {
			animal.getAnimal().setListaNotas(animalDAO.relatorioVermifugaAnimal(animal.getAnimal().getId()));
		}
		return animais;
	}
}
