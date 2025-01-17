package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.codegen.constructeurCMP;
import fr.ensimag.deca.codegen.constructeurADD;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        compiler.and = true;

        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);


        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurADD();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        // GPRegister reg1 = compiler.associerReg();
        // constructeur constructeur1 = new constructeurCMP();
        // codeGen gen1 = new codeGen();
        // DVal register1 = gen1.codeGen(register, new ImmediateInteger(2), reg1, constructeur1, compiler);
        compiler.addInstruction(new CMP(new ImmediateInteger(2), reg));
        // compiler.addInstruction(new CMP(new ImmediateInteger(2), reg1));
        // compiler.addInstruction(new LOAD(leftOperand,reg));
        // constructeur.constructeur(compiler, rightOperand, reg);
        compiler.addInstruction(new SEQ(reg));
        compiler.addInstruction(new CMP(new ImmediateInteger(0),reg));
        compiler.libererReg(reg.getNumber());
        gen.finalizeAndPush(reg, compiler);
        return reg;
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        constructeur constructeurADD = new constructeurADD();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurADD,compiler);
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }
    }

    @Override
    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodyLabel) {
        compiler.and = true;
        //compiler.nouvLabel = new Label("nouvLabel" + compiler.getUniqueID());
        if (compiler.or){
           // compiler.nouvLabel = new Label("nouvLabel" + compiler.getUniqueID());
            if (compiler.compteurOr != 1){
                compiler.compteurAnd = true;
                DVal leftOperand = getLeftOperand().codeGenInstrCond(compiler, compiler.nouvLabel,bodyLabel);
                DVal rightOperand = getRightOperand().codeGenInstrCond(compiler,compiler.nouvLabel,bodyLabel);
            }
            else {
                DVal leftOperand = getLeftOperand().codeGenInstrCond(compiler, endLabel,bodyLabel);
                DVal rightOperand = getRightOperand().codeGenInstrCond(compiler,endLabel,bodyLabel);
            }
        }
        else {
            compiler.compterLabel ++;
            DVal leftOperand = getLeftOperand().codeGenInstrCond(compiler, endLabel,bodyLabel);
            if (compiler.or){
                compiler.addLabel(compiler.nouvLabel);
                compiler.nouvLabel = new Label("nouvLabel" + compiler.getUniqueID());
            }
            DVal rightOperand = getRightOperand().codeGenInstrCond(compiler,endLabel,bodyLabel);
        }
        //DVal leftOperand = getLeftOperand().codeGenInstrCond(compiler, endLabel,bodyLabel);
//        if (compiler.or){
//            compiler.addLabel(compiler.nouvLabel);
//            compiler.or = false;
//        }
        //compiler.addLabel(compiler.nouvLabel);
       // DVal rightOperand = getRightOperand().codeGenInstrCond(compiler,endLabel,bodyLabel);
        GPRegister reg = compiler.associerReg();

        return reg;
    }



}
