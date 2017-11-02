package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.suporte.relatorios.ResumoDadosZootecAnimal;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relDadosZootecAnimalMB")
@ViewScoped
public class RelDadosZootecAnimalMB extends BaseBeans {

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

	private List<Animal> animais;

	private Rebanho rebanhosMarcados;

	private Animal animalMarcados;

	private ResumoDadosZootecAnimal resumoDadosZootecAnimal;

	private PieChartModel pieModelDistri;

	private LineChartModel curvaPesos;

	private TreeNode gerAnt;

	private TreeNode gerPost;

	public String onFlowProcess(FlowEvent event) {
		if (event.getNewStep().equals("relatorio")) {
			if (this.validaRelatorioAnimal()) {
				this.result();
				createCurvaPesosGraf();
				gerConhecidasAnt();
				gerConhecidasPos();
				return event.getNewStep();
			} else {
				return "escolheAnimal";
			}
		} else {
			if (this.validaRelatorio()) {
				if (this.escolheAnimal()) {
					return event.getNewStep();
				}
			} else {
				return "escolheRebanho";
			}
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

	public boolean validaRelatorioAnimal() {
		try {
			CaprioviValidations.animalSelecionadoSingleVal(animalMarcados);
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
		this.resumoDadosZootecAnimal = relProducaoFacade.relDadosZootecAnimal(animalMarcados, animais);
	}

	public boolean escolheAnimal() {
		try {
			this.animais = relProducaoFacade.listAllAnimal(rebanhosMarcados);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return false;
		}
		return true;
	}

	private void createCurvaPesosGraf() {
		curvaPesos = initLinearCurvaPesosGraf();
		if (curvaPesos != null) {
			curvaPesos.setTitle("Curva de Crescimento Animal");
			curvaPesos.setLegendPosition("e");
			curvaPesos.setAnimate(true);
			curvaPesos.setZoom(true);
			curvaPesos.setShowPointLabels(true);
			Axis yAxis = curvaPesos.getAxis(AxisType.Y);
			Axis xAxis = curvaPesos.getAxis(AxisType.X);

			yAxis.setLabel("Pesos (Kg)");
			xAxis.setLabel("Idade (dias)");

			// xAxis.setMin(-50);
			yAxis.setMin(0);
			yAxis.setMax(this.resumoDadosZootecAnimal.getLimiteCurva() + 20);
		}
	}

	private LineChartModel initLinearCurvaPesosGraf() {
		LineChartModel model = null;
		if (this.animalMarcados.getDesenvolvimentoPonderal() != null) {
			model = new LineChartModel();
			LineChartSeries series1 = new LineChartSeries();
			series1.setLabel("Pesos Cadastrados");
			for (DesenvolvimentoPonderal dp : this.animalMarcados.getDesenvolvimentoPonderal()) {
				if (dp.getPeso() != null) {
					series1.set(diferencaDatas(this.animalMarcados.getNascimento(), dp.getData()), dp.getPeso());
				}
			}
			LineChartSeries series2 = new LineChartSeries();
			series2.setLabel("Pesos Ajustados");
			if (this.resumoDadosZootecAnimal.getPeso60() > 0) {
				series2.set(60, resumoDadosZootecAnimal.getPeso60());
			}
			if (this.resumoDadosZootecAnimal.getPeso120() > 0) {
				series2.set(120, resumoDadosZootecAnimal.getPeso120());
			}
			if (this.resumoDadosZootecAnimal.getPeso180() > 0) {
				series2.set(180, resumoDadosZootecAnimal.getPeso180());
			}
			if (series1.getData().isEmpty() && series2.getData().isEmpty()) {
				model = null;
			} else {
				model.addSeries(series1);
				model.addSeries(series2);
			}
		}
		return model;
	}

	public void gerConhecidasAnt() {

		gerAnt = new DefaultTreeNode(animalMarcados.getNomeNumero(), null);
		gerAnt.setSelected(true);
		TreeNode nodePai, nodeMae;
		Queue<TreeNode> list = new LinkedList<TreeNode>();
		list.add(gerAnt);
		ArrayList<String> genealogia = resumoDadosZootecAnimal.getGenealogia();
		for (int i = 0; i < genealogia.size(); i += 2) {
			if(genealogia.get(i) != null){
				nodePai = new DefaultTreeNode(genealogia.get(i), list.element());
				list.add(nodePai);
			}
			if(genealogia.get(i+1) != null){
				nodeMae = new DefaultTreeNode(genealogia.get(i+1), list.element());
				list.add(nodeMae);
			}
			list.remove();
		}


	}

	public void gerConhecidasPos() {
		gerPost = new DefaultTreeNode(animalMarcados.getNomeNumero(), null);
		gerPost.setSelected(true);
		TreeNode nodeFilho;

		Queue<TreeNode> list = new LinkedList<TreeNode>();
		list.add(gerPost);
		HashMap<String, ArrayList<Animal>> descendentes = resumoDadosZootecAnimal.getDescendentes();
		ArrayList<String> ordemFilhos = resumoDadosZootecAnimal.getOrdemFilhos();
		ArrayList<Animal> filhos;
		for (String nomePai : ordemFilhos) {
			filhos = descendentes.get(nomePai);
			for (Animal animal : filhos) {
				nodeFilho = new DefaultTreeNode("Filho: "+animal.getNomeNumero(), list.element());
				list.add(nodeFilho); 
			}
			list.remove();
		}

	}

	public int diferencaDatas(Date dataFinal, Date dataInicial) {
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		long diferencaEmDias = ((diferenca / 1000) / 60 / 60 / 24);
		return (int) (diferencaEmDias >= 0 ? diferencaEmDias : -diferencaEmDias);
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

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public Animal getAnimalMarcados() {
		return animalMarcados;
	}

	public void setAnimalMarcados(Animal animalMarcados) {
		this.animalMarcados = animalMarcados;
	}

	public ResumoDadosZootecAnimal getResumoDadosZootecAnimal() {
		return resumoDadosZootecAnimal;
	}

	public void setResumoDadosZootecAnimal(ResumoDadosZootecAnimal resumoDadosZootecAnimal) {
		this.resumoDadosZootecAnimal = resumoDadosZootecAnimal;
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

	public TreeNode getGerAnt() {
		return gerAnt;
	}

	public void setGerAnt(TreeNode gerAnt) {
		this.gerAnt = gerAnt;
	}

	public TreeNode getGerPost() {
		return gerPost;
	}

	public void setGerPost(TreeNode gerPost) {
		this.gerPost = gerPost;
	}

	public void onRowSelectRebanho(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Rebanho selecionado", ((Rebanho) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}	 

	public void onRowSelectAnimal(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Animal selecionado", ((Animal) event.getObject()).getNomeNumero());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}	 

}
