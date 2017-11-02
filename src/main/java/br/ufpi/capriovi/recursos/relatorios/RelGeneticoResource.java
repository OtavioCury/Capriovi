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
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.relatorios.genetico.RelGeneticoFacade;
import br.ufpi.capriovi.suporte.relatorios.ResumoGanhoGeneticoEsp;

@Path("relGenetico")
@Produces(MediaType.APPLICATION_JSON)
public class RelGeneticoResource {

	@Inject
	private RelGeneticoFacade geneticoFacade;

	@Inject
	private RebanhoDAO rebanhoDAO;

	@Inject
	private AnimalDAO animalDAO;

	private ResumoGanhoGeneticoEsp resumoGanhoGeneticoEsp;

	@GET
	@Path("tamanhoEfetivo")	
	public List<Rebanho> tamanhoEfetivo(@QueryParam(value = "ids") List<Long> ids){		
		List<Rebanho> rebanhos = new ArrayList<Rebanho>();
		rebanhos = rebanhoDAO.findList(ids);
		geneticoFacade.relTamPopulacao(rebanhos);
		return rebanhos;
	}

	@GET
	@Path("intervaloGeracao")	
	public List<Animal> intervaloGeracao(@QueryParam(value = "id") Long id){		
		Rebanho rebanho = new Rebanho();
		rebanho = rebanhoDAO.find(id);
		List<Animal> animais = new ArrayList<Animal>();
		animais = geneticoFacade.relIntervaloGeracoes(rebanho);
		for (Animal animal : animais) {
			animal.setListaNotas(animalDAO.relatorioVermifugaAnimal(animal.getId()));
		}
		return animais;
	}

	@GET
	@Path("ganhoGeneticoEsperado")	
	public ResumoGanhoGeneticoEsp ganhoGeneticoEsperado(@QueryParam(value = "id") Long id, @QueryParam(value = "propMachos") double propMachos,
			@QueryParam(value = "propFemeas") double propFemeas, @QueryParam(value = "herdabilidade") Double herdabilidade, 
			@QueryParam(value = "ajuste") int ajuste, @QueryParam(value = "intensidadeFemea") double intensidadeFemea, 
			@QueryParam(value = "intensidadeMacho") double intensidadeMacho){
		Rebanho rebanho = new Rebanho();
		rebanho = rebanhoDAO.find(id);
		resumoGanhoGeneticoEsp = new ResumoGanhoGeneticoEsp();
		resumoGanhoGeneticoEsp.setHerdabilidade(herdabilidade);
		resumoGanhoGeneticoEsp.setPesoEscolhido(ajuste);
		resumoGanhoGeneticoEsp.setPropMachos(propMachos);
		resumoGanhoGeneticoEsp.setPropFemeas(propFemeas);
		resumoGanhoGeneticoEsp.setIntensidadeFemea(intensidadeFemea);
		resumoGanhoGeneticoEsp.setIntensidadeMacho(intensidadeMacho);
		resumoGanhoGeneticoEsp.setRebanho(rebanho.getNome());
		this.resumoGanhoGeneticoEsp = geneticoFacade.relGanhoGeneticoEsperado(id, 
				resumoGanhoGeneticoEsp);
		return resumoGanhoGeneticoEsp;
	}

}
