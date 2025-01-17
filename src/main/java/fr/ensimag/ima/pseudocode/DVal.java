package fr.ensimag.ima.pseudocode;

/**
 * Operand that contains a value.
 * 
 * @author Ensimag
 * @date 01/01/2025
 */
public abstract class DVal extends Operand {
    public boolean isRegistre;
    public boolean isOffSet;
    public boolean isVar;
    public boolean isNull;
    public boolean continuePush = false;

    public void DVal() {
        isRegistre = false;
        isOffSet = false;
        isVar = false;
        isNull = false;
    }

}

