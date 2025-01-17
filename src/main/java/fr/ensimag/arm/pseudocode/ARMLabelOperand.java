package fr.ensimag.arm.pseudocode;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public class ARMLabelOperand extends DVal {
    public ARMLabel getLabel() {
        return label;
    }

    private ARMLabel label;
    
    public ARMLabelOperand(ARMLabel label) {
        super();
        Validate.notNull(label);
        this.label = label;
    }

    @Override
    public String toString() {
        return label.toString();
    }

}
