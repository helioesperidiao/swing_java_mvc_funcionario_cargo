import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

public class teste {
    public void lerArquivo() throws IOException {
        FileReader file = new FileReader("arquivo.txt");
    }

    public static void main(String[] args) {
        try (FileReader fr = new FileReader("arquivo.txt");
                BufferedReader br = new BufferedReader(fr)) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }

    }

    public void processarArquivo(String nomeArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao processar arquivo: " + e.getMessage());
        } finally {
            System.out.println("Processamento finalizado.");
        }
    }
}
