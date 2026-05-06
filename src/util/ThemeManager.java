/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author leandrorocha
 */

import java.awt.Color;

public class ThemeManager {
    private static boolean darkMode = true;

    // --- DARK MODE (ALTO CONTRASTE) ---
    public static Color darkBg = new Color(13, 17, 23);       // Fundo mais escuro para destaque
    public static Color darkCard = new Color(22, 27, 34);     // Cards bem definidos
    public static Color darkText = new Color(240, 246, 252);  // Branco puro para leitura
    public static Color darkSubText = new Color(139, 148, 158); // Cinza claro para labels secundárias

    // --- LIGHT MODE (DIVISÕES CLARAS) ---
    public static Color lightBg = new Color(246, 248, 250);   // Fundo cinza gelo
    public static Color lightCard = new Color(255, 255, 255); // Branco total para cards
    public static Color lightText = new Color(31, 35, 40);    // Preto acinzentado
    public static Color lightSubText = new Color(87, 96, 106); // Cinza escuro para labels

    public static Color accent = new Color(31, 111, 235);     // Azul institucional

    public static void toggleTheme() { darkMode = !darkMode; }
    public static boolean isDarkMode() { return darkMode; }

    public static Color getBg() { return darkMode ? darkBg : lightBg; }
    public static Color getCard() { return darkMode ? darkCard : lightCard; }
    public static Color getText() { return darkMode ? darkText : lightText; }
    public static Color getSubText() { return darkMode ? darkSubText : lightSubText; }
    public static Color getAccent() { return accent; }
}