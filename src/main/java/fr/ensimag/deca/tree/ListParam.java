package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * 
 * @author gl02
 * @date 14/01/2025
 */
public class ListParam extends TreeList<AbstractParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractParam param : this.getList()) {
            param.decompile(s); 
            System.out.println(" , ");
        }    
    }

    public Signature verifyListParam(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Signature signature = new Signature();

        Iterator<AbstractParam> iterator = this.iterator();
        
        while (iterator.hasNext()) {
            AbstractParam param = iterator.next();
            Type paramType = param.verifyParam(compiler, localEnv, currentClass);
            signature.add(paramType);
        }
        return signature;
    }



    public void codeGen(DecacCompiler compiler) {
        for (AbstractParam Param : this.getList()) {
            Param.codeGenParam(compiler);
        }    
    }

}
