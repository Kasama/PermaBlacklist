package com.kasama.permablacklist;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.SecureRandom;

public class SessionManager {
	private SecureRandom rnd;

	public SessionManager() {
		rnd = new SecureRandom();
		rnd.setSeed(System.nanoTime());
	}

	public String generateKey(){
		return new BigInteger(130, rnd).toString(32);
	}

	public String encode(String key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256_HMAC.init(secret_key);

		return Base64.encode(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
	}

}
