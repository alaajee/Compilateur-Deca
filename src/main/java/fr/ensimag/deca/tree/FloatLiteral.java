package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Single precision, floating-point literal
 *
 * @author gl02
 * @date 01/01/2025
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Symbol symbolFloat = compiler.createSymbol("float");
                Type typeFloat = compiler.environmentType.getEnvtypes().get(symbolFloat).getType();       
                this.setType(typeFloat);
                return typeFloat;  
    }
    


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
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
    protected DVal codeGenExpr(DecacCompiler compiler) {
        DAddr adresse = compiler.getCurrentAdresse();
        DVal res = new ImmediateFloat(value);
        compiler.typeAssign = getType().toString();
        if (compiler.isVar == true){
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(res, reg));
            compiler.addInstruction(new STORE(reg, adresse));
            res.isRegistre = true;
            // Il faut liberer le registre
            compiler.isVar = false;
            compiler.typeAssign = getType().toString();
            compiler.libererReg(reg.getNumber());
            if (value == 0){
                adresse.isNull = true;
            }
            return adresse;
        }
        else {
            return res;
        }

    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler) {
        DVal res = new ARMImmediateFloat(value);
        if(compiler.isVar){
            DAddr adresse = compiler.associerAdresseARM();
            return res;
        }
        return res;
    }
    
    @Override
    protected void codeGenPrintx(DecacCompiler compiler) {
        DVal res = new ImmediateFloat(value);
        compiler.addInstruction(new LOAD(res, Register.R1));
        compiler.addInstruction(new WFLOATX());
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal register = codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(register, Register.R1));
        compiler.addInstruction(new WFLOAT());
    }

    @Override
    protected void codeGenPrintARM(DecacCompiler compiler) {
        compiler.print = true;
        if(!compiler.printfloat){
            String line = "formatfloat" + ": .asciz " + "\"%f\"";
            compiler.addFirstComment(line);
            compiler.printfloat = true;
        }
        compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatfloat")));
        compiler.addInstruction(new VMOVF64(ARMRegister.D0, new ARMImmediateFloat(value)));
        compiler.addInstruction(new VMOV(ARMRegister.R3,ARMRegister.R3, ARMRegister.D0));
        compiler.addInstruction(new BL(new ARMImmediateString("printf")));
    }

    @Override
    public DVal codeGenInit(DecacCompiler compiler){
        compiler.typeAssign = this.getType().toString();
        DVal res = new ImmediateFloat(value);
        GPRegister reg = compiler.associerReg();
        compiler.addInstruction(new LOAD(res, reg));
        compiler.addInstruction(new STORE(reg,new RegisterOffset(-1,Register.SP)));
        return res;
    }

    public DVal codeGenInitARM(DecacCompiler compiler){
        compiler.typeAssign = this.getType().toString();
        DVal res = new ARMImmediateFloat(value);
        return res;
    }
}
