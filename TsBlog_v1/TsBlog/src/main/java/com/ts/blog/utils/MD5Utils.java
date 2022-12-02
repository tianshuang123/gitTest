package com.ts.blog.utils;

import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.utils
 * @Author: ts
 * @CreateTime: 2022-11-22  22:59
 * @Description: TODO
 * @Version: 1.0
*/
public class MD5Utils {

    public static String code(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] bytesDigest = md.digest();
            int i;
            StringBuffer buffer = new StringBuffer("");
            for (int offset = 0; offset < bytesDigest.length; offset++) {
                i = bytesDigest[offset];
                if (i < 0){
                    i+=256;
                }
                if (i<16){
                    buffer.append("");
                }
                buffer.append(Integer.toHexString(i));
            }
            return buffer.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String str = "123456";
        String code = code(str);
        System.out.println(code);
    }

}

