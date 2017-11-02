package br.ufpi.capriovi.dao.cadastros;
import javax.ejb.Stateless;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Administrador;


@Stateless
public class AdministradorDAO extends GenericDAO<Administrador, Administrador>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8719639645587449872L;

	public AdministradorDAO() {
		super(Administrador.class);
	}

	/**
	 * Deleta o administrador.
	 * @param id
	 */
	public void deletarAdministrador(Long id){
		super.delete(id, Administrador.class);
	}

	/**
	 * Salva o administrador.
	 * @param Administrador
	 */
	public void adicionaAdministrador(Administrador administrador){
		super.save(administrador);
	}

}
