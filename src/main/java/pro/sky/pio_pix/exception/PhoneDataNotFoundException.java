package pro.sky.pio_pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PhoneDataNotFoundException extends RuntimeException {

    public PhoneDataNotFoundException(String message) {
        super(message);
    }

}
