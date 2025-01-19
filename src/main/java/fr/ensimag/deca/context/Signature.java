package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }

    public Type paramNumber(int n) {
        return args.get(n);
    }

    public int size() {
        return args.size();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Comparaison de référence
        if (o == null || getClass() != o.getClass()) return false; // Vérification du type
        Signature signature = (Signature) o;
        return args.equals(signature.args); // Comparaison logique des listes
    }

    @Override
    public int hashCode() {
        return args.hashCode(); // Délégué au hashCode de la liste
    }


}
