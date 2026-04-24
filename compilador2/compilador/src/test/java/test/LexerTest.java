 package test;

import lexer.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testSelectQuery() {
        Lexer lexer = new Lexer("SELECT * FROM users;");
        List<Token> tokens = lexer.tokenize();

        assertEquals(TokenType.SELECT, tokens.get(0).type);
        assertEquals(TokenType.ASTERISK, tokens.get(1).type);
        assertEquals(TokenType.FROM, tokens.get(2).type);
        assertEquals(TokenType.IDENTIFIER, tokens.get(3).type);
    }

    @Test
    public void testWhereCondition() {
        Lexer lexer = new Lexer("WHERE age >= 18;");
        List<Token> tokens = lexer.tokenize();

        assertEquals(TokenType.WHERE, tokens.get(0).type);
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals(TokenType.GREATER_EQUAL, tokens.get(2).type);
        assertEquals(TokenType.NUMBER, tokens.get(3).type);
    }

    @Test
    public void testString() {
        Lexer lexer = new Lexer("WHERE name = 'Juan';");
        List<Token> tokens = lexer.tokenize();

        assertEquals(TokenType.STRING, tokens.get(3).type);
    }
}