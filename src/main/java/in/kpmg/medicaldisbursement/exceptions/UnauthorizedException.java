package in.kpmg.medicaldisbursement.exceptions;

import javax.persistence.PersistenceException;

public class UnauthorizedException extends PersistenceException {

    public UnauthorizedException()
    {
        super("Unauthorized Access!");
    }

    public UnauthorizedException(String message)
    {
        super(message);
    }

}
