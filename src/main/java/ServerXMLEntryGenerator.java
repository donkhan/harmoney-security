import harmoney.security.CipherModule;


public class ServerXMLEntryGenerator {

	public static void main(String[] args) {
		try {
			CipherModule module = new CipherModule(); 
			String uencrypted = module.encrypt(args[0]);
			String pencrypted = module.encrypt(args[1]);
			System.out.print(" username=\"" + uencrypted + "\"");
			System.out.print(" password=\"" + pencrypted + "\"");
			System.out.print(" factory=\"harmoney.security.EncryptedDataSourceFactory\" " );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
