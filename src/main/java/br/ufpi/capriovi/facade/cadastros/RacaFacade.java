package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.cadastros.RacaDAO;
import br.ufpi.capriovi.entidades.cadastros.Raca;
import br.ufpi.capriovi.entidades.cadastros.Usuario;

@Stateless
public class RacaFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RacaDAO racaDAO; 

	/**
	 * Retorna raças cadastrados pelo usuário
	 * @param usuario
	 * @return
	 */
	public List<Raca> racasUsuario(Usuario usuario){
		List<Raca> racas = new ArrayList<Raca>();
		racas = racaDAO.racasUsuario(usuario.getId());
		return racas;
	}

	/**
	 * Retorna raças cadastradas por admins
	 */
	public List<Raca> racasAdmin(){
		List<Raca> racas = new ArrayList<Raca>();
		racas = racaDAO.racasAdmin();
		return racas;
	}		

	/**
	 * 
	 * @param id
	 */
	public void deletarRaca(Long id){
		racaDAO.deletarRaca(id);
	}

	/**
	 * 
	 * @param Raca
	 */
	public void atualizaRaca(Raca raca){
		racaDAO.update(raca);
	}

	/**
	 * 
	 * @param Raca
	 */

	public void adicionaRaca(Raca raca){
		racaDAO.adicionaRaca(raca);
	}

}
