package fr.ensimag.deca.context;

import java.io.IOException;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Assign;
import fr.ensimag.deca.tree.DeclVar;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.deca.tree.Main;
import fr.ensimag.deca.tree.Multiply;
import fr.ensimag.deca.tree.NoInitialization;
import fr.ensimag.deca.tree.Println;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.deca.tree.ReadInt;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class Test {
    public static void main(String[] args) throws IOException {

        // CrÃ©ation du compilateur
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);

        // RÃ©cupÃ©ration de l'environnement des types prÃ©dÃ©finis
        EnvironmentType envTypes = compiler.environmentType;
        ListDeclClass listClasses = new ListDeclClass();

        // Liste des dÃ©clarations de variables
        ListDeclVar listDeclVar = new ListDeclVar();

        // CrÃ©ation de la table des symboles et des identifiants
        SymbolTable symbolTable = compiler.getSymbolTable();
        Symbol symbolX = symbolTable.create("x");

        // CrÃ©ation de l'identifiant x avec sa location
        Identifier idX = new Identifier(symbolX);
        //idX.setLocation(new Location(1, 5, "test.deca"));

        Identifier idX1 = new Identifier(idX.getName());
        //idX1.setLocation(idX.getLocation());

        Identifier idX2 = new Identifier(idX.getName());
        //idX2.setLocation(idX.getLocation());


        // Utilisation du type prÃ©dÃ©fini int
        Identifier intType = new Identifier(envTypes.INT.getName());
        //intType.setLocation(new Location(1, 1, "test.deca"));

        // DÃ©claration de la variable : int x;
        listDeclVar.add(new DeclVar(intType, idX, new NoInitialization()));

        // Liste des instructions
        ListInst listInst = new ListInst();

        // Instruction : x = readInt();
        ReadInt readInt = new ReadInt();
        //readInt.setLocation(new Location(2, 5, "test.deca"));
        listInst.add(new Assign(idX, readInt));

        // CrÃ©ation du bloc main avec les dÃ©clarations et les instructions
        Main mainBlock = new Main(listDeclVar, listInst);

        // CrÃ©ation du programme complet
        AbstractProgram source = new Program(listClasses, mainBlock);

        source.codeGenProgram(compiler);

        // Affichage du programme vÃ©rifiÃ©
    }
}
