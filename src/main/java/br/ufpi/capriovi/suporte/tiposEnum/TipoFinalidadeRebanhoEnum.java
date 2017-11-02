package br.ufpi.capriovi.suporte.tiposEnum;import java.util.ArrayList;import java.util.List;/** * Tipos de finalidade de um rebanho. */public enum TipoFinalidadeRebanhoEnum{	DEFAULT(0, "-"),	FINALIDADE_CARNE(1,"Carne"),	FINALIDADE_LEITE(2,"Leite"),	FINALIDADE_MISTO(3,"Misto");		private Integer codigo;	private String descricao;		TipoFinalidadeRebanhoEnum(Integer codigo, String descricao){		this.codigo = codigo;		this.descricao = descricao;	}		public Integer getCodigo() {		return codigo;	}	public void setCodigo(Integer codigo) {		this.codigo = codigo;	}	public String getDescricao() {		return descricao;	}	public void setDescricao(String descricao) {		this.descricao = descricao;	}		public static TipoFinalidadeRebanhoEnum getEnumByCodigo(Integer codigo) {		for (TipoFinalidadeRebanhoEnum  elemento : TipoFinalidadeRebanhoEnum.values()) {			if(elemento.getCodigo().equals(codigo)) {				return elemento;			}		}		return null;	}		public static Integer getCodigoByDesc(String desc) {		for (TipoFinalidadeRebanhoEnum  elemento : TipoFinalidadeRebanhoEnum.values()) {			if(elemento.getDescricao().equals(desc)) {				return elemento.getCodigo();			}		}		return null;	}		public static List<String> getList() {		List<String> list = new ArrayList<String>();		for (TipoFinalidadeRebanhoEnum x : TipoFinalidadeRebanhoEnum.values()) {			list.add(x.getDescricao());		}		return list;	}}