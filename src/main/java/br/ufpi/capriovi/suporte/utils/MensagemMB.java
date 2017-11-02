package br.ufpi.capriovi.suporte.utils;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.facade.cadastros.UsuarioFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;

@Named(value = "mensagemMB")
@SessionScoped
public class MensagemMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;

	private String email;

	private String nome;

	private String assunto;

	private String mensagem;

	@Inject
	UsuarioFacade usuarioFacade;

	private Date data;

	private long inicioSegundos, fimSegundos;

	private boolean aux;

	@PostConstruct
	public void init(){
		aux = false;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public UsuarioFacade getUsuarioFacade() {
		return usuarioFacade;
	}

	public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}



	public long getInicioSegundos() {
		return inicioSegundos;
	}

	public void setInicioSegundos(long inicioSegundos) {
		this.inicioSegundos = inicioSegundos;
	}

	public long getFimSegundos() {
		return fimSegundos;
	}

	public void setFimSegundos(long fimSegundos) {
		this.fimSegundos = fimSegundos;
	}

	public boolean isAux() {
		return aux;
	}

	public void setAux(boolean aux) {
		this.aux = aux;
	}

	public void enviarMenssagem(){
		try {
			if (aux == true) {
				fimSegundos = data.getTime();
				if((fimSegundos - inicioSegundos)/1000 == 300){
					aux = false;
				}
			}
			if (aux == false) {
				if (usuarioFacade.buscarPorEmail(email) != null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Menssagem foi enviada com sucesso!","Sua sugestão foi enviada!"));
					usuarioFacade.enviarMensagem(nome, email,mensagem, assunto);
					aux = true;
					data = new Date();
					inicioSegundos = data.getTime();
				}else{
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Você ainda não está cadastrado no Capriovi!","Cadastre-se para enviar sua sugestão"));
				}				
			}else{
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Você já eviou sua mensagem!","Espere alguns minutos antes de enviar a próxima"));
			}			
		} catch (RuntimeException e ) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
		}
	}

}
