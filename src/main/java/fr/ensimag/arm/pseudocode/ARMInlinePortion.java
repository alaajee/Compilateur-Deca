package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

public class ARMInlinePortion extends ARMAbstractLine {
    private final String asmCode;
    
    public ARMInlinePortion(String asmCode) {
        super();
        this.asmCode = asmCode;
    }
    
    @Override
    void display(PrintStream s) {
        s.println(asmCode);
    }

}
