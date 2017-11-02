package br.ufpi.capriovi.recursos.controleAnimal;

import java.util.ArrayList;
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
import br.ufpi.capriovi.dao.controleAnimal.ControleParasitaDAO;
import br.ufpi.capriovi.entidades.controleAnimal.ControleParasita;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.ControleParasitaFacade;

@Path("controleParasita")
@Produces(MediaType.APPLICATION_JSON)
public class ControleParasitaResource {

	@Inject
	private ControleParasitaDAO controleParasitaDAO;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private ControleParasitaFacade controleParasitaFacade;

	@Inject
	private AnimalDAO animalDAO;

	/**
	 * Retorna o json com todos os controles de uma lista de rebanhos
	 * @param ids
	 * @return
	 */
	@GET
	@Path("controlesRebanhos")
	public List<ControleParasita> controleRebanhos(@QueryParam(value = "ids") List<Long> ids){
		List<ControleParasita> controlesParasita = new ArrayList<ControleParasita>();
		if (ids.size() > 0) {
			controlesParasita = controleParasitaFacade.listarPorRebanho(
					rebanhoFacade.rebanhosFazendasIds(ids));
			for (ControleParasita controleParasita : controlesParasita) {
				controleParasita.getAnimal().setListaNotas(
						animalDAO.relatorioVermifugaAnimal(controleParasita.getAnimal().getId()));
			}
		}
		return controlesParasita;
	}	

	/**
	 * Adiciona um controle parasita no banco de dados
	 * @param controle
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaControle(ControleParasita controle){
		controleParasitaDAO.adicionaControleParazita(controle);
		String resposta = "Controle Parasita adicionado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta um controle parasita do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaControle(@javax.ws.rs.PathParam(value = "id") Long id){
		controleParasitaDAO.deletarControleParazita(id);
		String resposta = "Controle parasita deletado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza um controle parasita do banco de dados
	 * @param manejo
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaControle(ControleParasita controle){
		controleParasitaDAO.update(controle);
		String resposta = "Controle parasita atualizado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

}
