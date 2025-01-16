package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;



public class ListMethod extends TreeList<AbstractDeclMethod>{

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod declVar : this.getList()) {
            declVar.decompile(s);  // Décompiler chaque déclaration de variable
        }
    }
       
    void verifyListDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {


        Iterator<AbstractDeclMethod> iterator = this.iterator(); 

        while (iterator.hasNext()) {
            AbstractDeclMethod declMethod = iterator.next(); 
            declMethod.verifyDeclMethod(compiler, localEnv, currentClass);
        }
    }


    void verifyListBlockMethod(DecacCompiler compiler, EnvironmentExp localEnv,
    ClassDefinition currentClass) throws ContextualError
    {
        Iterator<AbstractDeclMethod> iterator = this.iterator(); 

        while (iterator.hasNext()) {
            AbstractDeclMethod declMethod = iterator.next(); 
            declMethod.verifyBlockMethod(compiler, localEnv, currentClass);
        }
    }

    public void codeGen(DecacCompiler compiler){
        for (AbstractDeclMethod declVar : this.getList()) {
            declVar.codeGenMethod(compiler);
        }
    }

}
