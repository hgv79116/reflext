package org.example;

import org.example.testReflection.TreeNode2;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws InterruptedException,IOException {
        // Press Opt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        try(
                GameApplication gameApplication = new GameApplication()
        ) {
            gameApplication.start();
        } catch (InterruptedException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
}