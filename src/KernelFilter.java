import java.awt.Color;
import java.lang.Math;
import java.util.Scanner;

public class KernelFilter {
  // Returns the color of each pixel
  private static Picture kernel(Picture picture, double[][] index) {
    int w = picture.width();
    int h = picture.height();
    Picture target = new Picture(w, h);
    for (int col = 0; col < w; col++) {
      for (int row = 0; row < h; row++) {
        // now in a selected pixel
        double newr = 0.0;
        double newg = 0.0;
        double newb = 0.0;
        for (int i = -1; i < 2; i++) {
          for (int j = -1; j < 2; j++) {
            // loop to iterate through surrounding pixels
            Color color = picture.get((w + col + i) % w,
                (h + row + j) % h);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            // updating colors based on the conolution matrix
            newr += r * index[i + 1][j + 1];
            newg += g * index[i + 1][j + 1];
            newb += b * index[i + 1][j + 1];
          }
        }
        int fr = (int) Math.round(newr);
        int fg = (int) Math.round(newg);
        int fb = (int) Math.round(newb);
        // final rbg values within 0-255 range
        if (fr > 255)
          fr = 255;
        if (fg > 255)
          fg = 255;
        if (fb > 255)
          fb = 255;
        if (fr < 0)
          fr = 0;
        if (fg < 0)
          fg = 0;
        if (fb < 0)
          fb = 0;
        Color fcolor = new Color(fr, fg, fb);
        // setting up particular pixel color for the new picture
        target.set(col, row, fcolor);
      }
    }
    return target;
  }
  
  // Returns a new picture that applies a dimming filter on the           picture
  public static Picture dim(Picture picture) {
    double[][] index = new double[3][3];
    index[0][0] = 0.0;
    index[0][1] = 0.0;
    index[0][2] = 0.0;
    index[1][0] = 0.0;
    index[1][1] = 1.0 / 2;
    index[1][2] = 0.0;
    index[2][0] = 0.0;
    index[2][1] = 0.0;
    index[2][2] = 0.0;
    return kernel(picture, index);

  }

