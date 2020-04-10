import Components.Encrypter;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class EncrypterTest {

  @Test
  void getHash_EqualPasswords_ReturnDifferentHashes() {
    String password = "test";
    String hash1 = Encrypter.getHash(Encrypter.encrypt(password, null));
    String hash2 = Encrypter.getHash(Encrypter.encrypt(password, null));

    assertNotEquals(hash1, hash2);
  }

  @Test
  void getSalt_Random_ReturnTwoDifferentSalts() {
    String password = "test";
    String salt1 = Encrypter.getSalt(Encrypter.encrypt(password, null));
    String salt2 = Encrypter.getSalt(Encrypter.encrypt(password, null));

    assertNotEquals(salt1, salt2);
  }

  @Test
  void getHash_EqualPasswordAndSalt_ReturnEqualHashes() {
    String password = "test";
    String hash1 = Encrypter.getHash(Encrypter.encrypt(password, "salt"));
    String hash2 = Encrypter.getHash(Encrypter.encrypt(password, "salt"));

    assertEquals(hash1, hash2);
  }
}
