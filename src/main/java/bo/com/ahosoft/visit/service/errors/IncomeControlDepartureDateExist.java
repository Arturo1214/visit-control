package bo.com.ahosoft.visit.service.errors;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IncomeControlDepartureDateExist extends Exception {

    private String code;

    public IncomeControlDepartureDateExist() {
        super("Income control departure date exist");
        this.code = "INCOME_CONTROL_DEPARTURE_DATE_EXIST";
    }
}
