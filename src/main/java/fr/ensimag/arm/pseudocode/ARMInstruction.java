package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

public abstract class ARMInstruction {
    String getName() {
        return this.getClass().getSimpleName();
    }
    abstract void displayOperands(PrintStream s);
    void display(PrintStream s) {
        s.print(getName());
        displayOperands(s);
    }
}
