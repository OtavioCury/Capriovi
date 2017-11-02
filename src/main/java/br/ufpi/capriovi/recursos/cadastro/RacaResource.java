package br.ufpi.capriovi.recursos.cadastro;

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

import br.ufpi.capriovi.dao.cadastros.RacaDAO;
import br.ufpi.capriovi.entidades.cadastros.Raca;

@Path("raca")
@Produces("application/json")
public class RacaResource {

	@Inject
	private RacaDAO racaDAO;

	/**
	 * Busca as raças de um usuário
	 * @param id
	 * @return
	 */
	@GET 
	@Path("racasUsuario")
	public List<Raca> racasUsuario(@QueryParam("id") Long id){
		List<Raca> racas = new ArrayList<Raca>();
		racas = racaDAO.racasUsuario(id);
		return racas;
	}

	/**
	 * Adiciona uma raça no banco de dados
	 * @param raca
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaRaca(Raca raca){
		racaDAO.adicionaRaca(raca);
		String resposta = "Raça adicionada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta uma raça do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaRaca(@javax.ws.rs.PathParam(value = "id") Long id){
		racaDAO.deletarRaca(id);
		String resposta = "Raça deletada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza uma raça do banco de dados
	 * @param raça
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaRaca(Raca raca){
		racaDAO.update(raca);
		String resposta = "Raça atualizada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}
}
