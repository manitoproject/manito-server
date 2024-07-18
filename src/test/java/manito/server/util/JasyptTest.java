package manito.server.util;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JasyptConfig.class)
public class JasyptTest {
    @Autowired
    private StringEncryptor jasyptEncryptor;

    @Test
    void custom_jasypt_test_user() {
        String encrypted = jasyptEncryptor.encrypt("manitopemkey");
        System.out.println("encrypted: " + encrypted);

        String decrypted = jasyptEncryptor.decrypt(encrypted);
        System.out.println("decrypted: " + decrypted);
        Assertions.assertThat(decrypted).isEqualTo("manitopemkey");
    }
}
