package br.ufpi.capriovi.managedBean.controleAnimal.manejoReprodutivo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.ManejoReprodutivoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
import br.ufpi.capriovi.suporte.tiposEnum.TipoDesenvolvimentoPonderal;
import br.ufpi.capriovi.suporte.tiposEnum.TipoMotivoEntradaEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoParicao;
import br.ufpi.capriovi.suporte.tiposEnum.TipoPartoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoSexoEnum;

@SessionScoped
@Named(value = "manejoReprodutivoAddEditMB")
public class ManejoReprodutivoAddEdit extends BaseBeans{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5156307229061976822L;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	RebanhoMB rebanhoMB;

	@Inject
	ManejoReprodutivoFacade manejoReprodutivoFacade;

	@Inject
	AnimalFacade animalFacade;

	private ManejoReprodutivo manejoReprodutivo;

	private List<Animal> animaisMae;

	private List<Animal> animaisPai;

	private List<Rebanho> rebanhos;
	//
	private String title;

	private List<Animal> animaisColector;

	private Animal animal;

	private String partoNome;

	private String sexoNome;

	private DesenvolvimentoPonderal desenvolvimentoPonderal;

	private String status;

	private String parto;

	private String paricao;

	private String motivoEntrada;

	private List<String> allAnimalNamesPai;

	private List<String> allAnimalNamesMae;

	private List<String> allRebanhoNames;

	public ManejoReprodutivoAddEdit() {
		super();
		this.manejoReprodutivo = new ManejoReprodutivo();
	}

	public ManejoReprodutivo getManejoReprodutivo() {
		return manejoReprodutivo;
	}

