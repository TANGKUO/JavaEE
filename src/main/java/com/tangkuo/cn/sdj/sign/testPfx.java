package com.tangkuo.cn.sdj.sign;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import sun.misc.BASE64Encoder;
/**
 * 
* @ClassName: testPfx
* @Description: (pdf证书读取工具类)
* @author tangkuo
* @date 2019年5月
*
 */

public class testPfx {
	public testPfx() {
	}

	// 转换成十六进制字符串
	public static String Byte2String(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	public static byte[] StringToByte(int number) {
		int temp = number;
		byte[] b = new byte[4];
		for (int i = b.length - 1; i > -1; i--) {
			b[i] = new Integer(temp & 0xff).byteValue();// 将最高位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	public PrivateKey GetPvkformPfx(String strPfx, String strPassword) {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream fis = new FileInputStream(strPfx);
			// If the keystore password is empty(""), then we have to set
			// to null, otherwise it won't work!!!
			char[] nPassword = null;
			if ((strPassword == null) || strPassword.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = strPassword.toCharArray();
			}
			ks.load(fis, nPassword);
			fis.close();
			System.out.println("keystore type=" + ks.getType());
			// Now we loop all the aliases, we need the alias to get keys.
			// It seems that this value is the "Friendly name" field in the
			// detals tab <-- Certificate window <-- view <-- Certificate
			// Button <-- Content tab <-- Internet Options <-- Tools menu
			// In MS IE 6.
			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements())// we are readin just one certificate.
			{
				keyAlias = (String) enumas.nextElement();
				System.out.println("alias=[" + keyAlias + "]");
			}
			// Now once we know the alias, we could get the keys.
			System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
			PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			Certificate cert = ks.getCertificate(keyAlias);
			PublicKey pubkey = cert.getPublicKey();
			System.out.println("cert class = " + cert.getClass().getName());
			System.out.println("cert = " + cert);
			System.out.println("public key = " + pubkey);
			System.out.println("private key = " + prikey);
			return prikey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// String strPfx = "C:\\Users\\Administrator\\Documents\\Tencent
		// Files\\811970895\\FileRecv\\777290058142051.pfx";
		// String strPassword ="000000";
//    	 String strPfx = "D:\\Tencent\\client1.pfx";
//    	 String strPassword ="123456";

//    	 String strPfx = "E:\\online\\online-vidata-fe\\trunk\\cer\\0154403695800.pfx";
//    	 String strPassword ="c123456";

//    	 String strPfx = "D:\\Tencent\\client7(2).pfx";
//    	 String strPassword ="123456";

		String strPfx = "D:\\Tencent\\jiaoyi.pfx";
		String strPassword = "123456";

		KeyStore ks = KeyStore.getInstance("PKCS12");
		FileInputStream fis = new FileInputStream(strPfx);
		// If the keystore password is empty(""), then we have to set
		// to null, otherwise it won't work!!!
		char[] nPassword = null;
		if ((strPassword == null) || strPassword.trim().equals("")) {
			nPassword = null;
		} else {
			nPassword = strPassword.toCharArray();
		}
		ks.load(fis, nPassword);
		fis.close();
		System.out.println("keystore type=" + ks.getType());
		// Now we loop all the aliases, we need the alias to get keys.
		// It seems that this value is the "Friendly name" field in the
		// detals tab <-- Certificate window <-- view <-- Certificate
		// Button <-- Content tab <-- Internet Options <-- Tools menu
		// In MS IE 6.
		Enumeration<String> enumas = ks.aliases();
		String keyAlias = null;
		if (enumas.hasMoreElements())// we are readin just one certificate.
		{
			keyAlias = (String) enumas.nextElement();
			System.out.println("alias=[" + keyAlias + "]");
		}
		// Now once we know the alias, we could get the keys.
		// System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
		PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
		Certificate cert = ks.getCertificate(keyAlias);

		PublicKey pubkey = cert.getPublicKey();
		System.out.println("cert class = " + cert.getClass().getName());
		System.out.println("cert = " + cert);
		System.out.println("public key = " + pubkey);
		System.out.println("private key = " + prikey);
		BASE64Encoder base64Encoder = new BASE64Encoder();
		String prikeyStr = base64Encoder.encode(prikey.getEncoded());
		System.out.println("私钥 = " + prikeyStr);

		X509Certificate x509Certificate = (X509Certificate) cert;
		System.out.println("读取Cer证书信息...");
		System.out.println("x509Certificate_SerialNumber_序列号___:" + x509Certificate.getSerialNumber());
		System.out.println("x509Certificate_SerialNumber_序列号___16:" + x509Certificate.getSerialNumber().toString(16));
		System.out.println("x509Certificate_getIssuerDN_发布方标识名___:" + x509Certificate.getIssuerDN());
		System.out.println("x509Certificate_getSubjectDN_主体标识___:" + x509Certificate.getSubjectDN());
		System.out.println("x509Certificate_getSubjectDN_主体标识___:" + x509Certificate.getSubjectDN().getName());
		System.out.println("x509Certificate_getSigAlgOID_证书算法OID字符串___:" + x509Certificate.getSigAlgOID());
		System.out.println("x509Certificate_getSigAlgName_签名算法___:" + x509Certificate.getSigAlgName());
		System.out.println("x509Certificate_getVersion_版本号___:" + x509Certificate.getVersion());
		System.out.println("x509Certificate_getPublicKey_公钥___:" + x509Certificate.getPublicKey());
		System.out.println("证书有效期起始日：" + x509Certificate.getNotBefore());
		System.out.println("证书有效期截至日 ：" + x509Certificate.getNotAfter());
		System.out.println("类型：" + x509Certificate.getType());
		byte[] sig = x509Certificate.getSignature();
		System.out.println("签名：" + new BigInteger(sig).toString(16));
		PublicKey publicKey = x509Certificate.getPublicKey();

		String publicKeyString = base64Encoder.encode(publicKey.getEncoded());

		/*
		 * byte[] pkenc = pk.getEncoded(); for (byte b : pkenc) {
		 * System.out.println("b:"+b); }
		 */
		System.out.println("----公钥--    :    " + publicKeyString);

	}

}
