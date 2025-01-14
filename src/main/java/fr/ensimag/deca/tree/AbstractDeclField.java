package fr.ensimag.deca.tree;

import java.lang.instrument.ClassDefinition;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;






public abstract class AbstractDeclField extends Tree {




    protected abstract void verifyDeclField(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass);

    protected abstract void codeGenField(DecacCompiler compiler);

    
}
