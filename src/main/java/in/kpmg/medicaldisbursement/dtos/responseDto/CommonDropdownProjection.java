package in.kpmg.medicaldisbursement.dtos.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface CommonDropdownProjection {
    Long getId();

    String getLabel();

    @JsonIgnore
    Long getGeneralType();
}
