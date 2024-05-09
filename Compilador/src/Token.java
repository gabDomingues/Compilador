public class Token {
     public TipoToken nome;
     public String lexema;

    public Token(String lexema, TipoToken nome) {
        this.lexema = lexema;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "<" +
                 nome +
                 lexema +
                '>';
    }
}
