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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.ufpi.capriovi.dao.cadastros.FazendaDAO;
import br.ufpi.capriovi.entidades.cadastros.Fazenda;

@Path("fazenda")
@Produces(MediaType.APPLICATION_JSON)
public class FazendaResource {

	@Inject
	private FazendaDAO fazendaDAO;

	/**
	 * Método que busca fazendas do pecuarista
	 * @param login
	 * @param senha
	 * @return usuario
	 */
	@GET
	@Path("fazendas/{id}")
	public List<Fazenda> login(@javax.ws.rs.PathParam(value = "id") Long id){		
		List<Fazenda> fazendas = new ArrayList<Fazenda>();		
		fazendas = fazendaDAO.listAll("pecuarista", id);		
		return fazendas;
	}

	/**
	 * Adiciona uma fazenda no banco de dados
	 * @param rebanho
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionarFazenda(Fazenda fazenda){
		fazenda.setRegistro(new Date());
		fazendaDAO.adicionaFazenda(fazenda);
		String resposta = "Fazenda adicionada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta uma fazenda do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaFazenda(@javax.ws.rs.PathParam(value = "id") Long id){
		fazendaDAO.deletarFazenda(id);
		String resposta = "Fazenda deletada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza uma fazenda no banco de dados
	 * @param rebanho
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaFazenda(Fazenda fazenda){
		fazendaDAO.update(fazenda);
		String resposta = "Fazenda atualizada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}
}

