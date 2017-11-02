package br.ufpi.capriovi.suporte;

import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.view.facelets.ResourceResolver;

/**
 * @author thasciano
 *Classe para alteração das paginas xhtml sem precisar fazer redepoly.
 */
@SuppressWarnings("deprecation")
public class FilesystemResourceResolver extends ResourceResolver {

	private static final String PATH_TO_FACELETS_FILES_GOES_HERE = "/home/workspace/caprioviFinal/Capriovi/src/main/webapp";

	@Override
	public URL resolveUrl(String path) {
		try {
			return new URL("file", "", PATH_TO_FACELETS_FILES_GOES_HERE + path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}