package br.ufpi.capriovi.managedBean.controleAnimal.controleParasita;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.controleAnimal.ControleParasita;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.ControleParasitaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
/**
 * Comunicação direta com a view.
 * @author thasciano
 *
 */
@Named(value = "controleParasitaMB")
@ViewScoped
public class ControleParasitaMB extends BaseBeans{
	/**
	 * 
	 */
	private static final long serialVersionUID = -567433850282995682L;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private ControleParasitaFacade controleParasitaFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	RebanhoMB rebanhoMB;

	private List<ControleParasita> controle;

	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {	
		controle = new ArrayList<ControleParasita>();
		if (systemSessionMB.getFazenda() != null) {
			controle = controleParasitaFacade.listarPorRebanho(rebanhoFacade.listRebanhosByFazenda(
					systemSessionMB.getFazenda().getId()));
		}		
	}

	/**
	 * retorna todos os controles parasitas de todas as fazendas do usuário
	 * @return
	 */
	public List<ControleParasita> controlesFazendas(){
		controle = new ArrayList<ControleParasita>();	
		if (!systemSessionMB.getListFazendas().isEmpty()) {
			this.controle = controleParasitaFacade.listarPorRebanho(rebanhoFacade.rebanhosFazendas(systemSessionMB.getListFazendas()));
		}		
		return controle;
	}

	/**
	 * Deleta controle da lista e do banco de dados
	 * @param controleParasita
	 */
	public void deletaControle(ControleParasita controleParasita){
		controle.remove(controleParasita);
		controleParasitaFacade.deletarControleParazita(controleParasita.getId());
	}

	public List<ControleParasita> getControle() {
		return controle;
	}

	public void setControle(List<ControleParasita> controle) {
		this.controle = controle;
	}

}
