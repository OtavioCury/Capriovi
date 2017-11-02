package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.dao.cadastros.UsuarioDAO;
import br.ufpi.capriovi.entidades.cadastros.Usuario;
import br.ufpi.capriovi.suporte.Email;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
/**
 * Regras de Negócio do Usuário.
 * @author thasciano
 *
 */
@Stateless
public class UsuarioFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6687536349907724882L;

	@Inject
	private UsuarioDAO usuDAO; 	

	/**
	 * busca usuáiro por token
	 * @param token
	 * @return
	 */
	public Usuario usuarioToken(String token){
		return usuDAO.buscarToken(token);
	}

	/**
	 * busca usuáiro por token senha
	 * @param token
	 * @return
	 */
	public Usuario usuarioTokenSenha(String token){
		return usuDAO.buscarTokenSenha(token);
	}

	/**
	 * Lista todos os usuários do sistema
	 * @return
	 */
	public List<Usuario> todosUsuarios(){
		return usuDAO.findAll();
	}

	/**
	 * 
	 * @return A quantidade de Usuários cadastrados.
	 */
	public int countTotal() {
		return GenericDAO.getCountTable();
	}

	/**
	 * 
	 * @param id
	 */
	public void deletarUsuario(Long id){
		usuDAO.deletarUsuario(id);
	}
	/**
	 * 
	 * @param usuario
	 */
	public void atualizaUsuario(Usuario usuario){
		usuDAO.update(usuario);
	}
	/**
	 * 
	 * @param usuario
	 */
	public void adicionaUsuario(Usuario usuario){
		usuDAO.adicionaUsuario(usuario);
	}

	public Usuario buscarId(String username){
		Usuario usuario = usuDAO.buscarUsername(username);
		return usuario;
	}

	/**
	 * Envia menssagem ao email do Capriovi
	 * @param nome
	 * @param email
	 * @param menssagem
	 * @param assunto
	 * @throws EmailException
	 * @throws MensagensExceptions 
	 */
	public void enviarMensagem(String nome, String email,String mensagem, String assunto){			
		try {
			Email.enviarMenssagem(nome, email,assunto, mensagem);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void recuperaSenha(String email, String cpf) throws MensagensExceptions, RuntimeException, NoSuchAlgorithmException{

		Usuario p = usuDAO.buscarEmail(email);;

		if (p != null && (p.getCpf().equals(cpf))) {
			Email.confirmaEmail(p);
		}else{
			throw new MensagensExceptions(MensagensExceptions.EmailCpfInvalidos);
		}
	}

	public Usuario buscarPorEmail(String email){
		return usuDAO.buscarEmail(email);
	}
}
