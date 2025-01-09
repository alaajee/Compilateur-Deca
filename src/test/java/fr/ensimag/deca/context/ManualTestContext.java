package fr.ensimag.deca.context;

import java.io.IOException;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class ManualTestContext {
    public static void main(String[] args) throws IOException {

        // Création du compilateur
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);

        // Récupération de l'environnement des types prédéfinis
        EnvironmentType envTypes = compiler.environmentType;
        ListDeclClass listClasses = new ListDeclClass();

        // Liste des déclarations de variables
        ListDeclVar listDeclVar = new ListDeclVar();

        // Création de la table des symboles et des identifiants
        SymbolTable symbolTable = compiler.getSymbolTable();
        Symbol symbolX = symbolTable.create("x");

        // Création de l'identifiant x avec sa location
        Identifier idX = new Identifier(symbolX);

        // Utilisation du type prédéfini int
        Identifier intType = new Identifier(envTypes.INT.getName());

        // Déclaration de la variable : int x;
        listDeclVar.add(new DeclVar(intType, idX, new NoInitialization()));

        // Liste des instructions
        ListInst listInst = new ListInst();

        // Instruction : x = readInt();
        ReadInt readInt = new ReadInt();
        listInst.add(new Assign(idX, readInt));

        // Création du bloc main avec les déclarations et les instructions
        Main mainBlock = new Main(listDeclVar, listInst);

        // Création du programme complet
        AbstractProgram source = new Program(listClasses, mainBlock);


        source.codeGenProgram(compiler);

        source.prettyPrint(System.out);
    }
}
