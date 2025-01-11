package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
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
        // Labels pour les branches
        Label elseLabel = (elseBranch != null) ? new Label("else_label") : new Label("end_if");
        Label endIf = new Label("end_if");

        // Génération du code pour la condition
        DVal conditionResult = condition.codeGenExpr(compiler);  // Get the condition result as an expression
        GPRegister reg = compiler.associerReg();  // Allocate a register for the condition evaluation

        // If the condition result is an offset, we adjust accordingly
        if (conditionResult.isOffSet) {
            compiler.addInstruction(new POP(Register.R0));  // Pop the offset into R0
            compiler.spVal--;  // Decrease the stack pointer
            compiler.addInstruction(new POP(reg));  // Pop the value to the register
            compiler.addInstruction(new CMP(0, reg));  // Compare the register value with zero
        } else {
            compiler.addInstruction(new LOAD(conditionResult, reg));  // Load condition value into the register
            compiler.addInstruction(new CMP(0, reg));  // Compare the register value with zero
        }

        // If the condition is false (R0 = 0), jump to the else block
        compiler.addInstruction(new BEQ(elseLabel));

        // Génération du code pour le bloc "then"
        thenBranch.codeGenListInst(compiler);

        // Jump to the end if an "else" exists
        if (elseBranch != null) {
            compiler.addInstruction(new BRA(endIf));  // Jump to the end of the if-else block
        }

        // Génération du code pour le bloc "else"
        if (elseBranch != null) {
            compiler.addLabel(elseLabel);  // Else label
            elseBranch.codeGenListInst(compiler);  // Generate the code for the else block
        }

        // Ajouter le label de fin
        compiler.addLabel(endIf);  // End of the if-else block
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
