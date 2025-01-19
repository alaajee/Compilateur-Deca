package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
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
        this.expression = "instruction";
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }


    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        boolean assign = compiler.isAssign;
        boolean var = compiler.isVar;
        compiler.isDiv = true;
        GPRegister reg = compiler.associerReg();
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        if (getRightOperand().getType().isInt()){
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        }
        else {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));
        }
        compiler.addInstruction(new CMP(rightOperand, Register.R0));

        if (!compiler.getCompilerOptions().getNoCHeck()){
            compiler.addInstruction(new BEQ(compiler.labelMap.get("division_by_zero_error")));
        }
       // compiler.addInstruction(new BEQ(compiler.labelMap.get("division_by_zero_error")));
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        if (assign) {
            System.out.println(compiler.typeAssign);
            if (compiler.typeAssign.equals("float")) {
                constructeurDIV constructeurDIV = new constructeurDIV();
                codeGen gen = new codeGen();
                if (rightOperand instanceof GPRegister) {
                    if (getRightOperand().getType().isInt()){
                        compiler.addInstruction(new FLOAT(rightOperand, (GPRegister) rightOperand));
                    }
                    compiler.addInstruction(new LOAD(leftOperand, Register.R0));
                    compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    gen.codeGenPrint(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    return regis;
                } else {
                    compiler.addInstruction(new LOAD(rightOperand, Register.R0));
                    compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    compiler.addInstruction(new LOAD(leftOperand, Register.R1));
                    compiler.addInstruction(new FLOAT(Register.R1, Register.R1));
                    gen.codeGenPrint(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    return regis;
                }
            } else {
                constructeurQUO constructeurQUO = new constructeurQUO();
                codeGen gen = new codeGen();
                DVal regis = gen.codeGen(leftOperand, rightOperand, reg, constructeurQUO, compiler);
                return regis;
            }
        } else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }


    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");


        if (typeRight.isInt() && typeLeft.isInt()) {
            constructeurQUO constructeurQUO = new constructeurQUO();
            codeGen gen = new codeGen();
            gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurQUO, compiler);
            compiler.addInstruction(new WINT());
        } else if (typeRight.isFloat() || typeLeft.isFloat()) {
            constructeurDIV constructeurDIV = new constructeurDIV();
            codeGen gen = new codeGen();
            gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurDIV, compiler);
            compiler.addInstruction(new WFLOAT());
        } else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }


    }


    public DVal codeGenInit(DecacCompiler compiler) {
        boolean assign = compiler.isAssign;
        boolean var = compiler.isVar;
        compiler.isDiv = true;
        GPRegister reg = compiler.associerReg();
        DVal rightOperand = getRightOperand().codeGenInit(compiler);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        compiler.addInstruction(new CMP(rightOperand, Register.R0));
        if (!compiler.getCompilerOptions().getNoCHeck()){
            compiler.addInstruction(new BEQ(compiler.labelMap.get("division_by_zero_error")));
        }
        //compiler.addInstruction(new BEQ(compiler.labelMap.get("division_by_zero_error")));
        DVal leftOperand = getLeftOperand().codeGenInit(compiler);
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        DAddr adresse = compiler.getCurrentAdresse();

        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        if (assign) {
            if (compiler.typeAssign.equals("float")) {
                constructeurDIV constructeurDIV = new constructeurDIV();
                codeGen gen = new codeGen();
                if (rightOperand instanceof GPRegister) {
                    compiler.addInstruction(new FLOAT(rightOperand, (GPRegister) rightOperand));
                    compiler.addInstruction(new LOAD(leftOperand, Register.R0));
                    compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    gen.codeGenPrint(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    compiler.addInstruction(new STORE((GPRegister)regis, adresse));
                    return regis;
                } else {
                    compiler.addInstruction(new LOAD(rightOperand, Register.R0));
                    compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    compiler.addInstruction(new LOAD(leftOperand, Register.R1));
                    compiler.addInstruction(new FLOAT(Register.R1, Register.R1));
                    gen.codeGenPrint(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    compiler.addInstruction(new STORE((GPRegister)regis, adresse));
                    return regis;
                }
            } else {
                constructeurQUO constructeurQUO = new constructeurQUO();
                codeGen gen = new codeGen();
                DVal regis = gen.codeGen(leftOperand, rightOperand, reg, constructeurQUO, compiler);
                compiler.addInstruction(new STORE((GPRegister)regis, adresse));
                return regis;
            }
        } else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }

    }
}


// QUO
// DIV