	public void setManejoReprodutivo(ManejoReprodutivo ManejoReprodutivo) {
		this.manejoReprodutivo = ManejoReprodutivo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar um manejo reprodutivo
	 */
	public void add() {
		this.title = "Adicionar Manejo Reprodutivo - Matriz";
		this.manejoReprodutivo = new ManejoReprodutivo();

		inicializa();

		this.status = new String();

		this.parto = new String();

		this.paricao = new String();

		nomeAnimaisMae();

		nomeAnimaisPai();

		nomeRebanhos();
	}

	/**
	 * Método chamado para atualizar um manejo reprodutiva
	 * @param u
	 */
	public void update(ManejoReprodutivo u) {
		this.title = "Atualizar Manejo Reprodutivo";
		this.manejoReprodutivo = u;

		inicializa();		

		if (manejoReprodutivo.getParto() != null) {
			this.parto = this.manejoReprodutivo.getParto().getDescricao();
		}else{
			this.parto = new String();
		}		

		if (manejoReprodutivo.getParicao() != null) {
			this.paricao = this.manejoReprodutivo.getParicao().getDescricao();
		}else{
			this.paricao = new String();
		}		

		nomeAnimaisMae();

		nomeAnimaisPai();

		nomeRebanhos();
	}

	public void delete(Long id) {
		this.manejoReprodutivoFacade.deletarManejoReprodutivo(id);
	}

	public void cancel() {
		this.manejoReprodutivo = new ManejoReprodutivo();
	}

	/**
	 * Método chamado para salvar um manejo reprodutivo
	 */
	public void save() {
		if (this.manejoReprodutivo != null) {
			if (this.manejoReprodutivo.getId() == null) {
				// Add
				paiMaeRebanho();

				enums();

				animalList();

				calculaGestacaoAmamentacao();

				this.manejoReprodutivoFacade.adicionaManejoReprodutivo(manejoReprodutivo);

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Manejo Reprodutivo adicionado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update
				paiMaeRebanho();

				enums();

				animalList();

				calculaGestacaoAmamentacao();

				this.manejoReprodutivoFacade.atualizaManejoReprodutivo(manejoReprodutivo);

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Manejo Reprodutivo atualizado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
	}

	/**
	 * Calcula a gestação de uma manejo
	 */
	private void calculaGestacaoAmamentacao() {
		// TODO Auto-generated method stub
		if (manejoReprodutivo.getDataParto() != null) {
			if (manejoReprodutivo.getDataParto().getTime() > 
			manejoReprodutivo.getDataDaCobertura().getTime()) {
				long data1 = manejoReprodutivo.getDataParto().getTime();
				long data2 = manejoReprodutivo.getDataDaCobertura().getTime();
				long diferenca = (data1 - data2)/(1000 * 60 * 60 * 24);
				manejoReprodutivo.setGestacao((int) diferenca);
			}else{
				manejoReprodutivo.setGestacao(0);
			}
		}		
		if (manejoReprodutivo.getDataParto() != null && manejoReprodutivo.getDesmame() != null) {
			if (manejoReprodutivo.getDataParto().getTime() < 
					manejoReprodutivo.getDesmame().getTime()) {
				long data1 = manejoReprodutivo.getDesmame().getTime();
				long data2 = manejoReprodutivo.getDataParto().getTime();
				long diferenca = (data1 - data2)/(1000 * 60 * 60 * 24);
				manejoReprodutivo.setAmamentacao((int) diferenca);
			}else{
				manejoReprodutivo.setAmamentacao(0);
			}
		}
	}

	/**
	 * Método chamado no autocomplete 'Mãe' 
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
	 * Método chamado no autocomplete 'Pai'
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
	 * Método para popular uma lista com os nomes das femêas
	 */
	public void nomeAnimaisMae(){
		for (Animal a: this.animaisMae) {
			this.allAnimalNamesMae.add(a.getNomeNumero());
		}
	}

	/**
	 * Método para popular uma lista com os nomes dos machos
	 */
	public void nomeAnimaisPai(){
		for (Animal a: this.animaisPai) {
			this.allAnimalNamesPai.add(a.getNomeNumero());
		}
	}

	/**
	 * Método que retorna o objeto animal de acordo com o nome
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

	/**
	 * Método que retorna o objeto animal de acordo com o nome
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
	 * Método chamado no autocomplete 'Rebanho'
	 * @param query
	 * @return
	 */
	public List<String> completeRebanho(String query) {

		List<String> filteredNamesRebanho = new ArrayList<String>();

		for (int i = 0; i < allRebanhoNames.size(); i++) {
			String nome = allRebanhoNames.get(i);
			if(nome.startsWith(query)) {
				filteredNamesRebanho.add(nome);
			}
		}

		return filteredNamesRebanho;
	}

	/**
	 * Método que popula uma lista com os nomes dos rebanhos
	 */
	public void nomeRebanhos(){
		for (Rebanho r: this.rebanhos) {
			this.allRebanhoNames.add(r.getNome());
		}
	}

	/**
	 * Retorna um objeto rebanhos de acordo com um nome
	 * @param nome
	 * @return
	 */
	public Rebanho returnRebanho(String nome){
		for (Rebanho r : this.rebanhos) {
			if(r.getNome().equals(nome)){
				return r;
			}
		}
		return null;
	}

	/**
	 * Método chamado para inicializar os atributos do Bean
	 */
	public void inicializa() {

		this.rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());

		this.animaisPai = animalFacade.listAllSexo(rebanhos, 1);

		this.animaisMae = animalFacade.listAllSexo(rebanhos, 2);

		this.animal = new Animal();

		this.partoNome = new String();

		this.desenvolvimentoPonderal = new DesenvolvimentoPonderal();

		this.animaisColector = new ArrayList<Animal>();

		this.allAnimalNamesMae = new ArrayList<String>();

		this.allAnimalNamesPai = new ArrayList<String>();

		this.allRebanhoNames = new ArrayList<String>();
	}

	public List<Rebanho> getRebanhos() {
		return rebanhos;
	}

	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}

	public Date getTodayDate() {
		Date todayDate = new Date();
		return todayDate;
	}

	public List<Animal> getAnimaisColector() {
		return animaisColector;
	}

	public void setAnimaisColector(List<Animal> animaisColector) {
		this.animaisColector = animaisColector;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	/**
	 * Método chamado para salvar em uma lista e reiniciar o objeto animal 
	 * @return
	 */
	public String reinitAnimal(){

		if (animalFacade.nomePresente(this.animal, rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()), true)) {
			if (this.partoNome.equals("Simples")) {
				this.animal.setParto(TipoPartoEnum.getEnumByCodigo(1));
			}else if (this.partoNome.equals("Duplo")) {
				this.animal.setParto(TipoPartoEnum.getEnumByCodigo(2));
			}else if (this.partoNome.equals("Triplo")) {
				this.animal.setParto(TipoPartoEnum.getEnumByCodigo(3));
			}else if (this.partoNome.equals("Quádruplo")) {
				this.animal.setParto(TipoPartoEnum.getEnumByCodigo(4));
			}

			if(this.sexoNome.equals("Macho")){
				this.animal.setSexo(TipoSexoEnum.getEnumByCodigo(1));
			}else if (this.sexoNome.equals("Fêmea")){
				this.animal.setSexo(TipoSexoEnum.getEnumByCodigo(2));
			}

			if(this.motivoEntrada.equals("Compra")){
				this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(1));
			}else if (this.sexoNome.equals("Nascimento")){
				this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(2));
			}else if (this.sexoNome.equals("Emprestimo")){
				this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(3));
			}else if (this.sexoNome.equals("Outros")){
				this.animal.setMotivoEntrada(TipoMotivoEntradaEnum.getEnumByCodigo(4));
			}

			this.desenvolvimentoPonderal.setAnimal(this.animal);
			this.desenvolvimentoPonderal.setTipoDesenvolvimento(TipoDesenvolvimentoPonderal.getEnumByCodigo(1));

			this.animal.getDesenvolvimentoPonderal().add(this.desenvolvimentoPonderal);


			this.partoNome = new String();

