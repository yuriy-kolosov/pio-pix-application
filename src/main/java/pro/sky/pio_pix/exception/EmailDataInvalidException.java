package pro.sky.pio_pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailDataInvalidException extends RuntimeException {

    public EmailDataInvalidException(String message) {
        super(message);
    }

}
