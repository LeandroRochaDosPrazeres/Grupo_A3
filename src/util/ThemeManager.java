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

/**
 * Gestor de Identidade Visual - Finance Team
 * Centraliza as cores para garantir um design minimalista e profissional.
 */
public class ThemeManager {
    private static boolean darkMode = true; // Define se o sistema inicia no modo escuro

    // --- CORES MODO ESCURO (DARK MODE) ---
    public static final Color DARK_BG = new Color(13, 17, 23);     // Fundo principal
    public static final Color DARK_CARD = new Color(22, 27, 34);   // Fundo do formulário
    public static final Color DARK_TEXT = new Color(201, 209, 217); // Cor dos textos
    public static final Color DARK_ACCENT = new Color(88, 166, 255); // Azul de destaque (Links/Botões)

    // --- CORES MODO CLARO (LIGHT MODE) ---
    public static final Color LIGHT_BG = new Color(246, 248, 250);
    public static final Color LIGHT_CARD = new Color(255, 255, 255);
    public static final Color LIGHT_TEXT = new Color(31, 35, 40);
    public static final Color LIGHT_ACCENT = new Color(9, 105, 218);

    // Método para alternar o tema
    public static void toggleTheme() {
        darkMode = !darkMode;
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    // Getters dinâmicos que devolvem a cor certa conforme o tema ativo
    public static Color getBg() { return darkMode ? DARK_BG : LIGHT_BG; }
    public static Color getCard() { return darkMode ? DARK_CARD : LIGHT_CARD; }
    public static Color getText() { return darkMode ? DARK_TEXT : LIGHT_TEXT; }
    public static Color getAccent() { return darkMode ? DARK_ACCENT : LIGHT_ACCENT; }
}