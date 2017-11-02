/**
 * 
 */
package br.ufpi.capriovi.suporte.tiposEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Otávio Cury
 *
 */
public enum TipoStatusVermifugaEnum {
	DEFAULT(0,"-"),
	DESNECESSARIO(1,"Não Vermifugar"),
	ALERTA(2,"Alerta"),
	NECESSARIO(3,"Vermifugar"),
	ATENCAO(4,"Atenção"),
	RISCO(5,"Risco");

	private Integer codigo;
	private String descricao;


	private TipoStatusVermifugaEnum(Integer codigo, String descricao) {
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

	public static TipoStatusVermifugaEnum getEnumByCodigo(Integer codigo) {
		for (TipoStatusVermifugaEnum  elemento : TipoStatusVermifugaEnum.values()) {
			if(elemento.getCodigo().equals(codigo)) {
				return elemento;
			}
		}
		return null;
	}

	public static Integer getCodigoByDesc(String desc) {
		for (TipoStatusVermifugaEnum  elemento : TipoStatusVermifugaEnum.values()) {
			if(elemento.getDescricao().equals(desc)) {
				return elemento.getCodigo();
			}
		}
		return null;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (TipoStatusVermifugaEnum x : TipoStatusVermifugaEnum.values()) {
			list.add(x.getDescricao());
		}
		return list;
	}
}
