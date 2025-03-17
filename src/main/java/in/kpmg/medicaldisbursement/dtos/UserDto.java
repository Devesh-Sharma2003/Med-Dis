package in.kpmg.medicaldisbursement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Integer userId;

    private String userName;

    private String password;
    private String email;

    private Long mobileNo;

    private Integer createdBy;
    private Integer updatedBy;

    private String fullname;

    private String empId;

//    List<UserSchmDeptDesgMapDto> schmDeptDsgMaps;

}
