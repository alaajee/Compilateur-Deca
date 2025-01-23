package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.*;

import org.mockito.stubbing.ValidableAnswer;

import java.util.LinkedList;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr expr = this.getRightOperand().verifyRValue(compiler,localEnv,currentClass,leftType);
        this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        this.setType(leftType);
        this.setRightOperand(expr);
        return expr.getType();
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        compiler.isAssign = true;
        compiler.typeAssign = getLeftOperand().getType().toString();
        compiler.needToPush = true;
        DVal resultat = getLeftOperand().codeGenExpr(compiler);
        DVal val = getRightOperand().codeGenExpr(compiler);
        GPRegister register = compiler.associerReg();
        if (getLeftOperand().getType().isClass()){
            compiler.addInstruction(new LOAD(val,register));
            compiler.addInstruction(new STORE(register,(DAddr) resultat));
        }
        if (val instanceof GPRegister){
            compiler.addInstruction(new STORE((GPRegister)val,(DAddr )resultat));
            compiler.libererReg(((GPRegister) val).getNumber());
            return resultat;
        }
        else {
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(val, reg));
            if (compiler.typeAssign.equals("float")) {
                compiler.addInstruction(new FLOAT(reg,reg));
            }
            compiler.addInstruction(new STORE(reg,(DAddr )resultat));
            compiler.libererReg(reg.getNumber());
            return reg;
        }


    }

    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines){
        GPRegister reg = compiler.associerReg();
        if (!compiler.registeres.contains(reg)) {
            compiler.registeres.add(reg);
        }
        RegisterOffset registerOffset = new RegisterOffset(-2,Register.LB);
        lines.add(new LOAD(registerOffset, reg));
        DVal register = getLeftOperand().codeGenInstClass(compiler, lines,reg);
        DVal resultat = getRightOperand().codeGenInstClass(compiler, lines,reg);

        lines.add(new STORE((GPRegister) resultat , (DAddr )register));
        compiler.libererReg(reg.getNumber());
        return reg;
    }


    @Override
    public DVal codeGenExprARM(DecacCompiler compiler) {
        compiler.isAssign = true;
        compiler.typeAssign = getLeftOperand().getType().toString();
        DVal val = getRightOperand().codeGenExprARM(compiler);
        DAddr addr = (DAddr) getLeftOperand().codeGenExprARM(compiler);
        if (val instanceof ARMGPRegister) {
            if(compiler.typeAssign.equals("int") ){
                compiler.addInstruction(new LDR(ARMRegister.R1, addr));
                compiler.addInstruction(new STR((ARMGPRegister) val, new ARMImmediateString("[R1]")));
            }else if(compiler.typeAssign.equals("float")){
                compiler.addInstruction(new LDR(ARMRegister.R1, addr));
                compiler.addInstruction(new VSTR((ARMGPRegister) val, new ARMImmediateString("[R1]")));
            }
            return addr;
            
        } else {
            ARMGPRegister reg = compiler.associerRegARM();
            if (compiler.typeAssign.equals("int")) {
                compiler.addInstruction(new MOV(reg, val));
                compiler.addInstruction(new LDR(ARMRegister.R1, addr));
                compiler.addInstruction(new STR(reg, new ARMImmediateString("[R1]")));
            } else if (compiler.typeAssign.equals("float")){
                compiler.addInstruction(new VMOVF64(ARMRegister.D0, val));
                compiler.addInstruction(new LDR(ARMRegister.R1, addr));
                compiler.addInstruction(new VSTR(ARMRegister.D0, new ARMImmediateString("[R1]")));
            }
            
            compiler.libererReg(reg.getNumber());
            return addr;
        }
    }

}
