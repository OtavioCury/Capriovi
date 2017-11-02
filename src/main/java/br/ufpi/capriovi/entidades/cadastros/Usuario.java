package br.ufpi.capriovi.entidades.cadastros;import java.io.Serializable;import java.util.HashSet;import java.util.Set;import javax.persistence.Column;import javax.persistence.ElementCollection;import javax.persistence.Entity;import javax.persistence.FetchType;import javax.persistence.GeneratedValue;import javax.persistence.GenerationType;import javax.persistence.Id;import javax.persistence.Inheritance;import javax.persistence.InheritanceType;import javax.persistence.JoinColumn;import javax.persistence.JoinTable;import javax.persistence.Table;import javax.persistence.UniqueConstraint;@Entity@Inheritance(strategy = InheritanceType.JOINED)@Table(name = "usuario")public class Usuario implements Serializable{	/**	 * 	 */	private static final long serialVersionUID = 2866569670283809210L;	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)	private Long id;	private String username;	private String password;	private String nome;	private String endereco;	private String estado;	private String municipio;	private String cpf;	private String telefone;	private String celular;	@Column(unique = true)	private String email;	private Boolean statusUser;	private Boolean statusAdmin;	private Boolean statusGeral;	private String motivo;		private String token;		private String tokenSenha;	@ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)	@JoinTable(name = "usuario_permissao", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario","permissao"})}, joinColumns = @JoinColumn(name = "usuario"))	@Column(name = "permissao", length=50)	private Set<String> permissao = new HashSet<String>();	public Usuario() {		super();	}	public Long getId() {		return id;	}	public void setId(Long id) {		this.id = id;	}	public String getUsername() {		return username;	}	public void setUsername(String username) {		this.username = username;	}	public String getPassword() {		return password;	}	public void setPassword(String password) {		this.password = password;	}	public Set<String> getPermissao() {		return permissao;	}	public void setPermissao(Set<String> permissao) {		this.permissao = permissao;	}	public String getNome() {		return nome;	}	public void setNome(String nome) {		this.nome = nome;	}	public String getEndereco() {		return endereco;	}	public void setEndereco(String endereco) {		this.endereco = endereco;	}	public String getEstado() {		return estado;	}	public void setEstado(String estado) {		this.estado = estado;	}	public String getMunicipio() {		return municipio;	}	public void setMunicipio(String municipio) {		this.municipio = municipio;	}	public String getCpf() {		return cpf;	}	public void setCpf(String cpf) {		this.cpf = cpf;	}	public String getTelefone() {		return telefone;	}	public void setTelefone(String telefone) {		this.telefone = telefone;	}	public String getEmail() {		return email;	}	public void setEmail(String email) {		this.email = email;	}	public String getMotivo() {		return motivo;	}	public Boolean getStatusUser() {		return statusUser;	}	public void setStatusUser(Boolean statusUser) {		this.statusUser = statusUser;	}	public Boolean getStatusAdmin() {		return statusAdmin;	}	public void setStatusAdmin(Boolean statusAdmin) {		this.statusAdmin = statusAdmin;	}	public void setMotivo(String motivo) {		this.motivo = motivo;	}		@Override	public int hashCode() {		final int prime = 31;		int result = 1;		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());		result = prime * result + ((id == null) ? 0 : id.hashCode());		result = prime * result + ((permissao == null) ? 0 : permissao.hashCode());		return result;	}	@Override	public boolean equals(Object obj) {		if (this == obj)			return true;		if (obj == null)			return false;		if (getClass() != obj.getClass())			return false;		Usuario other = (Usuario) obj;		if (cpf == null) {			if (other.cpf != null)				return false;		} else if (!cpf.equals(other.cpf))			return false;		if (id == null) {			if (other.id != null)				return false;		} else if (!id.equals(other.id))			return false;		if (permissao == null) {			if (other.permissao != null)				return false;		} else if (!permissao.equals(other.permissao))			return false;		return true;	}	public String getCelular() {		return celular;	}	public void setCelular(String celular) {		this.celular = celular;	}	public String getToken() {		return token;	}	public void setToken(String token) {		this.token = token;	}	public Boolean getStatusGeral() {		return statusGeral;	}	public void setStatusGeral(Boolean statusGeral) {		this.statusGeral = statusGeral;	}	public String getTokenSenha() {		return tokenSenha;	}	public void setTokenSenha(String tokenSenha) {		this.tokenSenha = tokenSenha;	}}