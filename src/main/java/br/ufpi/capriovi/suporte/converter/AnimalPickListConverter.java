package br.ufpi.capriovi.suporte.converter;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;

@FacesConverter(value = "animalPickListConverter", forClass = Animal.class)
public class AnimalPickListConverter implements Converter { 

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) { 
		Object ret = null; 
		if (arg1 instanceof PickList) { 
			Object dualList = ((PickList) arg1).getValue(); 
			@SuppressWarnings("unchecked")
			DualListModel<Animal> dl = (DualListModel<Animal>) dualList; 
			for (Iterator<Animal> iterator = dl.getSource().iterator(); iterator 
					.hasNext();) { 
				Object o = iterator.next(); 
				String id = (new StringBuilder()).append( 
						((Animal) o).getId()).toString(); 
				if (arg2.equals(id)) { 
					ret = o; 
					break; 
				} 
			} 

			if (ret == null) { 
				for (Iterator<Animal> iterator1 = dl.getTarget().iterator(); iterator1 
						.hasNext();) { 
					Object o = iterator1.next(); 
					String id = (new StringBuilder()).append( 
							((Animal) o).getId()).toString(); 
					if (arg2.equals(id)) { 
						ret = o; 
						break; 
					} 
				} 

			} 
		} 
		return ret; 
	} 

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) { 
		String str = ""; 
		if (arg2 instanceof Animal) 
			str = ((Animal) arg2).getId().toString(); 
		return str; 
	} 
} 
