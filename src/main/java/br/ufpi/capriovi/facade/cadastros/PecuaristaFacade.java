package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.cadastros.PecuaristaDAO;
import br.ufpi.capriovi.dao.cadastros.UsuarioDAO;
import br.ufpi.capriovi.entidades.cadastros.Administrador;
import br.ufpi.capriovi.entidades.cadastros.Pecuarista;
import br.ufpi.capriovi.suporte.Email;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;



@Stateless
public class PecuaristaFacade implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5300896355924473790L;

	@Inject
	private PecuaristaDAO pecuaristaDAO;

	@Inject
	private UsuarioDAO usuarioDAO;

	@Inject
	private AdministradorFacade administradorFacade;

	/**
	 * 
	 * @param id
	 */
	public void deletarPecuarista(Long id){
		pecuaristaDAO.deletarPecuarista(id);
	}
	/**
	 * 
	 * @param pecuarista
	 */
	public void atualizaPecuarista(Pecuarista pecuarista){
		pecuaristaDAO.update(pecuarista);
	}

	/**
	 * 
	 * @param pecuarista
	 * @throws EmailException 
	 */
	public void adicionaPecuarista(Pecuarista pecuarista) throws MensagensExceptions, RuntimeException{

		List<Administrador> admins = new ArrayList<Administrador>();

		testaUsuario(pecuarista.getEmail(), pecuarista.getUsername(), pecuarista.getCpf());
		admins = administradorFacade.listAll();		
		for (Administrador administrador : admins) {
			Email.avisaAdmin(administrador, pecuarista);
		}
		Email.cadastro(pecuarista);		
		pecuaristaDAO.adicionaPecuarista(pecuarista);

	}

	/**
	 * Testa se o email ou senha do funcionário já estão cadastradas
	 * @param email
	 * @param username
	 * @param cpf
	 * @throws MensagensExceptions
	 * @throws EmailException 
	 */
	public void testaUsuario(String email, String username, String cpf) throws MensagensExceptions, RuntimeException{
		if (usuarioDAO.buscarEmail(email) != null) {
			throw new MensagensExceptions(MensagensExceptions.EmailExistenteException);
		}

		if (usuarioDAO.buscarUsername(username) != null) {
			throw new MensagensExceptions(MensagensExceptions.UsernameExistenteException);
		}

		if (usuarioDAO.buscarCpf(cpf) != null) {
			throw new MensagensExceptions(MensagensExceptions.CpfExistenteException);
		}

	}

	public void enviarMenssagem(String nome, String email,String menssagem, String assunto) throws RuntimeException{
		try {
			Email.enviarMenssagem(nome, email,menssagem, assunto);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public Pecuarista buscarId(String username){
		Pecuarista pecuarista = pecuaristaDAO.buscarId(username);
		return pecuarista;
	}

	public Pecuarista buscarPeloId(Long id){
		Pecuarista pecuarista = pecuaristaDAO.find(id);
		return pecuarista;
	}

	public String generateRandomPassword(){
		Random RANDOM = new SecureRandom();

		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";

		String pw = "";
		for (int i=0; i < 6; i++)
		{
			int index = (int)(RANDOM.nextDouble()*letters.length());
			pw += letters.substring(index, index+1);
		}
		return pw;
	}
	public PecuaristaDAO getPecuaristaDAO() {
		return pecuaristaDAO;
	}
	public void setPecuaristaDAO(PecuaristaDAO pecuaristaDAO) {
		this.pecuaristaDAO = pecuaristaDAO;
	}
	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}
	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

}
