package br.ufpi.capriovi.facade.controleAnimal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.controleAnimal.MovimentacaoAnimalDAO;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;

@Stateless
public class MovimentacaoAnimalFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7577575113044091423L;

	@Inject 
	private MovimentacaoAnimalDAO movimentacaoAnimalDAO;	

	/**
	 * Lista movimentações animais por rebanhos
	 * @param rebanhos
	 * @return
	 */
	public List<MovimentacaoAnimal> listarPorRebanho(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();
		List<MovimentacaoAnimal> movimentacaoAnimal = new ArrayList<MovimentacaoAnimal>();

		if (!rebanhos.isEmpty()) {
			for (Rebanho r : rebanhos) {
				ids.add(r.getId());
			}

			movimentacaoAnimal = movimentacaoAnimalDAO.movimentosRebs(ids);
		}

		return movimentacaoAnimal;
	}		

	/**
	 * 
	 * @param id
	 */
	public void deletarMovimentacaoAnimal(Long id){
		movimentacaoAnimalDAO.deletarMovimentacaoAnimal(id);
	}
	/**
	 * 
	 * @param movimentacaoAnimal
	 */
	public void atualizaMovimentacaoAnimal(MovimentacaoAnimal movimentacaoAnimal){
		movimentacaoAnimalDAO.update(movimentacaoAnimal);
	}
	/**
	 * 
	 * @param movimentacaoAnimal
	 */
	public void adicionaMovimentacaoAnimal(MovimentacaoAnimal movimentacaoAnimal){
		movimentacaoAnimalDAO.adicionaMovimentacaoAnimal(movimentacaoAnimal);
	}

}