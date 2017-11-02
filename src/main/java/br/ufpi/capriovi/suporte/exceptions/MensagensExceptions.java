package br.ufpi.capriovi.suporte.exceptions;

public class MensagensExceptions extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1275415246255957738L;

	public MensagensExceptions(String message) {
		super(message);
	}
	/**
	 * Mensagens Exceptions Animal
	 */
	public static String DataNascimento = "A data de nascimento deve ser anterior ou igual à data atual.";
	public static String DataNascimentoAnimalPai = "Data de Nascimento do Animal deve ser posterior a Data de Nascimento do Pai.";
	public static String DataNascimentoAnimalMae = "Data de Nascimento do Animal deve ser posterior a Data de Nascimento da Mãe.";
	public static String DataNascimentoDesmame = "A Data de Nascimento deve ser anterior a Data de Desmame.";
	public static String DataNascimentoDataEntrada = "A Data de Nascimento deve ser anterior a Data de Entrada.";
	public static String NomeAnimalExistenteException = "Já existe um animal com o mesmo Nome/Número cadastrado nesta fazenda!";
	public static String PaiInexistenteException = "O Pai do animal deve ser preenchido.";
	public static String Nome_DataNascimento_Pai = "Os campos Nome e Data de Nascimento do Pai devem ser preenchidos.";
	public static String DataNascimentoPaiException = "O campo Data de Nascimento do Pai deve ser preenchido.";
	public static String NomePaiException = "O campo Nome do Pai deve ser preenchido.";
	public static String NomePaiExistenteException = "Nome do Pai inválido. Existe um animal cadastrado com este nome, para o rebanho selecionado.";
	public static String MaeInexistenteException = "A Mãe do animal deve ser preenchido.";
	public static String Nome_DataNascimento_Mae = "Os campos Nome e Data de Nascimento da Mãe devem ser preenchidos.";
	public static String DataNascimentoMaeException = "O campo Data de Nascimento da Mãe deve ser preenchido.";
	public static String NomeMaeInexistenteException = "O campo Nome da Mãe deve ser preenchido.";
	public static String NomeMaeExistenteException = "Nome da Mãe inválido. Existe um animal cadastrado com este nome, para o rebanho selecionado.";

	/**
	 * Usuario
	 */
	public static String NomeTamanhoMinimoException = "O Campo Nome deve ser preenchido com no mínimo 5 caracteres.";
	public static String NomeCaracInvalidoException = "O Campo Nome deve ser preenchido com caracteres válidos.";
	public static String EmailExistenteException = "Existe um usuário cadastrado com este E-mail.";
	public static String UsernameExistenteException = "Login indiponível!";
	public static String EmailInvalidoException = "Este e-mail não é válido.";
	public static String CpfInvalidoException = "O campo CPF deve ser preenchido com um valor válido.";
	public static String CpfExistenteException = "Existe um usuário cadastrado com este CPF.";
	public static String CpfExistenteFazendaException = "Existe uma fazenda cadastrado com este CPF.";
	public static String LoginExistenteException = "Existe um usuário cadastrado com este Login.";
	public static String LoginMinimoException = "O Campo Login deve ser preenchido com no mínimo 5 caracteres.";
	public static String LoginInvalidoException = "O Campo Login deve ser preenchido com caracteres válido.";
	public static String SenhaMinimoException = "O Campo Senha deve ser preenchido com no mínimo 5 caracteres.";
	public static String SenhaInvalidoException = "O Campo Senha deve ser preenchido com caracteres válidos.";
	public static String EmailCpfInvalidos = "Email ou cpf inválidos.";
	public static String UsuarioNaoCadastrado = "Você não está cadastrado no Capriovi!";

	/**
	 * Fazenda
	 */
	public static String NomeFazendaMinimoException = "O Nome da Fazenda deve ter no mínimo 5 caracteres.";
	public static String NomeFazendaExistenteException = "O Pecuarista selecionado já possui uma fazenda cadastrada com este nome.";
	public static String NomeProprietarioMinimoException = "O Nome do Proprietário deve ter no mínimo 5 caracteres.";
	public static String NomeProprietarioInvalidoException = "O Nome do Proprietário ser preenchido com caracteres válidos.";
	public static String CnpjExistenteException = "Existe uma fazenda cadastrada com este CNPJ.";
	public static String CnpjInvalidoException = "O Campo Cnpj deve ser preenchido com um valor válido.";
	public static String CnpjOuCpfException = "Os campos CNPJ e/ou CPF devem ser preenchidos.";

	public static String NomeProdMinimoException = "O Nome do Produtor deve ter no mínimo 5 caracteres.";
	/**
	 * Rebanho
	 */
	public static String NomeRebanhoMinimoException = "O nome do Rebanho deve ter no mínimo 5 caracteres.";
	public static String NomeRebanhoExistenteException = "Existe um Rebanho cadastrado com esse Nome para a Fazenda Selecionada.";

	public static String NomeMinimoException = "O Nome deve ter no mínimo 3 caracteres.";
	public static String NomeInvalidoException = "O Nome não deve ter caracteres inválidos.";

	public static String CPMInvalidaException = "O Valor do CPM deve ser entre 1 e 5.";
	public static String DataPesagemException = "A Data da Pesagem não pode ser antes da Data do Parto.";
	public static String DataSecagemException = "A Data da Secagem não pode ser antes da Data do Parto.";
	public static String porcentagemGorduraException = "A Porcentagem de Gordura deve estar entre 1% a 100%.";
	public static String porcentagemProteinaException = "A Porcentagem de Proteína deve estar entre 1% a 100%.";

	public static String DataPartoException = "A Data do Parto deve ser preenchida.";
	public static String DataPartoBeforeCoberturaException = "A Data do Parto não deve ser anterior a Data de Cobertura.";
	public static String CriasMachoException = "Deve-se informar a quantidade de Crias Macho.";
	public static String CriasFemeaException = "Deve-se informar a quantidade de Crias Fêmea.";

	public static String QuantidadeLimiteFazendaException = "Você excedeu seu limite de Fazendas. Entre em contato com o Administrador.";
	public static String SenhaAntigaIncorretaException = "Senha Antiga Incorreta. Tente Novamente.";
	public static String NovaSenhaDiferenteConfirmacaoException = "Nova Senha Diferente da Confirmação de Senha.";

	public static String PesoAoNascerException = "Esse animal ainda não possui um Desenvolvimento Ponderal no Periodo Ao Nascer.";
	public static String PesoDesmameException = "Esse animal ainda não possui um Desenvolvimento Ponderal no Periodo Ao Desmame.";
	public static String DataPesoDesmameException = "A Data do Desenvolvimento do Periodo Ao Desmame deve ser depois do Periodo Ao Nascer.";
	public static String Peso1AnoException = "Esse animal ainda não possui um Desenvolvimento Ponderal no Periodo 1 Ano de Idade.";
	public static String ScoreCorporalMatrizInvalidaException = "O Valor do Score Corporal deve ser entre 1 e 5.";

	/**
	 * Relatorios
	 */
	public static String RebanhoNaoSelecionadoException = "Não foi selecionado rebanho. Por favor, selecione um rebanho para executar o relatorio.";
	public static String AnimalNaoSelecionadoException = "Não foi selecionado Animal. Por favor, selecione um animal para executar o relatorio.";
	public static String DataInicioMaiorDataFinalException = "Data inicial maior que data final.";
	public static String DataInicioMaiorDataAtualException = "Data inicial maior que data atual.";
	public static String DataFimMaiorDataAtualException = "Data final maior que data atual.";

	/**
	 * indices
	 */
	public static String MachosNaoSelecionadoException = "Não foi possivel continuar com a seleção, por favor adicione machos para acasalar.";
	public static String FemeasNaoSelecionadoException = "Não foi possivel continuar com a seleção, por favor adicione femeas para acasalar.";
	public static String MachosInsuficienteException = "Não foi possivel selecionar machos para essa configuração de melhoramento genético.";
	public static String FemeasInsuficienteException = "Não foi possivel selecionar femeas para essa configuração de melhoramento genético.";
	public static String MachoseFemeasInsuficienteException = "Não foi possivel selecionar machos e femeas para essa configuração de melhoramento genético.";

	/**
	 * Erro arquivo
	 */
	public static String ErroArquivo = "Não foi possível carregar o arquivo.";

}
