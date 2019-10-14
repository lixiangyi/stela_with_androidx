package com.lxy.baselibs.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptTool {
    public static final String TYPE_NORMAL = "UTF-8";
    public static final String TYPE_PSW = "GB2312";
    private static final String tag = "debug";
    private static final char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};



    public static int byteArrayToInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    public static long byteArrayToLong(byte[] bRefArr) {
        long lOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            lOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return lOutcome;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * desEncrypt_Deprecated 与 desEncrypt方法比较，desSEncrypt_Deprecated效率极低 <br>
     * 但旧版APP获取设备号时使用了次方法，故保留此方法【仅供设备号加密使用】<br>
     * 后续[DES]加密，必须使用【desSEncrypt】方法
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     * @author zqs
     * @createTime 2015年4月18日 下午2:14:35
     */
    @Deprecated
    public static String desEncrypt_Deprecated(String src, String key) throws Exception {
        DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(ks);
        Cipher cip = Cipher.getInstance("DES/CBC/PKCS7Padding");// Cipher.getInstance("DES");
        byte[] IV = key.getBytes("UTF-8");
        IvParameterSpec iv2 = new IvParameterSpec(IV);
        cip.init(Cipher.ENCRYPT_MODE, sk, iv2);
        byte[] input = cip.doFinal(src.getBytes("GB2312"));
        String dest = byte2Hex(input); // byte[]数组转换string，该方法被证实效率极低！所以该方法摈弃使用】
        return dest;
    }

    /***
     * DES加密
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     * @author zqs
     * @createTime 2015年12月30日 下午3:06:36
     */
    public static String desEncrypt(String src, String key) throws Exception {
        DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(ks);
        Cipher cip = Cipher.getInstance("DES/CBC/PKCS7Padding");// Cipher.getInstance("DES");
        byte[] IV = key.getBytes("UTF-8");
        IvParameterSpec iv2 = new IvParameterSpec(IV);
        cip.init(Cipher.ENCRYPT_MODE, sk, iv2);
        byte[] input = cip.doFinal(src.getBytes("GB2312"));
        String dest = new String(Base64.encode(input, Base64.DEFAULT));// DES加密后生成byte[]，使用base64保存byte[]
        return dest;
    }

    /***
     * DES解密
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     * @author zqs
     * @createTime 2015年12月30日 下午3:06:56
     */
    public static String desDecrypt(String src, String key) throws Exception {
        DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(ks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
        byte[] IV = key.getBytes("UTF-8");
        IvParameterSpec iv2 = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, sk, iv2);
        byte[] data = Base64.decode(src.toString().getBytes(), Base64.DEFAULT); // 用base64取出DES加密后的byte[]
        byte decryptedData[] = cipher.doFinal(data);
        return new String(decryptedData, "GB2312");
    }

    // /**
    // * Base64Util
    // * @param src
    // * @param key
    // * @return
    // * @throws Exception
    // */
    // public static String desSDecrypt(String src,String key) throws Exception{
    // DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
    // SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    // SecretKey sk = skf.generateSecret(ks);
    // Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
    // byte[] IV=key.getBytes("UTF-8");
    // IvParameterSpec iv2 = new IvParameterSpec(IV);
    // cipher.init(Cipher.DECRYPT_MODE, sk,iv2);// IV�ķ�ʽ
    // // byte[] data=hex2Byte(src);
    // byte[] data = Base64Util.decode(src);
    // byte decryptedData[] = cipher.doFinal(data);
    // return new String(decryptedData,"GB2312");
    // }

    /**
     * desDecrypt_Deprecated 与 desDecrypt方法比较，desDecrypt_Deprecated效率极低 <br>
     * 但为了使【新密方式】能够【兼容旧版本】加密方式，旧版加密方式的app升级后，首次解密将使用此方式解密obj<br>
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     * @author zqs
     * @createTime 2015年4月18日 下午2:14:35
     */
    @Deprecated
    public static String desDecrypt_Deprecated(String src, String key) throws Exception {
        DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(ks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
        byte[] IV = key.getBytes("UTF-8");
        IvParameterSpec iv2 = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, sk, iv2);
        byte[] data = hex2Byte(src);
        byte decryptedData[] = cipher.doFinal(data);
        return new String(decryptedData, "GB2312");
    }

    public static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2Byte(String hexString) {
        if (hexString.length() % 2 == 1) {
            return null;
        }
        byte[] ret = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            ret[i / 2] = Integer.decode("0x" + hexString.substring(i, i + 2)).byteValue();
        }
        return ret;
    }

    public static int stringToByte(String in, byte[] b) throws Exception {
        if (b.length < in.length() / 2) {
            throw new Exception("byte array too small");
        }

        int j = 0;
        StringBuffer buf = new StringBuffer(2);
        for (int i = 0; i < in.length(); i++, j++) {
            buf.insert(0, in.charAt(i));
            buf.insert(1, in.charAt(i + 1));
            int t = Integer.parseInt(buf.toString(), 16);
            System.out.println("byte hex value:" + t);
            b[j] = (byte) t;
            i++;
            buf.delete(0, 2);
        }

        return j;
    }

    /**
     * base64加密
     *
     * @param data
     * @return
     */
    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;

        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

}
