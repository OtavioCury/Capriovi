package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.relatorio.RelatorioVermifugação;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relVermifugacaoMB")
@ViewScoped
public class RelVermifugacaoMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6870029581334531067L;

	@Inject
	RebanhoMB rebanhoMB;

	@Inject
	private RebanhoFacade rebanhoFacade;


	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RelProducaoFacade relProducaoFacade;

	/**
	 * Rebanhos para popular datatable.
	 */
	private List<Rebanho> rebanhos;

	/**
	 * Rebanhos marcados para gerar relatório.
	 */
	private List<Rebanho> rebanhosMarcados;

	private List<Animal> animais;

	private PieChartModel pie;	

	private Animal animalSelecionado;	

	private LineChartModel graficoHistorico;

	public String onFlowProcess(FlowEvent event) {
		if (this.validaRelatorio()) {
			this.result();
			return event.getNewStep();
		}
		return "escolheRebanho";
	}

	/**
	 * Verifica se foi selecionado algum rebanho
	 * 
	 * @return
	 */
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
	 * Valida e carrega o resultados na dados da tabela.
	 */
	public void result() {	
		pie = new PieChartModel();
		animais = new ArrayList<Animal>();
		animais = relProducaoFacade.relVermifuga(idsRebanhos(rebanhosMarcados));
		if (animais.size() > 0) {
			createPie();
			ordenaHistorico();
		}				
	}		

	/**
	 * Retorna uma lista de ids de uma lista de rebanhos
	 * @param rebanhos
	 * @return
	 */
	public List<Long> idsRebanhos(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();

		for (Rebanho rebanho : rebanhos) {
			ids.add(rebanho.getId());
		}

		return ids;
	}

	private void ordenaHistorico() {
		// TODO Auto-generated method stub
		for (Animal animal : animais) {
			Collections.sort(animal.getListaNotas(), new Comparator<RelatorioVermifugação>() {				
				@Override
				public int compare(RelatorioVermifugação arg0, RelatorioVermifugação arg1) {
					// TODO Auto-generated method stub
					return arg0.getData().compareTo(arg1.getData());
				}
			});
		}
	}

	/**
	 * Cria o gráfico
	 */
	public void createPie() {		

		int desnecessario = 0;
		int necessario = 0;
		int alerta = 0;
		int risco = 0;
		int atencao = 0;

		for (Animal animal : animais) {

			if (animal.getListaNotas().get(animal.getListaNotas().size() - 1).getStatusVermifuga() != null) {
				if (animal.getListaNotas().get(animal.getListaNotas().size() - 1).getStatusVermifuga().getDescricao().equals("Não Vermifugar")) {
					desnecessario++;
					pie.set("Não Vermifugar", desnecessario);
				} else if (animal.getListaNotas().get(animal.getListaNotas().size() - 1).getStatusVermifuga().getDescricao().equals("Vermifugar")) {
					necessario++;
					pie.set("Vermifugar", necessario);
				} else if (animal.getListaNotas().get(animal.getListaNotas().size() - 1).getStatusVermifuga().getDescricao().equals("Alerta")) {
					alerta++;
					pie.set("Alerta", alerta);
				} else if (animal.getListaNotas().get(animal.getListaNotas().size() - 1).getStatusVermifuga().getDescricao().equals("Risco")) {
					risco++;
					pie.set("Risco", risco);
				} else {
					atencao++;
					pie.set("Atenção", atencao);
				}
			}

		}

		pie.setTitle("Gráfico Vermifugação");
		pie.setLegendPosition("w");
		pie.setShowDataLabels(true);

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

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public PieChartModel getPie() {
		return pie;
	}

	public void setPie(PieChartModel pie) {
		this.pie = pie;
	}

	public Animal getAnimalSelecionado() {
		return animalSelecionado;
	}

	public void setAnimalSelecionado(Animal animalSelecionado) {
		this.animalSelecionado = animalSelecionado;
	}	

	public LineChartModel getGraficoHistorico() {
		return graficoHistorico;
	}

	public void setGraficoHistorico(LineChartModel graficoHistorico) {
		this.graficoHistorico = graficoHistorico;
	}

	public void graficoAnimal(Animal animal){		
		graficoHistorico = new LineChartModel();

		ChartSeries series = new ChartSeries();
		series.setLabel("Nota");								
		for (RelatorioVermifugação relatorioVermifugação : animal.getListaNotas()) {	
			series.set(relatorioVermifugação.getData().toString(), relatorioVermifugação.getNotaVermifugacao());
		}
		graficoHistorico.addSeries(series);
		graficoHistorico.setTitle("Gráfico de análises");
		graficoHistorico.setLegendPosition("e");			
		graficoHistorico.setShowPointLabels(true);
		graficoHistorico.getAxes().put(AxisType.X, new CategoryAxis("Data"));
		graficoHistorico.setAnimate(true);
		Axis yAxis = graficoHistorico.getAxis(AxisType.Y);	        
		yAxis.setLabel("Nota");
		yAxis.setMin(0);
		yAxis.setMax(10);		

	}
}
