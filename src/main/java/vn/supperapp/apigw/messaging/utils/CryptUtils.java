package vn.supperapp.apigw.messaging.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class CryptUtils {
    private static final Logger logger = LoggerFactory.getLogger(CryptUtils.class.getName());
    private static final int RSA_KEY_SIZE = 2048;

    private static String DERtoString(byte[] bytes) {
        ByteArrayOutputStream pemStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(pemStream);

        byte[] stringBytes = encodeBase64(bytes).getBytes();
        String encoded = new String(stringBytes);
        encoded = encoded.replace("\r", "");
        encoded = encoded.replace("\n", "");

        int i = 0;
        while ((i + 1) * 64 <= encoded.length()) {
            writer.print(encoded.substring(i * 64, (i + 1) * 64));
            i++;
        }
        if (encoded.length() % 64 != 0) {
            writer.print(encoded.substring(i * 64));
        }
        writer.flush();
        byte[] b = pemStream.toByteArray();
        String rsaKey = new String(b);
        return rsaKey;
    }

    public static String encodeBase64(byte[] bytes) {
        return new String(org.apache.mina.util.Base64.encodeBase64(bytes));
    }

    public static String[] generateRSAKeys() {
        String[] keysResult = null;
        try {
            SecureRandom sr = new SecureRandom();

            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(RSA_KEY_SIZE, sr);

            KeyPair kp = kpg.genKeyPair();
            PublicKey pubKey = kp.getPublic();
            PrivateKey priKey = kp.getPrivate();

            keysResult = new String[2];
            keysResult[0] = DERtoString(pubKey.getEncoded());
            keysResult[1] = DERtoString(priKey.getEncoded());

        } catch (Exception ex) {
            logger.error("#generateKeys - EXCEPTION: ", ex);
        }

        return keysResult;
    }

    public static String encryptRSA(String plainData, String publicKeyText) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
            PublicKey pubKey = (PublicKey) keyFactory.generatePublic(publicKeySpec);
            // Mã hoá dữ liệu
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.ENCRYPT_MODE, pubKey);
            byte encryptedByte[] = c.doFinal(plainData.getBytes());
            String encrypted = Base64.encodeBase64String(encryptedByte);
            return encrypted;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decryptRSA(String dataEncrypted, String privateKeyText) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
            RSAPrivateKey _privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, _privateKey);
            dataEncrypted = dataEncrypted.replace("\r", "");
            dataEncrypted = dataEncrypted.replace("\n", "");
            int dwKeySize = _privateKey.getModulus().bitLength();
            int base64BlockSize = dwKeySize / 8 % 3 != 0 ? dwKeySize / 8 / 3 * 4 + 4 : dwKeySize / 8 / 3 * 4;
            int iterations = dataEncrypted.length() / base64BlockSize;
            ByteBuffer bb = ByteBuffer.allocate(100000);

            for (int i = 0; i < iterations; ++i) {
                String sTemp = dataEncrypted.substring(base64BlockSize * i, base64BlockSize * i + base64BlockSize);
                byte[] bTemp = Base64.decodeBase64(sTemp);
                byte[] encryptedBytes = cipher.doFinal(bTemp);
                bb.put(encryptedBytes);
            }

            byte[] bDecrypted = bb.array();
            return (new String(bDecrypted)).trim();
        } catch (Exception ex) {
            logger.error("#decryptRSA - EXCEPTION: ", ex);
        }
        return null;
    }

