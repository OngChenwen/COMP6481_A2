package Exception;

public class FileInvalidException extends Exception {
    String msg;

    public FileInvalidException (String error){
        this.msg = error;
    }

    @Override
    public String toString() {
        return "FileInvalidException: " + msg;
    }
}
