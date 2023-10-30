package utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

public class InputHandler {
    public static String sha256(String s) { return DigestUtils.sha256Hex(s); }
    public static FileReader getFile(String fileName) throws FileNotFoundException{
        return new FileReader(fileName);
    }
}
