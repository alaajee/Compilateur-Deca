package fr.ensimag.deca.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage unique symbols.
 * 
 * A Symbol contains the same information as a String, but the SymbolTable
 * ensures the uniqueness of a Symbol for a given String value. Therefore,
 * Symbol comparison can be done by comparing references, and the hashCode()
 * method of Symbols can be used to define efficient HashMap (no string
 * comparison or hashing required).
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class SymbolTable {
    private  Map<String, Symbol> map = new HashMap<String, Symbol>();
    
        /**
         * Create or reuse a symbol.
         * 
         * If a symbol already exists with the same name in this table, then return
         * this Symbol. Otherwise, create a new Symbol and add it to the table.
        */
        
        public Symbol create(String name) {
            // Check if the symbol already exists
            if (map.containsKey(name)) {
                // Return the existing symbol
                return map.get(name);
            } else {
                // Create a new symbol, add it to the table, and return it
                Symbol newSymbol = new Symbol(name);
                map.put(name, newSymbol);
                return newSymbol;
            }
        }

        public Symbol getSymbole(String name) {
            return map.get(name);
        }
    
        public static class Symbol {
            // Constructor is private, so that Symbol instances can only be created
            // through SymbolTable.create factory (which thus ensures uniqueness
            // of symbols).
            private Symbol(String name) {
                super();
                this.name = name;
            }
    
            public String getName() {
                return name;
            }
    
            @Override
            public String toString() {
                return name;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                Symbol other = (Symbol) obj;
                return this.name.equals(other.name);  // Compare les noms des symboles
            }

            @Override
            public int hashCode() {
                return name.hashCode();  // Utilise le hashCode de la chaîne name
            }



        private String name;
    }
}
