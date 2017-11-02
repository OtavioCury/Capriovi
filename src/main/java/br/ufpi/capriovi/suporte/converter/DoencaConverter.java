package br.ufpi.capriovi.suporte.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.facade.cadastros.DoencaFacade;

@FacesConverter(value = "doencaConverter", forClass = Doenca.class)
public class DoencaConverter implements Converter {
	
	@Inject
	DoencaFacade doencaFacade;
	
    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        
        Doenca d = (Doenca) arg1.getAttributes().get(arg2);
//        Long d;
// 
//        try {
//        	d = Long.valueOf(arg2);
//        } catch (NumberFormatException exception) {
//            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type the name of a Doenca and select it (or use the dropdow)", "Type the name of a Doenca and select it (or use the dropdow)"));
//        }
 
        return d;
    }
 
    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
    	
    	if(object != null) {
    		Doenca d = (Doenca) object;
            arg1.getAttributes().put(d.getId().toString(), d);
            return String.valueOf(d.getId());
        }else {
            return null;
        }
        
    }
}