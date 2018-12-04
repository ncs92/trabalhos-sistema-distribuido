package exercicio2;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author edvaldo
 */
public class FileUtils {
    
    public static String getFileChecksum(File file, String algorithm) throws FileNotFoundException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            FileInputStream fis = new FileInputStream(file);
            DigestInputStream dis = new DigestInputStream(fis, digest);

            // Converte bytes para hex
            StringBuilder result = new StringBuilder();
            for (byte b : dis.getMessageDigest().digest()) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
            
        } catch(NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
}
