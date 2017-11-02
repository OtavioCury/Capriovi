package br.ufpi.capriovi.facade.controleAnimal;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.controleAnimal.VacinacaoDAO;
import br.ufpi.capriovi.entidades.controleAnimal.Vacinacao;

@Stateless
public class VacinacaoFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1587624527692240295L;
	@Inject
	private VacinacaoDAO vacinacaoDAO; 	

	/**
	 * 
	 * Lista vacinações por usuário
	 * @param id
	 * @return
	 */
	public List<Vacinacao> listaPorUsuario(Long id){
		return vacinacaoDAO.vacinacaoUsuario(id);
	}	

	/**
	 * 
	 * @param id
	 */
	public void deletarVacinacao(Long id){
		vacinacaoDAO.deletarVacinacao(id);
	}
	/**
	 * 
	 * @param Vacinacao
	 */
	public void atualizaVacinacao(Vacinacao vacinacao){
		vacinacaoDAO.update(vacinacao);
	}
	/**
	 * 
	 * @param Vacinacao
	 */
	public void adicionaVacinacao(Vacinacao vacinacao){
		vacinacaoDAO.adicionaVacinacao(vacinacao);
	}

}
