package com.example.mauri.myemail;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
/**
 * Created by Mauri on 19/12/2015.
 */
public class CryptoUtil {
    private final String AES_PKCS7_CIPHER = "AES/CBC/PKCS7PADDING";
    private final String RSA_CIPHER = "RSA/ECB/PKCS1Padding";
    private final String PROVIDER = "BC";

    private final String TAG = "Crypto Util";

    private final String IV_BIODATA = "UN$2pIjPR$2pIj12";
    private final String IV_FINAL_BASE64 = "oXs2y13oWEe+0mPYX2OOFA==";
    private final String EKEY_FINAL_BASE64 = "72QWWN1hfhWhLqQneckrRcStu9ZyE+nDVCGXGQqg2Zs=";

    private final String DEFAULT_PRIVATE_KEY = "<RSAKeyValue><Modulus>pnYC9BEi7ZzSKyFFuAajtd+VBO0XAFqEwSvNCK81mjswEYQ4rcfM6fDLhgbPVvQBnT8mX+q+KUzetVKcCgGImw==</Modulus><Exponent>AQAB</Exponent><P>7bQPa1B2dPbSIQZT3gMJMBPk6ge0/vnkPUWoDhy3kAk=</P><Q>s0YfKgNdHrBbJEvje1fIKq1dXz+nqm6ATlG3TKqJNIM=</Q><DP>WbIqZ1pTeMtye1fB6fLHUscPCGikGdr+D0jro3eepNE=</DP><DQ>BBbqaRPwkN2nBpnwk4S6HTcdNSoHkhy5MpAspUQSoy8=</DQ><InverseQ>hKAEifeNnveetJHF7RI33/pdX/d0jEAnltCBPpqalEk=</InverseQ><D>kOrGcXMFxHU1Z0UFLEvjdG/cjThXQQCM+JtIWdonQUgA8eClYKfEvgH4sNxZLBOCzpwaY8k1+ieITNe8pf+p8Q==</D></RSAKeyValue>";
    private final String DEFAULT_PUBLIC_KEY = "<RSAKeyValue><Modulus>pnYC9BEi7ZzSKyFFuAajtd+VBO0XAFqEwSvNCK81mjswEYQ4rcfM6fDLhgbPVvQBnT8mX+q+KUzetVKcCgGImw==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
    private final int KEY_SIZE = 256;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private String encSymmKeyBase64;

    public CryptoUtil(String publicKeyString) {
        this.publicKey = publicKeyFromString(publicKeyString);
    }

    public CryptoUtil(String publicKeyString, String privateKeyString) {
        this.publicKey = publicKeyFromString(publicKeyString);
        this.privateKey = privateKeyFromString(privateKeyString);
    }

    public CryptoUtil() {
        this.publicKey = publicKeyFromString(DEFAULT_PUBLIC_KEY);
        this.privateKey = privateKeyFromString(DEFAULT_PRIVATE_KEY);
    }

