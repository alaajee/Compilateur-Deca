package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
            if (!t.isFloat() && !t.isInt())
            {
                throw new ContextualError("Type mismatch:the operand must be float or int" , this.getLocation());             
            }
            this.setType(t);
            return t;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
       DVal operand = this.getOperand().codeGenExpr(compiler);
       if (operand instanceof GPRegister){
            compiler.addInstruction(new OPP(operand,(GPRegister) operand));
            return operand;
        }
       else {
           GPRegister reg = compiler.associerReg();
           compiler.addInstruction(new OPP(operand,reg));
           if (operand instanceof DAddr){
               compiler.addInstruction(new STORE(reg,(DAddr) operand));
           }
           return reg;
       }
    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler){
        DVal operand = this.getOperand().codeGenExprARM(compiler);
        if(getOperand().getType().isFloat()){
            if(operand instanceof ARMGPRegister){
                compiler.addInstruction(new VNEGF64(operand, operand));
                return operand;
            }else{
                ARMGPRegister reg = compiler.associerRegARMD();
                compiler.addInstruction(new VMOVF64(reg, operand));
                compiler.addInstruction(new VNEGF64(reg, reg));
                if(operand instanceof DAddr){
                    compiler.addInstruction(new LDR(ARMRegister.R1, operand));
                    compiler.addInstruction(new VSTR(reg, new ARMImmediateString("[R1]")));
                }
                return reg;
            }
        }else{
            if(operand instanceof ARMGPRegister){
                compiler.addInstruction(new NEG(operand, operand));
                return operand;
            }else{
                ARMGPRegister reg = compiler.associerRegARM();
                compiler.addInstruction(new MOV(reg, operand));
                compiler.addInstruction(new NEG(reg, reg));
                if(operand instanceof DAddr){
                    compiler.addInstruction(new LDR(ARMRegister.R1, operand));
                    compiler.addInstruction(new STR(reg, new ARMImmediateString("[R1]")));
                }
                return reg;
            }
        }
    }

}
