import java.io.IOException;
import java.util.Calendar;
import java.util.TooManyListenersException;

public class Lexico {
    LeitorArquivoTexto ldat;


    public Lexico(String arquivo) throws IOException {
        ldat = new LeitorArquivoTexto(arquivo);
    }

    public Token proximoToken() throws IOException {
       Token proximo = null;
       espacoComentario();
       ldat.confirmar();

       proximo = fim();
       if(proximo == null){
               ldat.zerar();
       }else{
           ldat.confirmar();
           return proximo;
       }
       proximo = palavraChave();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = variavel();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = numeros();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = variavel();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = cadeia();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = operadorRelacional();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = operadorAritmetico();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = parenteses();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }

        proximo = chave();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        return null;
    }

    private Token parenteses() throws IOException {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '(') {
            return new Token("(", TipoToken.AbrePar);
        } else if (c == ')') {
            return new Token(")", TipoToken.FechaPar);
        }else{
            return null;
        }
    }
    private Token chave() throws IOException {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '{') {
            return new Token("{", TipoToken.AbreChav);
        } else if (c == '}') {
            return new Token("}", TipoToken.FechaChav);
        }else{
            return null;
        }
    }

    private Token operadorAritmetico() throws IOException {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '*') {
            return new Token("*", TipoToken.OpAriMult);
        } else if (c == '/') {
            return new Token("/", TipoToken.OpAriDiv);
        } else if (c == '+') {
            return new Token("+", TipoToken.OpAriSoma);
        } else if (c == '-') {
            return new Token("-", TipoToken.OpAriSub);
        } else {
            return null;
        }
    }

    private Token delimitador() throws IOException {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == ':') {
            return new Token(":", TipoToken.Delim);
        }else{
            return null;
        }
    }

    private Token operadorRelacional() throws IOException {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '<') {
            c = (char) ldat.lerProximoCaractere();
            if (c == '>') {
                return new Token("<>", TipoToken.OpRelDif);
            } else if (c == '=') {
                return new Token(">=", TipoToken.OpRelMenorIgualQue);
            } else {
                ldat.retroceder();
                return new Token("<", TipoToken.OpRelMenorQue);
            }
        } else if (c == '=') {
            return new Token("=", TipoToken.OpRelIgual);
        } else if (c == '>') {
            c = (char) ldat.lerProximoCaractere();
            if (c == '=') {
                return new Token(">=", TipoToken.OpRelMaiorIgualQue);
            } else {
                return new Token(">", TipoToken.OpRelMaiorQue);
            }
        } else {
            return null;
        }
    }

    private Token numeros() throws IOException {
        int estado = 1;
        while(true){
            char c = (char) ldat.lerProximoCaractere();
            if(estado == 1){
                if(Character.isDigit(c)){
                    estado = 2;
                }else{
                    return null;
                }
            }else if(estado == 2){
                if(c == '.'){
                    c = (char) ldat.lerProximoCaractere();
                    if(Character.isDigit(c)){
                        estado = 3;
                    }else{
                        return null;
                    }
                }else if(!Character.isDigit(c)){
                    ldat.retroceder();
                    return new Token(ldat.getLexema(), TipoToken.NumInt);
                }
            }else if(estado == 3){
                if(!Character.isDigit(c)){
                    ldat.retroceder();
                    return new Token(ldat.getLexema(), TipoToken.NumReal);
                }
            }
        }
    }

    private Token variavel() throws IOException {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isLetter(c)) {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (!Character.isLetter(c)) {
                    ldat.retroceder();
                    return new Token(ldat.getLexema(), TipoToken.Variavel);
                }
            }
        }
    }

    private Token cadeia() throws IOException {
        int estado = 1;
        while (true){
            char c = (char) ldat.lerProximoCaractere();
            if(estado == 1){
                if(c == '\''){
                    estado = 2;
                }else{
                    return null;
                }
            } else if (estado == 2) {
                if (c == '\n'){
                    return null;
                }
                if(c == '\''){
                    return new Token(ldat.getLexema(), TipoToken.Cadeia);
                }else if(c == '\\'){
                    estado = 3;
                }
            }else if(estado == 3){
                if(c == '\n'){
                    return null;
                }else{
                    estado = 2;
                }
            }
        }
    }

    private void espacoComentario() throws IOException {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isWhitespace(c) || c == ' ') {
                    estado = 2;
                } else if (c == '\'') {
                    estado = 3;
                } else {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 2) {
                if (c == '%') {
                    estado = 3;
                } else if (!Character.isWhitespace(c) || c == ' ') {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return;
                }
            }
        }
    }

    private Token palavraChave(){
        return null;
    }

    private Token fim() throws IOException {
        int caractereLido = ldat.lerProximoCaractere();
        if(caractereLido == -1){
            return new Token("Fim", TipoToken.Fim);
        }
        return null;
    }
}
