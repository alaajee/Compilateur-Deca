package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass)  {
                Symbol symbolFloat = compiler.createSymbol("float");
                Type typeFloat = compiler.environmentType.getEnvtypes().get(symbolFloat).getType();       
                this.setType(typeFloat);
                return typeFloat;        

    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        System.out.println(this.getOperand());
        DVal dVal = this.getOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        compiler.typeAssign = "float";
        if (!getOperand().getType().equals("float")) {
            if ( dVal instanceof DAddr){
                compiler.addInstruction(new FLOAT(dVal,reg));
                    if (compiler.init && compiler.isAssign){
                        System.out.println("j'entre ici");
                        compiler.addInstruction(new STORE(reg,(DAddr) dVal));
                        compiler.init = false;
                     }
                return reg;
            }
            else {
                if (dVal.name.equals("float")) {
                    compiler.addInstruction(new STORE((GPRegister)dVal,compiler.getCurrentAdresse()));
                }
                else {
                    compiler.addInstruction(new FLOAT(dVal,reg));
                    if (compiler.init && compiler.isAssign){
                        System.out.println("j'entre ici");
                        compiler.addInstruction(new STORE(reg,compiler.getCurrentAdresse()));
                        compiler.init = false;
                    }
                }


                return reg;
            }
        }
        return reg;
    }

    @Override
    public DVal codeGenInit(DecacCompiler compiler){
       // System.out.println("je viens ici");
        DVal dVal = this.getOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        compiler.typeAssign = "float";
        if ( dVal instanceof DAddr){
            compiler.addInstruction(new FLOAT(dVal,reg));
            return reg;
        }
        else {
            compiler.addInstruction(new FLOAT(dVal,reg));
            return reg;
        }
    }
}
