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
 *
 * @author gl02
 * @date 01/01/2025
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.weAreinWhile = true;
        compiler.condition = true;
        int ID = compiler.getUniqueID();
        Label conditionLabel = new Label("condition_" + ID);
        Label bodyLabel = new Label("body_" + ID);
        Label endLabel = new Label("end_while_" + ID);

        // Generate code for condition check
        compiler.addLabel(conditionLabel);
        DVal conditionResult = condition.codeGenInstrCond(compiler,endLabel,bodyLabel);  // Condition

        // Generate code for the body of the while loop
        compiler.debut =0;
        compiler.addLabel(bodyLabel);
        body.codeGenListInst(compiler);

        // Jump back to the condition label to check again
        compiler.addInstruction(new BRA(conditionLabel));
        // End of the while loop
        compiler.LabelFinWhile += compiler.debut ;
        compiler.weAreinWhile = false;
        compiler.addLabel(endLabel);
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
                this.condition.verifyCondition(compiler, localEnv, currentClass);
                this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
                
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
