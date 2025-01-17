package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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
        System.out.println("float " + value);
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
        return null;
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
    public DVal codeGenInit(DecacCompiler compiler){
        compiler.typeAssign = this.getType().toString();
        DVal res = new ImmediateFloat(value);
        return res;
    }
}
