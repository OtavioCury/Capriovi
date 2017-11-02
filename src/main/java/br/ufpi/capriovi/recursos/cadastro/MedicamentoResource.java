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

import br.ufpi.capriovi.dao.cadastros.MedicamentoDAO;
import br.ufpi.capriovi.entidades.cadastros.Medicamento;

@Path("medicamento")
@Produces("application/json")
public class MedicamentoResource {

	@Inject
	private MedicamentoDAO medicamentoDAO;

	/**
	 * Busca os medicamentos de um usuário
	 * @param id
	 * @return
	 */
	@GET 
	@Path("medicamentosUsuario/{id}")
	public List<Medicamento> medicamentosUsuario(@javax.ws.rs.PathParam(value = "id") Long id){
		List<Medicamento> medicamentos = new ArrayList<Medicamento>();
		medicamentos = medicamentoDAO.medicamentosUsuario(id);
		return medicamentos;
	}

	/**
	 * Adiciona um medicamento no banco de dados
	 * @param medicamento
	 * @return
	 */
	@POST
	@Path("adicionar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response adicionaMedicamento(Medicamento medicamento){
		medicamentoDAO.adicionaMedicamento(medicamento);
		String resposta = "Medicamento adicionado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Deleta um medicamento do banco de dados
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("deletar/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deletaMedicamento(@javax.ws.rs.PathParam(value = "id") Long id){
		medicamentoDAO.deletarMedicamento(id);
		String resposta = "Medicamento deletado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}

	/**
	 * Atualiza um medicamento do banco de dados
	 * @param medicamento
	 * @return
	 */
	@POST
	@Path("atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response atualizaDoenca(Medicamento medicamento){
		medicamentoDAO.update(medicamento);
		String resposta = "Medicamento atualizado com sucesso!";
		return Response.status(200).entity(resposta).build();
	}
}
