package fr.ensimag.deca.tree;

import java.lang.instrument.ClassDefinition;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;






public abstract class AbstractParam extends Tree {




    protected abstract void verifyParam(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass);

    protected abstract void codeGenParam(DecacCompiler compiler);

    
}
