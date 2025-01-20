package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public abstract class ARMUnaryInstruction extends ARMInstruction {
    private Operand operand;

    @Override
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(operand);
    }

    protected ARMUnaryInstruction(Operand operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    public Operand getOperand() {
        return operand;
    }

}
