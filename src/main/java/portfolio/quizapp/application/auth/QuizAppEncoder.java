package portfolio.quizapp.application.auth;

import org.springframework.stereotype.Component;
import portfolio.quizapp.exception.badrequest.NoPasswordException;
import portfolio.quizapp.exception.internalserver.AlgorithmNotSupportedException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class QuizAppEncoder {

    private final String algorithm = "SHA3-256";

    public String encode(final CharSequence rawPassword) {
        try {
            if (rawPassword == null) {
                throw new NoPasswordException();
            }

            final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            final byte[] hash = messageDigest.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
            final String encryptedText = bytesToHexString(hash);

            return encryptedText;
        } catch (final NoSuchAlgorithmException ex) {
            try (PrintWriter pw = new PrintWriter(new StringWriter())) {
                ex.printStackTrace(pw);
                throw new AlgorithmNotSupportedException(pw.toString());
            }
        }
    }

    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    private String bytesToHexString(final byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        for (final byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
