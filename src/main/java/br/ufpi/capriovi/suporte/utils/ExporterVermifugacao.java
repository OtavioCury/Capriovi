package br.ufpi.capriovi.suporte.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.entidades.controleAnimal.Verminose;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
import br.ufpi.capriovi.suporte.Fuzzy;

@Named(value = "exportarVermifugacaoMB")
@SessionScoped
public class ExporterVermifugacao extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String nomeArquivo = "C:\\Users\\Otávio Cury\\Downloads\\Fuzzy.xls";

	@Inject
	AnimalFacade animalFacade;

	@Inject
	RebanhoMB rebanhoMB;

	Fuzzy fuzzy;

	private List<Animal> animais;

	public void ler() throws IOException{

		animais = new ArrayList<Animal>();	
		fuzzy = new Fuzzy();

		try{

			FileInputStream arquivo = new FileInputStream(new File(nomeArquivo));
			HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
			HSSFSheet sheetAnimais = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheetAnimais.iterator();
			Row row = rowIterator.next();
			int quantidade = 0;

			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				Animal animal = new Animal();
				Verminose verminose = new Verminose();
				DesenvolvimentoPonderal desenvolvimentoPonderal = new DesenvolvimentoPonderal();
				Integer opg = 0, famacha = 0;
				Double escore = 0.0;
				boolean vazio = false;
				boolean presente = false;

				Date data = new Date();

				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					if (cell.getColumnIndex() == 1) {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {													

							if (!animais.isEmpty()) {
								for (Animal animalTeste : animais) {
									if (animalTeste.getNomeNumero().equals(String.valueOf(cell.getNumericCellValue()))) {
										animal = animalTeste;		
										presente = true;
									}
								}	
							}
							if (presente == false) {
								animal.setNomeNumero(String.valueOf(cell.getNumericCellValue()));
							}

						}else{
							vazio = true;
						}
					}

					if (cell.getColumnIndex() == 3) {
						if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
							if (DateUtil.isCellDateFormatted(cell)) {
								data = cell.getDateCellValue();								
							}
						}						
					}

					if (cell.getColumnIndex() == 4) {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							opg = (int) cell.getNumericCellValue();
							verminose.setOvosPorGramaDeFazes(opg);														
						}else{
							vazio = true;
						}
					}

					if (cell.getColumnIndex() == 5) {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							escore = cell.getNumericCellValue();
							desenvolvimentoPonderal.setEscoreCondicaoCorporal(escore);							
						}else{
							vazio = true;
						}

					}

					if (cell.getColumnIndex() == 6) {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							famacha = (int) cell.getNumericCellValue();
							verminose.setFamacha(famacha);							
						}else{
							vazio = true;
						}

					}

				}

				if (vazio == false) {

					desenvolvimentoPonderal.setData(data);
					desenvolvimentoPonderal.setAnimal(animal);
					verminose.setData(data);
					verminose.setAnimal(animal);																	

					animal.getDesenvolvimentoPonderal().add(desenvolvimentoPonderal);
					animal.getVerminose().add(verminose);

					fuzzy.fuzzyVermifuga(animal, famacha, escore, opg, data);

					if (presente == false) {
						quantidade++;
						animal.setRebanho(rebanhoMB.rebanhosFazendas().get(0));											
						animal.setRaca(null);
						animal.setMae(null);
						animal.setPai(null);

						animais.add(animal);							
					}												

				}

			}



			for (Animal animal : animais) {

				//animalFacade.adicionaAnimal(animal);
			}

			System.out.println();
			System.out.println(quantidade);

		}catch(FileNotFoundException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public static String getNomearquivo() {
		return nomeArquivo;
	}

}
