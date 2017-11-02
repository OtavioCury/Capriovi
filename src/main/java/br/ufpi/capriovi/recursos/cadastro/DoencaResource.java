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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.ufpi.capriovi.dao.cadastros.DoencaDAO;
import br.ufpi.capriovi.entidades.cadastros.Doenca;

@Path("doenca")
@Produces(MediaType.APPLICATION_JSON)
public class DoencaResource {

	@Inject
	private DoencaDAO doencaDAO;

	/**
	 * Busca as doenças de um usuário
	 * @param id
	 * @return
	 */
	@GET 
	@Path("doencasUsuario/{id}")
	public List<Doenca> doencaUsuario(@javax.ws.rs.PathParam(value = "id") Long id){
		List<Doenca> doencas = new ArrayList<Doenca>();
		doencas = doencaDAO.doencasUsuario(id);
		return doencas;
	}

	/**
	 * Adiciona uma doença no banco de dados
	 * @param doenca
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaDoenca(Doenca doenca){
		doencaDAO.adicionaDoenca(doenca);
		String resposta = "Doença adicionada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta uma doença do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaDoenca(@javax.ws.rs.PathParam(value = "id") Long id){
		doencaDAO.deletarDoenca(id);
		String resposta = "Doença deletada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza uma doença do banco de dados
	 * @param doenca
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaDoenca(Doenca doenca){
		doencaDAO.update(doenca);
		String resposta = "Doença atualizada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

}
