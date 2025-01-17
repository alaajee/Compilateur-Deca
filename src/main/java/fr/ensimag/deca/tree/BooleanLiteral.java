package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Symbol symbolBool = compiler.createSymbol("boolean");
                Type bool = compiler.environmentType.getEnvtypes().get(symbolBool).getType();       
                this.setType(bool);
                return bool;  
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler) {
        DAddr adresse = compiler.getCurrentAdresse();
        int res = value ? 1 : 0;
        GPRegister reg = compiler.associerReg();
        if (compiler.isVar == true){
            compiler.addInstruction(new LOAD(res, reg));
            compiler.addInstruction(new STORE(reg, adresse));
            reg.isRegistre = true;
            compiler.isVar = false;
            return adresse;
        }
        else {
            compiler.addInstruction(new LOAD(res,reg));
        }
        return new ImmediateInteger(res);
    }

    public DVal codeGenInstrCond(DecacCompiler compiler, Label endLabel, Label bodyLabel) {
        // Assignation de la valeur 1 si true, 0 si false
        int res = value ? 1 : 0;
        compiler.addInstruction(new LOAD(new ImmediateInteger(0),Register.R0));
        // Utilisation de CMP pour comparer avec 0 (res est 1 ou 0)
        compiler.addInstruction(new CMP(res, Register.R0));  // Comparer la valeur avec 0
        if (compiler.notCond){
            compiler.addInstruction(new BNE(endLabel));  // Si res == 0, saute à endLabel
            //compiler.addInstruction(new BRA(bodyLabel));  // Sinon, saute à bodyLabel
        }// Si res == 1, sauter à bodyLabel, sinon sauter à endLabel
        else {
            if (compiler.or){
                if (compiler.compteurOr == 1){
                    if (compiler.notCond){
                        compiler.addInstruction(new BNE(endLabel));
                        compiler.notCond = false;
                    }
                    else {
                        compiler.addInstruction(new BEQ(endLabel));
                    }
                }
                else {
                    compiler.addInstruction(new BEQ(endLabel));
                }
                compiler.compteurOr--;
            }
            else {
                compiler.addInstruction(new BEQ(endLabel));  // Si res == 0, saute à endLabel
            }
            //compiler.addInstruction(new BRA(bodyLabel));  // Sinon, saute à bodyLabel
        }
        return new ImmediateInteger(res);  // Retourne null (ou une valeur adaptée si nécessaire)
    }

}
