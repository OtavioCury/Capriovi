package br.ufpi.capriovi.managedBean.relatorios.melhoramentoGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ejml.simple.SimpleMatrix;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.Endogamia;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.tiposEnum.TipoSexoEnum;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relAvaliacaoGeneticaMB")
@ViewScoped
public class RelAvaliacaoGeneticaMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SystemSessionMB systemSessionMB;
	@Inject
	private RebanhoFacade rebanhoFacade;
	@Inject
	private AnimalFacade animalFacade;

	private List<Rebanho> rebanhosMarcados;
	private List<Animal> animais;

	private DualListModel<Animal> machos;
	private DualListModel<Animal> femeas;

	private Double[] resultadosValores;

	private HashMap<Animal, Animal> resultado;

	private SimpleMatrix matrizX, matrizY, matrizZ, matrizG; 

	//private SimpleMatrix matrizR;

	private int quantidadeGrupos;

	private double herdabilidade, lambida;

	private Endogamia end;

	public String onFlowProcess(FlowEvent event) {
		if (event.getNewStep().toString().equals("escolha_animais") && 
				event.getOldStep().toString().equals("escolheRebanho")) {
			if (validaRelatorio() ) {
				result();
				return event.getNewStep();
			}
		}else if (event.getNewStep().toString().equals("resultado")) {
			acasalamentos();
			return event.getNewStep();
		}	
		return event.getNewStep();
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

	public void result() {
		animais = animalFacade.animaisRebanhos(rebanhosMarcados);
		resultadosValores = new Double[animais.size()];
		Double peso = (double) 31;

		if (herdabilidade != 0) {
			lambida = (1-herdabilidade)/herdabilidade;
		}
		// a ser retirado
		Random gerador = new Random();
		for (Animal animal : animais) {
			animal.setPesoAjustado((double) gerador.nextInt(20));
			//peso++;
		}
		//
		preparaMatrizEndogamia();
		matrizZ();
		matrizY();
		matrixX();
		//matrizR();
		blup();
		inicializaMachosFemeas();
	}

	private void inicializaMachosFemeas() {
		//a ser alterado
		machos = new DualListModel<Animal>(retornaMachos(), new ArrayList<Animal>());
		femeas = new DualListModel<Animal>(retornaFemeas(), new ArrayList<Animal>());
	}

	/**
	 * Realiza a indicação dos melhores acasalamentos
	 */
	private void acasalamentos() {
		System.out.println("===================Machos======================");
		for (int i = 0; i < machos.getTarget().size(); i++) {
			System.out.println("Macho: "+i+" Valor genético: "
					+machos.getTarget().get(i).getGanhoGenetico()+" posição na G: "
					+machos.getTarget().get(i).getPosAtual());
		}
		System.out.println("===================Fêmeas======================");
		for (int i = 0; i < femeas.getTarget().size(); i++) {
			System.out.println("Fêmea: "+i+" Valor genético: "
					+femeas.getTarget().get(i).getGanhoGenetico()+" posição na G: "
					+femeas.getTarget().get(i).getPosAtual());
		}
		resultado = new HashMap<Animal, Animal>();
		int indexEndogamiaMinima;
		int indexValorGeneticoMaximo;
		int indexDistanciaMinima = 0;
		double endogamiaMinima;
		double valorGeneticoMaximo;
		double distaciaMinima;
		for (int i = 0; i < machos.getTarget().size(); i++) {
			System.out.println("=================Macho"+i+"========================");
			endogamiaMinima = 1000;
			valorGeneticoMaximo = -1000;
			double mediaValorGeneticoAux;
			double mediaEndogamiaAux;
			for (int j = 0; j < femeas.getTarget().size(); j++){
				mediaValorGeneticoAux = (machos.getTarget().get(i).getGanhoGenetico() + 
						femeas.getTarget().get(j).getGanhoGenetico())/2;
				mediaEndogamiaAux = matrizG.get(machos.getTarget().get(i).getPosAtual(), 
						femeas.getTarget().get(j).getPosAtual())/2;
				System.out.println("Macho "+i+" com fêmea "+j+" média valor genético "+mediaValorGeneticoAux+
						" média endogamia: "+mediaEndogamiaAux);
				if (mediaValorGeneticoAux > valorGeneticoMaximo) {
					valorGeneticoMaximo = mediaValorGeneticoAux;
					indexValorGeneticoMaximo = j;
				}
				if (mediaEndogamiaAux < endogamiaMinima) {
					endogamiaMinima = mediaEndogamiaAux;
					indexEndogamiaMinima = j;
				}
			}
			System.out.println("Média valor genético máximo :"+valorGeneticoMaximo);
			System.out.println("Média endogamia mínima :"+endogamiaMinima);
			distaciaMinima = 1000;
			double distanciaEndogamia;
			double distanciaValorGenetico;
			double distancia;
			for (int j = 0; j < femeas.getTarget().size(); j++){
				mediaValorGeneticoAux = (machos.getTarget().get(i).getGanhoGenetico() + 
						femeas.getTarget().get(j).getGanhoGenetico())/2;
				mediaEndogamiaAux = matrizG.get(machos.getTarget().get(i).getPosAtual(), 
						femeas.getTarget().get(j).getPosAtual())/2;
				if (mediaEndogamiaAux < 0.2) {
					distanciaEndogamia = Math.abs(mediaEndogamiaAux - endogamiaMinima);
					distanciaValorGenetico = Math.abs(valorGeneticoMaximo - mediaValorGeneticoAux);
					System.out.println("Distância valor genético máximo :"+distanciaValorGenetico+
							" Distância endogamia mínima: "+distanciaEndogamia+ " Macho "+i+" Fêmea "+j);
					distancia = distanciaEndogamia + distanciaValorGenetico; 
					System.out.println("Soma das distâncias: "+distancia);
					if (distancia < distaciaMinima) {
						distaciaMinima = distancia;
						indexDistanciaMinima = j;
					}
				}
			}
			System.out.println("Distância mínima: "+distaciaMinima+" macho"+i+" fêmea "+indexDistanciaMinima);
			resultado.put(machos.getTarget().get(i), femeas.getTarget().get(indexDistanciaMinima));
		}
	}

	private void matrizG() {
		ArrayList<Double[]> matrizA = null;
		double[][] matrizG = new double[animais.size()][animais.size()];
		try {
			matrizA = end.getAllMatrizA(animais.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		for (Double[] doubles : matrizA) {
			for (int j = 0; j < doubles.length; j++) {
				matrizG[i][j] = doubles[j];
			}
			i++;
		}
		System.out.println("=====================Matriz G============================");
		for (int j = 0; j < matrizG.length; j++) {
			for (int r = 0; r < matrizG.length; r++) {
				System.out.print(matrizG[j][r]+" ");
			}
			System.out.println();
		}
		this.matrizG = new SimpleMatrix(matrizG);
	}

	/**
	 * Calcula a matriz de endogamia
	 */
	private void preparaMatrizEndogamia() {
		end = new Endogamia();

		try {
			end.prepareInputFortran(animais);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread p = new Thread(end);
		p.start();
	}

	private void blup() {

		SimpleMatrix transpostaX = matrizX.transpose();
		SimpleMatrix transpostaZ = matrizZ.transpose();

		//MONTANDO A MATRIZ DO LADO ESQUERDO DA EQUAÇÃO
		//superior esquerdo
		System.out.println("==================Matriz superior esquerdo===============================");
		SimpleMatrix superiorEsquerda =transpostaX.mult(matrizX);
		superiorEsquerda.print();
		//

		//inferior esquerdo
		System.out.println("==================Matriz inferior esquerdo===============================");
		SimpleMatrix inferiorEsquerda = transpostaZ.mult(matrizX);
		inferiorEsquerda.print();
		//

		//superior direito
		System.out.println("==================Matriz superior direito===============================");
		SimpleMatrix superiorDireito = transpostaX.mult(matrizZ);
		superiorDireito.print();
		//	

		//inferior direito
		SimpleMatrix inferiorDireitoAux = transpostaZ.mult(matrizZ);
		matrizG();
		SimpleMatrix inversaG = matrizG.invert();
		System.out.println("==================Inversa da G===============================");
		inversaG.print();
		SimpleMatrix inversaGVezesLambida = inversaG.scale(lambida);
		System.out.println("==================Matriz G vezes escalar===============================");
		inversaGVezesLambida.print();
		SimpleMatrix inferiorDireito = inferiorDireitoAux.plus(inversaGVezesLambida);
		System.out.println("==================Matriz inferior direito===============================");
		inferiorDireito.print();

		//cobinando a superior esquerdo com a inferior esquerdo
		System.out.println("==================Matriz Total direita=================================");
		SimpleMatrix ladoDireito = superiorDireito.combine(SimpleMatrix.END, 0, inferiorDireito);
		ladoDireito.print();

		//cobinando a superior direito com a inferior direito
		System.out.println("==================Matriz Total esquerda=================================");
		SimpleMatrix ladoEsquerdo = superiorEsquerda.combine(SimpleMatrix.END, 0, inferiorEsquerda);
		ladoEsquerdo.print();

		//Matriz total
		System.out.println("==================Matriz Total esquerda=================================");
		SimpleMatrix matrizTotalEsquerda = ladoEsquerdo.combine(0, SimpleMatrix.END, ladoDireito);
		matrizTotalEsquerda.print();

		//Inversa da Matriz total esquerda
		System.out.println("==================Inversa Matriz Total esquerda=================================");
		SimpleMatrix matrizTotalEsquerdaInve = matrizTotalEsquerda.invert();
		matrizTotalEsquerdaInve.print();
		//
		//

		//MONTANDO A MATRIZ DO LADO DIREITO DA EQUAÇÃO
		System.out.println("==================Matriz Superior=================================");
		SimpleMatrix superior = transpostaX.mult(matrizY);
		superior.print();

		System.out.println("==================Matriz inferior=================================");
		SimpleMatrix inferior = transpostaZ.mult(matrizY);
		inferior.print();

		System.out.println("==================Matriz Total direita=================================");
		SimpleMatrix matrizTotalDireita = superior.combine(SimpleMatrix.END, 0, inferior);
		matrizTotalDireita.print();
		//

		//Resultado: multiplicando a inversa da esquerda pela direita
		System.out.println("=======================Resultado=====================================");
		SimpleMatrix resultado = matrizTotalEsquerdaInve.mult(matrizTotalDireita);
		resultado.print();

		//criando o vetor de valores genéticos
		resultadosValores = new Double[animais.size()];
		int diferenca = resultado.getNumElements() - animais.size();
		int j = 0;
		for (int i = 0; i < resultado.getNumElements(); i++) {
			if (i >= diferenca) {
				resultadosValores[j] = resultado.get(i);
				j++;
			}
		}
		for (int i = 0; i < animais.size(); i++) {
			animais.get(i).setGanhoGenetico(resultadosValores[i]);
		}
		System.out.println("=======================Vetor de valores genéticos animais=========================");
		for (Animal animal : animais) {
			System.out.println(animal.getGanhoGenetico());
		}
	}

	/**
	 * Cria a matriz R do calculo do BLUP
	 */
	//	private void matrizR() {
	//		// TODO Auto-generated method stub
	//		double [][] matrizR = new double[animais.size()][animais.size()];
	//		for (int i = 0; i < animais.size(); i++) {
	//			for (int j = 0; j < animais.size(); j++) {
	//				if (i == j) {
	//					matrizR[i][j] = varianciaCovariancia;
	//				}else{
	//					matrizR[i][j] = 0;
	//				}
	//			}
	//		}
	//
	//		System.out.println("============matriz r=======================");
	//		for (int i = 0; i < matrizR.length; i++) {
	//			for (int j = 0; j < matrizR[i].length; j++) {
	//				System.out.print(matrizR[i][j] + " ");
	//			}
	//			System.out.println();
	//		}
	//		System.out.println();
	//
	//		this.matrizR = new SimpleMatrix(matrizR);
	//	}

	/**
	 * Preenche os grupos contemporaneos dos animais
	 * @return
	 */
	public void gruposContemporaneos(){
		int grupo = 1;
		boolean presente;
		for (Animal animal : animais) {
			animal.setGrupoContemporaneo(-1);
		}
		for (Animal animal : animais) {
			presente = false;
			for (Animal animal2 : animais) {
				if (animal2.getTipoParto() != null && animal.getTipoParto() != null) {
					if (animal2.getRebanho().getId() == animal.getRebanho().getId()
							&& animal2.getSexo().equals(animal.getSexo())
							&& animal2.anoNascimento() == animal.anoNascimento()
							&& animal2.estacao().equals(animal.estacao())
							&& animal2.getTipoParto() == animal.getTipoParto()
							&& animal2.getId() != animal.getId()
							&& animal2.getGrupoContemporaneo() != -1) {
						animal.setGrupoContemporaneo(animal2.getGrupoContemporaneo());
						presente = true;
					}	
				}
			}
			if (presente == false) {
				animal.setGrupoContemporaneo(grupo);
				grupo++;
			}	
		}
		quantidadeGrupos = grupo;
	}

	/**
	 * Cria a matriz X do calculo do BLUP
	 */
	private void matrixX() {
		gruposContemporaneos();
		double [][] matrizX = new double[animais.size()][quantidadeGrupos];
		for (int i = 0; i < animais.size(); i++) {
			matrizX[i][0] = 1;
		}
		for (int j = 1; j < quantidadeGrupos; j++) {
			for (int r = 0; r < animais.size(); r++) {
				if (animais.get(r).getGrupoContemporaneo() == j) {
					matrizX[r][j] = 1;
				}else{
					matrizX[r][j] = 0;
				}
			}
		}
		System.out.println("============matriz x=======================");
		for (int i = 0; i < matrizX.length; i++) {
			for (int j = 0; j < matrizX[i].length; j++) {
				System.out.print(matrizX[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

		this.matrizX = new SimpleMatrix(matrizX);

	}

	/**
	 * Cria a matriz Y do calculo do BLUP
	 */
	private void matrizY() {
		double [][] matrizY = new double[animais.size()][1];
		for (int i = 0; i < matrizY.length; i++) {
			for (int j = 0; j < matrizY[i].length; j++) {
				//if (animais.get(i).ajuste(180) != null) {
				matrizY[i][j] = animais.get(i).getPesoAjustado();
				//}
			}
		}

		System.out.println("============matriz y=======================");
		for (int i = 0; i < matrizY.length; i++) {
			for (int j = 0; j < matrizY[i].length; j++) {
				System.out.print(matrizY[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

		this.matrizY = new SimpleMatrix(matrizY);
	}

	/**
	 * Cria a matriz Z do calculo do BLUP e seta a posição de cada animal
	 */
	private void matrizZ() {
		double [][] matrizZ = new double[animais.size()][animais.size()];
		for (int i = 0; i < animais.size(); i++) {
			animais.get(i).setPosAtual(i);
			for (int j = 0; j < animais.size(); j++) {
				if (i == j) {
					//					if (animais.get(i).ajuste(180) == null) {
					//						matrizZ[i][j] = 0;
					//					}else{
					matrizZ[i][j] = 1;	
					//}
				}else{
					matrizZ[i][j] = 0;
				}
			}
		}

		System.out.println("============matriz z=======================");
		for (int i = 0; i < matrizZ.length; i++) {
			for (int j = 0; j < matrizZ[i].length; j++) {
				System.out.print(matrizZ[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

		this.matrizZ = new SimpleMatrix(matrizZ);
	}

	public List<Rebanho> getRebanhos() {
		if (systemSessionMB.getFazenda() != null) {
			return rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
		}
		return new ArrayList<Rebanho>();
	}

	public List<Rebanho> getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(List<Rebanho> rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Rebanho selecionado", ((Rebanho) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}
	public int getQuantidadeGrupos() {
		return quantidadeGrupos;
	}

	public void setQuantidadeGrupos(int quantidadeGrupos) {
		this.quantidadeGrupos = quantidadeGrupos;
	}

	public SimpleMatrix getMatrizX() {
		return matrizX;
	}

	public void setMatrizX(SimpleMatrix matrizX) {
		this.matrizX = matrizX;
	}

	public SimpleMatrix getMatrizY() {
		return matrizY;
	}

	public void setMatrizY(SimpleMatrix matrizY) {
		this.matrizY = matrizY;
	}

	public SimpleMatrix getMatrizZ() {
		return matrizZ;
	}

	public void setMatrizZ(SimpleMatrix matrizZ) {
		this.matrizZ = matrizZ;
	}

	//	public SimpleMatrix getMatrizR() {
	//		return matrizR;
	//	}

	public Endogamia getEnd() {
		return end;
	}

	public void setEnd(Endogamia end) {
		this.end = end;
	}

	//	public void setMatrizR(SimpleMatrix matrizR) {
	//		this.matrizR = matrizR;
	//	}

	public SimpleMatrix getMatrizG() {
		return matrizG;
	}

	public void setMatrizG(SimpleMatrix matrizG) {
		this.matrizG = matrizG;
	}

	public double getHerdabilidade() {
		return herdabilidade;
	}

	public void setHerdabilidade(double herdabilidade) {
		this.herdabilidade = herdabilidade;
	}

	public double getLambida() {
		return lambida;
	}

	public void setLambida(double lambida) {
		this.lambida = lambida;
	}

	public Double[] getResultadosValores() {
		return resultadosValores;
	}

	public void setResultadosValores(Double[] resultadosValores) {
		this.resultadosValores = resultadosValores;
	}

	/**
	 * Retorna os machos do rebanhos ordenados pelo valor genético
	 * @return
	 */
	public List<Animal> retornaMachos(){
		List<Animal> aux = new ArrayList<Animal>();
		System.out.println("=================Machos======================");
		for (Animal animal : animais) {
			if (animal.getSexo().equals(TipoSexoEnum.getEnumByCodigo(1))) {
				aux.add(animal);
				System.out.println(animal.getGanhoGenetico());
			}
		}
		Collections.sort(aux, new Comparator<Animal>() {
			@Override
			public int compare(Animal animal, Animal animal2)
			{
				return  Double.compare(animal2.getGanhoGenetico(), animal.getGanhoGenetico());
			}
		});
		System.out.println("=================Machos ordenados======================");
		for (Animal animal : aux) {
			System.out.println(animal.getGanhoGenetico());
		}
		return aux;
	}

	/**
	 * Retorna as femeas do rebanhos ordenados pelo valor genético
	 * @return
	 */
	public List<Animal> retornaFemeas(){
		List<Animal> aux = new ArrayList<Animal>();
		System.out.println("=================Fêmas======================");
		for (Animal animal : animais) {
			if (animal.getSexo().equals(TipoSexoEnum.getEnumByCodigo(2))) {
				aux.add(animal);
				System.out.println(animal.getGanhoGenetico());
			}
		}
		Collections.sort(aux, new Comparator<Animal>() {
			@Override
			public int compare(Animal animal, Animal animal2)
			{
				return  Double.compare(animal2.getGanhoGenetico(), animal.getGanhoGenetico());
			}
		});
		System.out.println("=================Fêmeas ordenadas======================");
		for (Animal animal : aux) {
			System.out.println(animal.getGanhoGenetico());
		}
		return aux;
	}

	public List<Animal> retornaResultado(){
		List<Animal> filhos = new ArrayList<Animal>();
		Iterator<Entry<Animal, Animal>> it = resultado.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Animal, Animal> pair = (Map.Entry<Animal, Animal>)it.next();
			Animal animal = new Animal();
			animal.setEndogamia(matrizG.get(pair.getKey().getPosAtual(), pair.getValue().getPosAtual())/2);
			animal.setGanhoGenetico((pair.getKey().getGanhoGenetico()+pair.getValue().getGanhoGenetico())/2);
			animal.setPai(pair.getKey());
			animal.setMae(pair.getValue());
			filhos.add(animal);
			it.remove(); // avoids a ConcurrentModificationException
		}
		return filhos;
	}

	public DualListModel<Animal> getMachos() {
		return machos;
	}

	public void setMachos(DualListModel<Animal> machos) {
		this.machos = machos;
	}

	public DualListModel<Animal> getFemeas() {
		return femeas;
	}

	public void setFemeas(DualListModel<Animal> femeas) {
		this.femeas = femeas;
	}

	public HashMap<Animal, Animal> getResultado() {
		return resultado;
	}

	public void setResultado(HashMap<Animal, Animal> resultado) {
		this.resultado = resultado;
	}

}
