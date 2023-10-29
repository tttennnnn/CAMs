package app;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class utils {
    private static final String defaultKey = sha256("password");
    static String sha256(String s) {
        return DigestUtils.sha256Hex(s);
    }
    static boolean userLogin(String keyFile, String id, String key) {
        try {
            FileReader fileReader = new FileReader(keyFile);
            CSVReader csvReader = new CSVReaderBuilder(fileReader)
                                      .withSkipLines(1).build();
            String[] line;
            while ((line = csvReader.readNext()) != null){
                if (!id.equals(line[0]))
                    continue;
                if (line[1].isEmpty())
                    return key.equals(defaultKey);
                else
                    return key.equals(sha256(line[1]));
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("keyFile not found");
            System.exit(1);
        } catch (IOException | CsvValidationException e) {
            System.exit(1);
        }

        return false;
    }

    static UserList readUsers(String userFile) {
        List<String[]> userdata = null;
        try {
            FileReader fileReader = new FileReader(userFile);
            CSVReader csvReader = new CSVReaderBuilder(fileReader)
                .withSkipLines(1).build();
            userdata = csvReader.readAll();
            csvReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("userFile not found");
            System.exit(1);
        }
        catch (IOException | CsvException e) {
            System.exit(1);
        }

        UserList userList = new UserList();
        for (String[] lines : userdata) {
            // format: email, name, faculty, domain
            String id = lines[0].split("@")[0];
            if (lines[3].equals("1"))
                userList.putStudent(id, new String[]{lines[0], lines[1], lines[2]});
            else
                userList.putStaff(id, new String[]{lines[0], lines[1], lines[2]});
        }
        return userList;
    }

}
