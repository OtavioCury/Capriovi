package br.ufpi.capriovi.suporte.validators;

import java.util.Date;
import java.util.List;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;

public class CaprioviValidations {
	
	
	public CaprioviValidations() {
		super();
	}
	
	/**
	 * Verifica se foi selecionado algum item da lista.
	 * 
	 * @param rebanhoDT
	 * @return
	 * @throws MensagensExceptions
	 */
	public static boolean rebanhoSelecionadoVal(List<Rebanho> rebanhoDT) throws MensagensExceptions {
		if(rebanhoDT == null || rebanhoDT.isEmpty()){
			throw new MensagensExceptions(MensagensExceptions.RebanhoNaoSelecionadoException);
		}
		return true;
	}
	
	/**
	 * Verifica se foi selecionado algum item da lista.
	 * 
	 * @param rebanhoDT
	 * @return
	 * @throws MensagensExceptions
	 */
	public static boolean rebanhoSelecionadoSingleVal(Rebanho rebanhoDT) throws MensagensExceptions {
		if(rebanhoDT == null){
			throw new MensagensExceptions(MensagensExceptions.RebanhoNaoSelecionadoException);
		}
		return true;
	}
	/**
	 * Verifica se foi selecionado algum item da lista.
	 * 
	 * @param rebanhoDT
	 * @return
	 * @throws MensagensExceptions
	 */
	public static boolean animalSelecionadoSingleVal(Animal animalDT) throws MensagensExceptions {
		if(animalDT == null){
			throw new MensagensExceptions(MensagensExceptions.AnimalNaoSelecionadoException);
		}
		return true;
	}
	/**
	 * Verifica se foi selecionado algum item da lista.
	 * 
	 * @param rebanhoDT
	 * @return
	 * @throws MensagensExceptions
	 */
	public static boolean animalNaoSelecionado(List<Animal> machos, List<Animal> femeas) throws MensagensExceptions {
		if(machos.size() == 0 && femeas.size() == 0 ){
			throw new MensagensExceptions(MensagensExceptions.AnimalNaoSelecionadoException);
		}else{
			if(machos.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.MachosNaoSelecionadoException);
			}
			if(femeas.size() == 0){
				throw new MensagensExceptions(MensagensExceptions.FemeasNaoSelecionadoException);
			}
		}
		return true;
	}
	
	/**
	 * Verifica se as datas de um intervalo obedecem as restrições.
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 * @throws MensagensExceptions
	 */
	public static boolean dataInicioDataFimVal(Date inicio, Date fim) throws MensagensExceptions {
		if(inicio != null || fim != null){
			if (inicio != null && inicio.after(new Date())){
				throw new MensagensExceptions(MensagensExceptions.DataInicioMaiorDataAtualException);
			} else if (fim != null && fim.after(new Date())){
				throw new MensagensExceptions(MensagensExceptions.DataFimMaiorDataAtualException);
			} else if ((inicio != null && fim != null) && inicio.after(fim)){
				throw new MensagensExceptions(MensagensExceptions.DataInicioMaiorDataFinalException);
			}	
		}
		return true;
	}

}
