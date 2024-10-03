import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandala extends JPanel {

    private BufferedImage lienzo;
    private int ancho;
    private int alto;

    public Mandala(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        lienzo = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        // Rellenar el fondo de blanco
        rellenarFondo(Color.WHITE);
        // Dibujar la mandala
        dibujarMandala();
    }

    // Método para rellenar el fondo con un color
    private void rellenarFondo(Color color) {
        Graphics2D g2d = lienzo.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, ancho, alto);
        g2d.dispose();
    }

    // Método para establecer un píxel en el lienzo
    private void ponerPixel(int x, int y, Color color) {
        if (x >= 0 && x < ancho && y >= 0 && y < alto) {
            lienzo.setRGB(x, y, color.getRGB());
        }
    }

    // Algoritmo de Punto Medio para dibujar un círculo
    private void dibujarCirculo(int centroX, int centroY, int radio, Color color) {
        int x = radio;
        int y = 0;
        int decision = 1 - x;

        while (y <= x) {
            // Octantes del círculo
            ponerPixel(centroX + x, centroY + y, color);
            ponerPixel(centroX - x, centroY + y, color);
            ponerPixel(centroX + x, centroY - y, color);
            ponerPixel(centroX - x, centroY - y, color);
            ponerPixel(centroX + y, centroY + x, color);
            ponerPixel(centroX - y, centroY + x, color);
            ponerPixel(centroX + y, centroY - x, color);
            ponerPixel(centroX - y, centroY - x, color);

            y++;
            if (decision <= 0) {
                decision += 2 * y + 1;
            } else {
                x--;
                decision += 2 * (y - x) + 1;
            }
        }
    }

    // Algoritmo de Bresenham para dibujar una línea
    private void dibujarLinea(int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;

        while (true) {
            ponerPixel(x0, y0, color);

            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    // Método para dibujar un óvalo usando el algoritmo de Punto Medio
    private void dibujarOvalo(int centroX, int centroY, int radioX, int radioY, Color color) {
        int x = 0;
        int y = radioY;
        int rxSq = radioX * radioX;
        int rySq = radioY * radioY;
        int tworxSq = 2 * rxSq;
        int tworySq = 2 * rySq;

        // Regiones
        int p = (int)(rySq - (rxSq * radioY) + (0.25 * rxSq));
        int dx = tworxSq * y;
        int dy = tworySq * x;

        // Región 1
        while (dx < dy) {
            ponerPixel(centroX + x, centroY + y, color);
            ponerPixel(centroX - x, centroY + y, color);
            ponerPixel(centroX + x, centroY - y, color);
            ponerPixel(centroX - x, centroY - y, color);

            if (p < 0) {
                x++;
                dx += tworySq;
                p += rySq + dx;
            } else {
                x++;
                y--;
                dx += tworySq;
                dy += tworxSq;
                p += rySq + dx - dy;
            }
        }

        // Región 2
        p = (int)(rySq * (x + 0.5) * (x + 0.5) + rxSq * (y -1) * (y -1) - rxSq * rySq);
        while (y >= 0) {
            ponerPixel(centroX + x, centroY + y, color);
            ponerPixel(centroX - x, centroY + y, color);
            ponerPixel(centroX + x, centroY - y, color);
            ponerPixel(centroX - x, centroY - y, color);

            if (p > 0) {
                y--;
                dy -= tworxSq;
                p += rxSq - dy;
            } else {
                y--;
                x++;
                dx += tworySq;
                dy -= tworxSq;
                p += rxSq + dx - dy;
            }
        }
    }

    // Método para dibujar un rectángulo usando líneas
    private void dibujarRectangulo(int x0, int y0, int anchoRect, int altoRect, Color color) {
        // Dibujar las cuatro líneas del rectángulo
        dibujarLinea(x0, y0, x0 + anchoRect, y0, color); // Superior
        dibujarLinea(x0 + anchoRect, y0, x0 + anchoRect, y0 + altoRect, color); // Derecha
        dibujarLinea(x0 + anchoRect, y0 + altoRect, x0, y0 + altoRect, color); // Inferior
        dibujarLinea(x0, y0 + altoRect, x0, y0, color); // Izquierda
    }

    // Método para dibujar un cuadrado usando líneas
    private void dibujarCuadrado(int x0, int y0, int lado, Color color) {
        dibujarRectangulo(x0, y0, lado, lado, color);
    }

    // Método para dibujar la Mandala
    private void dibujarMandala() {
        int centroX = ancho / 2;
        int centroY = alto / 2;

        // Dibujar círculos concéntricos
        int radioInicial = 150;
        for (int i = 0; i < 10; i++) {
            Color color = Color.getHSBColor((float)i / 10, 0.8f, 0.8f);
            dibujarCirculo(centroX, centroY, radioInicial - i * 15, color);
        }

        // Dibujar líneas radiales
        int numeroLineas = 36;
        for (int i = 0; i < numeroLineas; i++) {
            double angulo = 2 * Math.PI / numeroLineas * i;
            int xFinal = centroX + (int)(radioInicial * Math.cos(angulo));
            int yFinal = centroY + (int)(radioInicial * Math.sin(angulo));

            // Alternar colores
            Color colorLinea = (i % 2 == 0) ? Color.RED : Color.BLUE;
            dibujarLinea(centroX, centroY, xFinal, yFinal, colorLinea);
        }

        // Dibujar óvalos alrededor de la mandala
        for (int i = 0; i < numeroLineas; i += 3) {
            double angulo = 2 * Math.PI / numeroLineas * i;
            int xOvalo = centroX + (int)((radioInicial + 30) * Math.cos(angulo));
            int yOvalo = centroY + (int)((radioInicial + 30) * Math.sin(angulo));
            int radioX = 20;
            int radioY = 40;
            Color colorOvalo = Color.MAGENTA;
            dibujarOvalo(xOvalo, yOvalo, radioX, radioY, colorOvalo);
        }

        // Dibujar cuadrados alrededor de la mandala
        for (int i = 0; i < numeroLineas; i += 4) {
            double angulo = 2 * Math.PI / numeroLineas * i;
            int lado = 30;
            int xCuadrado = centroX + (int)((radioInicial + 60) * Math.cos(angulo)) - lado / 2;
            int yCuadrado = centroY + (int)((radioInicial + 60) * Math.sin(angulo)) - lado / 2;
            Color colorCuadrado = Color.GREEN;
            dibujarCuadrado(xCuadrado, yCuadrado, lado, colorCuadrado);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar el lienzo en el panel
        g.drawImage(lienzo, 0, 0, null);
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Mandala PM - 22110092");
        Mandala panelMandala = new Mandala(800, 800);
        ventana.add(panelMandala);
        ventana.setSize(800, 800);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        ventana.setVisible(true);
    }
}
