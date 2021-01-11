package kim.hyunsub.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class MainController {
    @Value("${rootPath}")
    private String ROOT_PATH;

    @GetMapping("/**")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathStr = URLDecoder.decode(request.getRequestURI(), "UTF-8");
        Path path = Paths.get(ROOT_PATH, pathStr);

        if (Files.isRegularFile(path)) {
            response.setContentType(Files.probeContentType(path));
            response.getOutputStream().write(Files.readAllBytes(path));
        } else {
            throw new NotFileException();
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class NotFileException extends RuntimeException {}
}
