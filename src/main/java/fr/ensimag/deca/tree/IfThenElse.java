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

        GPRegister reg = compiler.associerReg();

        // Utilisation de `codeGen` pour la condition
        // DVal leftOperand = ((AbstractBinaryExpr) condition).getLeftOperand().codeGenExpr(compiler);
        // DVal rightOperand = ((AbstractBinaryExpr) condition).getRightOperand().codeGenExpr(compiler);

        DVal result = ((AbstractBinaryExpr) condition).codeGenExpr(compiler);
        // Génération de code pour comparaison

        compiler.addInstruction(new LOAD(result, reg));
        // ((AbstractBinaryExpr) condition).codeGenBranch(compiler, elseLabel, endIfLabel);
        compiler.addInstruction(new BLE(elseLabel != null ? elseLabel : endIfLabel));
        // Bloc "then"
        thenBranch.codeGenListInst(compiler);
        if (elseBranch != null) {
            compiler.addInstruction(new BRA(endIfLabel));
        }

        // Bloc "else"
        if (elseBranch != null) {
            compiler.addLabel(elseLabel);
            elseBranch.codeGenListInst(compiler);
        }

        compiler.addLabel(endIfLabel);
        compiler.libererReg(reg.getNumber());
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
