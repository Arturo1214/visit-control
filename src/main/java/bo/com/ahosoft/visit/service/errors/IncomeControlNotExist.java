package bo.com.ahosoft.visit.service.errors;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IncomeControlNotExist extends Exception {

    private String code;

    public IncomeControlNotExist() {
        super("Income control not exist");
        this.code = "INCOME_CONTROL_NOT_EXIST";
    }
}
