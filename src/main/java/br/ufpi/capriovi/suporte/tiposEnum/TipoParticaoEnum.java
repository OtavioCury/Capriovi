package br.ufpi.capriovi.suporte.tiposEnum;

import java.util.ArrayList;
import java.util.List;

public enum TipoParticaoEnum {
	DEFAULT(0, "-"),
	TIPO_NAO(1,"Não"),
	TIPO_CAUDA(2, "Cauda"),
	TIPO_MEIO(3, "Meio"),
	TIPO_PARTIDO(4, "Partido");

	private Integer codigo;
	private String descricao;

	TipoParticaoEnum(Integer codigo, String descricao) {
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
	
	public static TipoParticaoEnum getEnumByCodigo(Integer codigo) {
		for (TipoParticaoEnum  elemento : TipoParticaoEnum.values()) {
			if(elemento.getCodigo().equals(codigo)) {
				return elemento;
			}
		}
		return null;
	}
	
	public static Integer getCodigoByDesc(String desc) {
		for (TipoParticaoEnum  elemento : TipoParticaoEnum.values()) {
			if(elemento.getDescricao().equals(desc)) {
				return elemento.getCodigo();
			}
		}
		return null;
	}
	
	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (TipoParticaoEnum x : TipoParticaoEnum.values()) {
			list.add(x.getDescricao());
		}
		return list;
	}

}
