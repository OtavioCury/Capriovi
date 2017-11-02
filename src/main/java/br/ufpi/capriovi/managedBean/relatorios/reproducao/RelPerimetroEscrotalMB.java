package br.ufpi.capriovi.managedBean.relatorios.reproducao;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.reproducao.RelReproducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relPerimetroEscrotalMB")
@ViewScoped
public class RelPerimetroEscrotalMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3683529664820150205L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private RelReproducaoFacade relReproducaoFacade;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhosMarcados;

	private List<ManejoReprodutivo> manejos;

	public String onFlowProcess(FlowEvent event) {
		if (this.validaRelatorio()) {
			this.result();
			return event.getNewStep();
		}
		return "escolheRebanho";
	}

	public boolean validaRelatorio() {
		try {
			CaprioviValidations.rebanhoSelecionadoVal(rebanhosMarcados);
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return false;
		}
		return true;
	}

	/**
	 * carrega os dados da tabela.
	 */
	public void result() {
		manejos = relReproducaoFacade.relPerimetroEscrotal(rebanhosMarcados);
	}

	public List<Rebanho> getRebanhos() {
		if (systemSessionMB.getFazenda() != null) {
			this.rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
		}
		return rebanhos;
	}

	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}

	public List<Rebanho> getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(List<Rebanho> rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public List<ManejoReprodutivo> getManejos() {
		return manejos;
	}

	public void setManejos(List<ManejoReprodutivo> manejos) {
		this.manejos = manejos;
	}
}
