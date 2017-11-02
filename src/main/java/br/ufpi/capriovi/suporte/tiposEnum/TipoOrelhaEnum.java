package br.ufpi.capriovi.suporte.tiposEnum;

import java.util.ArrayList;
import java.util.List;

public enum TipoOrelhaEnum {

	DEFAULT(0, "-"),
	TIPO_CAIDA(1,"Caída"),
	TIPO_HORIZONTAL(2,"Horizontal"),
	TIPO_LANCA(3,"Lanca");

	private Integer codigo;
	private String descricao;

	TipoOrelhaEnum(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static TipoOrelhaEnum getEnumByCodigo(Integer codigo) {
		for (TipoOrelhaEnum  elemento : TipoOrelhaEnum.values()) {
			if(elemento.getCodigo().equals(codigo)) {
				return elemento;
			}
		}
		return null;
	}
	
	public static Integer getCodigoByDesc(String desc) {
		for (TipoOrelhaEnum  elemento : TipoOrelhaEnum.values()) {
			if(elemento.getDescricao().equals(desc)) {
				return elemento.getCodigo();
			}
		}
		return null;
	}
	
	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (TipoOrelhaEnum x : TipoOrelhaEnum.values()) {
			list.add(x.getDescricao());
		}
		return list;
	}
	
}
