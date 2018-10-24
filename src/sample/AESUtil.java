package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @since 0.0
 */
public class AESUtil {

    public static byte[] aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
        return bytes;
    }

    public static String aesDecrypt(byte[] bytes, String key) throws Exception {
        if (bytes == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
    }
    public static void fileAesEncrypt(String srcFile, String newFile, String password) {
        File file = new File(srcFile);
        File file1 = new File(newFile);
        BufferedReader reader = null;
        try {
            FileWriter fileWriter = new FileWriter(file1);
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            StringBuilder content = new StringBuilder();
            while ((tempString = reader.readLine()) != null) {
                content.append(tempString);
            }
            byte[] contentByte = aesEncrypt(String.valueOf(content), password);
            String encryptResultStr = parseByte2HexStr(contentByte);
            fileWriter.write(encryptResultStr);
            reader.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
    public static String fileAesDecrypt(String srcFile, String password) {
        File file = new File(srcFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            StringBuilder content = new StringBuilder();
            while ((tempString = reader.readLine()) != null) {
                content.append(tempString);
            }
            byte[] decryptFrom = parseHexStr2Byte(String.valueOf(content));
            reader.close();
            return (aesDecrypt(decryptFrom, password));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }

    public static String stringAesEncrypt(String content, String password) {
        byte[] contentByte = new byte[0];
        try {
            contentByte = aesEncrypt(content, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseByte2HexStr(contentByte);
    }


    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        try {
        	byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}

        return null;
    }

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static void main(String[] args) {
		try {
			String b = Make_CRC("asdfasdf".getBytes());
			System.out.println(b);
			byte[] a = aesEncrypt("201809300261", "123             ");
			System.out.println(parseByte2HexStr(a));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

    public static String Make_CRC(byte[] data) {
        byte[] buf = new byte[data.length];// �洢��Ҫ����У���������
        for (int i = 0; i < data.length; i++) {
            buf[i] = data[i];
        }
        int len = buf.length;
        int crc = 0xFFFF;//16λ
        for (int pos = 0; pos < len; pos++) {
            if (buf[pos] < 0) {
                crc ^= buf[pos] + 256; // XOR byte into least sig. byte of
                                                // crc
            } else {
                crc ^= buf[pos]; // XOR byte into least sig. byte of crc
            }
            for (int i = 8; i != 0; i--) { // Loop over each bit
                if ((crc & 0x0001) != 0) { // If the LSB is set
                    crc >>= 1; // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                } else
                    // Else LSB is not set
                    crc >>= 1; // Just shift right
            }
        }
        String c = Integer.toHexString(crc);
        if (c.length() == 4) {
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 3) {
            c = "0" + c;
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 2) {
            c = "0" + c.substring(1, 2) + "0" + c.substring(0, 1);
        }
        return c;
    }




}
