package br.ufpi.capriovi.managedBean.zootecnia.pecuarista;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Pecuarista;
import br.ufpi.capriovi.facade.cadastros.FazendaFacade;
import br.ufpi.capriovi.facade.cadastros.PecuaristaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.suporte.MyEncoder;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;


@Named(value = "pecuaristaAddEditMB")
@SessionScoped
public class PecuaristaAddEditMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5696158873102169483L;

	@Inject
	PecuaristaFacade pecuaristaFacade;	

	@Inject
	FazendaFacade fazendaFacade;

	private List<String> estados;

	private Pecuarista pecuarista;

	private String title;

	private String email;

	private String nome;

	private String assunto;

	private String menssagem;

	public Pecuarista getPecuarista() {
		return pecuarista;
	}

	public void setUsuario(Pecuarista pecuarista) {
		this.pecuarista = pecuarista;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void add() {
		this.title = "Adicionar Pecuarista";
		if (pecuarista == null) {
			this.pecuarista = new Pecuarista();
			this.estados = new ArrayList<String>();
			estados();
		}
	}

	public void update(Pecuarista u) {
		this.title = "Atualizar Pecuarista";
		this.pecuarista = u;
	}

	public void delete(Long id) {
		this.pecuaristaFacade.deletarPecuarista(id);
	}

	public void cancel() {
		this.pecuarista = null;
	}

	public String save() throws RuntimeException {
		this.email = pecuarista.getEmail();
		String senha;
		try {
			senha = MyEncoder.encriptar(pecuarista.getPassword());
			pecuarista.setPassword(senha);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return testUsuario();

	}


	public String testUsuario() throws RuntimeException{
		try {
			this.pecuaristaFacade.adicionaPecuarista(pecuarista);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Cadastro realizado com sucesso!!","Acesse sua conta."));
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			pecuarista = null;

			return "enviaEmail.xhtml?faces-redirect=true";
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));			

			return "cadastro";
		}
	}

	public void estados(){
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

	public List<String> getEstados() {
		return estados;
	}

	public void setEstados(List<String> estados) {
		this.estados = estados;
	}

	public void setPecuarista(Pecuarista pecuarista) {
		this.pecuarista = pecuarista;
	}

	public void info() {
		if(this.pecuarista != null){
			FacesContext.getCurrentInstance().addMessage(":form:messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Cadastro realizado com sucesso!"));
		}
	}

	public void enviarMenssagem(){
		try {
			pecuaristaFacade.enviarMenssagem(nome, email,menssagem, assunto);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Sua menssagem foi enviada com sucesso!"));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMenssagem() {
		return menssagem;
	}

	public void setMenssagem(String menssagem) {
		this.menssagem = menssagem;
	}

}
