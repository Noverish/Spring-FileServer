package kim.hyunsub.file;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageService {
    public BufferedImage resize(BufferedImage input, int width, int height) {
        if (input.getWidth() == width && input.getHeight() == height) {
            return input;
        }

        BufferedImage output = new BufferedImage(width, height, input.getType());

        double heightRatio = (double) height / input.getHeight();
        double widthRatio = (double) width / input.getWidth();
        double ratio = Math.max(heightRatio, widthRatio);

        double ratioWidth = width / ratio;
        double ratioHeight = height / ratio;
        double x = (input.getWidth() - ratioWidth) / 2;
        double y = (input.getHeight() - ratioHeight) / 2;

        BufferedImage cropped = input.getSubimage((int) x, (int) y, (int) ratioWidth, (int) ratioHeight);

        Graphics2D graphics2D = output.createGraphics();
        graphics2D.drawImage(cropped, 0, 0, width, height, null);
        graphics2D.dispose();

        return output;
    }

    public BufferedImage resizeWithWidth(BufferedImage input, int width) {
        if (input.getWidth() == width) {
            return input;
        }

        int height = (int) ((double) width * input.getHeight() / input.getWidth());
        BufferedImage output = new BufferedImage(width, height, input.getType());
        Graphics2D graphics2D = output.createGraphics();
        graphics2D.drawImage(input, 0, 0, width, height, null);
        graphics2D.dispose();

        return output;
    }

    public BufferedImage resizeWithHeight(BufferedImage input, int height) {
        if (input.getHeight() == height) {
            return input;
        }

        int width = (int) ((double) height * input.getWidth() / input.getHeight());
        BufferedImage output = new BufferedImage(width, height, input.getType());
        Graphics2D graphics2D = output.createGraphics();
        graphics2D.drawImage(input, 0, 0, width, height, null);
        graphics2D.dispose();

        return output;
    }

    public byte[] imageToByteArr(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, baos);
        return baos.toByteArray();
    }
}
