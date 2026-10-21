package ma.ensias.employe.vulnerable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * DELIBERATELY VULNERABLE endpoints, used only to test SonarCloud detection.
 * Never merge this class into main.
 */
@RestController
@CrossOrigin(origins = "*") // VULN A05 Security Misconfiguration: permissive CORS
public class VulnerableController {

    private static final Logger log = LoggerFactory.getLogger(VulnerableController.class);

    @PersistenceContext
    private EntityManager entityManager;

    // VULN A03 Injection: SQL injection (user input concatenated into a native query)
    @GetMapping("/employes/search")
    public List<?> search(@RequestParam String name) {
        log.info("Search for employe: " + name); // VULN A09: log injection
        return entityManager
                .createNativeQuery("SELECT * FROM employe WHERE name = '" + name + "'")
                .getResultList();
    }

    // VULN A03 Injection: OS command injection
    @GetMapping("/admin/ping")
    public String ping(@RequestParam String host) throws IOException {
        Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ping -c 1 " + host});
        return new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    // VULN A01 Broken Access Control: path traversal (?name=../../etc/passwd)
    @GetMapping("/files")
    public String readFile(@RequestParam String name) throws IOException {
        return Files.readString(Paths.get("/var/data/" + name));
    }

    // VULN A10 Server-Side Request Forgery: server fetches any URL the user gives
    @GetMapping("/fetch")
    public String fetch(@RequestParam String url) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // VULN A03 Injection: reflected XSS (user input written into HTML)
    @GetMapping(value = "/hello", produces = MediaType.TEXT_HTML_VALUE)
    public String hello(@RequestParam String name) {
        return "<h1>Bonjour " + name + "</h1>";
    }

    // VULN A08 Software and Data Integrity Failures: insecure deserialization
    @PostMapping("/import")
    public String importData(@RequestBody byte[] body) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body))) {
            return String.valueOf(ois.readObject());
        }
    }
}
