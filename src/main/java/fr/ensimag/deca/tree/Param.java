package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.lang.instrument.ClassDefinition;
import org.apache.commons.lang.Validate;


public class Param extends AbstractParam{
    final private AbstractIdentifier type;
    final private AbstractIdentifier paramName;
    public Param(AbstractIdentifier type, AbstractIdentifier paramName) {
        Validate.notNull(type);
        Validate.notNull(paramName);
        this.type = type;
        this.paramName = paramName;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        paramName.decompile(s);
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }

    @Override
    protected void verifyParam(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void codeGenParam(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
}