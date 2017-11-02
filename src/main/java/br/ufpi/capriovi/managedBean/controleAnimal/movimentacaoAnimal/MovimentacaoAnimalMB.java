package br.ufpi.capriovi.managedBean.controleAnimal.movimentacaoAnimal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.MovimentacaoAnimalFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;

@Named(value = "movimentacaoAnimalMB")
@ViewScoped
public class MovimentacaoAnimalMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2677904191415852676L;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private MovimentacaoAnimalFacade movimentacaoAnimalFacade;

	@Inject
	RebanhoMB rebanhoMB;

	@Inject
	private SystemSessionMB systemSessionMB;

	private List<MovimentacaoAnimal> movimentacaoAnimal;


	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {		
		movimentacaoAnimal = new ArrayList<MovimentacaoAnimal>();
		if (systemSessionMB.getFazenda() != null) {				
			movimentacaoAnimal = movimentacaoAnimalFacade.listarPorRebanho(rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()));
		}
	}
	
	/**
	 * retorna todos as movimentações de animais de todas as fazendas do usuário
	 * @return
	 */
	public List<MovimentacaoAnimal> movimentacoesFazendas(){
		movimentacaoAnimal = new ArrayList<MovimentacaoAnimal>();	
		if (!systemSessionMB.getListFazendas().isEmpty()) {
			this.movimentacaoAnimal = movimentacaoAnimalFacade.listarPorRebanho(rebanhoFacade.rebanhosFazendas(systemSessionMB.getListFazendas()));
		}		
		return movimentacaoAnimal;
	}

	/**
	 * Deleta movimentação animal da lista e do banco de dados
	 * @param movimentacaoAnimal
	 */
	public void deletaMovimentacao(MovimentacaoAnimal movimentacaoAnimal){
		this.movimentacaoAnimal.remove(movimentacaoAnimal);
		movimentacaoAnimalFacade.deletarMovimentacaoAnimal(movimentacaoAnimal.getId());
	}


	public List<MovimentacaoAnimal> getMovimentacaoAnimal() {
		return movimentacaoAnimal;
	}


	public void setMovimentacaoAnimal(List<MovimentacaoAnimal> movimentacaoAnimal) {
		this.movimentacaoAnimal = movimentacaoAnimal;
	}
}
