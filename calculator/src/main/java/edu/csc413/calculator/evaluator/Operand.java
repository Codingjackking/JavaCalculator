package edu.csc413.calculator.evaluator;

/**
 * Operand class used to represent an operand
 * in a valid mathematical expression.
 */
public class Operand {
    private int value;
    /**
     * construct operand from string token.
     */
    public Operand(String token) {
        try {
            this.value = Integer.parseInt(token);
        } catch (NumberFormatException e) {
            System.out.println("parseInt: NumberFormatException");
            System.exit(1);
        }
    }

    /**
     * construct operand from integer
     */
    public Operand(int value) {
        this.value = value;
    }

    /**
     * return value of operand
     */
    public int getValue() {
        return value;
    }

    /**
     * Check to see if given token is a valid
     * operand.
     */
    public static boolean check(String token) {
        try {
            Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
