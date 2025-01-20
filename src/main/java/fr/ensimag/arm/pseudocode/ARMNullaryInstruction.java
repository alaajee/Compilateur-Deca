package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

public abstract class ARMNullaryInstruction extends ARMInstruction {
    @Override
    void displayOperands(PrintStream s) {
        // no operand
    }
}
