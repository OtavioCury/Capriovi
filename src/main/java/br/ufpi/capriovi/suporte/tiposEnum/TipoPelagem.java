package br.ufpi.capriovi.suporte.tiposEnum;import java.util.ArrayList;import java.util.List;public enum TipoPelagem {	DEFAULT(0, "-"), 	PuroPorCruza(1, "Com Lã"), 	PuroDeOrigem(2, "Deslanado"), 	MESTICO(3, "Lanugem");	private Integer codigo;	private String descricao;	TipoPelagem(Integer codigo, String descricao) {		this.codigo = codigo;		this.descricao = descricao;	}		public Integer getCodigo() {		return codigo;	}	public void setCodigo(Integer codigo) {		this.codigo = codigo;	}	public String getDescricao() {		return descricao;	}	public void setDescricao(String descricao) {		this.descricao = descricao;	}		public static TipoPelagem getEnumByCodigo(Integer codigo) {		for (TipoPelagem  elemento : TipoPelagem.values()) {			if(elemento.getCodigo().equals(codigo)) {				return elemento;			}		}		return null;	}		public static Integer getCodigoByDesc(String desc) {		for (TipoPelagem  elemento : TipoPelagem.values()) {			if(elemento.getDescricao().equals(desc)) {				return elemento.getCodigo();			}		}		return null;	}		public static List<String> getList() {		List<String> list = new ArrayList<String>();		for (TipoPelagem x : TipoPelagem.values()) {			list.add(x.getDescricao());		}		return list;	}}