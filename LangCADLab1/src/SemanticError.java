/**
 * Created by pyvov on 30.10.2016.
 */
public class SemanticError extends Exception {
    private int line;
    public SemanticError(String message, int line) {
        super(message+"; Номер рядка = "+line);
        this.line = line;
    }
}
