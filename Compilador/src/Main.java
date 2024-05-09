import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexico lex = new Lexico(args[0]);
        Token t = null;

        while( (t = lex.proximoToken()) != null){
            System.out.println(t);
        }
    }

}