package bo.com.ahosoft.visit.service.errors;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VisitorExist extends Exception {

    private String code;

    public VisitorExist() {
        super("Visitor exist");
        this.code = "VISITOR_EXIST";
    }
}
