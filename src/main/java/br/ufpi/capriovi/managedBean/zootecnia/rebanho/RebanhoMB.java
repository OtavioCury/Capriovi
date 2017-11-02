package br.ufpi.capriovi.managedBean.zootecnia.rebanho;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Funcionario;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@Named(value = "rebanhoMB")
@ViewScoped
public class RebanhoMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5264933989579019373L;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhosUsuario;

	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {		
		rebanhosUsuario = new ArrayList<Rebanho>();
		if (systemSessionMB.getFazenda() != null) {				
			if (systemSessionMB.getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {			
				this.rebanhosUsuario = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
			}else {
				Funcionario f;
				f = (Funcionario) systemSessionMB.getUsuario();
				this.rebanhosUsuario = rebanhoFacade.listRebanhosByFazenda(f.getFazenda().getId());
			}
		}
	}

	/**
	 * retorna todos os rebanhos de todas as fazendas
	 * @return
	 */
	public List<Rebanho> rebanhosFazendas(){
		rebanhosUsuario = new ArrayList<Rebanho>();	
		if (!systemSessionMB.getListFazendas().isEmpty()) {
			rebanhosUsuario = rebanhoFacade.rebanhosFazendas(systemSessionMB.getListFazendas());
		}		
		return rebanhosUsuario;
	}

	/**
	 * Retorna os rebanhos de uma fazenda
	 * @param fazenda
	 * @return
	 */
	public List<Rebanho> rebanhosFazenda(Fazenda fazenda){
		rebanhos = new ArrayList<Rebanho>();
		rebanhos = rebanhoFacade.listRebanhosByFazenda(fazenda.getId());
		return rebanhos;
	}

	public List<Rebanho> getRebanhosUsuario() {
		return rebanhosUsuario;
	}

	@Transactional
	public void deletaRebanho(Rebanho rebanho){		
		rebanhosUsuario.remove(rebanho);
		rebanhoFacade.deletarRebanho(rebanho.getId());
	}

	public void setRebanhosUsuario(List<Rebanho> rebanhosUsuario) {
		this.rebanhosUsuario = rebanhosUsuario;
	}

	public List<Rebanho> getRebanhos() {
		return rebanhos;
	}

	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}	

}
