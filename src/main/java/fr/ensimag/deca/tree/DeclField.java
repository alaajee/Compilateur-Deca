package fr.ensimag.deca.tree;

import java.beans.Visibility;
import java.io.PrintStream;
import java.lang.instrument.ClassDefinition;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;


public class DeclField extends AbstractDeclField{


    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;
    final private Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization,Visibility visibility) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        Validate.notNull(visibility);
        this.type = type;
        this.fieldName = varName;
        this.initialization = initialization;
        this.visibility = visibility;
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
    protected void verifyDeclField(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void codeGenField(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}