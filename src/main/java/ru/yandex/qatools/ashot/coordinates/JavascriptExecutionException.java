package ru.yandex.qatools.ashot.coordinates;

/**
 * @author innokenty
 */
public class JavascriptExecutionException extends RuntimeException {

	public JavascriptExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

    public JavascriptExecutionException(String message) {
        super(message);
    }
}
