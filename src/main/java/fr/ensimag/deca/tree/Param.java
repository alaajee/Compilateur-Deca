package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.lang.instrument.ClassDefinition;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;


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

    @Override
    protected void verifyParam(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void codeGenParam(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
}