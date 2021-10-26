package Exception;

/**
 * @author ChenWen Wang
 * This method is a self-defined exception to handle with invalid file.
 */
public class FileInvalidException extends Exception {
    public FileInvalidException() {
        super("Error: Input file cannot be parsed due to missing information (i.e. month={}, title={}, etc.)");
    }

    public FileInvalidException(String s) {
        super(s);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
