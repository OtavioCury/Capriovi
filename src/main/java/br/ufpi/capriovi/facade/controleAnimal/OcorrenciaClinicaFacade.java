package br.ufpi.capriovi.facade.controleAnimal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.controleAnimal.OcorrenciaClinicaDAO;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;

@Stateless
public class OcorrenciaClinicaFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5441894959078815015L;	

	@Inject 
	private OcorrenciaClinicaDAO ocorrenciaClinicaDAO;	

	public List<OcorrenciaClinica> listarPorRebanho(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();
		List<OcorrenciaClinica> ocorrencia = new ArrayList<OcorrenciaClinica>();

		if (!rebanhos.isEmpty()) {
			for (Rebanho r : rebanhos) {
				ids.add(r.getId());
			}

			ocorrencia = ocorrenciaClinicaDAO.ocorrenciasRebanhos(ids);
		}

		return ocorrencia;


	}

	/**
	 * 
	 * @param id
	 */
	public void deletarOcorrenciaClinica(Long id){
		ocorrenciaClinicaDAO.deletarOcorrenciaClinica(id);
	}
	/**
	 * 
	 * @param OcorrenciaClinica
	 */
	public void atualizaOcorrenciaClinica(OcorrenciaClinica ocorrenciaClinica){
		ocorrenciaClinicaDAO.update(ocorrenciaClinica);
	}
	/**
	 * 
	 * @param OcorrenciaClinica
	 */
	public void adicionaOcorrenciaClinica(OcorrenciaClinica ocorrenciaClinica){
		ocorrenciaClinicaDAO.adicionaOcorrenciaClinica(ocorrenciaClinica);
	}

}