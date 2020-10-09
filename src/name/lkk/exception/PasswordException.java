package name.lkk.exception;

/**
 * @author: linkirk
 * @date: 2020/10/9 17:20
 * @description:
 */
public class PasswordException extends RuntimeException{
    static final long serialVersionUID = 1491735138985321019L;

    public PasswordException() {
    }

    public PasswordException(String message) {
        super(message);
    }
}