			this.sexoNome = new String();

			this.motivoEntrada = new String();

			this.desenvolvimentoPonderal = new DesenvolvimentoPonderal();

			this.animal = new Animal();
		}else{
			animaisColector.remove(this.animal);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Já existe uma animal com o mesmo Nome/Número cadastrado nesta fazenda!"));
		}


		return null;
	}

	/**
	 * Método chamado no hora de salvar um manejo reprodutivo que seta os enuns do manejo reprodutivo
	 */
	public void enums(){		

		if (this.parto.equals("Simples")) {
			this.manejoReprodutivo.setParto(TipoPartoEnum.getEnumByCodigo(1));
		}else if (this.parto.equals("Duplo")) {
			this.manejoReprodutivo.setParto(TipoPartoEnum.getEnumByCodigo(2));
		}else if (this.parto.equals("Triplo")) {
			this.manejoReprodutivo.setParto(TipoPartoEnum.getEnumByCodigo(3));
		}else if (this.parto.equals("Quádruplo")) {
			this.manejoReprodutivo.setParto(TipoPartoEnum.getEnumByCodigo(4));
		}

		if (this.paricao.equals("Sim")) {
			this.manejoReprodutivo.setParicao(TipoParicao.getEnumByCodigo(1));
		}else if (this.paricao.equals("Não")) {
			this.manejoReprodutivo.setParicao(TipoParicao.getEnumByCodigo(2));
		}else if (this.paricao.equals("Em andamento")) {
			this.manejoReprodutivo.setParicao(TipoParicao.getEnumByCodigo(3));
		}
	}

	public String getPartoNome() {
		return partoNome;
	}

	public void setPartoNome(String partoNome) {
		this.partoNome = partoNome;
	}

	public String getSexoNome() {
		return sexoNome;
	}

	public void setSexoNome(String sexoNome) {
		this.sexoNome = sexoNome;
	}

	public DesenvolvimentoPonderal getDesenvolvimentoPonderal() {
		return desenvolvimentoPonderal;
	}

	public void setDesenvolvimentoPonderal(DesenvolvimentoPonderal desenvolvimentoPonderal) {
		this.desenvolvimentoPonderal = desenvolvimentoPonderal;
	}

	/**
	 * Salva a lista de filhos 
	 */
	public void animalList(){
		for (Animal a : this.animaisColector) {
			a.setMae(this.manejoReprodutivo.getMatriz());
			a.setPai(this.manejoReprodutivo.getReprodutor());
			a.setRebanho(this.manejoReprodutivo.getRebanho());			
			a.setRaca(null);
			a.setDataEntrada(new Date());			
			animalFacade.adicionaAnimal(a);			
			if (a.getSexo().getDescricao().equals("Macho")) {
				this.manejoReprodutivo.atualizaMacho();
			}
			if(a.getSexo().getDescricao().equals("Fêmea")){
				this.manejoReprodutivo.atualizaFemea();
			}
		}
	}

	/**
	 * Salva a mãe, pai e rebanho de um manejo reprodutivo
	 */
	public void paiMaeRebanho(){
		this.manejoReprodutivo.setReprodutor(returnAnimalPai(this.manejoReprodutivo.getReprodutor().getNomeNumero()));
		this.manejoReprodutivo.setMatriz(returnAnimalMae(this.manejoReprodutivo.getMatriz().getNomeNumero()));
		this.manejoReprodutivo.setRebanho(returnRebanho(this.manejoReprodutivo.getRebanho().getNome()));

		if(this.manejoReprodutivo.getRebanho() == null){
			this.manejoReprodutivo.setRebanho(null);
		}
		if(this.manejoReprodutivo.getMatriz() == null){
			this.manejoReprodutivo.setMatriz(null);
		}
		if(this.manejoReprodutivo.getReprodutor() == null){
			this.manejoReprodutivo.setReprodutor(null);
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParto() {
		return parto;
	}

	public void setParto(String parto) {
		this.parto = parto;
	}

	public String getParicao() {
		return paricao;
	}

	public void setParicao(String paricao) {
		this.paricao = paricao;
	}

	public List<String> getAllRebanhoNames() {
		return allRebanhoNames;
	}

	public void setAllRebanhoNames(List<String> allRebanhoNames) {
		this.allRebanhoNames = allRebanhoNames;
	}

	public void onChangeRabanho(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Rebanho selecionado: ", manejoReprodutivo.getRebanho().getNome()));
	}

	public void onChangeMatriz(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Matriz selecionada: ", manejoReprodutivo.getMatriz().getNomeNumero()));
	}

	public void onChangeReprodutor(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Reprodutor selecionado: ", manejoReprodutivo.getReprodutor().getNomeNumero()));
	}

	public String getMotivoEntrada() {
		return motivoEntrada;
	}

	public void setMotivoEntrada(String motivoEntrada) {
		this.motivoEntrada = motivoEntrada;
	}
}
