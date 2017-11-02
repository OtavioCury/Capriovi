package br.ufpi.capriovi.facade.relatorios;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.dao.cadastros.RebanhoDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.entidades.controleAnimal.TamanhoCorporal;

@Stateless
public class GenericRelFacade implements Serializable{

	@Inject
	private RebanhoDAO rebanhoDAO;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5280303212275824089L;

	public static double round(double value, int scale) {
		BigDecimal bd1 = new BigDecimal(value);
		BigDecimal bd2 = bd1.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bd2.doubleValue();
	}
	
	/**
	 * 
	 * @param Animal
	 */
	public List<Long> getListIdsRebanho(List<Rebanho> rebanhos){
		List<Long> aux = new ArrayList<Long>();
		for (Rebanho r : rebanhos) {
			aux.add(r.getId());
		}
		return aux;
	}
	
	/**
	 * 
	 * @param Animal
	 */
	public List<Rebanho> getListRebanhosByFazenda(Long idFazenda){
		return rebanhoDAO.rebanhoFazenda(idFazenda);
	}
	
	/**
	 * 
	 * @return A quantidade de Animais cadastrados.
	 */
	public int countTotal() {
		return GenericDAO.getCountTable();
	}

	public int indiceUltimaData(List<DesenvolvimentoPonderal> desPond) {
		int aux = 0;
		Date lastDate = null;
		for (int i = 0; i < desPond.size(); i++) {
			if((desPond.get(i).getCpm() > 0) ){
				if(lastDate == null){
					lastDate = desPond.get(i).getData();
					aux = i;
				}else if (lastDate.before(desPond.get(i).getData())) {
					lastDate = desPond.get(i).getData();
					aux = i;
				}

			}
		}
		return aux;
	}
	
	public ArrayList<Animal> ordenaIndice(ArrayList<Animal> list){
		Collections.sort(list, new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o2.getIndice().compareTo(o1.getIndice());
			}
		});
		return list;
	}
	
	public List<ManejoReprodutivo> ordenaDataManejos(List<ManejoReprodutivo> list){
		Collections.sort(list, new Comparator<ManejoReprodutivo>() {
			@Override
			public int compare(ManejoReprodutivo o1, ManejoReprodutivo o2) {
				return o1.getDataParto().compareTo(o2.getDataParto());
			}
		});
		return list;
	}
	
	public List<TamanhoCorporal> ordenaDataTamanhoCorporal(List<TamanhoCorporal> list){
		Collections.sort(list, new Comparator<TamanhoCorporal>() {
			@Override
			public int compare(TamanhoCorporal o1, TamanhoCorporal o2) {
				return o1.getData().compareTo(o2.getData());
			}
		});
		return list;
	}
	
	public ArrayList<Animal> ordenaPesoAjustado(ArrayList<Animal> list){
		Collections.sort(list, new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o1.getPesoAjustado().compareTo(o2.getPesoAjustado());
			}
		});
		return list;
	}
	
	public ArrayList<Animal> ordenaNota(ArrayList<Animal> list){
		Collections.sort(list, new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o2.getNotaCasalIndice().compareTo(o1.getNotaCasalIndice());
			}
		});
		return list;
	}
}
