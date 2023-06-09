package edu.csc413.calculator.evaluator;

import edu.csc413.calculator.operators.*;

import java.util.Stack;
import java.util.StringTokenizer;

// Evaluator class is used to evaluate mathematical expressions in String format.
public class Evaluator {

  // operandStack is used to store all the operands in the mathematical expression.
  private Stack<Operand> operandStack;
  // operatorStack is used to store all the operators in the mathematical expression.
  private Stack<Operator> operatorStack;

  // StringTokenizer will break the mathematical expression into tokens.
  private StringTokenizer expressionTokenizer;

  // delimiters are the characters that separate tokens in the mathematical expression.
  private final String delimiters = " +/*-^()";

  // Constructor for the Evaluator class. It initializes the operand and operator stacks.
  public Evaluator() {
    operandStack = new Stack<>();
    operatorStack = new Stack<>();
  }
  /* process method pops the top two operands and one operator from their respective stacks,
     performs the operation, and pushes the result back onto the operand stack. */
  private void process() {
    Operator operatorFromStack = operatorStack.pop();
    Operand operandTwo = operandStack.pop();
    Operand operandOne = operandStack.pop();
    Operand result = operatorFromStack.execute(operandOne, operandTwo);
    operandStack.push(result);
  }

  /* evaluateExpression method takes a String mathematical expression,
     breaks it down into operands and operators, performs the operations
     in the correct order, and returns the result */
  public int evaluateExpression(String expression) throws InvalidTokenException {
    // filter out spaces
    expression = expression.trim().replace("\\s", "");

    //Initialize StringTokenizer with the expression and delimiters.
    this.expressionTokenizer = new StringTokenizer(expression, this.delimiters, true);

    //Loop through the tokens.
    while (this.expressionTokenizer.hasMoreTokens()) {
      String expressionToken;

      //If token is not a space, proceed.
      if (!(expressionToken = this.expressionTokenizer.nextToken()).equals(" ")) {

        // Check if token is an operand, if so push it onto operand stack.
        if (Operand.check(expressionToken)) {
          operandStack.push(new Operand(expressionToken));
        } else {

          //If token is not a valid operator, throw an exception.
          if (!Operator.check(expressionToken)) {
            throw new InvalidTokenException(expressionToken);
          } else {

            //Retrieve the specific Operator object from the token.
            Operator newOperator = Operator.getOperator(expressionToken);
            //Different behaviors depending on the type of operator.
            if (!(newOperator instanceof LeftParenthesesOperator)) {
              if (!(newOperator instanceof RightParenthesesOperator)) {
                //Keep processing operators from the stack while they have higher or equal priority.
                while (!(operatorStack.isEmpty() || !(operatorStack.peek().priority() >= newOperator.priority()))) {
                  process();
                }
                operatorStack.push(newOperator);
              } else {

                //If operator is a right parentheses, keep processing operators until a left parentheses is encountered.
                while (!(operatorStack.peek() instanceof LeftParenthesesOperator)) {
                  process();
                }
                operatorStack.pop();
              }
            } else {

              //If operator is a left parentheses, push it onto the stack.
              operatorStack.push(newOperator);
            }
          }
        }
      }
    }

    //After all tokens have been processed, keep processing remaining operators on the stack.
    while (!operatorStack.isEmpty()) {
      process();
    }

    /*
    After all operations are complete, the operand stack should contain the result.
    If not, and it is empty, then the expression was invalid.
     */
    if (!operandStack.isEmpty()) {
      return operandStack.pop().getValue();
    } else {
      throw new IllegalStateException("Opreand stack is empty");
    }
  }
}

