package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Full if/else if/else statement.
 *
 * @author gl02
 * @date 01/01/2025
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        if (this.elseBranch!=null)
        {
            this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        }

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int ID = compiler.getUniqueID();
        Label elseLabel = (elseBranch != null) ? new Label("else_block_" + ID) : null;
        Label endIfLabel = new Label("end_if_" + ID);
        Label bodyLabel = new Label("body_" + ID);

        DVal result = condition.codeGenInstrCond(compiler,elseLabel,bodyLabel);
       // DVal result2 = condition.codeGenInstrCond(compiler,elseLabel,endIfLabel);
        // Génération de code pour comparaison


//        // Choisir l'opérateur de comparaison en fonction du type de la condition (supérieur, inférieur, égal, etc.)
//        if (compiler.notGreater) {
//            compiler.addInstruction(new BGE(elseLabel != null ? elseLabel : endIfLabel));
//        } else if (compiler.notGreaterStric){
//            compiler.addInstruction(new BGT(elseLabel != null ? elseLabel : endIfLabel));
//        }
//        else if (compiler.greater) {
//            compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
//        } else if (compiler.greaterStric){
//            compiler.addInstruction(new BLT(elseLabel != null ? elseLabel : endIfLabel));
//        }
//        else if (compiler.equals) {
//            compiler.addInstruction(new BNE(elseLabel != null ? elseLabel : endIfLabel));
//        }
//        else if (compiler.notEquals){
//            compiler.addInstruction(new BEQ(elseLabel != null ? elseLabel : endIfLabel));
//        }
//        else {
//            // Vous pouvez ajouter d'autres cas selon la condition nécessaire
//            // Par exemple, pour `!=` vous pouvez utiliser BNE
//        }
        // Bloc "then"
        compiler.addLabel(bodyLabel);
        thenBranch.codeGenListInst(compiler);
        if (elseBranch != null) {
            if (compiler.weAreinWhile){
                compiler.addInstruction(new BRA(endIfLabel));

            }
            else {
                compiler.addInstruction(new BRA(endIfLabel));
                 //compiler.addLabel(compiler.endIfLabel);
                // compiler.addLabel(compiler.endIfLabel);
            }
        }

        if (elseBranch != null) {
            compiler.addLabel(elseLabel);
            elseBranch.codeGenListInst(compiler);
        }
        compiler.addLabel(endIfLabel);
        // Bloc "else"




    }



    @Override
    public void codeGenInstARM(DecacCompiler compiler) {
    }




    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("}");

        if (elseBranch != null) {
            s.println("else {");
            s.indent();
            elseBranch.decompile(s);
            s.unindent();
            s.println("}");
        }
    }


    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}