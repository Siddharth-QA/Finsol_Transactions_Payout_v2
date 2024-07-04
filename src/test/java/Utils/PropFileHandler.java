package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropFileHandler {
    public static Properties config = new Properties();
    static
    {
        try {
            FileInputStream configReader = new FileInputStream("config.properties");
            try {
                config.load(configReader);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String readProperty(String property)
    {
        return config.getProperty(property);
    }

}
