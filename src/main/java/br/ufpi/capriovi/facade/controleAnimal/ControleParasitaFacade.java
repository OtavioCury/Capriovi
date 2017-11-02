package br.ufpi.capriovi.facade.controleAnimal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.controleAnimal.ControleParasitaDAO;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.ControleParasita;

@Stateless
public class ControleParasitaFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1628615775381539407L;
	@Inject
	private ControleParasitaDAO controleParazitaDAO; 	
	
	/**
	 * Lista controles parasitários de uma lista de rebanhos
	 * @param rebanhos
	 * @return
	 */
	public List<ControleParasita> listarPorRebanho(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();
		List<ControleParasita> controles = new ArrayList<ControleParasita>();

		if (!rebanhos.isEmpty()) {
			for (Rebanho r : rebanhos) {
				ids.add(r.getId());
			}

			controles = controleParazitaDAO.controlesRebanho(ids);
		}

		return controles;


	}
	
	/**
	 * 
	 * @param id
	 */
	public void deletarControleParazita(Long id){
		controleParazitaDAO.deletarControleParazita(id);
	}
	/**
	 * 
	 * @param ControleParasita
	 */
	public void atualizaControleParazita(ControleParasita controleParazita){
		controleParazitaDAO.update(controleParazita);
	}
	/**
	 * 
	 * @param ControleParasita
	 */
	public void adicionaControleParazita(ControleParasita controleParazita){
		controleParazitaDAO.adicionaControleParazita(controleParazita);
	}

}
