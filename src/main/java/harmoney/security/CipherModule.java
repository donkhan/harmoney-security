package harmoney.security;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

public class CipherModule {

	private byte[] salt = "@!$#1#12".getBytes();
	private String passPhrase = "ahaitech-passphrase";
	private int iterationCount = 19;

	private Key generateKey(){
		PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray());
		try {
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			return key;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	CipherModule() {
	}
	
	private Cipher getCipher(int mode){
		Key key = generateKey();
		try {
			Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,iterationCount);
			cipher.init(mode, key, paramSpec);
			return cipher;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String encrypt(String str) {
		try {
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = cipher.doFinal(utf8);
			return DatatypeConverter.printBase64Binary(enc);
		} catch (javax.crypto.BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String decrypt(String str) {
		try {
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
			byte[] dec = DatatypeConverter.parseBase64Binary(str);
			byte[] utf8 = cipher.doFinal(dec);
			return new String(utf8, "UTF8");
		} catch (javax.crypto.BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static void main(String[] args) {

		try {
			CipherModule encrypter = new CipherModule(); // key for encryption
			String encrypted = encrypter.encrypt(args[0]);
			PrintWriter out = new PrintWriter(new File("c:\\users\\kkhan\\pw.txt"));
			System.out.println("user name : " + args[0]);
			out.println("user name : " + args[0]);
			System.out.println("encrypted username : " + encrypted);
			out.println("encrypted username : " + encrypted);
			
			encrypted = encrypter.encrypt(args[1]);
			System.out.println("password  : " + args[1]);
			out.println("password  : " + args[1]);
			System.out.println("encrypted password : " + encrypted);
			out.println("encrypted password : " + encrypted);
			out.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}