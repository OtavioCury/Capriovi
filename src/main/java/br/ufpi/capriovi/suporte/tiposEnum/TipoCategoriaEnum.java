package br.ufpi.capriovi.suporte.tiposEnum;

import java.util.ArrayList;
import java.util.List;

public enum TipoCategoriaEnum {
	DEFAULT(0, "-"),  
	Cabrito(1, "Cabrito/Cabrita ou Borrego/Borrega"), 
	Marrao(2, "Marrãos e Marrãs"), 
	Matriz(3, "Matriz ou Reprodutor");

	private Integer codigo;
	private String descricao;

	TipoCategoriaEnum(Integer codigo, String descricao) {
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

	public static TipoCategoriaEnum getEnumByCodigo(Integer codigo) {
		for (TipoCategoriaEnum  elemento : TipoCategoriaEnum.values()) {
			if(elemento.getCodigo().equals(codigo)) {
				return elemento;
			}
		}
		return null;
	}

	public static Integer getCodigoByDesc(String desc) {
		for (TipoCategoriaEnum  elemento : TipoCategoriaEnum.values()) {
			if(elemento.getDescricao().equals(desc)) {
				return elemento.getCodigo();
			}
		}
		return null;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (TipoCategoriaEnum x : TipoCategoriaEnum.values()) {
			list.add(x.getDescricao());
		}
		return list;
	}
}
