/**
 * Created by pyvov on 30.10.2016.
 */
public class SyntaxError extends Exception {
    private int line;
    public SyntaxError(String message, int line) {
        super(message+"; Номер рядка = "+line);
        this.line = line;
    }
}
