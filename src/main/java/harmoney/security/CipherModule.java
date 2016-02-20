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

import org.apache.commons.codec.binary.Base64;

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
			//return DatatypeConverter.printBase64Binary(enc);
			byte[] encodedData = Base64.encodeBase64(enc);
			return new String(encodedData);
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
			//byte[] dec = DatatypeConverter.parseBase64Binary(str);
			byte[] dec = Base64.decodeBase64(str.getBytes());
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
			CipherModule module = new CipherModule(); 
			String encrypted = module.encrypt(args[0]);
			
			System.out.println("user name : " + args[0]);
			System.out.println("encrypted username : " + encrypted);
			System.out.println("decryped back as for verification " + module.decrypt(encrypted));
			
			
			encrypted = module.encrypt(args[1]);
			System.out.println("password  : " + args[1]);
			System.out.println("encrypted password : " + encrypted);
			System.out.println("decryped back as for verification " + module.decrypt(encrypted));
			

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}