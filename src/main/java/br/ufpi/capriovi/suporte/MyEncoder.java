package br.ufpi.capriovi.suporte;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MyEncoder {
	public static String encriptar(String senha) throws NoSuchAlgorithmException {		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(senha.getBytes(StandardCharsets.UTF_8));
		byte[] digest = md.digest();
		return new String(Hex.encodeHexString(digest));
	}
}
