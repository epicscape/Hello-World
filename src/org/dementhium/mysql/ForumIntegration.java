package org.dementhium.mysql;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * @author 'Mystic Flow
 */
public class ForumIntegration {

    private static Connection connection;

    private static long lastConnection = System.currentTimeMillis();

    /*static {
         createConnection();
     }*/

    public static void init() {
        createConnection();
    } // this will auto call static constructor

    public static void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://www.dementhium.com/db369643274", "drsps_mystic", "lolrawr");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void destroyConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean verify(String username, String password) {
        try {
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT username, password, salt FROM user WHERE username LIKE '" + username + "'");
            while (rs.next()) {
                String pass = rs.getString("password").toLowerCase(); // our forum password
                String salt = rs.getString("salt");
                String encrypted = generate(password.toLowerCase());
                encrypted = generate(new StringBuilder(encrypted).append(salt).toString()); //password we entered
                if (pass.equals(encrypted)) {
                    return true;
                }
            }
            rs.close();
            return false;
        } catch (Throwable e) {
            if (System.currentTimeMillis() - lastConnection > 10000) {
                destroyConnection();
                createConnection();
                lastConnection = System.currentTimeMillis();
            }
        }
        return false;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte aData : data) {
            int halfbyte = (aData >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = aData & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String generate(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }

}
