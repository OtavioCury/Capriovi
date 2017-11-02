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

import br.ufpi.capriovi.dao.cadastros.RebanhoDAO;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;

@Path("rebanho")
@Produces(MediaType.APPLICATION_JSON)
public class RebanhoResource {

	@Inject
	private RebanhoDAO rebanhoDAO;

	/**
	 * Busca os rebanhos de várias fazendas
	 * @param ids
	 * @return
	 */
	@GET
	@Path("rebanhosFazendas")	
	public List<Rebanho> rebanhosFazenda(@QueryParam("ids") List<Long> ids){	
		List<Rebanho> rebanhos = new ArrayList<Rebanho>();
		if (ids.size() > 0) {
			rebanhos = rebanhoDAO.rebanhosFazendas(ids);
		}		
		return rebanhos;
	}

	/**
	 * Adiciona uma rebanho no banco de dados
	 * @param rebanho
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionarRebanho(Rebanho rebanho){
		rebanho.setRegistro(new Date());
		rebanhoDAO.adicionaRebanho(rebanho);
		String resposta = "Rebanho adicionado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta um rebanho do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaRebanho(@javax.ws.rs.PathParam(value = "id") Long id){
		rebanhoDAO.deletarRebanho(id);
		String resposta = "Rebanho deletado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza um rebanho no banco de dados
	 * @param rebanho
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaRebanho(Rebanho rebanho){
		rebanhoDAO.update(rebanho);
		String resposta = "Rebanho atualizado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

}
