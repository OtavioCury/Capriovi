package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.PieChartModel;

import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;
import br.ufpi.capriovi.facade.cadastros.DoencaFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relOcorrenciasClinicasMB")
@ViewScoped
public class RelOcorrenciasClinicasMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5297858320532478553L;

	@Inject
	private DoencaFacade doencaFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private RelProducaoFacade relProducaoFacade;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhosMarcados;

	private Date inicio;

	private Date fim;

	private List<Doenca> listDoencas;

	private Doenca doencaSel;

	private List<OcorrenciaClinica> ocorrenciasClinicas;

	private PieChartModel graficoOcorrencia;	

	private List<String> nomeDoenca;

	private Map<String, Integer> doencaQuant;

	private List<Integer> anos;

	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {
		this.listDoencas = doencaFacade.doencasUsuario(getUsuario());
	}

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
		this.ocorrenciasClinicas = relProducaoFacade.relOcorrenciaClinica(rebanhosMarcados, inicio, fim, doencaSel);
		criaGraficoPizza();
		inicializaAnos();
	}

	/**
	 * Inicializa a lista de anos
	 */
	private void inicializaAnos() {
		// TODO Auto-generated method stub
		anos = new ArrayList<Integer>();
		for (OcorrenciaClinica ocorrencia : ocorrenciasClinicas) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(ocorrencia.getData());	
			boolean aux = false;			
			for (Integer integer : anos) {
				if (integer == cal.get(Calendar.YEAR)) {
					aux = true;
				}
			}
			if (aux == false) {
				anos.add(cal.get(Calendar.YEAR));
			}
		}
	}

	/**
	 * Cria gráfico em pizza
	 */
	private void criaGraficoPizza() {
		// TODO Auto-generated method stub
		doencaQuant = new HashMap<String, Integer>();
		graficoOcorrencia = new PieChartModel();

		for (Doenca doenca : listDoencas) {
			doencaQuant.put(doenca.getNome(), 0);
		}

		for (OcorrenciaClinica ocorrencia : ocorrenciasClinicas) {
			int valor = doencaQuant.get(ocorrencia.getDoenca().getNome());			
			doencaQuant.put(ocorrencia.getDoenca().getNome(), valor + 1);
		}

		Iterator it = doencaQuant.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry par = (Map.Entry)it.next();
			graficoOcorrencia.set(par.getKey().toString(), Integer.parseInt(par.getValue().toString()));
			it.remove();
		}

		graficoOcorrencia.setTitle("Gráfico de Ocorrências clínicas");
		graficoOcorrencia.setLegendPosition("w");
		graficoOcorrencia.setShowDataLabels(true);
	}

	public List<Doenca> completaNome(String query) {
		List<Doenca> sugestoes = new ArrayList<Doenca>();
		for (Doenca d : this.listDoencas) {
			if (d.getNome().toLowerCase().startsWith(query.toLowerCase())) {
				sugestoes.add(d);
			}
		}
		return sugestoes;
	}
	
	public int ocorrenciasAnos(int ano){
		int quant = 0;
		for (OcorrenciaClinica ocorrencia : ocorrenciasClinicas) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(ocorrencia.getData());
			if (cal.get(Calendar.YEAR) == ano) {
				quant++;
			}
		}
		return quant;
	}

	public List<Rebanho> getRebanhos() {
		if (systemSessionMB.getFazenda() != null) {
			this.rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
		}
		return rebanhos;
	}

	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public List<Doenca> getListDoencas() {
		return listDoencas;
	}

	public void setListDoencas(List<Doenca> listDoencas) {
		this.listDoencas = listDoencas;
	}

	public Doenca getDoencaSel() {
		return doencaSel;
	}

	public void setDoencaSel(Doenca doencaSel) {
		this.doencaSel = doencaSel;
	}

	public List<OcorrenciaClinica> getOcorrenciasClinicas() {
		return ocorrenciasClinicas;
	}

	public void setOcorrenciasClinicas(List<OcorrenciaClinica> ocorrenciasClinicas) {
		this.ocorrenciasClinicas = ocorrenciasClinicas;
	}

	public List<Rebanho> getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(List<Rebanho> rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public PieChartModel getGraficoOcorrencia() {
		return graficoOcorrencia;
	}

	public void setGraficoOcorrencia(PieChartModel graficoOcorrencia) {
		this.graficoOcorrencia = graficoOcorrencia;
	}

	public List<String> getNomeDoenca() {
		return nomeDoenca;
	}

	public void setNomeDoenca(List<String> nomeDoenca) {
		this.nomeDoenca = nomeDoenca;
	}

	public Map<String, Integer> getDoencaQuant() {
		return doencaQuant;
	}

	public void setDoencaQuant(Map<String, Integer> doencaQuant) {
		this.doencaQuant = doencaQuant;
	}

	public List<Integer> getAnos() {
		return anos;
	}

	public void setAnos(List<Integer> anos) {
		this.anos = anos;
	}

}
