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
            AbstractDeclMethod declVar = iterator.next(); // Récupère l'élément suivant
            declVar.verifyDeclMethod(compiler, localEnv, currentClass);
        }
    }

    public void codeGen(DecacCompiler compiler,String className){
        for (AbstractDeclMethod declVar : this.getList()) {
            declVar.codeGenMethod(compiler,className);
        }
    }

}
