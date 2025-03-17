package in.kpmg.medicaldisbursement.exceptions;

import javax.persistence.PersistenceException;

public class BadRequestException extends PersistenceException {

    public BadRequestException()
    {
        super("Bad Request!");
    }

    public BadRequestException(String message)
    {
        super(message);
    }
}
