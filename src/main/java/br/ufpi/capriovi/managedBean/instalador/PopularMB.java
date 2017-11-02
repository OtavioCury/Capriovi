package br.ufpi.capriovi.managedBean.instalador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.tiposEnum.TipoPartoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoSexoEnum;

@Named(value = "popularMB")
@ViewScoped
public class PopularMB extends BaseBeans{

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

	public void popularBlup(){
		Rebanho rebanho1 = new Rebanho();
		rebanho1.setFazenda(systemSessionMB.getFazenda());
		rebanho1.setNome("RebanhoTeste1");
		rebanhoFacade.adicionaRebanho(rebanho1);
		rebanho1 = rebanhoFacade.buscaNome("RebanhoTeste1");

		Rebanho rebanho2 = new Rebanho();
		rebanho2.setFazenda(systemSessionMB.getFazenda());
		rebanho2.setNome("RebanhoTeste2");
		rebanhoFacade.adicionaRebanho(rebanho2);
		rebanho2 = rebanhoFacade.buscaNome("RebanhoTeste2");

		Rebanho rebanho3 = new Rebanho();
		rebanho3.setFazenda(systemSessionMB.getFazenda());
		rebanho3.setNome("RebanhoTeste3");
		rebanhoFacade.adicionaRebanho(rebanho3);
		rebanho3 = rebanhoFacade.buscaNome("RebanhoTeste3");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date seca;
		Date chuvosa;
		try {
			seca = sdf.parse("01/11/2017");
			chuvosa = sdf.parse("01/03/2017");
			Animal animal1 = new Animal(rebanho1, "animal1", null, null, TipoSexoEnum.getEnumByCodigo(1),
					seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal1);
			Animal animal2 = new Animal(rebanho1, "animal2", null, null, TipoSexoEnum.getEnumByCodigo(1), 
					seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal2);
			Animal animal3 = new Animal(rebanho1, "animal3", null, null, TipoSexoEnum.getEnumByCodigo(1), 
					seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal3);
			Animal animal4 = new Animal(rebanho2, "animal4", animalFacade.animalNome("animal1"), 
					null, TipoSexoEnum.getEnumByCodigo(2), chuvosa, TipoPartoEnum.getEnumByCodigo(2));
			animalFacade.adicionaAnimal(animal4);
			Animal animal5 = new Animal(rebanho2, "animal5", animalFacade.animalNome("animal1"), 
					null, TipoSexoEnum.getEnumByCodigo(2), chuvosa, TipoPartoEnum.getEnumByCodigo(2));
			animalFacade.adicionaAnimal(animal5);
			Animal animal6 = new Animal(rebanho1, "animal6", animalFacade.animalNome("animal1"), 
					null, TipoSexoEnum.getEnumByCodigo(1), seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal6);
			Animal animal7 = new Animal(rebanho1, "animal7", animalFacade.animalNome("animal2"), 
					null, TipoSexoEnum.getEnumByCodigo(1), seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal7);
			Animal animal8 = new Animal(rebanho1, "animal8", animalFacade.animalNome("animal2"), 
					null, TipoSexoEnum.getEnumByCodigo(1), seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal8);
			Animal animal9 = new Animal(rebanho3, "animal9", animalFacade.animalNome("animal2"), 
					null, TipoSexoEnum.getEnumByCodigo(2), chuvosa, TipoPartoEnum.getEnumByCodigo(3));
			animalFacade.adicionaAnimal(animal9);
			Animal animal10 = new Animal(rebanho3, "animal10", animalFacade.animalNome("animal3"), 
					null, TipoSexoEnum.getEnumByCodigo(2), chuvosa, TipoPartoEnum.getEnumByCodigo(3));
			animalFacade.adicionaAnimal(animal10);
			Animal animal11 = new Animal(rebanho1, "animal11", animalFacade.animalNome("animal3"), 
					null, TipoSexoEnum.getEnumByCodigo(1), seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal11);
			Animal animal12 = new Animal(rebanho3, "animal12", animalFacade.animalNome("animal3"), 
					null, TipoSexoEnum.getEnumByCodigo(2), chuvosa, TipoPartoEnum.getEnumByCodigo(3));
			animalFacade.adicionaAnimal(animal12);
			Animal animal13 = new Animal(rebanho1, "animal13", animalFacade.animalNome("animal3"), 
					null, TipoSexoEnum.getEnumByCodigo(1), seca, TipoPartoEnum.getEnumByCodigo(1));
			animalFacade.adicionaAnimal(animal13);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
