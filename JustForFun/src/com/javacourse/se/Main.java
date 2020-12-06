package com.javacourse.se;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static char[][] map;
    static int size = 3;
    static int dots_to_win = 3;
    static final char DOT_EMPTY = '•';
    static final char DOT_X = 'X';
    static final char DOT_O = 'O';
    public static int xAi = -1;
    public static int yAi = -1;
    static Scanner sc = new Scanner(System.in);
    static Random rnd = new Random();

    public static void main(String[] args) {

        startGame();

    }

    private static void startGame() {
        initMap();
        drawMap();
        while (true) {
            humanTurn();
            drawMap();
            if (checkWin(DOT_X)) {
                System.out.println("Победил человеческий детёныш!");
                endGame();
            }
            if (!isMapFull()) {
                System.out.println("Ничья");
                endGame();
            }
            aiTurn();
            drawMap();
            if (checkWin(DOT_O)) {
                System.out.println("Победила машина!");
                endGame();
            }
        }

    }

    private static void endGame() {
        while (true) {
            System.out.println("Хочешь поиграть ещё? y/n");
//          sc.nextLine();
            String answer = sc.nextLine();
            if (answer.equalsIgnoreCase("y") | answer.equalsIgnoreCase("")) startGame();
            if (answer.equalsIgnoreCase("n")) System.exit(0);
        }
    }

    private static void initMap() {
        map = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    private static void drawMap() {
        System.out.print("*" + " ");
        for (int i = 0; i < size; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }

    }

    private static void humanTurn() {
        int x, y;
        System.out.println("Человек, введи координаты Х и У.");
        do {
            System.out.print("Координаты Х - ");
            x = (giveMeInt() - 1);
            System.out.print("Координаты Y - ");
            y = (giveMeInt() - 1);
        } while (!freeField(x, y));
        map[y][x] = DOT_X;
    }

    static int giveMeInt() {
        String input;
        while (true) {
            input = sc.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ай ай ай... Введи число корректно!");
            }
        }
    }

    private static boolean checkWin(char symb) {
        int countDots;
        // Проверяем горизонтали
        for (int i = 0; i < size; i++) {
            countDots = 0;
            for (int j = 0; j < size; j++) {
                if (map[i][j] == symb) {
                    countDots += 1;
                    if (countDots == dots_to_win) return true;
                }
            }
        }
        // Проверяем вертикали
        for (int i = 0; i < size; i++) {
            countDots = 0;
            for (int j = 0; j < size; j++) {
                if (map[j][i] == symb) {
                    countDots += 1;
                    if (countDots == dots_to_win) return true;
                }
            }

        }
        // Проверяем диагональ
        countDots = 0;
        for (int i = 0; i < size; i++) {
            if (map[i][i] == symb) {
                countDots += 1;
                if (countDots == dots_to_win) return true;
            }
        }
        // Проверяем обратную диагональ
        countDots = 0;
        for (int i = 0; i < size; i++) {
            if (map[size - 1 - i][i] == symb) {
                countDots += 1;
                if (countDots == dots_to_win) return true;
            }
        }
        return false;
    }

    private static void aiTurn() {
        int x, y;
        do {
            if (bestAiTurn(DOT_O)) {
                x = xAi;
                y = yAi;
            } else if (bestAiTurn(DOT_X)) {
                x = xAi;
                y = yAi;
            } else
                x = rnd.nextInt(size);
            y = rnd.nextInt(size);
        } while (!freeField(y, x));
        map[x][y] = DOT_O;
        System.out.println("Машина ходит в клетку " + (y + 1) + " " + (x + 1) + ".");
    }

    private static boolean freeField(int x, int y) {
        if (x < 0 || x > size - 1 || y < 0 || y > size - 1) return false;
        if (map[y][x] == DOT_EMPTY) return true;
        return false;
    }

    private static boolean isMapFull() {
        int freeDots = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == DOT_EMPTY) freeDots += 1;
                if (freeDots > 0) return true;
            }
        }
        return false;
    }

    private static boolean bestAiTurn(char symb) {
        // Если в методе ставим О или Х, то побеждаем? Возвращаем координаты Х и У, если да.
        // Проверка горизонтальных линий
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    map[i][j] = symb;
                    if (checkWin(symb)) {
                        map[i][j] = DOT_EMPTY;
                        xAi = i;
                        yAi = j;
                        return true;
                    } else map[i][j] = DOT_EMPTY;
                }
            }
        }
        // Проверка по вертикали
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[j][i] == DOT_EMPTY) {
                    map[j][i] = symb;
                    if (checkWin(symb)) {
                        map[i][j] = DOT_EMPTY;
                        xAi = j;
                        yAi = i;
                        return true;
                    } else map[j][i] = DOT_EMPTY;
                }
            }
        }
        // Проверка диагонали i = j
        for (int i = 0; i < size; i++) {
            if (map[i][i] == DOT_EMPTY) {
                map[i][i] = symb;
                if (checkWin(symb)) {
                    map[i][i] = DOT_EMPTY;
                    xAi = i;
                    yAi = i;
                    return true;
                } else map[i][i] = DOT_EMPTY;
            }
        }
        // Проверка обратной диагонали
        for (int i = 0; i < size; i++) {
            if (map[size - 1 - i][i] == DOT_EMPTY) {
                map[size - 1 - i][i] = symb;
                if (checkWin(symb)) {
                    map[size - 1 - i][i] = DOT_EMPTY;
                    xAi = i;
                    yAi = i;
                    return true;
                } else map[size - 1 - i][i] = DOT_EMPTY;
            }
        }
        return false;
    }

}
