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
import br.ufpi.capriovi.dao.controleAnimal.ControleParasitaDAO;
import br.ufpi.capriovi.dao.controleAnimal.MovimentacaoAnimalDAO;
import br.ufpi.capriovi.dao.controleAnimal.OcorrenciaClinicaDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.controleAnimal.ControleParasita;
import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;

@Path("relProducao")
@Produces(MediaType.APPLICATION_JSON)
public class RelProducaoResource {

	@Inject
	private AnimalDAO animalDAO;

	@Inject
	private MovimentacaoAnimalDAO movimentacaoDAO;

	@Inject
	private OcorrenciaClinicaDAO ocorrenciaDAO;

	@Inject
	private ControleParasitaDAO controleParasitaDAO;

	@Inject
	private RelProducaoFacade relProducaoFacade;



	/**
	 * Retornos animais do relatório Animais por entrada
	 * @param ids
	 * @return animais
	 */
	@GET
	@Path("animaisPorEntrada")	
	public List<Animal> animaisPorEntrada(@QueryParam(value = "ids") List<Long> ids){					
		List<Animal> animais = new ArrayList<Animal>();	
		animais = animalDAO.animaisPorEntrada(ids, null, null);
		for (Animal animal : animais) {
			animal.setListaNotas(animalDAO.relatorioVermifugaAnimal(animal.getId()));
		}
		return animais;
	}

	/**
	 * Retornos animais do relatório movimentação animal
	 * @param ids
	 * @return
	 */
	@GET
	@Path("movimentacaoAnimal")	
	public List<MovimentacaoAnimal> movimentacaoAnimal(@QueryParam(value = "ids") List<Long> ids){					
		List<MovimentacaoAnimal> movimentacoes = new ArrayList<MovimentacaoAnimal>();	
		movimentacoes = movimentacaoDAO.relMovimentacao(ids, 0, null, null);
		return movimentacoes;
	}

	/**
	 * Retornos animais do relatório número de crias
	 * @param ids
	 * @return
	 */
	@GET
	@Path("numeroCrias")	
	public List<Animal> numeroCrias(@QueryParam(value = "ids") List<Long> ids){					
		List<Animal> animais = new ArrayList<Animal>();	
		animais= animalDAO.relNumeroDeCrias(ids, null, null);
		return animais;
	}

	/**
	 * Retornos animais do relatório ocorrências clinicas
	 * @param ids
	 * @return
	 */
	@GET
	@Path("ocorrenciasClinicas")	
	public List<OcorrenciaClinica> ocorrenciasClinicas(@QueryParam(value = "ids") List<Long> ids){					
		List<OcorrenciaClinica> ocorrenciasClinicas = new ArrayList<OcorrenciaClinica>();	
		ocorrenciasClinicas = ocorrenciaDAO.relOcorrenciaClinica(ids, null, null, null);
		return ocorrenciasClinicas;
	}

	/**
	 * Retornos animais do relatório controle parasitário
	 * @param ids
	 * @return
	 */
	@GET
	@Path("controleParasitario")	
	public List<ControleParasita> controlesParasitarios(@QueryParam(value = "ids") List<Long> ids){					
		List<ControleParasita> controlesParasitarios = new ArrayList<ControleParasita>();	
		controlesParasitarios = controleParasitaDAO.relControleParasitario(ids, null, null);
		for (ControleParasita controleParasita : controlesParasitarios) {
			controleParasita.getAnimal().setListaNotas(
					animalDAO.relatorioVermifugaAnimal(controleParasita.getAnimal().getId()));
		}
		return controlesParasitarios;
	}

	/**
	 * Retornos animais do relatório vermifugação
	 * @param ids
	 * @return
	 */
	@GET
	@Path("vermifugacao")	
	public List<Animal> vermifugacao(@QueryParam(value = "ids") List<Long> ids){					
		List<Animal> animais = new ArrayList<Animal>();	
		animais = relProducaoFacade.relVermifuga(ids);
		return animais;
	}


}
