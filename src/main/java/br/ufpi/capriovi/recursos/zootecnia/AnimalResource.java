package br.ufpi.capriovi.recursos.zootecnia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;

@Path("animal")
@Produces(MediaType.APPLICATION_JSON)
public class AnimalResource {

	@Inject
	private AnimalDAO animalDAO;

	@Inject
	private AnimalFacade animalFacade;

	@Inject
	private RebanhoFacade rebanhoFacade;

	/**
	 * Busca todos os animais de vários rebanhos
	 * @param ids
	 * @return
	 */
	@GET
	@Path("animaisFazendas")	
	public List<Animal> animaisFazendas(@QueryParam(value = "ids") List<Long> ids){
		List<Animal> animais = new ArrayList<Animal>();
		if (ids.size() > 0) {
			animais = animalFacade.animaisFazendasIds(ids);
			for (Animal animal : animais) {
				animal.setListaNotas(animalDAO.relatorioVermifugaAnimal(animal.getId()));
			}
		}
		return animais;
	}

	/**
	 * Busca todos os animais de um rebanho
	 * @param id
	 * @return
	 */
	@GET
	@Path("animaisRebanho")
	public Response animaisRebanho(@QueryParam(value = "id") Long id){
		List<Animal> animais = new ArrayList<Animal>();
		animais = animalDAO.listAll("rebanho", id);
		return Response
				.status(200)
				.entity(animais).build();
	}

	/**
	 * Adiciona um animal no banco de dados
	 * @param animal
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionarAnimal(Animal animal){
		if(animal.getRaca().getId() == null){
			animal.setRaca(null);
		}
		if (animal.getNascimento().after(new Date())) {
			return Response.status(206).entity(MensagensExceptions.DataNascimento).build();
		}
		if (!animalFacade.nomePresente(animal, rebanhoFacade.listRebanhosByFazenda(animal.getRebanho().getFazenda().getId()), true)) {
			animal.setDataEntrada(new Date());
			animalDAO.adicionaAnimal(animal);
			String resposta = "Animal adicionado com sucesso!";
			return Response.status(200).entity(resposta).build();
		}else{
			return Response.status(205).entity(MensagensExceptions.NomeAnimalExistenteException).build();
		}
	}

	/**
	 * Deleta um animal do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaAnimal(@javax.ws.rs.PathParam(value = "id") Long id){
		animalDAO.deletarAnimal(id);
		String resposta = "Animal deletado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza um rebanho no banco de dados
	 * @param animal
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaAnimal(Animal animal){
		animalDAO.update(animal);
		String resposta = "Animal atualizado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}
}
