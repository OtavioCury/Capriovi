package br.ufpi.capriovi.facade.controleAnimal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.controleAnimal.ManejoReprodutivoDAO;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;

@Stateless
public class ManejoReprodutivoFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8564886888745842867L;

	@Inject 
	private ManejoReprodutivoDAO manejoReprodutivoDAO;

	/**
	 * Lita manejo reprodutivo por rebanhos de um usuário
	 * @param rebanhos
	 * @return
	 */
	public List<ManejoReprodutivo> listarPorRebanho(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();
		List<ManejoReprodutivo> manejoReprodutivos = new ArrayList<ManejoReprodutivo>();

		if (!rebanhos.isEmpty()) {
			for (Rebanho r : rebanhos) {
				ids.add(r.getId());
			}

			manejoReprodutivos = manejoReprodutivoDAO.manejosRebs(ids);
		}

		return manejoReprodutivos;


	}	

	/**
	 * 
	 * @param id
	 */
	public void deletarManejoReprodutivo(Long id){
		manejoReprodutivoDAO.deletarManejoReprodutivo(id);
	}
	/**
	 * 
	 * @param ManejoReprodutivo
	 */
	public void atualizaManejoReprodutivo(ManejoReprodutivo manejoReprodutivo){
		manejoReprodutivoDAO.update(manejoReprodutivo);
	}
	/**
	 * 
	 * @param ManejoReprodutivo
	 */
	public void adicionaManejoReprodutivo(ManejoReprodutivo manejoReprodutivo){
		manejoReprodutivoDAO.adicionaManejoReprodutivo(manejoReprodutivo);
	}

}