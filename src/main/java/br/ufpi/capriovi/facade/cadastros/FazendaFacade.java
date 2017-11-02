package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.cadastros.FazendaDAO;
import br.ufpi.capriovi.entidades.cadastros.Fazenda;


@Stateless
public class FazendaFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6343841046976988349L;
	
	@Inject 
	private FazendaDAO fazendaDAO;				
	
	public List<Fazenda> listAllId(Long id) {
		List<Fazenda> result = fazendaDAO.listAll("pecuarista", id);
		return result;
	}
	/**
	 * 
	 * @param id
	 */
	public void deletarFazenda(Long id){
		fazendaDAO.deletarFazenda(id);
	}
	/**
	 * 
	 * @param Fazenda
	 */
	public void atualizaFazenda(Fazenda Fazenda){
		fazendaDAO.update(Fazenda);
	}
	/**
	 * 
	 * @param Fazenda
	 */
	public void adicionaFazenda(Fazenda Fazenda){
		fazendaDAO.adicionaFazenda(Fazenda);
	}
	
	public Fazenda find(Long id){
		return fazendaDAO.findFazenda(id);
	}
		
}
