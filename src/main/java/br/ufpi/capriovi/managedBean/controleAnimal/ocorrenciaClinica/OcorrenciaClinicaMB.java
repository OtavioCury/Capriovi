package br.ufpi.capriovi.managedBean.controleAnimal.ocorrenciaClinica;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.OcorrenciaClinicaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;

@Named(value = "ocorrenciaClinicaMB")
@ViewScoped
public class OcorrenciaClinicaMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3305918654964587944L;

	@Inject
	private OcorrenciaClinicaFacade ocorrenciaClinicaFacade;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	RebanhoMB rebanhoMB;

	private List<OcorrenciaClinica> ocorrencias;


	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {		
		ocorrencias = new ArrayList<OcorrenciaClinica>();
		if (systemSessionMB.getFazenda() != null) {
			this.ocorrencias = ocorrenciaClinicaFacade.listarPorRebanho(rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()));
		}		
	}

	/**
	 * retorna todos as ocorrências clinicas de todas as fazendas do usuário
	 * @return
	 */
	public List<OcorrenciaClinica> ocorrenciasFazendas(){
		ocorrencias = new ArrayList<OcorrenciaClinica>();	
		if (!systemSessionMB.getListFazendas().isEmpty()) {
			this.ocorrencias = ocorrenciaClinicaFacade.listarPorRebanho(rebanhoFacade.rebanhosFazendas(systemSessionMB.getListFazendas()));
		}		
		return ocorrencias;
	}

	/**
	 * Deleta a ocorrencia da lista e do banco de dados
	 * @param ocorrenciaClinica
	 */
	public void deletaOcorrencia(OcorrenciaClinica ocorrenciaClinica){
		ocorrencias.remove(ocorrenciaClinica);
		ocorrenciaClinicaFacade.deletarOcorrenciaClinica(ocorrenciaClinica.getId());
	}

	public List<OcorrenciaClinica> getOcorrencias() {
		return ocorrencias;
	}


	public void setOcorrencias(List<OcorrenciaClinica> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}


}
