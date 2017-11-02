package br.ufpi.capriovi.suporte.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.ufpi.capriovi.entidades.cadastros.Fazenda;

@FacesConverter(value = "fazendaConverter", forClass = Fazenda.class)
public class FazendaConverter implements Converter {
	
    @Override
    public Object getAsObject(FacesContext fc, UIComponent cp, String value) {
    	if(value != null && !value.isEmpty()){
    		return (Fazenda) cp.getAttributes().get(value); 
    	}
        return null;
    }
 
    @Override
    public String getAsString(FacesContext fc, UIComponent cp, Object value) {
    	if(value instanceof Fazenda) {
    		Fazenda f = (Fazenda) value;
    		if(f != null && f instanceof Fazenda){
    			cp.getAttributes().put(f.getId().toString(), f);
    			return f.getId().toString();
    		}
        }
        return ""; 
    }
}