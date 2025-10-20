import org.mindrot.jbcrypt.BCrypt;

public class teste {
    public static void main(String[] args) {
        String senha = "@Helio123456";
        String hash = BCrypt.hashpw(senha, BCrypt.gensalt(12)); // força rounds = 12
        System.out.println(hash); // copie e salve no DB
    }
}
