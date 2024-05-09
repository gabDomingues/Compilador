import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeitorArquivoTexto {

    private final static int TAMANHO_BUFFER = 20;
    int[] bufferDeLeitura;
    int ponteiro;
    int bufferAtual;
    int inicioLexema;
    private String lexema;

    InputStream is;

    public LeitorArquivoTexto(String arquivo) {

        try {
            is = new FileInputStream(new File(arquivo));
            inicializarBuffer();
        } catch (FileNotFoundException e) {
            Logger.getLogger(LeitorArquivoTexto.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void inicializarBuffer() throws IOException {
        bufferAtual = 2;
        inicioLexema = 0;
        lexema = "";
        bufferDeLeitura = new int[TAMANHO_BUFFER *2];
        ponteiro = 0;
        recarregarBuffer1();
    }

    private void incrementarPonteiro() throws IOException {
        ponteiro++;
        if(ponteiro == TAMANHO_BUFFER){
            recarregarBuffer2();
        }else if(ponteiro ==TAMANHO_BUFFER *2 ){
            recarregarBuffer1();
            ponteiro = 0;
        }
    }

    private void recarregarBuffer1() throws IOException {
        if(bufferAtual == 2 ) {
            bufferAtual = 1;
            for (int i = 0; i < TAMANHO_BUFFER; i++) {
                bufferDeLeitura[i] = is.read();
                if (bufferDeLeitura[i] == -1) {
                    break;
                }
            }
        }
    }

    private void recarregarBuffer2() throws IOException {
        if(bufferAtual == 1) {
            bufferAtual = 2;
            for (int i = TAMANHO_BUFFER; i < TAMANHO_BUFFER * 2; i++) {
                bufferDeLeitura[i] = is.read();
                if (bufferDeLeitura[i] == -1) {
                    break;
                }
            }
        }
    }

    private int lerCaractereDoBuffer() throws IOException {
        int ret = bufferDeLeitura[ponteiro];
        incrementarPonteiro();;
        return ret;
    }

    public int lerProximoCaractere() throws IOException {
        int c = lerCaractereDoBuffer();
        lexema+= (char)c;
        return c;
       /* try {
            int ret = is.read();
            System.out.println((char) ret);
            return ret;
        } catch (FileNotFoundException e) {
            Logger.getLogger(LeitorArquivoTexto.class.getName()).log(Level.SEVERE, null, e);
            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     */
    }

    public void retroceder(){
        ponteiro--;
        lexema = lexema.substring(0, lexema.length()-1);
        if(ponteiro < 0){
            ponteiro = TAMANHO_BUFFER * 2 - 1;
        }
    }

    public void zerar(){
        ponteiro = inicioLexema;
        lexema = "";
    }

    public void confirmar(){
        inicioLexema = ponteiro;
        lexema = "";
    }

    public String getLexema(){
        return lexema;
    }
}

