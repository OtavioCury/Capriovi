package br.ufpi.capriovi.managedBean.zootecnia.medicamento;



import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.facade.cadastros.MedicamentoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@SessionScoped
@Named(value = "medicamentoAddEditMB")
public class MedicamentoAddEdit extends BaseBeans{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7654673374433893403L;

	@Inject
	MedicamentoFacade medicamentoFacade;

	@Inject
	SystemSessionMB systemSessionMB;

	private Medicamento medicamento;

	private String title;

	public MedicamentoAddEdit() {
		super();
		this.medicamento = new Medicamento();
	}

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar um medicamento
	 */
	public void add() {
		this.title = "Adicionar Medicamento";
		this.medicamento = new Medicamento();
	}

	/**
	 * Método chamado para atualizar um medicamento
	 * @param u
	 */
	public void update(Medicamento u) {
		this.title = "Atualizar Medicamento";
		this.medicamento = u;
	}

	public void delete(Long id) {
		this.medicamentoFacade.deletarMedicamento(id);
	}

	public void cancel() {
		this.medicamento = new Medicamento();
	}

	/**
	 * Método chamado para salvar um medicamento
	 */
	public void save() {
		if (this.medicamento != null) {
			if (this.medicamento.getId() == null) {
				// Add
				if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")  ||  
						getUsuario().getPermissao().contains("ROLE_ADMIN")	) {
					medicamento.setUsuario(getUsuario());
				}else{
					medicamento.setUsuario(systemSessionMB.getFazenda().getPecuarista());
				}		
				this.medicamentoFacade.adicionaMedicamento(medicamento);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicamento adicionado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update
				this.medicamentoFacade.atualizaMedicamento(medicamento);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicamento atualizado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
	}

}
