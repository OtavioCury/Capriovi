package br.ufpi.capriovi.managedBean.controleAnimal.vacinacao;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.controleAnimal.Vacinacao;
import br.ufpi.capriovi.facade.controleAnimal.VacinacaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
/**
 * Comunicação direta com a view.
 * @author thasciano
 *
 */
@Named(value = "vacinacaoMB")
@ViewScoped
public class VacinacaoMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7283491248663137465L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private VacinacaoFacade vacinacaoFacade;

	private List<Vacinacao> vacinacao;

	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {		
		vacinacao = new ArrayList<Vacinacao>();
		if (systemSessionMB.getFazenda() != null) {
			if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {
				this.vacinacao = vacinacaoFacade.listaPorUsuario(getUsuario().getId());
			}else{
				this.vacinacao = vacinacaoFacade.listaPorUsuario(systemSessionMB.getFazenda().getPecuarista().getId());
			}
		}			
	}

	/**
	 * 
	 * Deleta vacinação da lista e do banco de dados
	 * @param vacinacao
	 */
	public void deletaVacinacao(Vacinacao vacinacao){
		this.vacinacao.remove(vacinacao);
		vacinacaoFacade.deletarVacinacao(vacinacao.getId());
	}

	public List<Vacinacao> getVacinacao() {
		return vacinacao;
	}

	public void setVacinacao(List<Vacinacao> vacinacao) {
		this.vacinacao = vacinacao;
	}

}
