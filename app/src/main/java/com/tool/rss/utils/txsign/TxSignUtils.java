package com.tool.rss.utils.txsign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.baidu.speech.utils.MD5Util;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tool.rss.ui.model.TxSignatureEntity;
import com.tool.rss.utils.txsign.TxSignBase64Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TxSignUtils {


    /** 生成腾讯图片签名 */ //long time_stamp, String nonce_str
    public static String getTxSignature(long time_stamp, String nonce_str) {
        String singStrs = "";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("app_id", 2118040378);
        try {
            map.put("key1", URLEncoder.encode("杰克", "utf-8"));
            map.put("key2", URLEncoder.encode("杰克6", "utf-8"));
            map.put("nonce_str", nonce_str);
            map.put("time_stamp", time_stamp);
            Map<String, Object> treeMap = new TreeMap<>(map);
            for (String key: treeMap.keySet()) {
                String ts = key + "=" + treeMap.get(key) + "&";
                singStrs = singStrs + ts;
            }
            singStrs = singStrs + "app_key=sgv4xfadexwA3POE";
            singStrs = getMD5(singStrs).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return singStrs;
    }

    public static String getTxSignatureTest() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("app_id", 10000);
        try {
            map.put("key1", URLEncoder.encode("腾讯AI开放平台", "utf-8"));
            map.put("key2", URLEncoder.encode("示例仅供参考", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("nonce_str", "20e3408a79");
        map.put("time_stamp", 1493449657);

        Map<String, Object> treeMap = new TreeMap<>(map);

        String singStrs = "";
        for (String key: treeMap.keySet()) {
            String ts = key + "=" + treeMap.get(key) + "&";
            singStrs = singStrs + ts;
        }
        singStrs = singStrs + "app_key=a95eceb1ac8c24ee28b70f7dbba912bf";

        singStrs = getMD5(singStrs).toUpperCase();

        return singStrs;
    }



    //升序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public final static String getMD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 生成 Authorization 签名字段
     *
     * @param appId
     * @param secretId
     * @param secretKey
     * @param bucketName
     * @param expired
     * @return 签名字符串
     * @throws Exception
     */
    public static String appSign(long appId, String secretId, String secretKey,
                                 String bucketName, long expired) throws Exception {
        long now = System.currentTimeMillis() / 1000;
        int rdm = Math.abs(new Random().nextInt());
        String plainText = String.format("a=%d&b=%s&k=%s&t=%d&e=%d&r=%d",
                appId, bucketName, secretId, now, now + expired, rdm);
        byte[] hmacDigest = HmacSha1(plainText, secretKey);
        byte[] signContent = new byte[hmacDigest.length
                + plainText.getBytes().length];
        System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
        System.arraycopy(plainText.getBytes(), 0, signContent,
                hmacDigest.length, plainText.getBytes().length);
        return Base64Encode(signContent);
    }




    /**
     * 生成 base64 编码
     *
     * @param binaryData
     * @return
     */
    public static String Base64Encode(byte[] binaryData) {
        String encodedstr = new String(TxSignBase64Utils.encode(binaryData));
        return encodedstr;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param binaryData
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(byte[] binaryData, String key)
            throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        mac.init(secretKey);
        byte[] HmacSha1Digest = mac.doFinal(binaryData);
        return HmacSha1Digest;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(String plainText, String key)
            throws Exception {
        return HmacSha1(plainText.getBytes(), key);
    }

    public static String base64(String path) {
        Bitmap bitmap = getLoacalBitmap(path);
        if (bitmap != null) {
            String base64DataString = bitmapToBase64(bitmap);
            return base64DataString;
        }
        return "";
    }

    /**
     * 图片编码
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
