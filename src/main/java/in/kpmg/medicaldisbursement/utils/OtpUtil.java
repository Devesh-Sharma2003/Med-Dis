package in.kpmg.medicaldisbursement.utils;

import org.springframework.stereotype.Component;
import java.util.Random;


@Component
public class OtpUtil {
    public Integer generateOTP() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}

