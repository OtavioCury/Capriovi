package br.ufpi.capriovi.managedBean.zootecnia.medicamento;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.facade.cadastros.MedicamentoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@Named(value = "medicamentoMB")
@ViewScoped
public class MedicamentoMB extends BaseBeans{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5765019214422157685L;

	@Inject
	private MedicamentoFacade medicamentoFacade;

	private List<Medicamento> medicamentos;

	@Inject
	private SystemSessionMB systemSessionMB;
	
	/**
	 * carrega os medicamentos na tabela
	 */
	@PostConstruct
	public void init(){
		medicamentos = new ArrayList<Medicamento>();
		if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {
			medicamentos = medicamentoFacade.medicamentosUsuario(getUsuario());
		}else {
			medicamentos = medicamentoFacade.medicamentosUsuario(systemSessionMB.getFazenda().getPecuarista());
		}
	}

	/**
	 * Medicamentos de um usuário
	 * @return
	 */
	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	/**
	 * Deleta medicamento da lista e do banco de dados
	 * @param principio
	 */
	public void deletaMedicamento(Medicamento medicamento){
		medicamentos.remove(medicamento);
		medicamentoFacade.deletarMedicamento(medicamento.getId());
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

}
