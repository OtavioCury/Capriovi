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
import br.ufpi.capriovi.dao.controleAnimal.ManejoReprodutivoDAO;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.ManejoReprodutivoFacade;

@Path("manejo")
@Produces(MediaType.APPLICATION_JSON)
public class ManejoReprodutivoResource {

	@Inject
	private ManejoReprodutivoDAO manejoReprodutivoDAO;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private ManejoReprodutivoFacade manejoReprodutivoFacade;

	@Inject
	private AnimalDAO animalDAO;

	/**
	 * Retorna o json com todos os manejos de uma lista de rebanhos
	 * @param ids
	 * @return
	 */
	@GET
	@Path("manejosRebanhos")	
	public List<ManejoReprodutivo> manejosRebanhos(@QueryParam(value = "ids") List<Long> ids){
		List<ManejoReprodutivo> manejoReprodutivos = new ArrayList<ManejoReprodutivo>();
		if (ids.size() > 0) {
			manejoReprodutivos = manejoReprodutivoFacade.listarPorRebanho(
					rebanhoFacade.rebanhosFazendasIds(ids));
			for (ManejoReprodutivo manejoReprodutivo : manejoReprodutivos) {
				manejoReprodutivo.getMatriz().setListaNotas(
						animalDAO.relatorioVermifugaAnimal(manejoReprodutivo.getMatriz().getId()));
				manejoReprodutivo.getReprodutor().setListaNotas(
						animalDAO.relatorioVermifugaAnimal(manejoReprodutivo.getReprodutor().getId()));

			}
		}		
		return manejoReprodutivos;
	}	

	/**
	 * Adiciona um manejo reprodutivo no banco de dados
	 * @param manejo
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaManejo(ManejoReprodutivo manejo){
		manejoReprodutivoDAO.adicionaManejoReprodutivo(manejo);
		String resposta = "Manejo reprodutivo adicionado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta uma manejo reprodutivo do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaManejo(@javax.ws.rs.PathParam(value = "id") Long id){
		manejoReprodutivoDAO.deletarManejoReprodutivo(id);;
		String resposta = "Manejo deletado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza um manejo reprodutivo do banco de dados
	 * @param manejo
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaMenejo(ManejoReprodutivo manejo){
		manejoReprodutivoDAO.update(manejo);
		String resposta = "Manejo Reprodutivo atualizado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

}