    /**
     * AES encryption using the default IV and EKEY
     *
     * @param json The final Json to encrypt
     * @return The encrypted array of bytes
     */
    public byte[] aesEncryptFinalJson(String json) {
        byte[] ivData = Base64.decode(IV_FINAL_BASE64, Base64.NO_WRAP);

        byte[] encryptedText = null;

        try {
            // decode the base64 encoded string
            byte[] decodedKey = Base64.decode(EKEY_FINAL_BASE64, Base64.NO_WRAP);
            // rebuild key using SecretKeySpec
            SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            // AES 256 encrypt of the given text
            Cipher c = Cipher.getInstance(AES_PKCS7_CIPHER, PROVIDER);
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivData));
            encryptedText = c.doFinal(json.getBytes(Charset.forName("UTF-8")));

        } catch (NoSuchProviderException e) {
            logException(e, "No provider -> ");
        } catch (NoSuchAlgorithmException e) {
            logException(e, "Algorithm not found -> ");
        } catch (NoSuchPaddingException e) {
            logException(e, "Padding not found -> ");
        } catch (InvalidAlgorithmParameterException e) {
            logException(e, "Invalid Algorithm -> ");
        } catch (InvalidKeyException e) {
            logException(e, "Invalid key -> ");
        } catch (IllegalBlockSizeException e) {
            logException(e, "Illegal Block size -> ");
        } catch (BadPaddingException e) {
            logException(e, "Bad padding -> ");
        }

        return encryptedText;
    }

    /**
     * AES 256 encrypt of the given array of bytes
     *
     * @param biodataFull Stream to encrypt
     * @return The encrypted array of bytes
     */
    public byte[] aesEncryptBiodata(byte[] biodataFull) {
        byte[] ivData = StringUtils.rightPad(IV_BIODATA, 32, "0").getBytes();

        byte[] encryptedText = null;

        try {
            // AES key generation and RSA encrypt
            SecretKey key = generateAesKey(KEY_SIZE);
            byte[] encSymmKey = rsaEncryptSecretKey(key);
            this.encSymmKeyBase64 = Base64.encodeToString(encSymmKey, Base64.NO_WRAP);


            final PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new RijndaelEngine(256)));
            final CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key.getEncoded()), ivData);
            aes.init(true, ivAndKey);
            final int minSize = aes.getOutputSize(biodataFull.length);
            final byte[] outBuf = new byte[minSize];
            final int length1 = aes.processBytes(biodataFull, 0, biodataFull.length, outBuf, 0);
            final int length2 = aes.doFinal(outBuf, length1);
            final int actualLength = length1 + length2;
            final byte[] biodataDecFull = new byte[actualLength];
            System.arraycopy(outBuf, 0, biodataDecFull, 0, biodataDecFull.length);

            encryptedText = biodataDecFull;

        } catch (InvalidCipherTextException e) {
            logException(e, "InvalidCipher Text -> ");
        } catch (Exception e) {
            logException(e, "Exception -> ");
        }

        return encryptedText;
    }


    /**
     * AES decryption using the biodata IV and the given key
     *
     * @param encrypted AES encrypted array of bytes
     * @param ekey      The array of bytes representing the Aes key
     * @return Decrypted array of bytes
     */
    public byte[] aesDecryptBiodata(byte[] encrypted, byte[] ekey) {
        byte[] ivData = IV_BIODATA.getBytes();
        byte[] decrypted = null;

        try {
            // rebuild key using SecretKeySpec
            SecretKey key = new SecretKeySpec(ekey, 0, ekey.length, "AES");

            // AES 256 decrypt of the given text
            Cipher c = Cipher.getInstance(AES_PKCS7_CIPHER, PROVIDER);
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivData));
            decrypted = c.doFinal(encrypted);

        } catch (NoSuchProviderException e) {
            logException(e, "No padding -> ");
        } catch (NoSuchAlgorithmException e) {
            logException(e, "Algorithm not found -> ");
        } catch (NoSuchPaddingException e) {
            logException(e, "Padding not found -> ");
        } catch (InvalidAlgorithmParameterException e) {
            logException(e, "Invalid Algorithm -> ");
        } catch (InvalidKeyException e) {
            logException(e, "Invalid key -> ");
        } catch (IllegalBlockSizeException e) {
            logException(e, "Illegal Block size -> ");
        } catch (BadPaddingException e) {
            logException(e, "Bad padding -> ");
        }

        return decrypted;
    }

    /**
     * AES decryption using the default IV and EKEY
     *
     * @param encrypted AES encrypted array of bytes representing the base64
     * @return Decrypted array of bytes
     */
    public byte[] aesDecrypt(byte[] encrypted) {
        byte[] ivData = Base64.decode(IV_FINAL_BASE64, Base64.NO_WRAP);
        byte[] decrypted = null;

        try {
            // decode the base64 encoded string
            byte[] decodedKey = Base64.decode(EKEY_FINAL_BASE64, Base64.NO_WRAP);
            // rebuild key using SecretKeySpec
            SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            // AES 256 encrypt of the given text
            Cipher c = Cipher.getInstance(AES_PKCS7_CIPHER, PROVIDER);
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivData));
            decrypted = c.doFinal(encrypted);

        } catch (NoSuchProviderException e) {
            logException(e, "Exception ");
        } catch (NoSuchAlgorithmException e) {
            logException(e, "Algorithm not found -> ");
        } catch (NoSuchPaddingException e) {
            logException(e, "Padding not found -> ");
        } catch (InvalidAlgorithmParameterException e) {
            logException(e, "Invalid Algorithm -> ");
        } catch (InvalidKeyException e) {
            logException(e, "Invalid key -> ");
        } catch (IllegalBlockSizeException e) {
            logException(e, "Illegal Block size -> ");
        } catch (BadPaddingException e) {
            logException(e, "Bad padding -> ");
        }

        return decrypted;
    }

    /**
     * Generate AES key
     *
     * @param keySize The key size
     * @return SecretKey object representing the key
     */
    private SecretKey generateAesKey(int keySize) {
        SecretKey key = null;
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(keySize);
            key = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "AES key generation -> " + e.getMessage());
        }

        return key;
    }

    /**
     * RSA encryption of the secretkey
     *
     * @param secretKey SecretKey object to encrypt
     * @return The encrypted array of bytes
     * @throws NullPointerException If the public key is null
     */
    public byte[] rsaEncryptSecretKey(SecretKey secretKey) throws NullPointerException {
        return rsaEncrypt(secretKey.getEncoded());
    }

    /**
     * RSA encryption of String which return byte array
     * @param string
     * @return
     * @throws NullPointerException
     */
    public byte[] rsaEncryptString(String string) throws NullPointerException {
        return rsaEncrypt(string.getBytes());
    }

    /**
     * RSA encryption of data which return byte array
     * @param input
     * @return
     * @throws NullPointerException
     */
    public byte[] rsaEncrypt(byte[] input) throws NullPointerException{
        if (this.publicKey == null)
            throw new NullPointerException("Public key is null.");

        byte[] cipherText = null;
        try {
            Cipher c = Cipher.getInstance(RSA_CIPHER, PROVIDER);
            c.init(Cipher.ENCRYPT_MODE, this.publicKey);
            cipherText = c.doFinal(input);
        } catch (NoSuchProviderException e) {
            logException(e, "No provider ->");
        } catch (NoSuchAlgorithmException e) {
            logException(e, "Algorithm not found -> ");
        } catch (NoSuchPaddingException e) {
            logException(e, "Padding not found -> ");
        } catch (InvalidKeyException e) {
            logException(e, "Invalid key -> ");
        } catch (IllegalBlockSizeException e) {
            logException(e, "Illegal Block size -> ");
        } catch (BadPaddingException e) {
            logException(e, "Bad padding -> ");
        }

        return cipherText;
    }


    /**
     * RSA decryption of text
     *
     * @param encrypted The encrypted array of bytes
     * @return The decrypted array of bytes
     */
    public byte[] rsaDecrypt(byte[] encrypted) {
        byte[] decryptedText = null;
        try {
            Cipher c = Cipher.getInstance(RSA_CIPHER, PROVIDER);
            c.init(Cipher.DECRYPT_MODE, this.privateKey);
            decryptedText = c.doFinal(encrypted);
        } catch (NoSuchProviderException e) {
            logException(e, "No provider -> ");
        } catch (NoSuchAlgorithmException e) {
            logException(e, "Algorithm not found -> ");
        } catch (NoSuchPaddingException e) {
            logException(e, "Padding not found -> ");
        } catch (InvalidKeyException e) {
            logException(e, "Invalid key -> ");
        } catch (IllegalBlockSizeException e) {
            logException(e, "Illegal Block size -> ");
        } catch (BadPaddingException e) {
            logException(e, "Bad padding -> ");
        }

        return decryptedText;
    }

    /**
     * Simple string encryption with RSA
     * @param toEncrypt
     * @return
     */
    public String simpleStringEncrypt(String toEncrypt){
        byte[] encrypted = rsaEncryptString(toEncrypt);
        String encryptedString = new String(Base64.encode(encrypted, Base64.NO_WRAP));

        return encryptedString;
    }

    /**
     * Simple base64 decryption with RSA
     * @param base64ToDecrypt
     * @return
     */
    public String simpleStringDecrypt(String base64ToDecrypt){
        byte[] encrypted = Base64.decode(base64ToDecrypt, Base64.NO_WRAP);
        byte[] decrypted = rsaDecrypt(encrypted);
        String decryptedString = new String(decrypted);

        return decryptedString;
    }

    /**
     * Gets the base64 string representing the generated and RSA encrypted key
     *
     * @return The base64 encoded string of the encrypted key
     */
    public String getRsaEncryptedKey() {
        return encSymmKeyBase64;
    }

    /**
     * XML string to RSAPublicKey conversion using modulus and exponent
     *
     * @param publicKeyString XML public key
     * @return The RSAPublicKey object
     */
    private RSAPublicKey publicKeyFromString(String publicKeyString) {
        RSAPublicKey publicKey = null;

        try {
            String modulusString = readFromKey(publicKeyString, "Modulus");
            String exponentString = readFromKey(publicKeyString, "Exponent");
            byte[] modulus = Base64.decode(modulusString.getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] publicExponent = Base64.decode(exponentString.getBytes("UTF-8"), Base64.NO_WRAP);

            BigInteger bigMod = new BigInteger(1, modulus);
            BigInteger bigExp = new BigInteger(1, publicExponent);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigMod, bigExp);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            logException(e, "Algorithm not found -> ");
        } catch (InvalidKeySpecException e) {
            logException(e, "Invalid key specs -> ");
        } catch (UnsupportedEncodingException e) {
            logException(e, "Unsupported encoding -> ");
        }

        return publicKey;
    }

    /**
     * Reads the text content of the first element with the given tag
     *
     * @param tag String tag of the element containing the desired value
     * @return Text content of the tag
     */
    private String readFromKey(String keyString, String tag) {
        String content = null;
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(keyString.getBytes()));

            content = parse.getElementsByTagName(tag).item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            logException(e, "Error parsing public key -> ");
        } catch (SAXException e) {
            logException(e, "Sax error with public key -> ");
        } catch (IOException e) {
            logException(e, "IOException with public key parsing -> ");
        }

        return content;
    }

    /**
     * XML string to RSAPrivateKey conversion using all the needed values
     *
     * @param privateKeyString XML private key
     * @return The RSAPrivateKey object
     */
    private RSAPrivateKey privateKeyFromString(String privateKeyString) {
        RSAPrivateKey privateKey = null;

        try {
            String modulusString = readFromKey(privateKeyString, "Modulus");
            String privExpString = readFromKey(privateKeyString,
                    "D");
            String pubExpString = readFromKey(privateKeyString,
                    "Exponent");
            String primePString = readFromKey(privateKeyString,
                    "P");
            String primeQString = readFromKey(privateKeyString,
                    "Q");
            String primeExpPString = readFromKey(privateKeyString,
                    "DP");
            String primeExpQString = readFromKey(privateKeyString,
                    "DQ");
            String crtCoeffString = readFromKey(privateKeyString,
                    "InverseQ");

            byte[] modulus = Base64.decode(modulusString.getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] privateExponent = Base64.decode(privExpString
                    .getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] publicExponent = Base64.decode(pubExpString
                    .getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] primeP = Base64.decode(primePString
                    .getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] primeQ = Base64.decode(primeQString
                    .getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] primeExponentP = Base64.decode(primeExpPString
                    .getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] primeExponentQ = Base64.decode(primeExpQString
                    .getBytes("UTF-8"), Base64.NO_WRAP);
            byte[] crtCoefficient = Base64.decode(crtCoeffString
                    .getBytes("UTF-8"), Base64.NO_WRAP);

            BigInteger bigMod = new BigInteger(1, modulus);
            BigInteger bigExp = new BigInteger(1, privateExponent);
            BigInteger bigPubExp = new BigInteger(1, publicExponent);
            BigInteger bigPrimeP = new BigInteger(1, primeP);
            BigInteger bigPrimeQ = new BigInteger(1, primeQ);
            BigInteger bigExponentP = new BigInteger(1, primeExponentP);
            BigInteger bigExponentQ = new BigInteger(1, primeExponentQ);
            BigInteger bigCrt = new BigInteger(1, crtCoefficient);

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(bigMod, bigPubExp, bigExp, bigPrimeP, bigPrimeQ, bigExponentP, bigExponentQ, bigCrt);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            logException(e, "Algorithm not found -> ");
        } catch (InvalidKeySpecException e) {
            logException(e, "Invalid key specs -> ");
        } catch (UnsupportedEncodingException e) {
            logException(e, "Unsupported encoding -> ");
        }

        return privateKey;
    }

    /**
     * Log the stack trace with a given identifying message
     *
     * @param e       Exception
     * @param message Message to log
     */
    private void logException(Exception e, String message) {
        e.printStackTrace();
        Log.e(TAG, message + e.getMessage());
    }


}
