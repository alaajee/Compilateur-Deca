package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand representing an integer.
 * 
 * @author Ensimag
 * @date 01/01/2025
 */
public class ImmediateInteger extends DVal {
    private int value;

    public ImmediateInteger(int value) {
        super();
        this.value = value;
        if (value == 0) {
            this.isNull = true;
        }
    }



    @Override
    public String toString() {
        return "#" + value;
    }


}
