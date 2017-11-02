package br.ufpi.capriovi.suporte.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.dao.cadastros.RebanhoDAO;
import br.ufpi.capriovi.dao.controleAnimal.ManejoReprodutivoDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.suporte.tiposEnum.TipoDesenvolvimentoPonderal;
import br.ufpi.capriovi.suporte.tiposEnum.TipoPartoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoSexoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoStatusEnum;

@Stateless
public class ExporterFacade implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3612561466480722559L;

	private static final String fileName = "C:\\Users\\Thasciano\\Dropbox\\1 - Capriovi\\Indice Mestrado Laylsson\\Pasta1.xls";

	int contTuplas = 0, contVivos = 0, contMachos = 0, contAnimais = 0;

	@Inject
	private AnimalDAO animalDAO;

	@Inject
	private RebanhoDAO reb;

	@Inject
	private ManejoReprodutivoDAO manejoRepDAO;

	public void ler() throws IOException {

		HashMap<String, Animal> listaAnimais = new HashMap<String, Animal>();
		Long ids = 563L;
		Rebanho r = reb.find(006);
		ArrayList<ManejoReprodutivo> listManejo = new ArrayList<ManejoReprodutivo>();

		try {
			FileInputStream arquivo = new FileInputStream(new File(ExporterFacade.fileName));
			HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
			HSSFSheet sheetAnimais = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheetAnimais.iterator();
			Row row = rowIterator.next();// pula linha de nomes

			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				Animal animal = new Animal();

				DesenvolvimentoPonderal desenPond = new DesenvolvimentoPonderal();
				ArrayList<DesenvolvimentoPonderal> listDesPond = new ArrayList<DesenvolvimentoPonderal>();

				ManejoReprodutivo manejo = new ManejoReprodutivo();


				int flagDesenPond = 0, flagTamanhoCorp = 0, flagManejo = 0, salvar = 0;


				/**
				 * de 0 a 5: caracteristicas geral animal. de 6 a 9:
				 * caracteristicas de desenvolvimento ponderal. de 10 a 14:
				 * caracteristicas de tamnho corporal. Com o porem do 13 que não
				 * temos cadastro de perimetro toracico. de 15 a 32:
				 * caracteristicas de manejo reprodutivo.
				 */
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:// nome
						animal.setNomeNumero(""+ (int) cell.getNumericCellValue());
						//se não contem o nome do animal
						if( !listaAnimais.containsKey(animal.getNomeNumero()) ){
							flagManejo = 0;
							salvar++;
							animal.setId(ids);
						}else{
							listDesPond = (ArrayList<DesenvolvimentoPonderal>) listaAnimais.get(animal.getNomeNumero()).getDesenvolvimentoPonderal();
							animal.setId(listaAnimais.get(animal.getNomeNumero()).getId());
							flagManejo = buscaAnimalManejo(animal, listManejo);
						}
						break;
					case 1:// nome pai
						//animal.setNomePai(""+ (int) cell.getNumericCellValue());
						//						if(listaAnimais.containsKey(animal.getNomePai()) && salvar>0){
						//							animal.setPai(listaAnimais.get(animal.getNomePai()));
						//						}
						break;
					case 2:// nome mae
						//animal.setNomeMae(""+ (int) cell.getNumericCellValue());
						//						if(listaAnimais.containsKey(animal.getNomeMae()) && salvar>0){
						//							animal.setMae( listaAnimais.get(animal.getNomeMae()) );
						//						}
						break;
					case 3:// sexo
						if (cell.getStringCellValue().equals("F")) {
							animal.setSexo(TipoSexoEnum.SEXO_FEMEA);
						} else {
							animal.setSexo(TipoSexoEnum.SEXO_MACHO);
						}
						break;
					case 4:// tipo de parto em que nasceu
						if (cell.getStringCellValue() == "S") {
							animal.setParto(TipoPartoEnum.PARTO_SIMPLES);
						} else if (cell.getStringCellValue() == "D") {
							animal.setParto(TipoPartoEnum.PARTO_DUPLO);
						} else if (cell.getStringCellValue() == "T") {
							animal.setParto(TipoPartoEnum.PARTO_TRIPLO);
						} else if (cell.getStringCellValue() == "Q") {
							animal.setParto(TipoPartoEnum.PARTO_QUADRUPLO);
						}
						break;
					case 5:// nascimento
						animal.setNascimento(cell.getDateCellValue());
						animal.setStatus(TipoStatusEnum.STATUS_ATIVO);
						break;
					case 6:// dias de pesagem
						if (cell.getNumericCellValue() >= 0 && cell.getNumericCellValue() <= 3) {
							desenPond.setTipoDesenvolvimento(TipoDesenvolvimentoPonderal.TIPO_AONASCER);
						} else if (cell.getNumericCellValue() >= 120 && cell.getNumericCellValue() <= 130) {
							desenPond.setTipoDesenvolvimento(TipoDesenvolvimentoPonderal.TIPO_AODESMAME);
						} else {
							desenPond.setTipoDesenvolvimento(TipoDesenvolvimentoPonderal.TIPO_OUTROS);
						}
						desenPond.setAnimal(animal);
						if(cell.getNumericCellValue()>0){
							flagDesenPond++;
						}
						break;
					case 7:// data da mensuração do peso
						desenPond.setData(cell.getDateCellValue());
						if(desenPond.getTipoDesenvolvimento().getCodigo() == 2){
							//					desenPond.getAnimal().setDesmame(cell.getDateCellValue());
						}
						break;
					case 8:// P120
						animal.setPeso120(cell.getNumericCellValue());

						break;
					case 9:// IPP
						animal.setIpp(cell.getNumericCellValue());
						break;
					case 10://PTCN
						animal.setPtcn(cell.getNumericCellValue());
						break;
					case 11://PTCD
						animal.setPtcd(cell.getNumericCellValue());
						break;
					case 12:// Grupo de comtemporaneos
						animal.setGrupo(""+ cell.getStringCellValue());
						break;

					}
				} // fim colunas
				if(flagDesenPond > 0){
					listDesPond.add(desenPond);
				}


				if(salvar > 0 ){					

					animal.setRebanho(r);
					ids++;

					animal.setDesenvolvimentoPonderal(listDesPond);
					animal.setManejoReprodutivo(listManejo);

					listaAnimais.put(animal.getNomeNumero(), animal);
				}
				contTuplas++;
				if (contTuplas % 1000 == 0) {
					System.out.println("contador de tuplas do Excel: " + contTuplas);
				}
			}//fim while
			arquivo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo Excel não encontrado!");
		}

		if (listaAnimais.size() == 0) {
			System.out.println("Nenhum animal encontrado!");
		} else {
			contabilizaAnimais(listaAnimais);
			System.out.println("Quant. de linhas: " + contTuplas);
			System.out.println("Quant. de Animais: " + listaAnimais.size());
			System.out.println("Quant. de Animais Vivos : " + contVivos);
			System.out.println("Quant. de Animais mortos:" + (listaAnimais.size() - contVivos));
			System.out.println("Quant. de Machos : " + contMachos);
			System.out.println("Quant. de Femeas:" + (contVivos - contMachos));



			save(listaAnimais, listManejo);

		}
	}

	private static int procuraAnimalNascidosMesmaData(Date nasc, HashMap<String, Animal> listaAnimais){
		Set<String> chaves = listaAnimais.keySet();
		int cont = 0;
		for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();)
		{
			String chave = iterator.next();
			if(listaAnimais.get(chave).getNascimento().equals(nasc)){
				cont++;
			}
		}
		return cont;

	}

	private int contabilizaAnimais(HashMap<String, Animal> listaAnimais){
		Set<String> chaves = listaAnimais.keySet();
		int cont = 0;
		for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();)
		{
			String chave = iterator.next();
			if(listaAnimais.get(chave).getStatus().getCodigo() == 1){
				contVivos++;
			}
			if((listaAnimais.get(chave).getSexo().getCodigo() == 1) && (listaAnimais.get(chave).getStatus().getCodigo() == 1) ){
				contMachos++;
			}
		}
		return cont;
	}

	private Double desmame(List<DesenvolvimentoPonderal> desPon ){

		for (DesenvolvimentoPonderal desenvolvimentoPonderal : desPon) {
			if(desenvolvimentoPonderal.getTipoDesenvolvimento().getCodigo() == 1){
				return desenvolvimentoPonderal.getPeso();
			}
		}
		return 0.0;
	}

	private int buscaAnimalManejo(Animal a, ArrayList<ManejoReprodutivo> listManejo ){

		for (ManejoReprodutivo manejoReprodutivo : listManejo) {
			if(a.getNomeNumero() == manejoReprodutivo.getMatriz().getNomeNumero() || 
					a.getNomeNumero()== manejoReprodutivo.getReprodutor().getNomeNumero()){
				return 1;
			}
		}
		return 0;
	}

	private void save(HashMap<String, Animal> listaAnimais, ArrayList<ManejoReprodutivo> listManejo){
		ArrayList<Animal> animais = new ArrayList<Animal>();
		Set<String> chaves = listaAnimais.keySet();
		for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();)
		{
			String chave = iterator.next();
			animais.add(listaAnimais.get(chave));
		}

		Collections.sort(animais, new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return Double.valueOf(o1.getId()).compareTo(Double.valueOf(o2.getId()));
			}
		});

		for (Animal animal : animais) {
			List<DesenvolvimentoPonderal> desP = animal.getDesenvolvimentoPonderal();
			Collections.sort(desP, new Comparator<DesenvolvimentoPonderal>() {
				@Override
				public int compare(DesenvolvimentoPonderal o1, DesenvolvimentoPonderal o2) {
					return o1.getData().compareTo(o2.getData());
				}
			});
		}
		/*
		HashMap<String, ManejoReprodutivo> hashManejo = new HashMap<String, ManejoReprodutivo>(); 
		listManejo = new ArrayList<ManejoReprodutivo>();
		ManejoReprodutivo mr;
		String s;
		for (Animal animal : animais) {
			s = animal.getNomeMae() + animal.getNomePai() + animal.getNascimento();
			mr = new ManejoReprodutivo();
			if(!hashManejo.containsKey(s)){
				if(listaAnimais.containsKey(animal.getNomePai())){
					mr.setReprodutor(listaAnimais.get(animal.getPai().getNomeNumero()));
					mr.setDataParto(animal.getNascimento());
					mr.setPesoTotal(desmame(animal.getDesenvolvimentoPonderal()));
					//					mr.setStatus(TipoStatusControle.STATUS_INATIVO);
					if(animal.getSexo().getCodigo() == 1){
						mr.setCriasMacho(1);
					}else{
						mr.setCriasFemea(1);
					}
					mr.setRebanho(animal.getRebanho());
					hashManejo.put(s, mr);
				}
				if(listaAnimais.containsKey(animal.getNomeMae() )){
					mr.setMatriz(listaAnimais.get(animal.getMae().getNomeNumero()));
					mr.setDataParto(animal.getNascimento());
					mr.setPesoTotal(desmame(animal.getDesenvolvimentoPonderal()));
					//					mr.setStatus(TipoStatusControle.STATUS_INATIVO);
					if(animal.getSexo().getCodigo() == 1){
						mr.setCriasMacho(1);
					}else{
						mr.setCriasFemea(1);
					}
					mr.setRebanho(animal.getRebanho());
					hashManejo.put(s, mr);
				}

			}
		}
		chaves = hashManejo.keySet();
		for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();)
		{
			String chave = iterator.next();

			listManejo.add(hashManejo.get(chave));

		}

		Collections.sort(listManejo, new Comparator<ManejoReprodutivo>() {
			@Override
			public int compare(ManejoReprodutivo o1, ManejoReprodutivo o2) {
				return o1.getDataParto().compareTo(o2.getDataParto());
			}
		});

		System.out.println(listManejo.size());
		 */
		for (int i = 0; i < animais.size(); i++) {
			animalDAO.save(animais.get(i));
			//manejoRepDAO.save(listManejo.get(i));
		}
		/*
		for (int i = 0; i < listManejo.size(); i++) {
			//animalDAO.save(animais.get(i));
			manejoRepDAO.save(listManejo.get(i));
		}
		 */
	}

}