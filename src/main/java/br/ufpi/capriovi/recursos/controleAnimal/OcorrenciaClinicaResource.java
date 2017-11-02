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
import br.ufpi.capriovi.dao.controleAnimal.OcorrenciaClinicaDAO;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.OcorrenciaClinicaFacade;

@Path("ocorrencia")
@Produces(MediaType.APPLICATION_JSON)
public class OcorrenciaClinicaResource{

	@Inject
	private OcorrenciaClinicaDAO ocorrenciaDAO;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private OcorrenciaClinicaFacade ocorrenciaClinicaFacade;

	@Inject
	private AnimalDAO animalDAO;

	/**
	 * Retorna o json com todos as ocorrencias de uma lista de rebanhos
	 * @param ids
	 * @return
	 */
	@GET
	@Path("ocorrenciasRebanhos")
	public List<OcorrenciaClinica> ocorrenciasRebanhos(@QueryParam(value = "ids") List<Long> ids){
		List<OcorrenciaClinica> ocorrencias = new ArrayList<OcorrenciaClinica>();
		if (ids.size() > 0) {
			ocorrencias = ocorrenciaClinicaFacade.listarPorRebanho(
					rebanhoFacade.rebanhosFazendasIds(ids));
			for (OcorrenciaClinica ocorrencia : ocorrencias) {
				ocorrencia.getAnimal().setListaNotas(
						animalDAO.relatorioVermifugaAnimal(ocorrencia.getAnimal().getId()));
			}
		}
		return ocorrencias;
	}	

	/**
	 * Adiciona uma ocorrência clinica no banco de dados
	 * @param ocorrencia
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaOcorrencia(OcorrenciaClinica ocorrencia){
		ocorrenciaDAO.adicionaOcorrenciaClinica(ocorrencia);
		String resposta = "Ocorrência Clinica adicionado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta uma ocorrência clinica do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaOcorrencia(@javax.ws.rs.PathParam(value = "id") Long id){
		ocorrenciaDAO.deletarOcorrenciaClinica(id);
		String resposta = "Ocorrência Clinica deletada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza uma ocorrência clinica do banco de dados
	 * @param ocorrencia
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)	
	public Response atualizaOcorrencia(OcorrenciaClinica ocorrencia){
		ocorrenciaDAO.update(ocorrencia);
		String resposta = "Ocorrência clinica atualizada com sucesso!";
		return Response.status(200).entity(resposta).build();
	}


}
