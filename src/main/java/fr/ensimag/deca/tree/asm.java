package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.lang.instrument.ClassDefinition;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.Tree;






// Sous-classe pour une simple instruction
public class asm extends AbstractBlock {
    private StringLiteral asm;

    public asm(StringLiteral asm) {
        Validate.notNull(asm);
        this.asm = asm;
    }

    @Override
    public void verifyBlock(DecacCompiler compiler, EnvironmentExp localEnv, fr.ensimag.deca.context.ClassDefinition currentClass, Type t) throws ContextualError {
        //nothing to verify
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
