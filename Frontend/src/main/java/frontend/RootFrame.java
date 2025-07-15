package frontend;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.sound.sampled.*;

public class RootFrame {
    private static final int WIDTH = 580;
    private static final int HEIGHT = 560;
    private static JFrame rootFrame = new JFrame();
    private static JPanel currentPanel = new JPanel();
    private static JPanel previousPanel = null; // Aggiunto un riferimento al pannello precedente
    private static Clip MenuTheme;

    public RootFrame() {
        rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootFrame.setSize(WIDTH, HEIGHT);
        rootFrame.setLayout(new BorderLayout());
        rootFrame.setResizable(false);
        startMenuTheme();
        new StartPanel(); // Non è chiaro cosa faccia questo costruttore senza dettagli aggiuntivi
        rootFrame.add(currentPanel, BorderLayout.CENTER); // Aggiungi currentPanel al centro
        rootFrame.setVisible(true);
    }

    public static void setPanel(JPanel newPanel){

        if (currentPanel != null) {
            rootFrame.remove(currentPanel); // Rimuovi il pannello corrente
        }
        previousPanel = currentPanel; // Salva il pannello corrente come pannello precedente
        currentPanel = newPanel; // Imposta il nuovo pannello corrente
        rootFrame.add(currentPanel, BorderLayout.CENTER); // Aggiungi il nuovo pannello al centro
        rootFrame.revalidate(); // Rivalida il layout
        rootFrame.repaint(); // Ridisegna il frame
    }

    public static void setPrecPannel(){
        if(previousPanel != null) {
            setPanel(previousPanel);
        }
        else {
            System.out.println("Nessun pannello precedente da ripristinare.");
        }
    }

    public static void startMenuTheme() {
        try {
            // Carica il file audio da una risorsa all'interno del tuo progetto
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    RootFrame.class.getResourceAsStream("/music/main_title.wav"));

            // Crea un clip per riprodurre il file audio
            MenuTheme = AudioSystem.getClip();

            // Verifica se il clip è stato correttamente inizializzato
            if (MenuTheme != null) {
                // Apre il clip utilizzando l'audioInputStream
                MenuTheme.open(audioInputStream);
                MenuTheme.loop(Clip.LOOP_CONTINUOUSLY);
                MenuTheme.start();
            } else {
                System.err.println("Clip non inizializzato correttamente");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            // Gestisci l'errore di avvio della musica qui
        } catch (IllegalArgumentException e) {
            System.err.println("Errore durante l'apertura del Clip: " + e.getMessage());
            // Gestisci l'errore specifico qui
        }
    }

    public static void stopMenuTheme() {
        if (MenuTheme != null && MenuTheme.isRunning()) {
            MenuTheme.stop();
            MenuTheme.close();
        }
    }
    public static void showLoadVideo(Runnable onVideoEnd){
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel(new BorderLayout());
            JFXPanel jfxPanel = new JFXPanel(); // Crea un JFXPanel
            panel.add(jfxPanel, BorderLayout.CENTER);

            // Avvia JavaFX
            Platform.runLater(() -> {
                URL resource = RootFrame.class.getResource("/video/Caricamento.mp4");
                if (resource == null) {
                    System.err.println("Il file video non è stato trovato.");
                    return;
                }

                String uri = resource.toString();

                BorderPane borderPane = new BorderPane();
                Media media = new Media(uri);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);

                // Imposta la dimensione del MediaView
                mediaView.setFitWidth(500); // Imposta la larghezza desiderata
                mediaView.setFitHeight(500); // Imposta l'altezza desiderata
                mediaView.setPreserveRatio(true); // Mantieni le proporzioni originali

                borderPane.setCenter(mediaView);
                Scene scene = new Scene(borderPane, 500, 500);
                jfxPanel.setScene(scene);
                // Avvia la riproduzione del video
                mediaPlayer.setOnEndOfMedia(() -> {
                    Platform.runLater(() -> {
                        onVideoEnd.run(); // Esegui l'azione passata quando il video finisce
                    });
                });

                mediaPlayer.play();
            });
            RootFrame.setPanel(panel);
        });
    }
}