  // Returns a new picture that applies a EdgeDectect filter on the         picture
  public static Picture edgeDetect(Picture picture) {

    double[][] grayindex1 = new double[3][3];
    grayindex1[0][0] = 1.0;
    grayindex1[0][1] = 2.0;
    grayindex1[0][2] = 1.0;
    grayindex1[1][0] = 0.0;
    grayindex1[1][1] = 0.0;
    grayindex1[1][2] = 0.0;
    grayindex1[2][0] = -1.0;
    grayindex1[2][1] = -2.0;
    grayindex1[2][2] = -1.0;

    double[][] grayindex2 = new double[3][3];
    grayindex2[0][0] = -1.0;
    grayindex2[0][1] = 0.0;
    grayindex2[0][2] = 1.0;
    grayindex2[1][0] = -2.0;
    grayindex2[1][1] = 0.0;
    grayindex2[1][2] = 2.0;
    grayindex2[2][0] = -1.0;
    grayindex2[2][1] = 0.0;
    grayindex2[2][2] = 1.0;
    int width = picture.width();
    int height = picture.height();
    Picture target = new Picture(width, height);
    Picture firsthalf = new Picture(width, height);
    Picture secondhalf = new Picture(width, height);

    firsthalf = kernel(picture, grayindex1);
    secondhalf = kernel(picture, grayindex2);

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int gray1 = (int) Luminance.intensity(firsthalf.get(i, j));
        int gray2 = (int) Luminance.intensity(secondhalf.get(i, j));
        int magnitude = Math.abs(255 - Math.abs((int) (Math.sqrt(gray1 * gray1 + gray2 * gray2))));
        Color grayscale = new Color(magnitude, magnitude, magnitude);
        target.set(i, j, grayscale);

      }
    }
    return target;
  }

  // Returns a new picture that applies the identity filter to the given picture.
  public static Picture identity(Picture picture) {
    return picture;
  }

  // Returns a new picture that applies a gaussian blur filter to the given
  // picture.
  public static Picture gaussian(Picture picture) {
    double[][] index = new double[3][3];
    index[0][0] = 1.0 / 16;
    index[0][1] = 2.0 / 16;
    index[0][2] = 1.0 / 16;
    index[1][0] = 2.0 / 16;
    index[1][1] = 4.0 / 16;
    index[1][2] = 2.0 / 16;
    index[2][0] = 1.0 / 16;
    index[2][1] = 2.0 / 16;
    index[2][2] = 1.0 / 16;
    return kernel(picture, index);

  }

  // Returns a new picture that applies a sharpen filter to the given picture.
  public static Picture sharpen(Picture picture) {
    double[][] index = new double[3][3];
    index[0][0] = 0.0;
    index[0][1] = -1.0;
    index[0][2] = 0.0;
    index[1][0] = -1.0;
    index[1][1] = 5.0;
    index[1][2] = -1.0;
    index[2][0] = 0.0;
    index[2][1] = -1.0;
    index[2][2] = 0.0;
    return kernel(picture, index);
  }

  // Returns a new picture that applies a Laplacian filter to the given picture.
  public static Picture laplacian(Picture picture) {
    double[][] index = new double[3][3];
    index[0][0] = -1.0;
    index[0][1] = -1.0;
    index[0][2] = -1.0;
    index[1][0] = -1.0;
    index[1][1] = 8.0;
    index[1][2] = -1.0;
    index[2][0] = -1.0;
    index[2][1] = -1.0;
    index[2][2] = -1.0;
    return kernel(picture, index);
  }

  // Returns a new picture that applies an emboss filter to the given picture.
  public static Picture emboss(Picture picture) {
    double[][] index = new double[3][3];
    index[0][0] = -2.0;
    index[0][1] = -1.0;
    index[0][2] = 0.0;
    index[1][0] = -1.0;
    index[1][1] = 1.0;
    index[1][2] = 1.0;
    index[2][0] = 0.0;
    index[2][1] = 1.0;
    index[2][2] = 2.0;
    return kernel(picture, index);
  }

  // Returns a new picture that applies a motion blur filter to the given picture.
  public static Picture motionBlur(Picture picture) {
    double[][] index = new double[9][9];
    for (int i = 0; i < 9; i++)
      index[i][i] = 1.0 / 9;
    int w = picture.width();
    int h = picture.height();
    Picture target = new Picture(w, h);
    for (int col = 0; col < w; col++) {
      for (int row = 0; row < h; row++) {
        double newr = 0.0;
        double newg = 0.0;
        double newb = 0.0;
        for (int i = -4; i < 5; i++) {
          for (int j = -4; j < 5; j++) {
            Color color = picture.get((w + col + i) % w,
                (h + row + j) % h);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            newr += r * index[i + 4][j + 4];
            newg += g * index[i + 4][j + 4];
            newb += b * index[i + 4][j + 4];
          }
        }
        int fr = (int) Math.round(newr);
        int fg = (int) Math.round(newg);
        int fb = (int) Math.round(newb);
        if (fr > 255)
          fr = 255;
        if (fg > 255)
          fg = 255;
        if (fb > 255)
          fb = 255;
        if (fr < 0)
          fr = 0;
        if (fg < 0)
          fg = 0;
        if (fb < 0)
          fb = 0;
        Color fcolor = new Color(fr, fg, fb);
        target.set(col, row, fcolor);
      }
    }
    return target;
  }

  // Returns a new picture that scales the image to new dimensions
  public static Picture scale(Picture picture) {
    int width = 1600;
    int height = 900;

    Picture target = new Picture(width, height);

    for (int targetCol = 0; targetCol < width; targetCol++) {
      for (int targetRow = 0; targetRow < height; targetRow++) {
        int sourceCol = targetCol * picture.width() / width;
        int sourceRow = targetRow * picture.height() / height;
        Color color = picture.get(sourceCol, sourceRow);
        target.set(targetCol, targetRow, color);
      }
    }

    return target;
  }

  // Return a new picture that applies a black and white filter on        the picture
  public static Picture grayScale(Picture picture) {
    int width = picture.width();
    int height = picture.height();

    Picture target = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        Color color = picture.get(col, row);
        Color gray = Luminance.toGray(color);
        target.set(col, row, gray);
      }
    }

    return target;
  }

  // Returns a new picture that horizontally flips the picture
  public static Picture flipS(Picture picture) {
    int width = picture.width();
    int height = picture.height();

    Picture target = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        Color color = picture.get(row, col);
        target.set(width - row - 1, col, color);
      }
    }

    return target;
  }

  // Returns a new picture that vertically flips the picture
  public static Picture flipV(Picture picture) {
    int width = picture.width();
    int height = picture.height();

    Picture target = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        Color color = picture.get(col, row);
        target.set(height - col - 1, width - row - 1, color);
      }
    }

    return target;
  }

  // Returns a new picture that applies a glass filter 
  public static Picture glass(Picture picture) {
    int width = picture.width();
    int height = picture.height();

    Picture target = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        int xx = (width + col + (int) (Math.random() * 10) - 5) % width;
        int yy = (height + row + (int) (Math.random() * 10) - 5) % height;
        Color color = picture.get(xx, yy);
        target.set(col, row, color);
      }
    }

    return target;
  }

  // Returns a new picture that applies a wave filter 
  public static Picture wave(Picture picture) {
    int width = picture.width();
    int height = picture.height();

    Picture target = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        int xx = col;
        int yy = (int) (row + 20 * Math.sin(col * 2 * Math.PI / 64));
        if (yy >= 0 && yy < height)
          target.set(col, row, picture.get(xx, yy));
      }
    }

    return target;
  }

  // Returns a new picture with that is brighter
  public static Picture bright(Picture picture) {
    int width = picture.width();
    int height = picture.height();

    Picture target = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        Color color = picture.get(col, row).brighter();
        target.set(col, row, color);
      }
    }

    return target;
  }

  // Returns a new picture that returns a Digital Zoom at a               particular point 
  public static Picture zoom(Picture picture) {
    int width = picture.width();
    int height = picture.height();
    double scale = 2;
    double posx = 0.5;
    double posy = 0.4;

    Picture target = new Picture(width, height);

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        int cols = (int) (posx * width - width / (2 * scale) + col / scale);
        int rows = (int) (posy * height - height / (2 * scale) + row / scale);
        if (cols < 0 || cols >= width || rows < 0 || rows >= height)
          target.set(col, row, Color.BLACK);
        else
          target.set(col, row, picture.get(cols, rows));
      }
    }

    return target;

  }

  // Main function
  public static void main(String[] args) {
    System.out.println("***************     Welcome to Kernel Filter     ***************");
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter the image file : ");
    String file = sc.next();
    Picture picture = new Picture(file);
    boolean flag = true;
    System.out.println("The filters that are available are");
    System.out.println(
        "\t1. Scale to dimensions\n\t2. Sharpen\n\t3. Brighten\n\t4. Dim\n\t5. Zoom\n\t6. Black & White\n\t7. Gaussian Blur\n\t8. Lateral Invert\n\t9. Vertical Invert\n\t10. Motion Blur\n\t11. Laplacian\n\t12. Emboss\n\t13. Glass\n\t14. Wave\n\t15. Edge Detect");
    while (flag) {
      System.out.print("Enter your choice : ");
      int ch = sc.nextInt();
      switch (ch) {
        case 1:
          Picture scale = scale(picture);
          scale.show();
          break;
        case 2:
          Picture sharpen = sharpen(picture);
          sharpen.show();
          break;
        case 3:
          Picture bright = bright(picture);
          bright.show();
          break;
        case 4:
          Picture dim = dim(picture);
          dim.show();
          break;
        case 5:
          Picture zoom = zoom(picture);
          zoom.show();
          break;
        case 6:
          Picture grayscale = grayScale(picture);
          grayscale.show();
          break;
        case 7:
          Picture gaussian = gaussian(picture);
          gaussian.show();
          break;
        case 8:
          Picture flipS = flipS(picture);
          flipS.show();
          break;
        case 9:
          Picture flipV = flipV(picture);
          flipV.show();
          break;
        case 10:
          Picture motionBlur = motionBlur(picture);
          motionBlur.show();
          break;
        case 11:
          Picture laplacian = laplacian(picture);
          laplacian.show();
          break;
        case 12:
          Picture emboss = emboss(picture);
          emboss.show();
          break;
        case 13:
          Picture glass = glass(picture);
          glass.show();
          break;
        case 14:
          Picture wave = wave(picture);
          wave.show();
          break;
        case 15:
          Picture edgedetect = edgeDetect(picture);
          edgedetect.show();
          break;
      }
      System.out.print("Do you want to continue (y/n) : ");
      char chf = sc.next().charAt(0);
      if (chf == 'n')
        flag = false;

    }
    sc.close();
    System.out.println("Thank You");
  }
}
