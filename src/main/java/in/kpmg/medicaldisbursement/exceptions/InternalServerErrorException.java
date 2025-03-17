package in.kpmg.medicaldisbursement.exceptions;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PersistenceException;

@Slf4j
public class InternalServerErrorException extends PersistenceException {

    public InternalServerErrorException()
    {
        super();
    }

    public InternalServerErrorException(String message)
    {
        super("Unable to fetch data at a moment! Please try again later.");
        log.info("INTERNAL SERVER ERROR: {}", message);
    }
}
