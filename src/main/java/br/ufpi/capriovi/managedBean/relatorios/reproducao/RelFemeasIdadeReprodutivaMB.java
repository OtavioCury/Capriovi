package br.ufpi.capriovi.managedBean.relatorios.reproducao;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import br.ufpi.capriovi.suporte.relatorios.ResumoAnimalReprodutivo;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.reproducao.RelReproducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relFemeasIdadeReprodutivaMB")
@ViewScoped
public class RelFemeasIdadeReprodutivaMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -229716363721751207L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private RelReproducaoFacade relReproducaoFacade;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhosMarcados;

	private List<ResumoAnimalReprodutivo> resumoAnimalReprodutivo;

	private int idadeEmMeses;

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
		this.resumoAnimalReprodutivo = relReproducaoFacade.relFemeasIdadeReprodutiva(idsRebanhos(rebanhosMarcados), idadeEmMeses);
	}

	/**
	 * Retorna uma lista de ids de uma lista de rebanhos
	 * @param rebanhos
	 * @return
	 */
	public List<Long> idsRebanhos(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();

		for (Rebanho rebanho : rebanhos) {
			ids.add(rebanho.getId());
		}

		return ids;
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

	public int getIdadeEmMeses() {
		return idadeEmMeses;
	}

	public void setIdadeEmMeses(int idadeEmMeses) {
		this.idadeEmMeses = idadeEmMeses;
	}

	public List<ResumoAnimalReprodutivo> getResumoAnimalReprodutivo() {
		return resumoAnimalReprodutivo;
	}

	public void setResumoAnimalReprodutivo(List<ResumoAnimalReprodutivo> resumoAnimalReprodutivo) {
		this.resumoAnimalReprodutivo = resumoAnimalReprodutivo;
	}

}
