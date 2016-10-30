import java.io.*;

/**
 * Created by pyvov on 06.10.2016.
 */
public class FileManager {
    private String fileName;
    private File file;

    public FileManager(String fileName) {
        this.fileName = fileName;
        file = new File(fileName);
    }

    public String read() {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    text.append(s);
                    text.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return text.toString();
    }

    public void write(String text) {
        try {
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
