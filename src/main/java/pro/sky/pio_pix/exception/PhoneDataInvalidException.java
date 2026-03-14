package pro.sky.pio_pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneDataInvalidException extends RuntimeException {

    public PhoneDataInvalidException(String message) {
        super(message);
    }

}
