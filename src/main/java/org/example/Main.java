package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Introduce una expresión (escribe 'salir' para finalizar el código):");
            System.out.print("→  ");
            String expresion = scanner.nextLine();

            if (expresion.equalsIgnoreCase("salir")) {
                break;
            }

            try {
                // Verificar si los paréntesis están balanceados
                if (!parentesisBalanceados(expresion)) {
                    throw new Exception("Error: Los paréntesis no están balanceados.");
                }

                // Obtener los tokens (números y operadores separados)
                List<String> tokens = obtenerTokens(expresion);
                System.out.println("Expresión en tokens: " + tokens);

                // Convertir a notación postfija
                List<String> postfija = convertirAPostfija(tokens);
                System.out.println("Expresión postfija: " + postfija);

                // Evaluar la expresión postfija
                double resultado = evaluarPostfija(postfija);
                System.out.println("Resultado: " + resultado);

            } catch (EmptyStackException e) {
                System.out.println("Error: La expresión ingresada es inválida.");
            } catch (ArithmeticException e) {
                System.out.println("Error: Ocurrió un problema con una operación aritmética (posible división por cero).");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();  // Cerrar el Scanner al final
    }

    // Método para verificar si los paréntesis están balanceados
    public static boolean parentesisBalanceados(String expresion) {
        int balance = 0;
        for (char c : expresion.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            }
            // Si en algún momento hay más paréntesis de cierre que de apertura, no está balanceado
            if (balance < 0) {
                return false;
            }
        }
        // Al final, el balance debe ser 0 (mismos paréntesis de apertura que de cierre)
        return balance == 0;
    }

    // Ver si es un operador (+, -, *, /, ^)
    public static boolean esOperador(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/") || token.equals("^");
    }

    // Separar la expresión en tokens (números y operadores)
    public static List<String> obtenerTokens(String expresion) {
        List<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(expresion, " ()+-*/^", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    // Convertir a notación postfija usando la regla de precedencia
    public static List<String> convertirAPostfija(List<String> tokens) {
        Stack<String> pila = new Stack<>();
        List<String> salida = new ArrayList<>();

        for (String token : tokens) {
            if (esNumero(token)) {
                salida.add(token);
            } else if (esOperador(token)) {
                while (!pila.isEmpty() && precedenciaOperador(pila.peek()) >= precedenciaOperador(token)) {
                    salida.add(pila.pop());
                }
                pila.push(token);
            } else if (token.equals("(")) {
                pila.push(token);
            } else if (token.equals(")")) {
                while (!pila.peek().equals("(")) {
                    salida.add(pila.pop());
                }
                pila.pop();
            }
        }

        while (!pila.isEmpty()) {
            salida.add(pila.pop());
        }

        return salida;
    }

    // Ver si es un número
    public static boolean esNumero(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Obtener la precedencia de los operadores
    public static int precedenciaOperador(String operador) {
        switch (operador) {
            case "^": return 3;
            case "*":
            case "/": return 2;
            case "+":
            case "-": return 1;
            default: return 0;
        }
    }

    // Evaluar la expresión postfija
    public static double evaluarPostfija(List<String> postfija) {
        Stack<Double> pila = new Stack<>();

        for (String token : postfija) {
            if (esNumero(token)) {
                pila.push(Double.parseDouble(token));
            } else if (esOperador(token)) {
                double b = pila.pop();
                double a = pila.pop();
                switch (token) {
                    case "+": pila.push(a + b); break;
                    case "-": pila.push(a - b); break;
                    case "*": pila.push(a * b); break;
                    case "/": pila.push(a / b); break;
                    case "^": pila.push(Math.pow(a, b)); break;
                }
            }
        }
        return pila.pop();
    }
}
