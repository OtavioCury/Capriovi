package br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import br.ufpi.capriovi.entidades.cadastros.Animal;

public class Endogamia implements Runnable {
	//teste
	public Endogamia() {
		super();
	}

	/**
	 * executa o fortran.
	 */
	public void run() {
		// my pc
		//File file = new File("/var/lib/openshift/59c3bcec7628e19a320000e5/app-root/data/matrizadeendogamia");
		// servidor
		// File file = new File("/home/capriove/fortran-master");
		// win 
		File file = new File("C:\\Users\\Otavio\\Desktop\\Computação\\Capriovi\\matrizadeendogamia\\");
		try {
			// linux
			//String[] cmds = {"/var/lib/openshift/59c3bcec7628e19a320000e5/app-root/data/matrizadeendogamia", "./calcMatrix"};
			// win
			String cmds = "cmd /c start calcMatrix.exe";
			// linux
			//Runtime.getRuntime().exec(cmds, null, file);
			// win
			Runtime.getRuntime().exec(cmds, null, file);
			File fileVer = new File("C:\\Users\\Otavio\\Desktop\\Computação\\Capriovi\\matrizadeendogamia\\output.txt");
			while(!fileVer.exists()){

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * prepara o arquivo de texto contendo o dados filho , pai e m�e. Para a
	 * execu��o do fortran.
	 * 
	 * @author thasciano
	 * @param animais
	 * @throws IOException
	 */
	public void prepareInputFortran(List<Animal> animais) throws IOException {
		// my pc
		// File file = new
		// File("/home/thasciano/Documents/CC/Capriovi/fortran-master/input.txt");
		// servidor
		// File file = new File("/home/capriove/fortran-master/input.txt");
		// win
		File file = new File("C:\\Users\\Otavio\\Desktop\\Computação\\Capriovi\\matrizadeendogamia\\input.txt");

		FileWriter gravaArq = new FileWriter(file, false);
		PrintWriter print = new PrintWriter(gravaArq);
		print.println(animais.size());

		for (int i = 0; i < animais.size(); i++) {
			print.print("" + (i + 1));
			if (animais.get(i).getPai() == null)
				print.print(" 0");
			else {
				for (int j = 0; j < animais.size(); j++) {
					if (animais.get(i).getPai().getId() == animais.get(j).getId())
						print.print(" " + (j + 1));
				}

			}

			if (animais.get(i).getMae() == null)
				print.println(" 0");
			else {
				for (int j = 0; j < animais.size(); j++) {
					if (animais.get(i).getMae().getId() == animais.get(j).getId())
						print.println(" " + (j + 1));
				}
			}

		}
		print.flush();
		print.close();
	}

	/**
	 * le para a memoria a matriz resultante do output do fortran.
	 * 
	 * @author thasciano
	 * @param quantAnimais
	 * @param indiceAnimaisAptos
	 * @return matriz A de endogamia.
	 * @throws IOException
	 */
	public ArrayList<Double[]> getMatrizA(int quantAnimais, ArrayList<Integer> indiceAnimaisAptos) throws IOException {
		// my pc
		// File file = new
		// File("/home/thasciano/Documents/CC/Capriovi/fortran-master/output.txt");
		// servidor
		// File file = new File("/home/capriove/fortran-master/output.txt");
		// win
		File file = new File("C:\\Users\\Otavio\\Desktop\\Computação\\Capriovi\\matrizadeendogamia\\output.txt");
		FileReader arq = new FileReader(file);
		BufferedReader lerArq = new BufferedReader(arq);
		/**
		 * l� a primeira linha, a vari�vel "linha" recebe o valor "null" quando
		 * o processo, de repeti��o atingir o final do arquivo texto.
		 */
		String linha = lerArq.readLine();
		int k = 0, idAux = 0;
		Double[] matrizAux = null;
		ArrayList<Double[]> matrizA = new ArrayList<Double[]>();
		String[] parts;
		while (linha != null) {
			if (indiceAnimaisAptos.get(k) == idAux) {
				matrizAux = new Double[quantAnimais];
				parts = linha.split("   ");
				for (int j = 0; j < quantAnimais; j++) {
					matrizAux[j] = Double.valueOf(parts[j + 1]);
				}
				matrizA.add(matrizAux);
				matrizAux = null;
				k++;
			}
			idAux++;
			linha = lerArq.readLine(); // l� da segunda at� a �ltima linha
		}
		arq.close();
		file.delete();
		return matrizA;

	}

	/**
	 * le para a memoria a matriz resultante do output do fortran.
	 * 
	 * @author thasciano
	 * @param quantAnimais
	 * @param indiceAnimaisAptos
	 * @return matriz A de endogamia.
	 * @throws IOException
	 */
	public ArrayList<Double[]> getAllMatrizA(int quantAnimais) throws IOException {
		// my pc
		// File file = new
		// File("/home/thasciano/Documents/CC/Capriovi/fortran-master/output.txt");
		// servidor
		// File file = new File("/home/capriove/fortran-master/output.txt");
		// win
		File file = new File("C:\\Users\\Otavio\\Desktop\\Computação\\Capriovi\\matrizadeendogamia\\output.txt");

		FileReader arq = new FileReader(file);
		BufferedReader lerArq = new BufferedReader(arq);
		/**
		 * l� a primeira linha, a vari�vel "linha" recebe o valor "null" quando
		 * o processo, de repeti��o atingir o final do arquivo texto.
		 */
		String linha = lerArq.readLine();
		Double[] matrizAux = null;
		ArrayList<Double[]> matrizA = new ArrayList<Double[]>();
		String[] parts;
		while (linha != null) {
			matrizAux = new Double[quantAnimais];
			parts = linha.split("   ");
			for (int j = 0; j < quantAnimais; j++) {
				matrizAux[j] = Double.valueOf(parts[j + 1]);
			}
			matrizA.add(matrizAux);
			matrizAux = null;
			linha = lerArq.readLine(); // l� da segunda at� a �ltima linha
		}
		arq.close();
		file.delete();
		return matrizA;

	}

}
