package br.ufpi.capriovi.managedBean.zootecnia.animal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.event.SelectEvent;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Raca;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.Carcaca;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.entidades.controleAnimal.TamanhoCorporal;
import br.ufpi.capriovi.entidades.controleAnimal.Verminose;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.RacaFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.tiposEnum.TipoCompGeneticaEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoDesenvolvimentoPonderal;
import br.ufpi.capriovi.suporte.tiposEnum.TipoFinalidadeAnimalEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoMotivoEntradaEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoOrelhaEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoParticaoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoPartoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoSexoEnum;

@SessionScoped
@Named(value = "animalAddEditMB")
public class AnimalAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9104833315358988965L;

	//
	@Inject
	AnimalFacade animalFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	RelProducaoFacade rel;

	@Inject
	RacaFacade racaFacade;

	@Inject
	RebanhoFacade rebanhoFacade;

	private Animal animal;

	private List<Rebanho> rebanhos;

	private List<String> allRebanhosName;

	private List<Animal> animaisPai;

	private List<Animal> animaisMae;

	private String title;

	private List<Raca> racas;

	private List<String> nomesRacas;

	private DesenvolvimentoPonderal desenvolvimentoPonderal;

	private Verminose verminose;

	private TamanhoCorporal tamanhoCorporal;	

	private Carcaca carcaca;	

	private String sexoNome;

	private String motivoEntrada;

	private String finalidade;

	private String compGenetico;

	private String categoria;

	private String partoNome;

	private String particaoNome;

	private String orelha;

	private String desenvolvimento;

	private String nomePai, nomeMae;

	private List<String>  allAnimalNamesMae;

	private List<String>  allAnimalNamesPai;

	private double cpm;

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal Animal) {
		this.animal = Animal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void add() {

		this.title = "Adicionar Animal";
		this.animal = new Animal();

		setClasses();

		this.sexoNome = new String();

		this.particaoNome = new String();

		this.orelha = new String();

		this.partoNome = new String();

		motivoEntrada = new String();

		finalidade = new String();

		compGenetico = new String();

		categoria = new String();

		nomeAnimaisMae();
		nomeAnimaisPai();

	}


	public void update(Animal a) {
		this.title = "Atualizar Animal";
		this.animal = a;
		boolean presente = false;

		setClasses();

		for (Animal animal : animaisPai) {
			if (animal.getId() == a.getId()) {
				presente = true;				
			}
		}

		if (presente == true) {
			animaisPai.remove(a);
		}

		presente = false;

		for (Animal animal : animaisMae) {
			if (animal.getId() == a.getId()) {
				presente = true;				
			}
		}

		if (presente == true) {
			animaisMae.remove(a);
		}

		if (this.animal.getRaca() == null) {
			Raca r = new Raca();
			this.animal.setRaca(r);
		}	

		if (this.animal.getSexo() != null) {
			this.sexoNome = this.animal.getSexo().getDescricao();	
		}else {
			this.sexoNome = new String();
		}

		if (this.animal.getParticao() != null) {
			this.particaoNome = this.animal.getParticao().getDescricao();	
		}else {
			this.particaoNome= new String();
		}

		if (this.animal.getOrelha() != null) {
			this.orelha = this.animal.getOrelha().getDescricao();
		}else{
			this.orelha = new String();
		}

		if (this.animal.getParto() != null) {
			this.partoNome = this.animal.getParto().getDescricao();
		}else {
			this.partoNome = new String();
		}

		if (this.animal.getMotivoEntrada() != null) {
			this.motivoEntrada = this.animal.getMotivoEntrada().getDescricao();
		}else {
			this.motivoEntrada = new String();
		}

		if (this.animal.getFinalidadeAnimal() != null) {
			this.finalidade = this.animal.getFinalidadeAnimal().getDescricao();
		}else {
			this.finalidade = new String();
		}

		if (this.animal.getComposicaoGenetica() != null) {
			this.compGenetico = this.animal.getComposicaoGenetica().getDescricao();
		}else {
			this.compGenetico = new String();
		}

		if (this.animal.getCategoria() != null) {
			this.categoria = this.animal.getCategoria().getDescricao();
		}else {
			this.categoria = new String();
		}

		nomeAnimaisMae();
		nomeAnimaisPai();

	}	

	public String cancel() {
		this.animal = new Animal();
		return "/pages/entidades/rebanho/list.xhtml?faces-redirect=true";
	}

	@Transactional
	public String save() {
		if (this.animal != null) {
			if (this.animal.getId() == null) {
				// Add			
				if (!animalFacade.nomePresente(animal, rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()), true)) {
					percorreListas();

					setEnunsAnimal();

					setPaiMae();

					this.animal.setRebanho(returnRebanho(animal.getRebanho().getNome()));				

					setListas();

					animal.setDataEntrada(new Date());

					this.animalFacade.adicionaAnimal(this.animal);

					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Animal adicionado com sucesso!",  null);
					FacesContext.getCurrentInstance().addMessage(null, message);
					FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
					return "list.xhtml?faces-redirect=true";
				}else{
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(MensagensExceptions.NomeAnimalExistenteException));					
					return "addEdit";
				}
			} else {
				// Update
				if (!animalFacade.nomePresente(animal, rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()), false)) {
					percorreListas();

					setEnunsAnimal();

					setPaiMae();

					this.animal.setRebanho(returnRebanho(animal.getRebanho().getNome()));				

					setListas();

					this.animalFacade.atualizaAnimal(this.animal);

					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Animal atualizado com sucesso!",  null);
					FacesContext.getCurrentInstance().addMessage(null, message);
					FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
					return "list.xhtml?faces-redirect=true";
				}else{
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(MensagensExceptions.NomeAnimalExistenteException));				
					return "addEdit";
				}

			}			
		}
		return "/pages/entidades/rebanho/list.xhtml?faces-redirect=true";
	}

	public List<Raca> getRacas() {
		return racas;
	}

	public void setRacas(List<Raca> racas) {
		this.racas = racas;
	}

	/**
	 * Popula um array com o nome de todas as raças
	 */
	public void nomeRacas(){
		if(nomesRacas.isEmpty()){
			for (Raca r : racas) {
				nomesRacas.add(r.getNome());
			}
		}
	}

	/**
	 * Método chamado pelo componente de autocomplete da mãe do animal
	 * @param query
	 * @return
	 */
	public List<String> completeAnimalMae(String query) {
		List<String> filteredNames = new ArrayList<String>();

		for (int i = 0; i < allAnimalNamesMae.size(); i++) {
			String nome = allAnimalNamesMae.get(i);
			if(nome.startsWith(query)) {
				filteredNames.add(nome);
			}
		}

		return filteredNames;
	}

	/**
	 * Método chamado pelo componente de autocomplete da pai do animal
	 * @param query
	 * @return
	 */
	public List<String> completeAnimalPai(String query) {
		List<String> filteredNames = new ArrayList<String>();

		for (int i = 0; i < allAnimalNamesPai.size(); i++) {
			String nome = allAnimalNamesPai.get(i);
			if(nome.startsWith(query)) {
				filteredNames.add(nome);
			}
		}

		return filteredNames;
	}

	/**
	 * Popula um array com o nomeNumero de todas as fêmeas
	 */
	public void nomeAnimaisMae(){
		for (Animal a: this.animaisMae) {
			this.allAnimalNamesMae.add(a.getNomeNumero());
		}
	}

	/**
	 * Popula um array com o nomeNumero de todas os machos
	 */
	public void nomeAnimaisPai(){
		for (Animal a: this.animaisPai) {
			this.allAnimalNamesPai.add(a.getNomeNumero());
		}
	}

	/**
	 * Retorna o objeto Animal de acordo com o nomeNumero
	 * @param nome
	 * @return
	 */
	public Animal returnAnimalMae(String nome){
		for (Animal a : this.animaisMae) {
			if(a.getNomeNumero().equals(nome)){
				return a;
			}
		}
		return null;
	}

	/**
	 * Retorna o objeto Animal de acordo com o nomeNumero
	 * @param nome
	 * @return
	 */
	public Animal returnAnimalPai(String nome){
		for (Animal a : this.animaisPai) {
			if(a.getNomeNumero().equals(nome)){
				return a;
			}
		}
		return null;
	}

	public String reinitDesenvolvimento(){
		double media = (desenvolvimentoPonderal.getConformidade()+
				desenvolvimentoPonderal.getMusculosidade() +
				desenvolvimentoPonderal.getPrecocidade())/3;

		if(this.desenvolvimento.equals("Ao nascer")){
			this.desenvolvimentoPonderal.setTipoDesenvolvimento(TipoDesenvolvimentoPonderal.getEnumByCodigo(1));
		}else if (this.desenvolvimento.equals("Desmame")) {
			this.desenvolvimentoPonderal.setTipoDesenvolvimento(TipoDesenvolvimentoPonderal.getEnumByCodigo(2));
		}else if (this.desenvolvimento.equals("Outros")) {
			this.desenvolvimentoPonderal.setTipoDesenvolvimento(TipoDesenvolvimentoPonderal.getEnumByCodigo(3));
		}

		this.desenvolvimentoPonderal.setCpm(media);

		this.desenvolvimento = new String();

		this.desenvolvimentoPonderal = new DesenvolvimentoPonderal();

		return null;
	}

	public void percorreListas(){

		int aux = 0;
		for (Raca r : racas) {
			if (r.getNome().equals(this.animal.getRaca().getNome())) {
				this.animal.setRaca(r);
				aux=1;

			}
		}
		if (aux==0) {
			this.animal.setRaca(null);
		}

		aux = 0;					

	}

	public void setClasses(){

		allRebanhosName = new ArrayList<String>();

		rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());

		nomeRebanhos(rebanhos);

		this.racas = new ArrayList<Raca>();
		this.nomesRacas = new ArrayList<String>();

		this.setRacas(racaFacade.racasUsuario(getUsuario()));
		this.nomeRacas();		

		this.animaisMae = animalFacade.listAllSexo(rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()), 2);
		this.animaisPai = animalFacade.listAllSexo(rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()), 1);

		this.desenvolvimentoPonderal = new DesenvolvimentoPonderal();		

		this.verminose = new Verminose();	

		this.tamanhoCorporal = new TamanhoCorporal();		

		this.carcaca = new Carcaca();		

		this.desenvolvimento = new String();

		this.allAnimalNamesMae = new ArrayList<String>();
		this.allAnimalNamesPai = new ArrayList<String>();



	}

	public void setEnunsAnimal(){

		if(this.sexoNome.equals("Macho")){
			this.animal.setSexo(TipoSexoEnum.getEnumByCodigo(1));
		}else if(this.sexoNome.equals("Fêmea")){
			this.animal.setSexo(TipoSexoEnum.getEnumByCodigo(2));
		}

		if(this.particaoNome.equals("Não")){
			this.animal.setParticao(TipoParticaoEnum.getEnumByCodigo(1));
		}else if (this.particaoNome.equals("Cauda")) {
			this.animal.setParticao(TipoParticaoEnum.getEnumByCodigo(2));
		}else if (this.particaoNome.equals("Meio")) {
			this.animal.setParticao(TipoParticaoEnum.getEnumByCodigo(3));
		}else if (this.particaoNome.equals("Partido")) {
			this.animal.setParticao(TipoParticaoEnum.getEnumByCodigo(4));
		}

		if (this.orelha.equals("Caída")) {
			this.animal.setOrelha(TipoOrelhaEnum.getEnumByCodigo(1));
		}else if (this.orelha.equals("Horizontal")) {
			this.animal.setOrelha(TipoOrelhaEnum.getEnumByCodigo(2));
		}else if (this.orelha.equals("Forma de Lança")) {
			this.animal.setOrelha(TipoOrelhaEnum.getEnumByCodigo(3));
		}

		if (this.partoNome.equals("Simples")) {
			this.animal.setParto(TipoPartoEnum.getEnumByCodigo(1));
		}else if (this.partoNome.equals("Duplo")) {
			this.animal.setParto(TipoPartoEnum.getEnumByCodigo(2));
		}else if (this.partoNome.equals("Triplo")) {
			this.animal.setParto(TipoPartoEnum.getEnumByCodigo(3));
		}else if (this.partoNome.equals("Quádruplo")) {
			this.animal.setParto(TipoPartoEnum.getEnumByCodigo(4));
		}

		if (this.compGenetico.equals("Puro por cruza")) {
			this.animal.setComposicaoGenetica(TipoCompGeneticaEnum.getEnumByCodigo(1));
		}else if (this.partoNome.equals("Puro de origem")) {
			this.animal.setComposicaoGenetica(TipoCompGeneticaEnum.getEnumByCodigo(2));
		}else if (this.partoNome.equals("Mestiço")) {
			this.animal.setComposicaoGenetica(TipoCompGeneticaEnum.getEnumByCodigo(3));
		}

		if (this.motivoEntrada.equals("Compra")) {
			this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(1));		
		}else if (this.motivoEntrada.equals("Nascimento")) {
			this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(2));
		}else if (this.motivoEntrada.equals("Emprestimo")) {
			this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(3));
		}else if (this.motivoEntrada.equals("Outros")) {
			this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(4));
		}

		if (this.finalidade.equals("Reprodução")) {
			this.animal.setFinalidadeAnimal(TipoFinalidadeAnimalEnum.getEnumByCodigo(1));		
		}else if (this.finalidade.equals("Produção")) {
			this.animal.setFinalidadeAnimal(TipoFinalidadeAnimalEnum.getEnumByCodigo(1));
		}

	}

	public void setPaiMae(){

		this.animal.setPai(returnAnimalPai(getNomePai()));
		this.animal.setMae(returnAnimalMae(getNomeMae()));

		if (this.animal.getPai() == null) {
			this.animal.setPai(null);
		}

		if (this.animal.getMae() == null) {
			this.animal.setMae(null);
		}
	}

	public void setListas(){		

		for(DesenvolvimentoPonderal d: animal.getDesenvolvimentoPonderal()){			
			d.setAnimal(animal);			
		}

		for (Verminose v: animal.getVerminose()) {
			v.setAnimal(animal);
		}

		for (TamanhoCorporal t: animal.getTamanhoCorporal()) {
			t.setAnimal(animal);
		}

		for (Carcaca c: animal.getCarcaca()) {
			c.setAnimal(animal);
		}

	}

	public String reinitVerminose(){
		if (this.verminose.getOvosPorGramaDeFazes()%100 == 0) {			
			this.verminose = new Verminose();
			return null;
		}else{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Valor inválido para OPG!"));
			return null;
		}
	}

	public List<String> completeRebanho(String query) {

		List<String> filteredNamesRebanho = new ArrayList<String>();

		for (int i = 0; i < allRebanhosName.size(); i++) {
			String nome = allRebanhosName.get(i);
			if(nome.startsWith(query)) {
				filteredNamesRebanho.add(nome);
			}
		}

		return filteredNamesRebanho;
	}

	public Rebanho returnRebanho(String nome){
		for (Rebanho r : rebanhos) {
			if(r.getNome().equals(nome)){
				return r;
			}
		}
		return null;
	}

	public void nomeRebanhos(List<Rebanho> rebanhos){
		for (Rebanho rebanho : rebanhos) {
			allRebanhosName.add(rebanho.getNome());
		}
	}

	public String reinitTamanho(){
		this.tamanhoCorporal = new TamanhoCorporal();
		return null;
	}

	public String reinitCarcaca(){
		this.carcaca = new Carcaca();
		return null;
	}

	public Date getTodayDate() {
		Date todayDate = new Date();
		return todayDate;
	}

	public List<String> getNomesRacas() {
		return nomesRacas;
	}

	public void onChangeRabanho(SelectEvent event){
		Rebanho r = returnRebanho(animal.getRebanho().getNome());
		if(r.getCriacao().getDescricao().equals("Caprinos")){
			nomesRacas = new ArrayList<String>();
			for(Raca raca: racas){
				if (raca.getCriacao().getDescricao().equals("Caprinos")) {
					nomesRacas.add(raca.getNome());
				}
			}
		}else if(r.getCriacao().getDescricao().equals("Ovinos")){
			nomesRacas = new ArrayList<String>();
			for(Raca raca: racas){
				if (raca.getCriacao().getDescricao().equals("Ovinos")) {
					nomesRacas.add(raca.getNome());
				}
			}
		}else{
			nomesRacas = new ArrayList<String>();
			for(Raca raca: racas){
				nomesRacas.add(raca.getNome());
			}
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rebanho selecionado: ", animal.getRebanho().getNome()));
	}

	public void setNomesRacas(List<String> nomesRacas) {
		this.nomesRacas = nomesRacas;
	}	

	public DesenvolvimentoPonderal getDesenvolvimentoPonderal() {
		return desenvolvimentoPonderal;
	}

	public void setDesenvolvimentoPonderal(DesenvolvimentoPonderal desenvolvimentoPonderal) {
		this.desenvolvimentoPonderal = desenvolvimentoPonderal;
	}

	public Verminose getVerminose() {
		return verminose;
	}

	public void setVerminose(Verminose verminose) {
		this.verminose = verminose;
	}	

	public TamanhoCorporal getTamanhoCorporal() {
		return tamanhoCorporal;
	}

	public void setTamanhoCorporal(TamanhoCorporal tamanhoCorporal) {
		this.tamanhoCorporal = tamanhoCorporal;
	}

	public Carcaca getCarcaca() {
		return carcaca;
	}

	public void setCarcaca(Carcaca carcaca) {
		this.carcaca = carcaca;
	}

	public String getSexoNome() {
		return sexoNome;
	}

	public void setSexoNome(String sexoNome) {
		this.sexoNome = sexoNome;
	}

	public String getParticaoNome() {
		return particaoNome;
	}

	public void setParticaoNome(String particaoNome) {
		this.particaoNome = particaoNome;
	}

	public String getOrelha() {
		return orelha;
	}

	public void setOrelha(String orelha) {
		this.orelha = orelha;
	}

	public String getPartoNome() {
		return partoNome;
	}

	public void setPartoNome(String partoNome) {
		this.partoNome = partoNome;
	}

	public String getDesenvolvimento() {
		return desenvolvimento;
	}

	public void setDesenvolvimento(String desenvolvimento) {
		this.desenvolvimento = desenvolvimento;
	}

	public List<Animal> getAnimaisPai() {
		return animaisPai;
	}

	public void setAnimaisPai(List<Animal> animaisPai) {
		this.animaisPai = animaisPai;
	}

	public List<Animal> getAnimaisMae() {
		return animaisMae;
	}

	public void setAnimaisMae(List<Animal> animaisMae) {
		this.animaisMae = animaisMae;
	}

	public List<String> getAllAnimalNamesMae() {
		return allAnimalNamesMae;
	}

	public void setAllAnimalNamesMae(List<String> allAnimalNamesMae) {
		this.allAnimalNamesMae = allAnimalNamesMae;
	}

	public List<String> getAllAnimalNamesPai() {
		return allAnimalNamesPai;
	}

	public void setAllAnimalNamesPai(List<String> allAnimalNamesPai) {
		this.allAnimalNamesPai = allAnimalNamesPai;
	}

	public double getCpm() {
		return (desenvolvimentoPonderal.getMusculosidade() + 
				desenvolvimentoPonderal.getConformidade() + 
				desenvolvimentoPonderal.getPrecocidade())/3;
	}

	public void setCpm(double cpm) {
		this.cpm = cpm;
	}

	public List<Rebanho> getRebanhos() {
		return rebanhos;
	}

	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}

	public List<String> getAllRebanhosName() {
		return allRebanhosName;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public void setAllRebanhosName(List<String> allRebanhosName) {
		this.allRebanhosName = allRebanhosName;
	}

	public String getMotivoEntrada() {
		return motivoEntrada;
	}

	public void setMotivoEntrada(String motivoEntrada) {
		this.motivoEntrada = motivoEntrada;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public String getCompGenetico() {
		return compGenetico;
	}

	public void setCompGenetico(String compGenetico) {
		this.compGenetico = compGenetico;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

}
