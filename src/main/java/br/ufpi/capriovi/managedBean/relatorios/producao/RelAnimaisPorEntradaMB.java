package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.PieChartModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.tiposEnum.TipoMotivoEntradaEnum;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relAnimaisPorEntradaMB")
@ViewScoped
public class RelAnimaisPorEntradaMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6870029581334531067L;

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

	private List<Animal> animais;

	private List<Integer> anos;

	private List<String> entrada;

	private PieChartModel graficoEntrada;	

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
			CaprioviValidations.dataInicioDataFimVal(inicio, fim);
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return false;
		}
		return true;
	}

	/**
	 * Valida e carrega o resultados na dados da tabela.
	 */
	public void result() {
		this.animais = new ArrayList<Animal>();
		animais = relProducaoFacade.relAnimaisPorEntrada(rebanhosMarcados, inicio, fim);
		criaGraficoPizza();		
		inicializaEntradas();
	}

	/**
	 * Inicializa a lista entrada com os tipos de entradas existentes
	 */
	public void inicializaEntradas(){
		entrada = TipoMotivoEntradaEnum.getList();
		entrada.remove(0);
	}

	/**
	 * Método responsável por criar o gráfico em pizza do relatório
	 */
	private void criaGraficoPizza() {
		// TODO Auto-generated method stub
		anos = new ArrayList<Integer>();
		graficoEntrada = new PieChartModel();
		int compra = 0, nascimento = 0, emprestimo = 0,outros = 0;
		for (Animal animal : animais) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(animal.getDataEntrada());	
			if (contemAno(cal.get(Calendar.YEAR)) == false) {
				anos.add(cal.get(Calendar.YEAR));
			}
			if (animal.getMotivoEntrada().getCodigo() == 1) {
				compra++;
				graficoEntrada.set("Compra", compra);
			}else if (animal.getMotivoEntrada().getCodigo() == 2) {
				nascimento++;
				graficoEntrada.set("Nascimento", nascimento);
			}else if (animal.getMotivoEntrada().getCodigo() == 3) {
				emprestimo++;
				graficoEntrada.set("Emprestimo", emprestimo);
			}else{
				outros++;
				graficoEntrada.set("Outros", outros);
			}
		}

		graficoEntrada.setTitle("Gráfico de Entrada de Animais");
		graficoEntrada.setLegendPosition("w");
		graficoEntrada.setShowDataLabels(true);
	}

	/**
	 * Ordena o array de anos
	 */
	public void ordenaArray(){		
		Collections.sort(anos, new Comparator<Integer>(){
			@Override
			public int compare(Integer ano1, Integer ano2) {
				// TODO Auto-generated method stub
				return ano1.compareTo(ano2);
			}
		});
	}

	/**
	 * Testa se um ano já está na lista
	 * @param ano
	 * @return
	 */
	public boolean contemAno(int ano){
		for (Integer integer : anos) {
			if (ano == integer) {
				return true;
			}
		}
		return false;
	}

	public List<Rebanho> getRebanhos() {
		if (systemSessionMB.getFazenda() != null) {
			rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
		}
		return rebanhos;
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
	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public PieChartModel getGraficoEntrada() {
		return graficoEntrada;
	}

	public void setGraficoEntrada(PieChartModel graficoEntrada) {
		this.graficoEntrada = graficoEntrada;
	}

	public List<Integer> getAnos() {
		return anos;
	}

	public void setAnos(List<Integer> anos) {
		this.anos = anos;
	}

	public List<String> getEntrada() {
		return entrada;
	}

	public void setEntrada(List<String> entrada) {
		this.entrada = entrada;
	}

	/**
	 * Retorna a quantidade de um tipo de entrada em um ano
	 * @param tipo
	 * @param ano
	 * @return
	 */
	public int entradasAno(String tipo, int ano){
		int quant = 0;
		for (Animal animal : animais) {	
			Calendar cal = Calendar.getInstance();
			cal.setTime(animal.getDataEntrada());				
			if (animal.getMotivoEntrada().getDescricao().equals(tipo) && cal.get(Calendar.YEAR) == ano) {
				quant++;			
			}
		}		

		return quant;
	}
}
