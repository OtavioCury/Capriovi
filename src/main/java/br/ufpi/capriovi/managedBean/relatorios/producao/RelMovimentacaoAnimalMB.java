package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.PieChartModel;

import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.tiposEnum.TipoMotivoSaidaEnum;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relMovimentacaoAnimalMB")
@ViewScoped
public class RelMovimentacaoAnimalMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1705376503571742810L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private RelProducaoFacade relProducaoFacade;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhosMarcados;

	private String motivoSaida;

	private Date inicio;

	private Date fim;

	private List<MovimentacaoAnimal> listagemMovimentacao;

	private List<String> list;

	private List<String> listTipos;

	private PieChartModel movimentacaoAnimal;

	private List<Integer> anos;

	@PostConstruct
	public void init() {
		list = TipoMotivoSaidaEnum.getList();	
		constroeListTipo();
	}

	public String onFlowProcess(FlowEvent event) {
		if (this.validaRelatorio()) {
			this.result();
			return event.getNewStep();
		}
		return "escolheRebanho";

	}

	public void constroeListTipo(){
		listTipos = new ArrayList<String>();
		for (int i = 1; i < list.size(); i++) {
			listTipos.add(list.get(i));
		}
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
	 * carrega os dados da tabela.
	 */
	public void result() {
		listagemMovimentacao = new ArrayList<MovimentacaoAnimal>();
		listagemMovimentacao = relProducaoFacade.relMovimentacaoAnimal(rebanhosMarcados,
				TipoMotivoSaidaEnum.getCodigoByDesc(motivoSaida), inicio, fim);
		criaGrafico();		

	}

	/**
	 * Cria gráfioo de pizza
	 */
	private void criaGrafico() {
		// TODO Auto-generated method stub	
		anos = new ArrayList<Integer>();
		movimentacaoAnimal = new PieChartModel();
		int venda = 0, morte = 0, roubo = 0, alimentacao = 0, emprestimo = 0, outros = 0;

		for (MovimentacaoAnimal movimentacao : listagemMovimentacao) {		
			Calendar cal = Calendar.getInstance();
			cal.setTime(movimentacao.getData());	
			if (contemAno(cal.get(Calendar.YEAR)) == false) {
				anos.add(cal.get(Calendar.YEAR));
			}
			if (movimentacao.getMotivoSaida().getCodigo() == 1) {
				venda++;
				movimentacaoAnimal.set("Venda", venda);
			}else if (movimentacao.getMotivoSaida().getCodigo() == 2) {
				morte++;
				movimentacaoAnimal.set("Morte", morte);
			}else if (movimentacao.getMotivoSaida().getCodigo() == 3) {
				roubo++;
				movimentacaoAnimal.set("Roubo", roubo);
			}else if (movimentacao.getMotivoSaida().getCodigo() == 4) {
				alimentacao++;
				movimentacaoAnimal.set("Alimentação", alimentacao);
			}else if (movimentacao.getMotivoSaida().getCodigo() == 5){
				emprestimo++;
				movimentacaoAnimal.set("Emprestimo", emprestimo);
			}else{
				outros++;
				movimentacaoAnimal.set("Outros", outros);
			}
		}

		movimentacaoAnimal.setTitle("Gráfico Movimentação Animal");
		movimentacaoAnimal.setLegendPosition("w");
		movimentacaoAnimal.setShowDataLabels(true);

		ordenaArray();

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
			this.rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
		}
		return rebanhos;
	}


	public List<Integer> getAnos() {
		return anos;
	}

	public void setAnos(List<Integer> anos) {
		this.anos = anos;
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

	public String getMotivoSaida() {
		return motivoSaida;
	}

	public void setMotivoSaida(String motivoSaida) {
		this.motivoSaida = motivoSaida;
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

	public List<MovimentacaoAnimal> getListagemMovimentacao() {
		return listagemMovimentacao;
	}

	public void setListagemMovimentacao(List<MovimentacaoAnimal> listagemMovimentacao) {
		this.listagemMovimentacao = listagemMovimentacao;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public PieChartModel getMovimentacaoAnimal() {
		return movimentacaoAnimal;
	}

	public void setMovimentacaoAnimal(PieChartModel movimentacaoAnimal) {
		this.movimentacaoAnimal = movimentacaoAnimal;
	}

	/**
	 * Retorna a quantidade de um tipo de movimento em um ano
	 * @param tipo
	 * @param ano
	 * @return
	 */
	public int movimentosAno(String tipo, int ano){
		int quant = 0;
		for (MovimentacaoAnimal movimentacao : listagemMovimentacao) {	
			Calendar cal = Calendar.getInstance();
			cal.setTime(movimentacao.getData());				
			if (movimentacao.getMotivoSaida().getDescricao().equals(tipo) && cal.get(Calendar.YEAR) == ano) {
				quant++;			
			}
		}		

		return quant;
	}

	public List<String> getListTipos() {
		return listTipos;
	}

	public void setListTipos(List<String> listTipos) {
		this.listTipos = listTipos;
	}

}