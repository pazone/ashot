package pazone.ashot;

/**
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class InvalidViewportHeightException extends RuntimeException {

    public InvalidViewportHeightException(String message) {
        super(message);
    }

    public InvalidViewportHeightException(String message, Exception e) {
        super(message, e);
    }
}
