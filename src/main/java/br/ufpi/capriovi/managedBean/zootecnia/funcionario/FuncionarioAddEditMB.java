package br.ufpi.capriovi.managedBean.zootecnia.funcionario;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.ufpi.capriovi.entidades.cadastros.Funcionario;
import br.ufpi.capriovi.facade.cadastros.FuncionarioFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.MyEncoder;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;

@SessionScoped
@Named(value = "funcionarioAddEditMB")
public class FuncionarioAddEditMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1284163057682683920L;

	@Inject
	private FuncionarioFacade funcionarioFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	private Funcionario funcionario;

	private String title;

	private List<String> estados;

	public Funcionario getfuncionario() {
		return funcionario;
	}

	public void setfuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar um funcionário
	 */
	public void add() {
		this.title = "Adicionar Funcionário";
		this.funcionario = new Funcionario();	
		estados();
	}

	/**
	 * Método chamado para atualizar um funcionário
	 * @param p
	 */
	public void update(Funcionario p) {
		this.title = "Atualizar Funcionário";
		this.funcionario = p;
		estados();
	}

	public void delete(Long id) {
		this.funcionarioFacade.deletarFuncionario(id);
	}

	public void cancel() {
		this.funcionario = new Funcionario();		
	}

	/**
	 * Método chamado para salvar um funcionário
	 * @throws IOException 
	 */
	public String save() throws IOException {
		if (this.funcionario != null) {
			if (this.funcionario.getId() == null) {
				// Add
				setAtributos();

				try {
					this.funcionarioFacade.adicionaFuncionario(funcionario);					
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Funcionário adicionado com sucesso!!",  null);
					FacesContext.getCurrentInstance().addMessage(null, message);
					FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
					return "list.xhtml?faces-redirect=true";					
				} catch (MensagensExceptions e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(e.getMessage()));

					return "addEdit";
				}
			} else {
				// Update
				setAtributos();

				try {
					this.funcionarioFacade.atualizaFuncionario(funcionario);					
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Funcionário atualizado com sucesso!!",  null);
					FacesContext.getCurrentInstance().addMessage(null, message);
					FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
					return "list.xhtml?faces-redirect=true";
				} catch (MensagensExceptions e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(e.getMessage()));

					return "addEdit";
				}
			}
		}
		return null;
	}

	public void estados(){
		this.estados = new ArrayList<String>();

		this.estados.add("Acre");
		this.estados.add("Alagoas");
		this.estados.add("Amapá");
		this.estados.add("Amazonas");
		this.estados.add("Bahia");
		this.estados.add("Ceará");
		this.estados.add("Distrito Federal");
		this.estados.add("Espírito Santo");
		this.estados.add("Goiás");
		this.estados.add("Maranhão");
		this.estados.add("Mato Grosso");
		this.estados.add("Mato Grosso do Sul");
		this.estados.add("Minas Gerais");
		this.estados.add("Pará");
		this.estados.add("Paraíba");
		this.estados.add("Paraná");
		this.estados.add("Pernambuco");
		this.estados.add("Piauí");
		this.estados.add("Rio de Janeiro");
		this.estados.add("Rio Grande do Norte");
		this.estados.add("Rio Grande do Sul");
		this.estados.add("Rondônia");
		this.estados.add("Roraima");
		this.estados.add("Santa Catarina");
		this.estados.add("São Paulo");
		this.estados.add("Sergipe");
		this.estados.add("Tocantins");		
	}	

	public void setAtributos(){
		String senha;
		try {
			senha = MyEncoder.encriptar(funcionario.getPassword());
			funcionario.setPassword(senha);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		this.funcionario.setFazenda(systemSessionMB.getFazenda());
	}

	public List<String> getEstados() {
		return estados;
	}

	public void setEstados(List<String> estados) {
		this.estados = estados;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public void onChangeFazenda(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Fazenda selecionada: ", funcionario.getFazenda().getNome()));
	}

}
