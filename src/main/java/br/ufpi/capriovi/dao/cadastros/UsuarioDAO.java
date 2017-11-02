package br.ufpi.capriovi.dao.cadastros;

import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Usuario;
import br.ufpi.capriovi.suporte.MyEncoder;
/**
 * 
 * @author thasciano
 *
 */
@Stateless
public class UsuarioDAO extends GenericDAO<Usuario, Usuario>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1839993736619315359L;

	public UsuarioDAO() {
		super(Usuario.class);
	}

	/**
	 * Busca usuário por username
	 * @param username
	 * @return
	 */
	public Usuario buscarUsername(String username){

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> r = cq.from(Usuario.class);

		cq.select(r);

		CriteriaQuery<Usuario> select = cq.where(cb.equal(r.get("username"), username));

		TypedQuery<Usuario> tq = getEm().createQuery(select);

		try {
			Usuario usuario = tq.getSingleResult();
			return usuario;
		} catch (NoResultException nre) {
			return null;
		}
	}

	/**
	 * Busca usuário por cpf
	 * @param cpf
	 * @return
	 */
	public Usuario buscarCpf(String cpf){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> r = cq.from(Usuario.class);

		cq.select(r);

		CriteriaQuery<Usuario> select = cq.where(cb.equal(r.get("cpf"), cpf));

		TypedQuery<Usuario> tq = getEm().createQuery(select);

		try {
			Usuario usuario = tq.getSingleResult();
			return usuario;
		} catch (NoResultException nre) {
			return null;
		}
	}

	/**
	 * Busca usuário por email
	 * @param email
	 * @return
	 */
	public Usuario buscarEmail(String email){

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> r = cq.from(Usuario.class);

		cq.select(r);

		CriteriaQuery<Usuario> select = cq.where(cb.equal(r.get("email"), email));

		TypedQuery<Usuario> tq = getEm().createQuery(select);

		try {
			Usuario usuario = tq.getSingleResult();
			return usuario;
		} catch (NoResultException nre) {
			return null;
		}

	}	

	/**
	 * Busca usuário por token
	 * @param email
	 * @return
	 */
	public Usuario buscarToken(String token){

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> r = cq.from(Usuario.class);

		cq.select(r);

		CriteriaQuery<Usuario> select = cq.where(cb.equal(r.get("token"), token));

		TypedQuery<Usuario> tq = getEm().createQuery(select);

		try {
			Usuario usuario = tq.getSingleResult();
			return usuario;
		} catch (NoResultException nre) {
			return null;
		}

	}	

	/**
	 * Busca usuário por token senha
	 * @param email
	 * @return
	 */
	public Usuario buscarTokenSenha(String token){

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> r = cq.from(Usuario.class);

		cq.select(r);

		CriteriaQuery<Usuario> select = cq.where(cb.equal(r.get("tokenSenha"), token));

		TypedQuery<Usuario> tq = getEm().createQuery(select);

		try {
			Usuario usuario = tq.getSingleResult();
			return usuario;
		} catch (NoResultException nre) {
			return null;
		}

	}	

	/**
	 * Testa se existe um usuário com a senha e login informados e se ele pode acessar o sistema
	 * @param login
	 * @param senha
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public Usuario testaLogin(String login, String senha) throws NoSuchAlgorithmException{

		String hash = MyEncoder.encriptar(senha);

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> r = cq.from(Usuario.class);

		cq.select(r);

		Predicate loginPredicate = cb.equal(r.get("username"), login);

		Predicate senhaPredicate = cb.equal(r.get("password"), hash);

		Predicate statusPredicate = cb.equal(r.get("statusGeral"), 1);

		CriteriaQuery<Usuario> select = cq.where(cb.and(loginPredicate, senhaPredicate, statusPredicate));

		TypedQuery<Usuario> tq = getEm().createQuery(select);

		try {
			Usuario usuario = tq.getSingleResult();
			return usuario;
		} catch (NoResultException nre) {
			return null;
		}
	}	

	/**
	 * Deleta o usuário do BD.
	 * @param id
	 */
	public void deletarUsuario(Long id){
		super.delete(id, Usuario.class);
	}

	/**
	 * Salva o usuário no BD.
	 * @param usuario
	 */
	public void adicionaUsuario(Usuario usuario){
		super.save(usuario);
	}
}
