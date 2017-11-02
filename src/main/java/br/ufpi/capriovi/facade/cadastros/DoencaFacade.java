package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.cadastros.DoencaDAO;
import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.entidades.cadastros.Usuario;

@Stateless
public class DoencaFacade  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3738001603860641256L;

	@Inject
	private DoencaDAO doencaDAO; 
	
	

	/**
	 * Retorna doenças cadastradas por admins
	 */
	public List<Doenca> doencasAdmin(){
		List<Doenca> doencas = new ArrayList<Doenca>();
		doencas = doencaDAO.doencasAdmin();
		return doencas;
	}

	/**
	 * Retorna doencas cadastrados pelo usuário
	 * @param usuario
	 * @return
	 */
	public List<Doenca> doencasUsuario(Usuario usuario){
		List<Doenca> doencas = new ArrayList<Doenca>();
		doencas = doencaDAO.doencasUsuario(usuario.getId());
		return doencas;
	}

	/**
	 * 
	 * @param id
	 */
	public void deletarDoenca(Long id){
		doencaDAO.deletarDoenca(id);
	}
	/**
	 * 
	 * @param Doenca
	 */
	public void atualizaDoenca(Doenca doenca){
		doencaDAO.update(doenca);
	}
	/**
	 * 
	 * @param Doenca
	 */
	public void adicionaDoenca(Doenca doenca){
		doencaDAO.adicionaDoenca(doenca);
	}

}
