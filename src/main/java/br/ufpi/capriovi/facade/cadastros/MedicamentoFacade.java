package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.cadastros.MedicamentoDAO;
import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.entidades.cadastros.Usuario;

@Stateless
public class MedicamentoFacade  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5232181473380838423L;
	@Inject
	private MedicamentoDAO medicamentoDAO; 

	/**
	 * Retorna todos os medicamentos de um usuário
	 * @return
	 */
	public List<Medicamento> medicamentosUsuario(Usuario usuario){
		List<Medicamento> medicamentos = new ArrayList<Medicamento>();
		medicamentos = medicamentoDAO.medicamentosUsuario(usuario.getId());
		return medicamentos;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void deletarMedicamento(Long id){
		medicamentoDAO.deletarMedicamento(id);
	}
	/**
	 * 
	 * @param Medicamento
	 */
	public void atualizaMedicamento(Medicamento medicamento){
		medicamentoDAO.update(medicamento);
	}
	/**
	 * 
	 * @param Medicamento
	 */
	public void adicionaMedicamento(Medicamento medicamento){
		medicamentoDAO.adicionaMedicamento(medicamento);
	}

}
