package lexer;
public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("SELECT * FROM users WHERE age >= 18;");

        for (Token token : lexer.tokenize()) {
            System.out.println(token);
        }
    }
}