package br.ufpi.capriovi.facade.relatorios;

import java.io.File;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;

import br.ufpi.capriovi.suporte.relatorios.ResumoCPM;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class RelatoriosFacade {   
	public static final int	RELATORIO_PDF							= 1;
	public static final int	RELATORIO_EXCEL						= 2;
	public static final int	RELATORIO_PLANILHA_OPEN_OFFICE	= 3;

	public StreamedContent gerarRelatorio(List<ResumoCPM> resumoCpm, String nomeRelatorioJasper, String nomeRelatorioSaida) throws MensagensExceptions {    



		StreamedContent arquivoRetorno = null;

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			String caminhoRelatorio = context.getExternalContext().getRealPath("relatorios");
			String caminhoArquivoJasper = caminhoRelatorio + File.separator + nomeRelatorioJasper + ".jasper";

			JasperReport relatorioJasper = (JasperReport) JRLoader.loadObject(new File(caminhoArquivoJasper));
			JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, null, new JRBeanCollectionDataSource(resumoCpm));


			String tmp = System.getProperty("java.io.tmpdir"); // obtem o caminho do diretorio temporário do SO

			JasperExportManager.exportReportToPdfFile(impressoraJasper, tmp + "/"+nomeRelatorioSaida+".pdf"); // exporta o relatório em PDF para o diretório temporário

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arquivoRetorno;
	}
}
