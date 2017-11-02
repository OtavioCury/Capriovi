package br.ufpi.capriovi.managedBean.zootecnia.funcionario;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Funcionario;
import br.ufpi.capriovi.facade.cadastros.FuncionarioFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@Named(value = "funcionarioMB")
@ViewScoped
public class FuncionarioMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919753928511006883L;

	@Inject
	private FuncionarioFacade funcionarioFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	private List<Funcionario> funcionarios;	


	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {		
		funcionarios = new ArrayList<Funcionario>();
		if (systemSessionMB.getFazenda() != null) {
			funcionarios = funcionarioFacade.retornaFuncionarioFazenda(systemSessionMB.getFazenda());
		}
	}	

	/**
	 * retorna todos os funcionários de todas as fazendas
	 * @return
	 */
	public List<Funcionario> funcionariosFazendas(){
		funcionarios = new ArrayList<Funcionario>();	
		if (!systemSessionMB.getListFazendas().isEmpty()) {
			funcionarios = funcionarioFacade.retornaFuncionario(systemSessionMB.getListFazendas());
		}		
		return funcionarios;
	}

	/**
	 * Deleta um funcionario da lista e do banco de dados
	 * @param funcionario
	 */
	public void deletaFuncionario(Funcionario funcionario){
		funcionarios.remove(funcionario);
		funcionarioFacade.deletarFuncionario(funcionario.getId());
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}	

}
