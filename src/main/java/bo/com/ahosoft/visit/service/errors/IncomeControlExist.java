package bo.com.ahosoft.visit.service.errors;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IncomeControlExist extends Exception {

    private String code;

    public IncomeControlExist() {
        super("Income control exist");
        this.code = "INCOME_CONTROL_EXIST";
    }
}
