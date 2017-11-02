package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.cadastros.FuncionarioDAO;
import br.ufpi.capriovi.dao.cadastros.UsuarioDAO;
import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Funcionario;
import br.ufpi.capriovi.entidades.cadastros.Usuario;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;



@Stateless
public class FuncionarioFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5682341080963980574L;

	@Inject
	private FuncionarioDAO funcionarioDAO; 

	@Inject
	private UsuarioDAO usuarioDAO; 

	@Inject
	private FuncionarioFacade funcionarioFacade;

	private List<Funcionario> funcionarios;

	public List<Funcionario> listAllId(Long id) {
		List<Funcionario> result = funcionarioDAO.listAll("fazenda", id);
		return result;
	}
	/**
	 * 
	 * @param id
	 */
	public void deletarFuncionario(Long id){
		funcionarioDAO.deletarFuncionario(id);
	}
	/**
	 * 
	 * @param Peao
	 */
	public void atualizaFuncionario(Funcionario funcionario) throws MensagensExceptions{

		testaUsuarioAtualizar(funcionario.getUsername(), funcionario.getId());

		funcionarioDAO.update(funcionario);
	}
	/**
	 * 
	 * @param Peao
	 * @throws MensagensExceptions 
	 */
	public void adicionaFuncionario(Funcionario funcionario) throws MensagensExceptions{

		testaUsuarioAdicionar(funcionario.getUsername());

		funcionarioDAO.adicionaFuncionario(funcionario);
	}

	/**
	 * Testa se o cpf ou login do funcionário já estão cadastradas
	 * @param email
	 * @param username
	 * @param cpf
	 * @throws MensagensExceptions
	 */
	public void testaUsuarioAdicionar(String username) throws MensagensExceptions{
		if (usuarioDAO.buscarUsername(username) != null) {
			throw new MensagensExceptions(MensagensExceptions.UsernameExistenteException);
		}
	}

	public void testaUsuarioAtualizar(String username, Long id)throws MensagensExceptions{
		Usuario usuarioLogin = usuarioDAO.buscarUsername(username);

		if (usuarioLogin!= null && (usuarioLogin.getId() != id)) {
			throw new MensagensExceptions(MensagensExceptions.UsernameExistenteException);
		}
	}	

	public List<Funcionario> retornaFuncionario(List<Fazenda> fazendas){

		funcionarios = new ArrayList<Funcionario>();

		if (!fazendas.isEmpty()) {
			for (Fazenda fazenda: fazendas) {
				this.funcionarios.addAll(funcionarioFacade.listAllId(fazenda.getId()));
			}
		}

		return funcionarios;

	}

	public List<Funcionario> retornaFuncionarioFazenda(Fazenda fazenda){

		funcionarios = new ArrayList<Funcionario>();

		this.funcionarios.addAll(funcionarioFacade.listAllId(fazenda.getId()));		

		return funcionarios;

	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}
	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
}
