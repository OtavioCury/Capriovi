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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.dao.controleAnimal.VacinacaoDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.controleAnimal.Vacinacao;

@Path("vacinacao")
@Produces(MediaType.APPLICATION_JSON)
public class VacinacaoResource {

	@Inject
	private VacinacaoDAO vacinacaoDAO;

	@Inject
	private AnimalDAO animalDAO;

	/**
	 * Retorna o json das vacinações de um rebanho
	 * @param id
	 * @return
	 */
	@GET 
	@Path("vacinacoesUsuario/{id}")
	public List<Vacinacao> vacinacoesUsuario(@javax.ws.rs.PathParam(value = "id") Long id){
		List<Vacinacao> vacinacoes = new ArrayList<Vacinacao>();
		vacinacoes = vacinacaoDAO.vacinacaoUsuario(id);
		for (Vacinacao vacinacao : vacinacoes) {
			for (Animal animal : vacinacao.getAnimais()) {
				animal.setListaNotas(animalDAO.relatorioVermifugaAnimal(animal.getId()));
			}
		}
		return vacinacoes;
	}

	/**
	 * Adiciona uma vacinação no banco de dados
	 * @param vacinacao
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaVacinacao(Vacinacao vacinacao){
		vacinacaoDAO.adicionaVacinacao(vacinacao);
		String resposta = "Vacinação adicionada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta uma vacinação do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaVacinacao(@javax.ws.rs.PathParam(value = "id") Long id){
		vacinacaoDAO.deletarVacinacao(id);
		String resposta = "Vacinação deletada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza uma vacinação do banco de dados
	 * @param vacinacao
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaVacinacao(Vacinacao vacinacao){
		vacinacaoDAO.update(vacinacao);
		String resposta = "Vacinação atualizada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}
}
