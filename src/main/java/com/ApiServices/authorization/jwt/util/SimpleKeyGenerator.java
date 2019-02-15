package com.ApiServices.authorization.jwt.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * 
 * @author nitish.k --
 */

public class SimpleKeyGenerator implements KeyGenerator {

	// ======================================
	// = Business methods =
	// ======================================

	@Override
	public String generateKey() {
		// String keyString = "traceon";

		// SecretKeySpec(byte[] key, int offset, int len, String algorithm)
		// Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "HmacSHA256");

		String filenameStr = "traceon";

		File filename = new File(filenameStr);
		 
			 
				final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
				final byte[] secretBytes = secret.getEncoded();
				FileOutputStream out;
				try {
					out = new FileOutputStream(filename, false);
					out.write(secretBytes);
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			
				final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);
				System.out.println("created / updated  file and saved " + base64SecretBytes);
				return base64SecretBytes;
			 
	 
 

	}
	
	@Override
	public String retriveKey() {
		// String keyString = "traceon";

		// SecretKeySpec(byte[] key, int offset, int len, String algorithm)
		// Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "HmacSHA256");

		String filenameStr = "traceon";

		File filename = new File(filenameStr);
		if (!filename.exists()) {
			return null;
		} else {
			System.out.println("File already exists");
			byte[] secretBytes = null;
			try {
				secretBytes = Files.readAllBytes(Paths.get("", filenameStr));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);
			System.out.println("already exists secret key " + base64SecretBytes);
			return base64SecretBytes;

		}
		 

	}
}