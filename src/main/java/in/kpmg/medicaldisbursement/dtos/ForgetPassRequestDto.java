package in.kpmg.medicaldisbursement.dtos;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ForgetPassRequestDto {
    private String username;
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W]).{8,}$", message = "Password must be at least 8 characters long and include at least one lowercase letter, one uppercase letter, one number, and one special character")
    private String password;
    private Integer otp;
    private String captcha;
    private String captchaId;
}
