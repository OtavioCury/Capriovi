package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.ufpi.capriovi.suporte.relatorios.ResumoCPM;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.RelatoriosFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relCpmMB")
@ViewScoped
public class RelCpmMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6995374687857288299L;

	@Inject
	private SystemSessionMB systemSessionMB;
	
	@Inject
	private RelatoriosFacade relatoriosFacade;
	
	@Inject
	private RebanhoFacade rebanhoFacade;
	
	@Inject
	private RelProducaoFacade relProducaoFacade;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhosMarcados;

	private List<ResumoCPM> resumoCpm;
	
	public String onFlowProcess(FlowEvent event) {
		if (this.validaRelatorio()) {
			this.result();
			return event.getNewStep();
		}
		return "escolheRebanho";

	}

	public boolean validaRelatorio() {
		try {
			CaprioviValidations.rebanhoSelecionadoVal(rebanhosMarcados);
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return false;
		}
		return true;
	}

	/**
	 * carrega os dados da tabela.
	 */
	public void result() {
		this.resumoCpm = relProducaoFacade.relCpm(rebanhosMarcados);
	}

	public List<Rebanho> getRebanhos() {
		if (systemSessionMB.getFazenda() != null) {
			this.rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
		}
		return rebanhos;
	}
		
	public StreamedContent getDownloadRelPDF() throws MensagensExceptions {
		
        try {
            this.relatoriosFacade.gerarRelatorio(resumoCpm, "RelCPM", "RelCPM"); // método que gera o relatório no diretório temporário
            String path = System.getProperty("java.io.tmpdir") + "/Relatorio.pdf"; // referencia para o caminho do arquivo gerado
            java.io.File f = new java.io.File(path); // referencia para o arquivo gerado
            InputStream stream = new java.io.FileInputStream(f); // referencia para a stream do arquivo gerado

            return new DefaultStreamedContent(stream, "application/pdf", "RelCPM.pdf"); // parametros: stream do arquivo, tipo do arquivo (mimetype), nome do arquivo de download
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }

        return null;
	}
	
	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}

	public List<Rebanho> getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(List<Rebanho> rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public List<ResumoCPM> getResumoCpm() {
		return resumoCpm;
	}

	public void setResumoCpm(List<ResumoCPM> resumoCpm) {
		this.resumoCpm = resumoCpm;
	}

}