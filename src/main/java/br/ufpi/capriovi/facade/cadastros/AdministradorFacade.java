package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.dao.cadastros.AdministradorDAO;
import br.ufpi.capriovi.dao.cadastros.UsuarioDAO;
import br.ufpi.capriovi.entidades.cadastros.Administrador;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;



@Stateless
public class AdministradorFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3441104285380896782L;

	@Inject
	private AdministradorDAO administradorDAO;

	@Inject
	private UsuarioDAO usuarioDAO;

	public void addAdministrador(Administrador administrador) throws MensagensExceptions{

		if (usuarioDAO.buscarEmail(administrador.getEmail()) != null) {
			throw new MensagensExceptions(MensagensExceptions.EmailExistenteException);
		}
		if(usuarioDAO.buscarUsername(administrador.getUsername()) != null){
			throw new MensagensExceptions(MensagensExceptions.UsernameExistenteException);
		}

		if (usuarioDAO.buscarCpf(administrador.getCpf()) != null) {
			throw new MensagensExceptions(MensagensExceptions.CpfExistenteException);
		}

		administradorDAO.adicionaAdministrador(administrador);


	}

	/**
	 * 
	 * @return A quantidade de Administradores cadastrados.
	 */
	public int countTotal() {
		return GenericDAO.getCountTable();
	}
	/**
	 * 
	 * @return Todos os Administradores cadastrados
	 */
	public List<Administrador> listAll() {
		List<Administrador> result = administradorDAO.findAll();
		return result;
	}
	/**
	 * 
	 * @param id
	 */
	public void deletarAdministrador(Long id){
		administradorDAO.deletarAdministrador(id);
	}
	/**
	 * 
	 * @param Administrador
	 */
	public void atualizaAdministrador(Administrador administrador){
		administradorDAO.update(administrador);
	}
	/**
	 * 
	 * @param Administrador
	 */
	public void adicionaAdministrador(Administrador administrador){
		administradorDAO.adicionaAdministrador(administrador);
	}

}