public static Boolean verifySignature(String plainData, String signatureStr, String publicKeyText, String algorithm) {
        try {
            byte[] signatureBytes = Base64.decodeBase64(signatureStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
            PublicKey pubKey = (PublicKey) keyFactory.generatePublic(publicKeySpec);
            Signature signature = Signature.getInstance(algorithm);
            signature.initVerify(pubKey);
            signature.update(plainData.getBytes(StandardCharsets.UTF_8));
            return signature.verify(signatureBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String sign(String plainText, String privateKeyText, String algorithm) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
            RSAPrivateKey _privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(_privateKey);
            signature.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] bDecrypted = signature.sign();
            return (new String(bDecrypted)).trim();
        } catch (Exception ex) {
            logger.error("#decryptRSA - EXCEPTION: ", ex);
        }
        return null;
    }

    public static String jwtToken(String secret, String audience, String subject, Date expiration) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("NATCASH")
                    .withAudience(audience)
                    .withSubject(subject)
                    .withExpiresAt(expiration)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            logger.error("#jwtToken - ERROR: ", exception);
        } catch (Exception ex) {
            logger.error("#jwtToken - ERROR: ", ex);
        }
        return null;
    }

    public static DecodedJWT verifyJwtToken(String secret, String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("NATCASH")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            logger.error("#verifyJwtToken - ERROR: ", exception);
        } catch (Exception ex) {
            logger.error("#verifyJwtToken - ERROR: ", ex);
        }
        return null;
    }

    public static String hmac256Encrypt(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rs = mac.doFinal(data.getBytes());
        return Hex.encode(rs);
//        return encodeBase64(rs);
    }


    public static void main(String[] args) {
        try {
            if (true){
                String plain = "transactionId=bfbed453-80ce-475d-9215-ad2b8e802571&requestId=ttnjavxzjdnxvoyjjbfddpxfhpvoeyyesclrbhhcsgmzcgpcjtoxjlfdvhblziulejftdhibudkwcxdbzsfljbvgwhvtgvcjmbvt&money=100";
                String sig = "ugU3kyACaWHUpieSmbcmShpijTpgah8M94KgludFh0nIi9bX2RvI2wvls1Ftn7KuuVitVS/dNSOJ4U52B7oRrf2c4iOFZrLkxGKUpxzKrzwnD//1o47c9+qWjSYShJPRsYFl+t++hxJ5NFYQNibXTE8J2i8uFmIeSPcXTfGNQiELBOspmJXieeXV9epXZeKC5V106tTh6FabtOsgOF8jhed9niyOB5GzZzrmxME4iUR3BZDTMw0rC16I85T2oovNeeT+c5Giio0sEdv1usjmOV4UpDZXCxHzru5gtpcZy39WhAGb0/nbFYFXsaO33QxxVwNOT3Sm0cQr14FXOsqa5w==";
                String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyM2QCmwy4RUu1Uq0f8exh4EmHwTmiOqF55Nm5U3KZwdtQhCYrEFaN8OMkE/c/qTyAvXyYQmF8Wu64dtHzC3eYEdBGP5o72LBAXGDJ8Sb9ecZ+9vFaAnu6gkYLrtfFEQ+1YdQaJDiKJXgOw2hpBipbL13D/5xlagrHVwpwhIhZw0pjjMQPuUz86XHJxvwzMIsu7/0bV84zASkfJ1Cy071hjFrWHhCS9C6Kk4kXq72GqFbPMFIA6Nrdpk0nTgJRo+kG4Q66KQIuMbo38Zo13nz84NHH2VtZSrOIFprB8w4UIg6FZNziUA7urPeLdMTWyoqgOAHQLxgGsPjh6Yy3+ponwIDAQAB";
                String alg = "SHA1WithRSA";
                System.out.println("verify:" + verifySignature(plain, sig, publicKey, alg));
                return;
            }
            if (false) {
                String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0dSDoWJ9B+Ube5EArUuxbcn49LDojE1ucepkUeqIXE62WqDzQc45FbiGJ9JRLTAX6hbbTBF2ojhGEnZ12Hw1fWmwNfgpjDU5mQjyiJdJRzwCeD5+ppgsLU6RPzIEZXo4Ead562BNuo106vpC4cAO0rPN3YOis/IqRT9X3m3lMTiCj0m1FX+fGqSm9/QpXP78N1WxDNezajkOQsosQtnBqi4K0Jtm/wsM5dZMe0TqEdBoXiQ9w1Q9d8W3zJa36Tq9UfpcVoAlCW6umXu6QKKj3LkROK0VeFBri4wCgrCBCyPQXNjBO9mL86MsGOOzL14WLM7NMMPRtRagM0y98r5R9AgMBAAECggEAED+fp+wa4ujcHcO1l3EPASjs/vsVfQUIMa3ETNjB8Yv/DFq3pxlQjSXedTqFg8Yjn6wciyGQgVXRc/xxKiqfnJ9arVhU/Rg8n+6E4SDAsEcQSisj+DWQNwPf/qNuWGdwKCuUMcF09nhVM63nAARuAP21qmEackEHBRCINpRUNq+HV/cZlUFO/l1JQhYwi0XK4148tyEIHpDs0wmeni/lStqXSvWcu6lo2Da1khIoDlcrN6sYV+CmSboE5RCsbOBL8UdL/nNqHTgF+ttPyteDZd3jI8p1gw3EtP5LDIAUk3MwxX/cYtvvOjBwCzOouRdI4GJZIaDxcT4zjC0Q/GUhbQKBgQDrqmcyhw6Y8VFDQ4qG5fHB2jONUaLg4VK7Au6NPZ7IAYNNGzsPTQyo+TqYp1kX99IGtn5iuN6C3nQLO6rYkTTixirOk0mAqtKtTcN0kRPN4EmRk0hpkkiw34rs9rWAjMN1MnBnnvznFFui++WVGTEfI3Ar/Lu2TqL9z1qOZuIMhwKBgQDEBz0XEVAgNh4WfI268728QEtn7fwQoggm0I4rNgaLWtne2hXttMbTgqsWi2/jBA4pHunKToAmnqAFcNQX73fnTEnscNZQVzyAaXhwUMHl6ybE9vHmL4TMPce90acZuRGAxcUjXnHo93I0riaTOr2RUzDUfSLxaU/me2UOhOZ72wKBgQCikHGPkUqohg6gb77nP0d9iZ92JJJOvh/rkSItnAwju6dvURF0hJnvFb0uAIKc3f6rUsfsIcqcTAeQmCBKNCBfLoPo7AEQGMYrdjEV/93Bbq5RFWAmMxqhqBKVU7nwaymJdrnDQRED9i8gRtMf/ri72h5OuZBjOeuFWbwF70KPkQKBgF+XAA09ngr3GNjyR9LAhOdFmv/FJ0OdH8h9rrYXlD1kQhXP1x2hJ8uATpmhyR+0nIskzfCfMxKiC3pbuLErTxb1Np8brwZiY9hilh5+4u6RKsilevQ6e9F01vATSh7bBlP7HNTQe0+VSpWxzpHA9Svmm0si8mUya4oC5hacbCA9AoGAT+Nyeq2iljLJ+NrRsWSQYT7PkuWCgy+xW80rICSwglUfEhZyOmY9cRQzVpD6mnxmQGXHPpE49P/+g00V+G03jNMDohrB+94AqyNlRvKrDPhoD+2qpFO4CJZLgMTMJHSEZiwo3Fjd9Jutm16aTrTnMXeDZ8log7tO2Jrz1CnFD7U=";
                String enc = "fVa/fUG7hTYd8Tgzm3/sKjpkVyl9YwIbho7g5TBp96c4FIa8oHpIFlkJMM3MMdrAQYwksTcyjnwrvRprvyxjlKBUCH2kXqctWK6DyXoaP96zSlqSLx3BAyrHdRcllsd3MGscKxUiDTAqRCM0IE9SzFHUTlmbMuptbPiIdZY6t+K2XUFGATsI8b/RCt9mMPGZd5+1aK81wdvyZT+23DzcSqrfyh9gE6cTdUtHwmdDI38F5IOiRwYHTKLYWj36WA1pFcmjKzpOt0WVkAovYS8h0p5arBYK7MCEySo7gPxa1bV0MRhr0xM/LpGjPS5p9JtwZsCfH6+EXmktHwDPFqNEdg==";
                String dec = decryptRSA(enc, privateKey);
                System.out.println("Decrypted: " + dec);
                return;
            }
            if (true) {
                String hmacSHA512Algorithm = "HmacSHA256";
                String data1 = "0973153351" + System.currentTimeMillis();
                String data2 = "Testdata with difference lenght 123";
                String data3 = "Testdata with difference lenght 12312 3123 123 123123 12312 3123123123 Testdata with difference lenght 12312 3123 123 12312Testdata with difference lenght 12312 3123 123 12312Testdata with difference lenght 12312 3123 123 12312Testdata with difference lenght 12312 3123 123 12312";
                String key = "Nat@#Alt2022";

                long t = System.currentTimeMillis();
                System.out.println(hmac256Encrypt(data1, key));
                long t1 = System.currentTimeMillis();
                System.out.println("T1: " + (t1 - t));
                System.out.println(hmac256Encrypt(data2, key));
                System.out.println(hmac256Encrypt(data3, key));
                return;
            }
            if (true) {


                String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtHUg6FifQflG3uRAK1LsW3J+PSw6IxNbnHqZFHqiFxOtlqg80HOORW4hifSUS0wF+oW20wRdqI4RhJ2ddh8NX1psDX4KYw1OZkI8oiXSUc8Ang+fqaYLC1OkT8yBGV6OBGneetgTbqNdOr6QuHADtKzzd2DorPyKkU/V95t5TE4go9JtRV/nxqkpvf0KVz+/DdVsQzXs2o5DkLKLELZwaouCtCbZv8LDOXWTHtE6hHQaF4kPcNUPXfFt8yWt+k6vVH6XFaAJQlurpl7ukCio9y5ETitFXhQa4uMAoKwgQsj0FzYwTvZi/OjLBjjsy9eFizOzTDD0bUWoDNMvfK+UfQIDAQAB";
                String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0dSDoWJ9B+Ube5EArUuxbcn49LDojE1ucepkUeqIXE62WqDzQc45FbiGJ9JRLTAX6hbbTBF2ojhGEnZ12Hw1fWmwNfgpjDU5mQjyiJdJRzwCeD5+ppgsLU6RPzIEZXo4Ead562BNuo106vpC4cAO0rPN3YOis/IqRT9X3m3lMTiCj0m1FX+fGqSm9/QpXP78N1WxDNezajkOQsosQtnBqi4K0Jtm/wsM5dZMe0TqEdBoXiQ9w1Q9d8W3zJa36Tq9UfpcVoAlCW6umXu6QKKj3LkROK0VeFBri4wCgrCBCyPQXNjBO9mL86MsGOOzL14WLM7NMMPRtRagM0y98r5R9AgMBAAECggEAED+fp+wa4ujcHcO1l3EPASjs/vsVfQUIMa3ETNjB8Yv/DFq3pxlQjSXedTqFg8Yjn6wciyGQgVXRc/xxKiqfnJ9arVhU/Rg8n+6E4SDAsEcQSisj+DWQNwPf/qNuWGdwKCuUMcF09nhVM63nAARuAP21qmEackEHBRCINpRUNq+HV/cZlUFO/l1JQhYwi0XK4148tyEIHpDs0wmeni/lStqXSvWcu6lo2Da1khIoDlcrN6sYV+CmSboE5RCsbOBL8UdL/nNqHTgF+ttPyteDZd3jI8p1gw3EtP5LDIAUk3MwxX/cYtvvOjBwCzOouRdI4GJZIaDxcT4zjC0Q/GUhbQKBgQDrqmcyhw6Y8VFDQ4qG5fHB2jONUaLg4VK7Au6NPZ7IAYNNGzsPTQyo+TqYp1kX99IGtn5iuN6C3nQLO6rYkTTixirOk0mAqtKtTcN0kRPN4EmRk0hpkkiw34rs9rWAjMN1MnBnnvznFFui++WVGTEfI3Ar/Lu2TqL9z1qOZuIMhwKBgQDEBz0XEVAgNh4WfI268728QEtn7fwQoggm0I4rNgaLWtne2hXttMbTgqsWi2/jBA4pHunKToAmnqAFcNQX73fnTEnscNZQVzyAaXhwUMHl6ybE9vHmL4TMPce90acZuRGAxcUjXnHo93I0riaTOr2RUzDUfSLxaU/me2UOhOZ72wKBgQCikHGPkUqohg6gb77nP0d9iZ92JJJOvh/rkSItnAwju6dvURF0hJnvFb0uAIKc3f6rUsfsIcqcTAeQmCBKNCBfLoPo7AEQGMYrdjEV/93Bbq5RFWAmMxqhqBKVU7nwaymJdrnDQRED9i8gRtMf/ri72h5OuZBjOeuFWbwF70KPkQKBgF+XAA09ngr3GNjyR9LAhOdFmv/FJ0OdH8h9rrYXlD1kQhXP1x2hJ8uATpmhyR+0nIskzfCfMxKiC3pbuLErTxb1Np8brwZiY9hilh5+4u6RKsilevQ6e9F01vATSh7bBlP7HNTQe0+VSpWxzpHA9Svmm0si8mUya4oC5hacbCA9AoGAT+Nyeq2iljLJ+NrRsWSQYT7PkuWCgy+xW80rICSwglUfEhZyOmY9cRQzVpD6mnxmQGXHPpE49P/+g00V+G03jNMDohrB+94AqyNlRvKrDPhoD+2qpFO4CJZLgMTMJHSEZiwo3Fjd9Jutm16aTrTnMXeDZ8log7tO2Jrz1CnFD7U=";

                long t = System.currentTimeMillis();
                String plainData = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJUQVUiLCJzdWIiOiJMQU5BIiwiaXNzIjoiTkFUQ0FTSCIsImV4cCI6MTY0MzgyMTIwMH0.7Q-pwHBlahZdweSwCOrj7NQpTZZtf4Qp3HzcGIy3sSIeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJUQVUiLCJzdWIiOiJMQU5BIiwiaXNzIjoiTkF11231231231";
                String enc = encryptRSA(plainData, publicKey);
                System.out.println("Encrypted: " + enc);
                long t1 = System.currentTimeMillis();
                System.out.println("T1: " + (t1 - t));
                return;
            }
            if (true) {
                String jwt = jwtToken("TEST123", "TAU", "LANA", DateTimeUtils.addDate(new Date(), 1));
                System.out.println(jwt);

                DecodedJWT j = verifyJwtToken("TEST123", jwt);

                Object t = j.getClaims();
                System.out.println(j.getPayload());
                System.out.println(j.getToken());
                return;
            }

            if (false) {
                String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlpaD/B6T4FoB/mfDEpiJvd/gUGmU4D7Fxd35wJK8jucTUXiCZgJ02N4ocB2T8Kr+/T/8g6GVMmzX7QcGVS+xu0d2z4KWiKUD6j5SciwRPyD8Ec4muana7MYOlDuYw1j6F6E1XHh8KDil/LQ7n5XibHbcGjdzXpLgBzPusa0wSjdmK1z3IEFob4ZRMVH/MwfZY0SVQyqYpWaTbfb5Br9cxwWmvaHlHWQq9ZZTblSyz4tKlNLLkxWU+ABTD5l9QuVQK2BPGCjKbzZWin4OD0iOdgMaFJQLVETapeIpUHid4oU5mQ50lVK/OFKwoNMFmhnT3tg4yKvAxNnBJ+SnmwITNwIDAQAB";
                String plainData = "TEST ABC";
                String enc = encryptRSA(plainData, publicKey);
                System.out.println("Encrypted: " + enc);
                return;
            }

            if (true) {
                String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtHUg6FifQflG3uRAK1LsW3J+PSw6IxNbnHqZFHqiFxOtlqg80HOORW4hifSUS0wF+oW20wRdqI4RhJ2ddh8NX1psDX4KYw1OZkI8oiXSUc8Ang+fqaYLC1OkT8yBGV6OBGneetgTbqNdOr6QuHADtKzzd2DorPyKkU/V95t5TE4go9JtRV/nxqkpvf0KVz+/DdVsQzXs2o5DkLKLELZwaouCtCbZv8LDOXWTHtE6hHQaF4kPcNUPXfFt8yWt+k6vVH6XFaAJQlurpl7ukCio9y5ETitFXhQa4uMAoKwgQsj0FzYwTvZi/OjLBjjsy9eFizOzTDD0bUWoDNMvfK+UfQIDAQAB";
                String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0dSDoWJ9B+Ube5EArUuxbcn49LDojE1ucepkUeqIXE62WqDzQc45FbiGJ9JRLTAX6hbbTBF2ojhGEnZ12Hw1fWmwNfgpjDU5mQjyiJdJRzwCeD5+ppgsLU6RPzIEZXo4Ead562BNuo106vpC4cAO0rPN3YOis/IqRT9X3m3lMTiCj0m1FX+fGqSm9/QpXP78N1WxDNezajkOQsosQtnBqi4K0Jtm/wsM5dZMe0TqEdBoXiQ9w1Q9d8W3zJa36Tq9UfpcVoAlCW6umXu6QKKj3LkROK0VeFBri4wCgrCBCyPQXNjBO9mL86MsGOOzL14WLM7NMMPRtRagM0y98r5R9AgMBAAECggEAED+fp+wa4ujcHcO1l3EPASjs/vsVfQUIMa3ETNjB8Yv/DFq3pxlQjSXedTqFg8Yjn6wciyGQgVXRc/xxKiqfnJ9arVhU/Rg8n+6E4SDAsEcQSisj+DWQNwPf/qNuWGdwKCuUMcF09nhVM63nAARuAP21qmEackEHBRCINpRUNq+HV/cZlUFO/l1JQhYwi0XK4148tyEIHpDs0wmeni/lStqXSvWcu6lo2Da1khIoDlcrN6sYV+CmSboE5RCsbOBL8UdL/nNqHTgF+ttPyteDZd3jI8p1gw3EtP5LDIAUk3MwxX/cYtvvOjBwCzOouRdI4GJZIaDxcT4zjC0Q/GUhbQKBgQDrqmcyhw6Y8VFDQ4qG5fHB2jONUaLg4VK7Au6NPZ7IAYNNGzsPTQyo+TqYp1kX99IGtn5iuN6C3nQLO6rYkTTixirOk0mAqtKtTcN0kRPN4EmRk0hpkkiw34rs9rWAjMN1MnBnnvznFFui++WVGTEfI3Ar/Lu2TqL9z1qOZuIMhwKBgQDEBz0XEVAgNh4WfI268728QEtn7fwQoggm0I4rNgaLWtne2hXttMbTgqsWi2/jBA4pHunKToAmnqAFcNQX73fnTEnscNZQVzyAaXhwUMHl6ybE9vHmL4TMPce90acZuRGAxcUjXnHo93I0riaTOr2RUzDUfSLxaU/me2UOhOZ72wKBgQCikHGPkUqohg6gb77nP0d9iZ92JJJOvh/rkSItnAwju6dvURF0hJnvFb0uAIKc3f6rUsfsIcqcTAeQmCBKNCBfLoPo7AEQGMYrdjEV/93Bbq5RFWAmMxqhqBKVU7nwaymJdrnDQRED9i8gRtMf/ri72h5OuZBjOeuFWbwF70KPkQKBgF+XAA09ngr3GNjyR9LAhOdFmv/FJ0OdH8h9rrYXlD1kQhXP1x2hJ8uATpmhyR+0nIskzfCfMxKiC3pbuLErTxb1Np8brwZiY9hilh5+4u6RKsilevQ6e9F01vATSh7bBlP7HNTQe0+VSpWxzpHA9Svmm0si8mUya4oC5hacbCA9AoGAT+Nyeq2iljLJ+NrRsWSQYT7PkuWCgy+xW80rICSwglUfEhZyOmY9cRQzVpD6mnxmQGXHPpE49P/+g00V+G03jNMDohrB+94AqyNlRvKrDPhoD+2qpFO4CJZLgMTMJHSEZiwo3Fjd9Jutm16aTrTnMXeDZ8log7tO2Jrz1CnFD7U=";

                long t = System.currentTimeMillis();
                String plainData = "TEST ABC";
                String enc = encryptRSA(plainData, publicKey);
                System.out.println("Encrypted: " + enc);
                long t1 = System.currentTimeMillis();
                System.out.println("T1: " + (t1 - t));
                String dec = decryptRSA(enc, privateKey);
                System.out.println("Decrypted: " + dec);
                long t2 = System.currentTimeMillis();
                System.out.println("T2: " + (t2 - t1));
                return;
            }

            if (true) {
                String[] keys = generateRSAKeys();
                System.out.println("Public: " + keys[0]);
                System.out.println("Private: " + keys[1]);
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
