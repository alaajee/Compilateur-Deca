package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    EnvironmentExp parentEnvironment;
    private  Map<Symbol, ExpDefinition> envExp;
    private LinkedList<MethodDefinition> methods;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment){
        this.parentEnvironment = parentEnvironment;
        this.envExp = new HashMap<>();
        this.methods = new LinkedList<>();  
    }

    public void addMethode(MethodDefinition method)
    {
        this.methods.add(method);
    }

    public void add(Symbol symbol,ExpDefinition exp)
    {
        this.envExp.put(symbol, exp);
    }

    public Map<Symbol, ExpDefinition> getEnvExp()
    {
        return this.envExp;
    }

    public LinkedList getMethods()
    {
        return this.methods;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
