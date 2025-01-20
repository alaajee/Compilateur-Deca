package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
    
public class ARMBinaryInstruction extends ARMInstruction {
    private Operand operand1, operand2;

    public Operand getOperand1() {
        return operand1;
    }

    public Operand getOperand2() {
        return operand2;
    }

    @Override
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(operand1);
        s.print(", ");
        s.print(operand2);
    }

    protected ARMBinaryInstruction(Operand op1, Operand op2) {
        Validate.notNull(op1);
        Validate.notNull(op2);
        this.operand1 = op1;
        this.operand2 = op2;
    }
}
