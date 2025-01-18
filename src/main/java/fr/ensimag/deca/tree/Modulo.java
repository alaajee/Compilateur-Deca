package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
        this.expression = "instruction";
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

                if (!rightType.isInt() || !leftType.isInt())
                {
                    throw new ContextualError("Type mismatch:both operands must be of type int" , this.getLocation());             
                }

                this.setType(leftType);
                return leftType;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }


    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
            compiler.incrementTsto();
        }

        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurREM();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
//        gen.finalizeAndPush(reg, compiler);
        compiler.libererReg(reg.getNumber());
        return register;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
            compiler.incrementTsto();
        }

        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        constructeur constructeurREM = new constructeurREM();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurREM,compiler);
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }
    }



    public DVal codeGenInit(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenInit(compiler);
        DVal rightOperand = getRightOperand().codeGenInit(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurREM();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        gen.finalizeAndPush(reg, compiler);
        DAddr adresse = compiler.getCurrentAdresse();
        compiler.addInstruction(new STORE((GPRegister)register, adresse));
        return register;
    }
}
