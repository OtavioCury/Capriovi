package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.ufpi.capriovi.suporte.relatorios.ResumoDadosZootecRebanho;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relDadosZootecRebanhoMB")
@ViewScoped
public class RelDadosZootecRebanhoMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6995374687857288299L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private RelProducaoFacade relProducaoFacade;

	private List<Rebanho> rebanhos;

	private Rebanho rebanhosMarcados;

	private ResumoDadosZootecRebanho resumoDadosZootecRebanho;

	private PieChartModel pieModelDistri;

	private PieChartModel pieMovAnimal;

	private LineChartModel curvaPesos;

	private BarChartModel mortNat;

	public String onFlowProcess(FlowEvent event) {
		if (this.validaRelatorio()) {
			this.result();
			createPieDistAnimal();
			createPieMovAnimal();
			createcurvaPesosGraf();
			createMortNatGraf();
			return event.getNewStep();

		}
		return "escolheRebanho";

	}

	public boolean validaRelatorio() {
		try {
			CaprioviValidations.rebanhoSelecionadoSingleVal(rebanhosMarcados);
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
		this.resumoDadosZootecRebanho = relProducaoFacade.relDadosZootecRebanho(rebanhosMarcados);
	}

	private void createPieDistAnimal() {
		pieModelDistri = new PieChartModel();

		pieModelDistri.set("Femeas Vivas", resumoDadosZootecRebanho.getQuantFemeasV());
		pieModelDistri.set("Femeas Mortas", resumoDadosZootecRebanho.getQuantFemeasM());
		pieModelDistri.set("Machos Vivos", resumoDadosZootecRebanho.getQuantMachosV());
		pieModelDistri.set("Machos Mortos", resumoDadosZootecRebanho.getQuantMachosM());

		pieModelDistri.setLegendPosition("e");
		pieModelDistri.setTitle("Distribuição de Machos e Fêmeas");
		pieModelDistri.setShowDataLabels(true);
		pieModelDistri.setDiameter(180);
	}

	private void createPieMovAnimal() {
		pieMovAnimal = new PieChartModel();

		pieMovAnimal.set("Venda", resumoDadosZootecRebanho.getVenda());
		pieMovAnimal.set("Morte", resumoDadosZootecRebanho.getMorte());
		pieMovAnimal.set("Roubo", resumoDadosZootecRebanho.getRoubo());
		pieMovAnimal.set("Alimentação", resumoDadosZootecRebanho.getAlimentacao());
		pieMovAnimal.set("Emprestimo", resumoDadosZootecRebanho.getEmprestimo());

		pieMovAnimal.setLegendPosition("e");
		pieMovAnimal.setTitle("Distribuição de Movimentação Animal");
		pieMovAnimal.setShowDataLabels(true);
		pieMovAnimal.setDiameter(180);
	}

	private void createcurvaPesosGraf() {
		curvaPesos = initLinearcurvaPesosGraf();
		if (curvaPesos != null) {
			curvaPesos.setAnimate(true);
			curvaPesos.setZoom(true);
			curvaPesos.setShowPointLabels(true);
			curvaPesos.setTitle("Curva de Crescimento Animal Médio");

			Axis yAxis = curvaPesos.getAxis(AxisType.Y);
			Axis xAxis = curvaPesos.getAxis(AxisType.X);

			yAxis.setLabel("Pesos (Kg)");
			xAxis.setLabel("Idade (dias)");
			yAxis.setMin(0);
			yAxis.setMax((this.resumoDadosZootecRebanho.getLimiteCurvaPesos() + 20));
		}
	}

	private LineChartModel initLinearcurvaPesosGraf() {
		LineChartModel model = new LineChartModel();

		LineChartSeries series1 = new LineChartSeries();
		if (this.resumoDadosZootecRebanho.getMediaPeso60() > 0) {
			series1.set(60, resumoDadosZootecRebanho.getMediaPeso60());
		}
		if (this.resumoDadosZootecRebanho.getMediaPeso120() > 0) {
			series1.set(120, resumoDadosZootecRebanho.getMediaPeso120());
		}
		if (this.resumoDadosZootecRebanho.getMediaPeso180() > 0) {
			series1.set(180, resumoDadosZootecRebanho.getMediaPeso180());
		}

		if (series1.getData().isEmpty()) {
			model = null;
		} else {
			model.addSeries(series1);
		}

		return model;
	}

	private void createMortNatGraf() {
		mortNat = initBarMortNat();
		if (mortNat != null) {
			mortNat.setTitle("Natalidade e Mortalidade do Rebanho");
			mortNat.setLegendPosition("ne");
			mortNat.setShowPointLabels(true);
			Axis xAxis = mortNat.getAxis(AxisType.X);
			xAxis.setLabel("Anos");

			Axis yAxis = mortNat.getAxis(AxisType.Y);
			yAxis.setLabel("Quantidade");
			yAxis.setMin(0);
			yAxis.setMax((this.resumoDadosZootecRebanho.getLimiteGraf() + 20));
		}
	}

	private BarChartModel initBarMortNat() {
		BarChartModel model = new BarChartModel();

		ChartSeries nat = new ChartSeries();
		nat.setLabel("Natalidade");
		for (Map.Entry<String, Integer> hashNat : resumoDadosZootecRebanho.getNatalidade().entrySet()) {
			nat.set(hashNat.getKey(), hashNat.getValue());
		}

		ChartSeries mort = new ChartSeries();
		mort.setLabel("Mortalidade");
		for (Map.Entry<String, Integer> hashMort : resumoDadosZootecRebanho.getMortalidade().entrySet()) {
			mort.set(hashMort.getKey(), hashMort.getValue());
		}

		if (nat.getData().isEmpty() && mort.getData().isEmpty()) {
			model = null;
		} else {
			model.addSeries(nat);
			model.addSeries(mort);
		}
		return model;
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

	public Rebanho getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(Rebanho rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public ResumoDadosZootecRebanho getResumoDadosZootecRebanho() {
		return resumoDadosZootecRebanho;
	}

	public void setResumoDadosZootecRebanho(ResumoDadosZootecRebanho resumoDadosZootecRebanho) {
		this.resumoDadosZootecRebanho = resumoDadosZootecRebanho;
	}

	public PieChartModel getPieModelDistri() {
		return pieModelDistri;
	}

	public void setPieModelDistri(PieChartModel pieModelDistri) {
		this.pieModelDistri = pieModelDistri;
	}

	public LineChartModel getCurvaPesos() {
		return curvaPesos;
	}

	public void setCurvaPesos(LineChartModel curvaPesos) {
		this.curvaPesos = curvaPesos;
	}

	public BarChartModel getMortNat() {
		return mortNat;
	}

	public void setMortNat(BarChartModel mortNat) {
		this.mortNat = mortNat;
	}

	public PieChartModel getPieMovAnimal() {
		return pieMovAnimal;
	}

	public void setPieMovAnimal(PieChartModel pieMovAnimal) {
		this.pieMovAnimal = pieMovAnimal;
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Rebanho selecionado", ((Rebanho) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}	 

}
