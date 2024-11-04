package dat.exceptions;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class JpaOperationException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(JpaOperationException.class);
    private final Exception e;

    public JpaOperationException(String message, Exception e) {
        super(message);
        this.e = e;
        writeToLog(message);
    }

    private void writeToLog(String message) {
        logger.error(message);
    }
}