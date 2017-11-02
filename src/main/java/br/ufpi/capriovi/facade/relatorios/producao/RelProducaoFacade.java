package br.ufpi.capriovi.facade.relatorios.producao;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.dao.controleAnimal.ControleParasitaDAO;
import br.ufpi.capriovi.dao.controleAnimal.MovimentacaoAnimalDAO;
import br.ufpi.capriovi.dao.controleAnimal.OcorrenciaClinicaDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.Carcaca;
import br.ufpi.capriovi.entidades.controleAnimal.ControleParasita;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;
import br.ufpi.capriovi.entidades.controleAnimal.Verminose;
import br.ufpi.capriovi.entidades.relatorio.RelatorioVermifugação;
import br.ufpi.capriovi.facade.relatorios.GenericRelFacade;
import br.ufpi.capriovi.suporte.Fuzzy;
import br.ufpi.capriovi.suporte.relatorios.ResumoAniEstatistico;
import br.ufpi.capriovi.suporte.relatorios.ResumoCPM;
import br.ufpi.capriovi.suporte.relatorios.ResumoDadosZootecAnimal;
import br.ufpi.capriovi.suporte.relatorios.ResumoDadosZootecRebanho;
import br.ufpi.capriovi.suporte.utils.Data;


@Stateless
public class RelProducaoFacade extends GenericRelFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8659171450206776067L;

	@Inject
	private AnimalDAO animalDAO;

	@Inject
	private MovimentacaoAnimalDAO movimentacaoDAO;

	@Inject
	private ControleParasitaDAO controleParazitaDAO;

	@Inject
	private OcorrenciaClinicaDAO ocorrenciaClinicaDAO;

	/**
	 * Produz relatório de vermifugação.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 */
	public List<Animal> relVermifuga(List<Long> rebanhosId) {

		List<Animal> animals = animalDAO.animaisVivosRebanhos(rebanhosId);		
		List<Animal> animaisFuzzy = new ArrayList<Animal>();				

		Fuzzy fuzzy = new Fuzzy();

		if (!animals.isEmpty()) {
			for (int i = 0; i < animals.size(); i++) {

				animals.get(i).setListaNotas(animalDAO.relatorioVermifugaAnimal(animals.get(i).getId()));

				boolean possuiRegistroFamacha = false;
				boolean possuiRegistroOPG = false;
				boolean possuiRegistroEscore = false;

				if ((animals.get(i).getVerminose().size() > 0)
						&& (animals.get(i).getDesenvolvimentoPonderal().size() > 0)) {

					Verminose ultimaFamacha = new Verminose();
					Verminose ultimoOpg = new Verminose();
					DesenvolvimentoPonderal ultimoDesenvolvimento = new DesenvolvimentoPonderal();

					for (Verminose v : animals.get(i).getVerminose()) {
						if (v.getFamacha() != null) {
							if (v.getFamacha() > 0) {
								possuiRegistroFamacha = true;
								ultimaFamacha = v;
							}
						}
						if (v.getOvosPorGramaDeFazes() != null) {													
							possuiRegistroOPG = true;
							ultimoOpg = v;							
						}
					}

					for (DesenvolvimentoPonderal d : animals.get(i).getDesenvolvimentoPonderal()) {
						if (d.getEscoreCondicaoCorporal() != null) {
							if (d.getEscoreCondicaoCorporal() > 0) {
								possuiRegistroEscore = true;
								ultimoDesenvolvimento = d;
							}
						}
					}
					if (possuiRegistroOPG == true && possuiRegistroEscore == true && possuiRegistroFamacha == true) {

						for (Verminose verminose : animals.get(i).getVerminose()) {
							if (verminose != null) {

								if (verminose.getData().after(ultimaFamacha.getData()) && verminose.getFamacha() > 0) {
									ultimaFamacha = verminose;
								}

								if (verminose.getData().after(ultimoOpg.getData())
										&& verminose.getOvosPorGramaDeFazes() > 0) {
									ultimoOpg = verminose;
								}
							}
						}

						for (DesenvolvimentoPonderal desenvolvimentoPonderal : animals.get(i)
								.getDesenvolvimentoPonderal()) {
							if (desenvolvimentoPonderal != null) {

								if (desenvolvimentoPonderal.getData().after(ultimoDesenvolvimento.getData())
										&& desenvolvimentoPonderal.getEscoreCondicaoCorporal() > 0) {
									ultimoDesenvolvimento = desenvolvimentoPonderal;
								}

							}
						}

						fuzzy.fuzzyVermifuga(animals.get(i), ultimaFamacha.getFamacha(),
								ultimoDesenvolvimento.getEscoreCondicaoCorporal(), ultimoOpg.getOvosPorGramaDeFazes());

						Double soma = 0.0;						

						for (RelatorioVermifugação relatorio : animals.get(i).getListaNotas()) {
							soma = soma + relatorio.getNotaVermifugacao();							
						}						
						animals.get(i).setMediaVermifugacao(soma/animals.get(i).getListaNotas().size());						

						animaisFuzzy.add(animals.get(i));						

					}
				}

			}
		}					

		return animaisFuzzy;
	}

	/**
	 * Relatório animais por entrada.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<Animal> relAnimaisPorEntrada(List<Rebanho> idsReb, Date inicio, Date fim) {
		List<Animal> result = new ArrayList<Animal>(); 
		result = animalDAO.animaisPorEntrada(getListIdsRebanho(idsReb), inicio, fim);
		return result;
	}

	/**
	 * Relatório Movimentação animal.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<MovimentacaoAnimal> relMovimentacaoAnimal(List<Rebanho> rebanhos, Integer tipoMov, Date inicio,
			Date fim) {

		List<MovimentacaoAnimal> relMovimentacao = new ArrayList<MovimentacaoAnimal>();
		relMovimentacao = movimentacaoDAO.relMovimentacao(getIds(rebanhos), tipoMov, inicio, fim);
		return relMovimentacao;
	}

	/**
	 * Retorna uma lista e ids de uma lista de rebanhos
	 * @param rebanhos
	 * @return
	 */
	public List<Long> getIds(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();
		for (Rebanho rebanho : rebanhos) {
			ids.add(rebanho.getId());
		}
		return ids;
	}

	/**
	 * Relatório Movimentação animal.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<Animal> relNumeroDeCrias(List<Rebanho> idsReb, Date inicio, Date fim) {

		List<Animal> result = animalDAO.relNumeroDeCrias(getListIdsRebanho(idsReb), inicio, fim);

		return result;
	}

	/**
	 * Relatório CPM.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<ResumoCPM> relCpm(List<Rebanho> idsReb) {

		List<Animal> result = animalDAO.relCpm(getListIdsRebanho(idsReb));
		List<ResumoCPM> res = new ArrayList<ResumoCPM>();
		for (Animal animal : result) {
			int aux = indiceUltimaData(animal.getDesenvolvimentoPonderal());
			res.add(new ResumoCPM(animal, animal.getDesenvolvimentoPonderal().get(aux)));
		}
		return res;
	}

	/**
	 * Relatório Área Olho Lombo.
	 * 
	 * @param idsReb
	 * @return
	 */
	public List<ResumoAniEstatistico> relOlhoLombo(List<Rebanho> idsReb) {

		List<Animal> result = animalDAO.relCarcaca(getListIdsRebanho(idsReb));
		List<ResumoAniEstatistico> res = new ArrayList<ResumoAniEstatistico>();
		DescriptiveStatistics[] stats = new DescriptiveStatistics[4];
		Integer[] quant = new Integer[4];
		Date[] lastDates = new Date[4];
		Double[] totais = new Double[4];
		for (Animal animal : result) {
			for (int j = 0; j < stats.length; j++) {
				stats[j] = new DescriptiveStatistics();
				quant[j] = 0;
				lastDates[j] = null;
				totais[j] = null;
			}

			for (Carcaca carcaca : animal.getCarcaca()) {
				if (carcaca.getAreaDeOlhoLombo() != null) {
					stats[0].addValue(carcaca.getAreaDeOlhoLombo());
					quant[0]++;
					if (lastDates[0] == null) {
						lastDates[0] = carcaca.getData();
						totais[0] = carcaca.getAreaDeOlhoLombo();
					} else if (lastDates[0].before(carcaca.getData())) {
						lastDates[0] = carcaca.getData();
						totais[0] = carcaca.getAreaDeOlhoLombo();
					}
				}
				if (carcaca.getCompDeOlhoLombo() != null) {
					stats[1].addValue(carcaca.getCompDeOlhoLombo());
					quant[1]++;
					if (lastDates[1] == null) {
						lastDates[1] = carcaca.getData();
						totais[1] = carcaca.getCompDeOlhoLombo();
					} else if (lastDates[1].before(carcaca.getData())) {
						lastDates[1] = carcaca.getData();
						totais[1] = carcaca.getCompDeOlhoLombo();
					}
				}
				if (carcaca.getProfDeOlhoLombo() != null) {
					stats[2].addValue(carcaca.getProfDeOlhoLombo());
					quant[2]++;
					if (lastDates[2] == null) {
						lastDates[2] = carcaca.getData();
						totais[2] = carcaca.getProfDeOlhoLombo();
					} else if (lastDates[0].before(carcaca.getData())) {
						lastDates[2] = carcaca.getData();
						totais[2] = carcaca.getProfDeOlhoLombo();
					}
				}
				if (carcaca.getMarmoDeOlhoLombo() != null) {
					stats[3].addValue(carcaca.getMarmoDeOlhoLombo());
					quant[3]++;
					if (lastDates[3] == null) {
						lastDates[3] = carcaca.getData();
						totais[3] = carcaca.getMarmoDeOlhoLombo();
					} else if (lastDates[3].before(carcaca.getData())) {
						lastDates[3] = carcaca.getData();
						totais[3] = carcaca.getMarmoDeOlhoLombo();
					}
				}
			}
			ResumoAniEstatistico x = new ResumoAniEstatistico(animal);

			for (int i = 0; i < stats.length; i++) {
				x.getQuantIdade().add(quant[i]);
				x.getUltimoRegistro().add(lastDates[i]);
				x.getTotal().add(totais[i]);
				x.getMedia().add(round(stats[i].getMean(), 2));
			}

			res.add(x);

		}
		return res;
	}

	/**
	 * Relatório espessura de Gordura.
	 * 
	 * @param idsReb
	 * @return
	 */
	public List<ResumoAniEstatistico> relEspessuraDeGordura(List<Rebanho> idsReb) {
		List<Animal> result = animalDAO.relCarcaca(getListIdsRebanho(idsReb));
		List<ResumoAniEstatistico> res = new ArrayList<ResumoAniEstatistico>();
		DescriptiveStatistics[] stats = new DescriptiveStatistics[3];
		Integer[] quant = new Integer[3];
		Date[] lastDates = new Date[3];
		Double[] totais = new Double[3];
		for (Animal animal : result) {
			for (int j = 0; j < stats.length; j++) {
				stats[j] = new DescriptiveStatistics();
				quant[j] = 0;
				lastDates[j] = null;
				totais[j] = null;
			}

			for (Carcaca carcaca : animal.getCarcaca()) {
				if (carcaca.getEspessuraGorduraBic() != null) {
					stats[0].addValue(carcaca.getEspessuraGorduraBic());
					quant[0]++;
					if (lastDates[0] == null) {
						lastDates[0] = carcaca.getData();
						totais[0] = carcaca.getEspessuraGorduraBic();
					} else if (lastDates[0].before(carcaca.getData())) {
						lastDates[0] = carcaca.getData();
						totais[0] = carcaca.getEspessuraGorduraBic();
					}
				}
				if (carcaca.getEspessuraGorduraEst() != null) {
					stats[1].addValue(carcaca.getEspessuraGorduraEst());
					quant[1]++;
					if (lastDates[1] == null) {
						lastDates[1] = carcaca.getData();
						totais[1] = carcaca.getEspessuraGorduraEst();
					} else if (lastDates[1].before(carcaca.getData())) {
						lastDates[1] = carcaca.getData();
						totais[1] = carcaca.getEspessuraGorduraEst();
					}
				}
				if (carcaca.getEspessuraGorduraSub() != null) {
					stats[2].addValue(carcaca.getEspessuraGorduraSub());
					quant[2]++;
					if (lastDates[2] == null) {
						lastDates[2] = carcaca.getData();
						totais[2] = carcaca.getEspessuraGorduraSub();
					} else if (lastDates[0].before(carcaca.getData())) {
						lastDates[2] = carcaca.getData();
						totais[2] = carcaca.getEspessuraGorduraSub();
					}
				}
			}
			ResumoAniEstatistico x = new ResumoAniEstatistico(animal);

			for (int i = 0; i < stats.length; i++) {
				x.getQuantIdade().add(quant[i]);
				x.getUltimoRegistro().add(lastDates[i]);
				x.getTotal().add(totais[i]);
				x.getMedia().add(round(stats[i].getMean(), 2));
			}

			res.add(x);

		}
		return res;
	}

	/**
	 * Relatório Ocorrencia Clinica.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<OcorrenciaClinica> relOcorrenciaClinica(List<Rebanho> idsReb, Date inicio, Date fim, Doenca doenca) {

		List<OcorrenciaClinica> result = ocorrenciaClinicaDAO.relOcorrenciaClinica(getListIdsRebanho(idsReb), inicio,
				fim, doenca);

		return result;
	}

	/**
	 * Relatório Controle parasitario.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public List<ControleParasita> relControleParasitario(List<Rebanho> idsReb, Date inicio, Date fim) {

		List<ControleParasita> result = controleParazitaDAO.relControleParasitario(getListIdsRebanho(idsReb), inicio,
				fim);

		return result;
	}

	public ResumoDadosZootecRebanho relDadosZootecRebanho(Rebanho rebanho) {
		List<Animal> result = animalDAO.animaisRebanho(rebanho.getId());
		ResumoDadosZootecRebanho resumo = new ResumoDadosZootecRebanho(rebanho);
		LinkedHashMap<String, Integer> natalidade = resumo.getNatalidade();
		LinkedHashMap<String, Integer> mortalidade = resumo.getMortalidade();
		int contNat = 0, limite = 0, contP60 = 0, contP120 = 0, contP180 = 0;
		String ano;
		int machos = 0, femeas = 0, machosM = 0, femeasM = 0, venda = 0, morte = 0, roubo = 0, alimentacao = 0,
				emprestimo = 0;
		resumo.setQuantAni(result.size());
		double peso60 = 0, peso120 = 0, peso180 = 0, escoreMedio = 0, pesoA60 = 0, pesoA120 = 0, pesoA180 = 0,
				limiteCurva = 0;
		if (!result.isEmpty()) {
			for (Animal animal : result) {
				ano = "" + Data.getAno(animal.getNascimento());
				if (!natalidade.containsKey(ano)) {
					natalidade.put(ano, 1);
				} else {
					contNat = natalidade.get(ano);
					natalidade.replace(ano, ++contNat);
				}
				if (limite < contNat) {
					limite = contNat;
				}
				if (!animal.getMovimentacaoAnimal().isEmpty()) {
					for (MovimentacaoAnimal move : animal.getMovimentacaoAnimal()) {
						if (move.getTipoSaida() == 1) {
							venda++;
						}
						if (move.getTipoSaida() == 2) {
							morte++;
						}
						if (move.getTipoSaida() == 3) {
							roubo++;
						}
						if (move.getTipoSaida() == 4) {
							alimentacao++;
						}
						if (move.getTipoSaida() == 5) {
							emprestimo++;
						}
						if (move.getTipoSaida() == 2 || move.getTipoSaida() == 4) {
							if (!mortalidade.containsKey(ano)) {
								mortalidade.put(ano, 1);

							} else {
								contNat = natalidade.get(ano);
								mortalidade.replace(ano, ++contNat);
							}
						}
					}
					if (limite < contNat) {
						limite = contNat;
					}
				}
				if (animal.getSexo().getCodigo() == 1) {
					if (animal.getStatus().getCodigo() == 1) {
						machos++;
					} else {
						machosM++;
					}
				} else {
					if (animal.getStatus().getCodigo() == 1) {
						femeas++;
					} else {
						femeasM++;
					}
				}
				if (!animal.getDesenvolvimentoPonderal().isEmpty()) {
					pesoA60 = animal.AjustaPeso60D();
					if (pesoA60 > 0) {
						peso60 += pesoA60;
						contP60++;
						if (pesoA60 > limiteCurva) {
							limiteCurva = pesoA60;
						}
					}
					pesoA120 = animal.AjustaPeso120D();
					if (pesoA120 > 0) {
						peso120 += pesoA120;
						contP120++;
						if (pesoA120 > limiteCurva) {
							limiteCurva = pesoA120;
						}
					}
					pesoA180 = animal.AjustaPeso180D();
					if (pesoA180 > 0) {
						peso180 += pesoA180;
						contP180++;
						if (pesoA180 > limiteCurva) {
							limiteCurva = pesoA180;
						}
					}

				}

			}
			resumo.setRoubo(roubo);
			resumo.setMorte(morte);
			resumo.setEmprestimo(emprestimo);
			resumo.setAlimentacao(alimentacao);
			resumo.setVenda(venda);
			resumo.setQuantFemeasV(femeas);
			resumo.setQuantFemeasM(femeasM);
			resumo.setQuantMachosV(machos);
			resumo.setQuantMachosM(machosM);
			resumo.setMediaPeso60(peso60 / contP60);
			resumo.setMediaPeso120(peso120 / contP120);
			resumo.setMediaPeso180(peso180 / contP180);
			resumo.setEscoreMedio(escoreMedio);
			resumo.setLimiteGraf(limite);
			resumo.setLimiteCurvaPesos((int) limiteCurva);
		} else {
			resumo.setAtivaGrafs(false);
		}
		return resumo;
	}

	public List<Animal> listAllAnimal(Rebanho rebanho) {
		return animalDAO.animaisRebanho(rebanho.getId());
	}

	/**
	 * Relatório de Dados Zootecnicos de um rebanho.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param id
	 * @return
	 */
	public ResumoDadosZootecAnimal relDadosZootecAnimal(Animal animal, List<Animal> animais) {
		ResumoDadosZootecAnimal resumo = new ResumoDadosZootecAnimal(animal);
		HashMap<String, ArrayList<Animal>> descendentes = new HashMap<String, ArrayList<Animal>>();
		ArrayList<String> genealogia = new ArrayList<String>();
		double limite = 0;
		int contGen = 0, contNos = 0;
		Double pesoAjustado;
		if (!animal.getDesenvolvimentoPonderal().isEmpty()) {
			pesoAjustado = animal.AjustaPeso60D();
			if (pesoAjustado >= 0) {
				resumo.setPeso60(pesoAjustado);
				if (pesoAjustado > limite) {
					limite = pesoAjustado;
				}
			}
			pesoAjustado = animal.AjustaPeso120D();
			if (pesoAjustado >= 0) {
				resumo.setPeso120(pesoAjustado);
				if (pesoAjustado > limite) {
					limite = pesoAjustado;
				}
			}
			pesoAjustado = animal.AjustaPeso180D();
			if (pesoAjustado >= 0) {
				resumo.setPeso180(pesoAjustado);
				if (pesoAjustado > limite) {
					limite = pesoAjustado;
				}
			}
			for (DesenvolvimentoPonderal dp : animal.getDesenvolvimentoPonderal()) {
				if (dp.getPeso() != null) {
					if (dp.getPeso() > limite) {
						limite = dp.getPeso();
					}
				}
			}
		}
		// genealogia Animal
		Queue<Animal> list = new LinkedList<Animal>();
		list.add(animal);
		Animal aux;
		contGen = 1;
		contNos = 0;
		while (!list.isEmpty()) {
			aux = list.element();
			contNos++;
			if (aux.getPai() != null) {
				genealogia.add("Pai: " + aux.getPai().getNomeNumero());
				list.add(aux.getPai());
			} else {
				genealogia.add(null);
			}
			contNos++;
			if (aux.getMae() != null) {
				genealogia.add("Mãe: " + aux.getMae().getNomeNumero());
				list.add(aux.getMae());
			} else {
				genealogia.add(null);
			}
			list.remove();
			if ((contGen * 2) == contNos) {
				contNos = 0;
				contGen++;
			}
		}
		// descendencia animal
		ArrayList<Animal> filhos = new ArrayList<Animal>();
		list.add(animal);
		ArrayList<String> ordemFilhos = new ArrayList<String>();
		ordemFilhos.add(animal.getNomeNumero());
		while (!list.isEmpty()) {
			aux = list.element();
			for (Animal animal2 : animais) {
				if ((aux.getSexo().getCodigo() == 1) && (animal2.getPai() != null)) {
					if (animal2.getPai().getNomeNumero() == aux.getNomeNumero()) {
						filhos.add(animal2);
						list.add(animal2);
						ordemFilhos.add(animal2.getNomeNumero());
					}
				}
				if ((aux.getSexo().getCodigo() == 2) && (animal2.getMae() != null)) {
					if (animal2.getMae().getNomeNumero() == aux.getNomeNumero()) {
						filhos.add(animal2);
						list.add(animal2);
						ordemFilhos.add(animal2.getNomeNumero());
					}
				}
			}
			descendentes.put(aux.getNomeNumero(), filhos);
			filhos = new ArrayList<Animal>();
			list.remove();
		}
		resumo.setLimiteCurva((int) limite);
		resumo.setGenealogia(genealogia);
		resumo.setDescendentes(descendentes);
		resumo.setOrdemFilhos(ordemFilhos);
		return resumo;
	}

}
