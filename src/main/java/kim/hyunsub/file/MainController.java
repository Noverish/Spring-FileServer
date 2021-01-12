package kim.hyunsub.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class MainController {
    @Value("${rootPath}")
    private String ROOT_PATH;

    @Autowired
    private ImageService imageService;

    @GetMapping("/**/*.{ext:(?:jpg|jpeg|png|JPG|JPEG|PNG)}")
    public void jpg(HttpServletRequest request,
                    HttpServletResponse response,
                    @PathVariable String ext,
                    @RequestParam(required=false, name="w") Integer width,
                    @RequestParam(required=false, name="h") Integer height) throws IOException {
        String pathStr = URLDecoder.decode(request.getRequestURI(), "UTF-8");
        Path path = Paths.get(ROOT_PATH, pathStr);
        File file = path.toFile();

        if (!Files.isRegularFile(path)) {
            throw new NotFileException();
        }

        BufferedImage input = ImageIO.read(new FileInputStream(file));
        BufferedImage output;

        if (width != null && height != null) {
            output = imageService.resize(input, width, height);
        } else if (width != null) {
            output = imageService.resizeWithWidth(input, width);
        } else if (height != null) {
            output = imageService.resizeWithHeight(input, height);
        } else {
            output = input;
        }

        response.setContentType(Files.probeContentType(path));
        response.getOutputStream().write(imageService.imageToByteArr(output, ext));
    }

    @GetMapping("/**")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathStr = URLDecoder.decode(request.getRequestURI(), "UTF-8");
        Path path = Paths.get(ROOT_PATH, pathStr);

        if (!Files.isRegularFile(path)) {
            throw new NotFileException();
        }

        response.setContentType(Files.probeContentType(path));
        response.getOutputStream().write(Files.readAllBytes(path));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class NotFileException extends RuntimeException {}
}
