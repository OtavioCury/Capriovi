package br.ufpi.capriovi.suporte.tiposEnum;

import java.util.ArrayList;
import java.util.List;

public enum TipoEstacaoEnum {
	DEFAULT(0, "-"),
	CHUVOSA(1,"Chuvosa"),
	SECA(2,"Seca");

	private Integer codigo; 
	private String descricao;

	TipoEstacaoEnum(Integer codigo, String descricao){
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

	public static TipoEstacaoEnum getEnumByCodigo(Integer codigo) {
		for (TipoEstacaoEnum  elemento : TipoEstacaoEnum.values()) {
			if(elemento.getCodigo().equals(codigo)) {
				return elemento;
			}
		}
		return null;
	}

	public static Integer getCodigoByDesc(String desc) {
		for (TipoEstacaoEnum  elemento : TipoEstacaoEnum.values()) {
			if(elemento.getDescricao().equals(desc)) {
				return elemento.getCodigo();
			}
		}
		return null;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (TipoEstacaoEnum x : TipoEstacaoEnum.values()) {
			list.add(x.getDescricao());
		}
		return list;
	}
}
