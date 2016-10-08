package helvidios.uva.utils;

import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;

public class AES {
  private String password = "IwAn__t2?^Visit,,SF";
  private Cipher cipher;
  private SecretKeySpec secretKey;
  private final String FMT = "UTF-8";

  public AES() throws Exception{
    byte[] key = Arrays.copyOf(MessageDigest.getInstance("SHA-1").digest(password.getBytes(FMT)), 16); // generate 128 bit key
    cipher = Cipher.getInstance("AES");
    secretKey = new SecretKeySpec(key, "AES");
  }

  public String encrypt(String plainText) {
    try {
         cipher.init(Cipher.ENCRYPT_MODE, secretKey);
         byte[] cipherText = cipher.doFinal(plainText.getBytes(FMT));
         String encryptedText = javax.xml.bind.DatatypeConverter.printBase64Binary(cipherText);
         //System.out.printf("(plain)'%s' => (encrypt)'%s'\n", plainText, encryptedText);
         return encryptedText;
     } catch (Exception e) {
         System.out.printf("Unable to encrypt. Cause: %s.\n", e.getMessage());
     }
     return plainText;
  }

  public String decrypt(String encryptedText) {
    try {
         cipher.init(Cipher.DECRYPT_MODE, secretKey);
         //System.out.printf("Decrypt '%s'\n", encryptedText);
         return new String(cipher.doFinal(
            javax.xml.bind.DatatypeConverter.parseBase64Binary(encryptedText)
         ),FMT);
     } catch (Exception e) {
         System.out.printf("Unable to decrypt. Cause: %s.\n", Utils.getStackTrace(e));
     }
     return encryptedText;
  }
}
