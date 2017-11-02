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
import br.ufpi.capriovi.dao.controleAnimal.MovimentacaoAnimalDAO;
import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.MovimentacaoAnimalFacade;

@Path("movimentacao")
@Produces(MediaType.APPLICATION_JSON)
public class MovimentacaoAnimalResource {

	@Inject
	private MovimentacaoAnimalDAO movimentacaoAnimal;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private MovimentacaoAnimalFacade movimentacaoFacade;

	@Inject
	private AnimalDAO animalDAO;

	/**
	 * Retorna o json dos movimentos de vários rebanhos
	 * @param ids
	 * @return
	 */
	@GET
	@Path("movimentosRebanhos")
	public List<MovimentacaoAnimal> movimentosRebanhos(@QueryParam(value = "ids") List<Long> ids){
		List<MovimentacaoAnimal> movimentos = new ArrayList<MovimentacaoAnimal>();
		if (ids.size() > 0) {
			movimentos = movimentacaoFacade.listarPorRebanho(
					rebanhoFacade.rebanhosFazendasIds(ids));
			for (MovimentacaoAnimal movimento : movimentos) {
				movimento.getAnimal().setListaNotas(
						animalDAO.relatorioVermifugaAnimal(movimento.getAnimal().getId()));
			}
		}
		return movimentos;
	}

	/**
	 * Adiciona uma movimentação no banco de dados
	 * @param movimentacao
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaMovimentacao(MovimentacaoAnimal movimentacao){
		movimentacaoAnimal.adicionaMovimentacaoAnimal(movimentacao);
		String resposta = "Movimentação animal adicionada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta uma movimentação animal do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaMovimentacao(@javax.ws.rs.PathParam(value = "id") Long id){
		movimentacaoAnimal.deletarMovimentacaoAnimal(id);
		String resposta = "Movimentação animal deletada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza uma movimentação animal do banco de dados
	 * @param movimentacao
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaMovimentacao(MovimentacaoAnimal movimentacao){
		movimentacaoAnimal.update(movimentacao);
		String resposta = "Movimentação animal atualizada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}
}
