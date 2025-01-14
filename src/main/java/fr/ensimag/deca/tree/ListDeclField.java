package fr.ensimag.deca.tree;

import java.lang.instrument.ClassDefinition;
import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * 
 * @author gl02
 * @date 14/01/2025
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField declField : this.getList()) {
            declField.decompile(s); 
        }    }

    void verifyListDeclField(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError {

        Iterator<AbstractDeclField> iterator = this.iterator(); 

        while (iterator.hasNext()) {
            AbstractDeclField declField = iterator.next(); // Récupère l'élément suivant
            declField.verifyDeclField(compiler,localEnv,currentClass);
        }    
    }


    public void codeGen(DecacCompiler compiler) {
        for (AbstractDeclField declField : this.getList()) {
            declField.codeGenField(compiler);
        }    
    }

}
