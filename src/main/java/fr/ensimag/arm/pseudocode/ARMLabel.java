package fr.ensimag.arm.pseudocode;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public class ARMLabel extends Operand {

    @Override
    public String toString() {
        return name;
    }

    public ARMLabel(String name) {
        super();
        Validate.isTrue(name.length() <= 1024, "Label name too long, not supported by ARM");
        Validate.isTrue(name.matches("^[a-zA-Z][a-zA-Z0-9_.]*$"), "Invalid label name " + name);
        this.name = name;
    }
    private String name;
}
