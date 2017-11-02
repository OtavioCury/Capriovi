package br.ufpi.capriovi.suporte.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;

@FacesConverter(value = "animalConverter", forClass = Animal.class)
public class AnimalConverter implements Converter {
	
	@Inject
	AnimalFacade animalFacade;
	
    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        
    	Animal a = (Animal) arg1.getAttributes().get(arg2);
//        Long d;
// 
//        try {
//        	d = Long.valueOf(arg2);
//        } catch (NumberFormatException exception) {
//            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type the name of a Doenca and select it (or use the dropdow)", "Type the name of a Doenca and select it (or use the dropdow)"));
//        }
 
        return a;
    }
 
    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
    	
    	if(object != null) {
    		Animal a = (Animal) object;
            arg1.getAttributes().put(a.getId().toString(), a);
            return String.valueOf(a.getId());
        }else {
            return null;
        }
        
    }
}