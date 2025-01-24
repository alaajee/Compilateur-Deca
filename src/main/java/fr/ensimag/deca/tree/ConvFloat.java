package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;
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
        DVal dVal = this.getOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        compiler.typeAssign = "float";
        if (!getOperand().getType().equals("float")) {
            if ( dVal instanceof DAddr){
                compiler.addInstruction(new FLOAT(dVal,reg));
                    if (compiler.init && compiler.isAssign){
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

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler) {
        // Associer un registre pour le résultat
        ARMGPRegister reg = compiler.associerRegARMD();
        DVal operand = this.getOperand().codeGenExprARM(compiler);

        
        if (operand instanceof DAddr) {
            compiler.addInstruction(new LDR(ARMRegister.R1, operand));
            compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
            compiler.addInstruction(new VMOV(ARMRegister.S0, ARMRegister.R1));
            compiler.addInstruction(new VCVTF64S32(ARMRegister.D0, ARMRegister.S0));
        } else if (operand instanceof ARMImmediateInteger) {
            if(!operand.toString().equals("#0")){
                compiler.addInstruction(new VMOVF64(ARMRegister.D0, operand));
            }else{
                if(!compiler.zeroARM){
                    String line = "zero: .double 0.0";
                    compiler.addFirstComment(line);
                    line = ".align 0";
                    compiler.addFirstComment(line);
                    compiler.zeroARM = true;
                }
                compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("=zero")));
                compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
                compiler.addInstruction(new VMOV(ARMRegister.S0, ARMRegister.R1));
                compiler.addInstruction(new VCVTF64S32(ARMRegister.D0, ARMRegister.S0));

            }
        } else if (operand instanceof ARMImmediateFloat) {
            compiler.addInstruction(new VMOV(ARMRegister.D0, operand));
        } else if (operand instanceof ARMGPRegister) {
            compiler.addInstruction(new VMOVF64(ARMRegister.D0, operand));
        }

        compiler.addInstruction(new VMOVF64(reg, ARMRegister.D0));

        return reg;
    }

}
