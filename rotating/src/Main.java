import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Main {

    static float A = 0, B = 0, C = 0;
    static float cubeWidth = 20;
    static int width = 160, height = 44;
    static float[] zBuffer = new float[160 * 44];
    static char[] buffer = new char[160 * 44];
    static int backgroundASCIICode = '.';
    static int distanceFromCam = 100;
    static float horizontalOffset;
    static float K1 = 40;
    static float incrementSpeed = 0.6f;
    static float x, y, z, ooz;
    static int xp, yp, idx;

    public static float calculateX(float i, float j, float k) {
        return (float) (j * Math.sin(A) * Math.sin(B) * Math.cos(C) - k * Math.cos(A) * Math.sin(B) * Math.cos(C) +
                        j * Math.cos(A) * Math.sin(C) + k * Math.sin(A) * Math.sin(C) + i * Math.cos(B) * Math.cos(C));
    }

    public static float calculateY(float i, float j, float k) {
        return (float) (j * Math.cos(A) * Math.cos(C) + k * Math.sin(A) * Math.cos(C) -
                        j * Math.sin(A) * Math.sin(B) * Math.sin(C) + k * Math.cos(A) * Math.sin(B) * Math.sin(C) -
                        i * Math.cos(B) * Math.sin(C));
    }

    public static float calculateZ(float i, float j, float k) {
        return (float) (k * Math.cos(A) * Math.cos(B) - j * Math.sin(A) * Math.cos(B) + i * Math.sin(B));
    }


    public static void main(String[] args) {
        System.out.print("\033[2J");
        while (true) {
            fillBuffer(buffer, backgroundASCIICode);
            Arrays.fill(zBuffer, 0);
            cubeWidth = 20;
            horizontalOffset = -2 * cubeWidth;
            // drawing first cube
            for (float cubeX = -cubeWidth; cubeX < cubeWidth; cubeX += incrementSpeed) {
                for (float cubeY = -cubeWidth; cubeY < cubeWidth; cubeY += incrementSpeed) {
                    calculateForSurface(cubeX, cubeY, -cubeWidth, '@');
                    calculateForSurface(cubeWidth, cubeY, cubeX, '$');
                    calculateForSurface(-cubeWidth, cubeY, -cubeX, '~');
                    calculateForSurface(-cubeX, cubeY, cubeWidth, '#');
                    calculateForSurface(cubeX, -cubeWidth, -cubeY, ';');
                    calculateForSurface(cubeX, cubeWidth, cubeY, '+');
                }
            }
            cubeWidth = 10;
            horizontalOffset = 1 * cubeWidth;

            System.out.print("\033[H");
            for (int k = 0; k < width * height; k++) {
                System.out.print(buffer[k]);
                if (k % width == 0) {
                    System.out.println();
                }
            }

            A += 0.05f;
            B += 0.05f;
            C += 0.01f;
            try {
                sleep(35);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fillBuffer(char[] buffer, int ch) {
        Arrays.fill(buffer, (char) ch);
    }

    public static void calculateForSurface(float cubeX, float cubeY, float cubeZ, int ch) {
        x = calculateX(cubeX, cubeY, cubeZ);
        y = calculateY(cubeX, cubeY, cubeZ);
        z = calculateZ(cubeX, cubeY, cubeZ) + distanceFromCam;
        ooz = 1 / z;
        xp = (int)(width / 2 + horizontalOffset + K1 * ooz * x * 2);
        yp = (int)(height / 2 + K1 * ooz * y);
        idx = xp + yp * width;
        if (idx >= 0 && idx < width * height) {
            if (ooz > zBuffer[idx]) {
                zBuffer[idx] = ooz;
                buffer[idx] = (char) ch;
            }
        }
    }
}