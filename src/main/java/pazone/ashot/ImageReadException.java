package pazone.ashot;

/**
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class ImageReadException extends RuntimeException {

    public ImageReadException(String message) {
        super(message);
    }

    public ImageReadException(String message, Exception e) {
        super(message, e);
    }
}
