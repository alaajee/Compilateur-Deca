package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl02
 * @date 01/01/2025
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        int size = this.getList().size();
        int index = 0;
    
        for (AbstractExpr expr : this.getList()) {
            expr.decompile(s);  
            index++;
            if (index < size) {
                s.print(", "); 
            }
        }
    }

    public void codeGenInst(DecacCompiler compiler){
        compiler.addInstruction(new RFLOAT());
        for (AbstractExpr expr : this.getList()) {
            expr.codeGenPrint(compiler);
        }
    }

    public void codeGenInstARM(DecacCompiler compiler){
        System.out.println("im in ListExpr");
        for (AbstractExpr expr : this.getList()){
            expr.codeGenPrintARM(compiler);
        }
    }
}
