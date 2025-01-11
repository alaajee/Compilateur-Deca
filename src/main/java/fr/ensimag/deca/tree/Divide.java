package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }



    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        // Je dois savoir si le leftOperand est stocké dans un registre ou non ?
        // DVal to reg ?
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        if (typeLeft.isNull()){
            throw new RuntimeException("Division by zero");
        }
        if (typeRight.isInt() && typeLeft.isInt()){
            GPRegister reg = compiler.associerReg();
            if (reg.isOffSet) {
                if (leftOperand.isOffSet && rightOperand.isOffSet) {
                    compiler.addInstruction(new POP(Register.R0));
                    compiler.spVal--;
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new QUO(Register.R0, reg));
                    return reg;
                } else if (leftOperand.isOffSet) {
                    System.out.println("hhh");
                    if (rightOperand.isVar){
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new LOAD(reg,Register.R0));
                        compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                        compiler.addInstruction(new QUO(Register.R0,reg));
                        compiler.addInstruction(new PUSH(reg));
                        return reg;
                    }
                    else {
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new QUO(rightOperand,reg));
                        compiler.addInstruction(new PUSH(reg));
                        return reg;
                    }
                } else if (rightOperand.isOffSet) {
                    System.out.println("hh");
                    if (leftOperand.isVar){
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new LOAD(reg,Register.R0));
                        compiler.addInstruction(new LOAD((RegisterOffset) leftOperand, reg));
                        compiler.addInstruction(new QUO(Register.R0,reg));
                        compiler.addInstruction(new PUSH(reg));
                        return reg;
                    }
                    else {
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new QUO(reg,(GPRegister) leftOperand));
                        compiler.addInstruction(new PUSH(reg));
                        return leftOperand;
                    }


                } else {
                    System.out.println("h");

                    compiler.addInstruction(new LOAD(leftOperand, reg));
                    compiler.addInstruction(new QUO(rightOperand, reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
            }
            else {
                System.out.println("hhh");
                if (leftOperand instanceof DAddr){
                    System.out.println("hhh");
                    compiler.addInstruction(new LOAD(leftOperand,reg));
                    compiler.addInstruction(new QUO(rightOperand,reg));
                    return reg;
                }
                else {
                    System.out.println(leftOperand.isVar);
                    System.out.println("hhh");
                    //compiler.addInstruction(new LOAD(rightOperand,reg));
                    compiler.addInstruction(new QUO(rightOperand, (GPRegister) leftOperand));
                    return leftOperand;
                }
            }
        }
        else if (typeLeft.isFloat() || typeRight.isFloat()) {
            GPRegister reg = compiler.associerReg();
            if (reg.isOffSet) {
                if (leftOperand.isOffSet && rightOperand.isOffSet) {
                    compiler.addInstruction(new POP(Register.R0));
                    compiler.spVal--;
                    compiler.addInstruction(new POP(reg));
                    compiler.addInstruction(new DIV(Register.R0, reg));
                    return reg;
                } else if (leftOperand.isOffSet) {
                    System.out.println("hhh");
                    if (rightOperand.isVar){
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new LOAD(reg,Register.R0));
                        compiler.addInstruction(new LOAD((RegisterOffset) rightOperand, reg));
                        compiler.addInstruction(new DIV(Register.R0,reg));
                        compiler.addInstruction(new PUSH(reg));
                        return reg;
                    }
                    else {
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new DIV(rightOperand,reg));
                        compiler.addInstruction(new PUSH(reg));
                        return reg;
                    }
                } else if (rightOperand.isOffSet) {
                    System.out.println("hh");
                    if (leftOperand.isVar){
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new LOAD(reg,Register.R0));
                        compiler.addInstruction(new LOAD((RegisterOffset) leftOperand, reg));
                        compiler.addInstruction(new DIV(Register.R0,reg));
                        compiler.addInstruction(new PUSH(reg));
                        return reg;
                    }
                    else {
                        compiler.addInstruction(new POP(reg));
                        compiler.addInstruction(new DIV(reg,(GPRegister) leftOperand));
                        compiler.addInstruction(new PUSH(reg));
                        return leftOperand;
                    }


                } else {
                    System.out.println("h");

                    compiler.addInstruction(new LOAD(leftOperand, reg));
                    compiler.addInstruction(new DIV(rightOperand, reg));
                    compiler.addInstruction(new PUSH(reg));
                    return reg;
                }
            }
            else {
                System.out.println("hhh");
                if (leftOperand instanceof DAddr){
                    System.out.println("hhh");
                    compiler.addInstruction(new LOAD(leftOperand,reg));
                    compiler.addInstruction(new DIV(rightOperand,reg));
                    return reg;
                }
                else {
                    System.out.println(leftOperand.isVar);
                    System.out.println("hhh");
                    //compiler.addInstruction(new LOAD(rightOperand,reg));
                    compiler.addInstruction(new DIV(rightOperand, (GPRegister) leftOperand));
                    return leftOperand;
                }
            }
        }
        else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        if (typeRight.isInt() && typeLeft.isInt()){
            compiler.addInstruction(new LOAD(leftOperand,reg));
            compiler.addInstruction(new QUO(rightOperand,reg));
            compiler.addInstruction(new LOAD(reg, Register.R1));
            compiler.addInstruction(new WINT());
        }
        else if (typeRight.isFloat() || typeLeft.isFloat()) {
            compiler.addInstruction(new LOAD(leftOperand,reg));
            compiler.addInstruction(new DIV(rightOperand,reg));
            compiler.addInstruction(new LOAD(reg, Register.R1));
            compiler.addInstruction(new WINT());
        }
        else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }


    }
}


// QUO
// DIV