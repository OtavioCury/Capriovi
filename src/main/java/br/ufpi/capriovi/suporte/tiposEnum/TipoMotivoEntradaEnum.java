package br.ufpi.capriovi.suporte.tiposEnum;

import java.util.ArrayList;
import java.util.List;

public enum TipoMotivoEntradaEnum {
	DEFAULT(0, "-"),  
	Compra(1, "Compra"), 
	Nascimento(2, "Nascimento"), 
	Emprestimo(3, "Emprestimo"),
	Outros(4, "Outros");

	private Integer codigo;
	private String descricao;

	TipoMotivoEntradaEnum(Integer codigo, String descricao) {
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

	public static TipoMotivoEntradaEnum getEnumByCodigo(Integer codigo) {
		for (TipoMotivoEntradaEnum  elemento : TipoMotivoEntradaEnum.values()) {
			if(elemento.getCodigo().equals(codigo)) {
				return elemento;
			}
		}
		return null;
	}

	public static Integer getCodigoByDesc(String desc) {
		for (TipoMotivoEntradaEnum  elemento : TipoMotivoEntradaEnum.values()) {
			if(elemento.getDescricao().equals(desc)) {
				return elemento.getCodigo();
			}
		}
		return null;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (TipoMotivoEntradaEnum x : TipoMotivoEntradaEnum.values()) {
			list.add(x.getDescricao());
		}
		return list;
	}
}
