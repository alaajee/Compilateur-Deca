package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.LinkedList;

import fr.ensimag.deca.context.ClassDefinition;

import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.Instruction;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.Tree;




public abstract class AbstractBlock extends Tree {
    public abstract void verifyBlock(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass,Type t)
            throws ContextualError;

    protected abstract void codeGen(DecacCompiler compiler, LinkedList<Instruction> lines);
}