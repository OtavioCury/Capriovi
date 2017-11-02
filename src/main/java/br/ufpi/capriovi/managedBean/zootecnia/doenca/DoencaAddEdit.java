package br.ufpi.capriovi.managedBean.zootecnia.doenca;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.facade.cadastros.DoencaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@SessionScoped
@Named(value = "doencaAddEditMB")
public class DoencaAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8447617529743642290L;

	@Inject
	DoencaFacade doencaFacade;

	@Inject
	SystemSessionMB systemSessionMB;

	private Doenca doenca;

	private String title;

	public DoencaAddEdit() {
		super();
		this.doenca = new Doenca();
	}

	public Doenca getDoenca() {
		return doenca;
	}

	public void setDoenca(Doenca doenca) {
		this.doenca = doenca;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar uma doença
	 */
	public void add() {
		this.title = "Adicionar Doença";
		this.doenca = new Doenca();
	}

	/**
	 * Método chamado para atualizar
	 * @param u
	 */
	public void update(Doenca u) {
		this.title = "Atualizar Doença";
		this.doenca = u;
	}

	/**
	 * Deleta uma doença do BD
	 * @param id
	 */
	public void delete(Long id) {
		this.doencaFacade.deletarDoenca(id);
	}

	public void cancel() {
		this.doenca = new Doenca();
	}

	/**
	 * Método chamado para salvar uma doença no BD
	 */
	public void save() {
		if (this.doenca != null) {
			if (this.doenca.getId() == null) {
				// Add
				if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")  ||  
						getUsuario().getPermissao().contains("ROLE_ADMIN")	) {
					doenca.setUsuario(getUsuario());
				}else{
					doenca.setUsuario(systemSessionMB.getFazenda().getPecuarista());
				}				

				if(getUsuario().getPermissao().contains("ROLE_ADMIN")){
					doenca.setGeral(true);
				}
				this.doencaFacade.adicionaDoenca(doenca);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Doença adicionada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update
				this.doencaFacade.atualizaDoenca(doenca);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Doença atualizada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
	}

}
