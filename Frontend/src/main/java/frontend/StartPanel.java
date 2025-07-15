package frontend;

import frontend.endpointApi.EndpoinApi;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

// Classe principale che gestisce il pannello di avvio dell'applicazione
public class StartPanel {
    private static final String IMG_PATH = "/images/start.jpg"; // percorso dell'immagine iniziale di avvio

    /**
     * Costruttore della classe StartPanel.
     * Crea e inizializza il pannello di avvio con un'immagine di sfondo e bottoni per Nuova Partita e Carica Partita.
     */
    public StartPanel() {
        JPanel jPanel = new JPanel();  // Creazione del pannello principale
        try{
            ImagePanel imagePanel = loadImage(); // Caricamento dell'immagine di sfondo
            jPanel.add(imagePanel); // Aggiunta del pannello dell'immagine al pannello principale

            // Creazione dei bottoni per Nuova Partita e Carica Partita
            JButton newGame = new JButton("Nuova Partita");
            JButton loadGame = new JButton("Carica Partita");

            // Aggiunta degli ActionListener per gestire i click sui bottoni
            newGame.addActionListener(e -> startActionPerformed("Nuova Partita"));
            loadGame.addActionListener(e -> startActionPerformed("Carica Partita"));

            // Fissaggio dei bottoni fatto orizzontalmente
            newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
            loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Creazione del pannello per contenere i bottoni con layout verticale
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Layout verticale per i bottoni
            buttonPanel.setOpaque(false); // Trasparenza del pannello

            // Aggiungi più spazio flessibile sopra i bottoni per spostarli più in basso
            buttonPanel.add(Box.createVerticalGlue()); // Spazio flessibile sopra i bottoni
            buttonPanel.add(Box.createVerticalStrut(200)); // Spazio fisso sopra i bottoni
            buttonPanel.add(newGame); // Aggiunta il bottone "Nuova Partita"
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio fisso tra i bottoni
            buttonPanel.add(loadGame); // Aggiunta del bottone "Carica Partita"
            buttonPanel.add(Box.createVerticalGlue()); // Spazio flessibile sotto i bottoni

            // Aggiunta del pannello dei bottoni al pannello dell'immagine di sfondo
            imagePanel.add(buttonPanel);

        } // fine del blocco try
        catch(Exception e) { // processa l'eccezione
            e.printStackTrace(); // stampa lo stack trace dell'eccezione
            JOptionPane.showMessageDialog(null, "Si è verificato un errore: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } // fine del blocco catch
        RootFrame.setPanel(jPanel); // Imposta il pannello principale nel RootFrame
    } // fine del costruttore StartPanel

    /**
     * Gestisce le azioni dei bottoni "Nuova Partita" e "Carica Partita".
     * @param buttonClicked Il testo del bottone cliccato ("Nuova Partita" o "Carica Partita").
     */
    public void startActionPerformed(String buttonClicked) {
        if(buttonClicked.equals("Nuova Partita")) { // se l'utente sceglie di iniziare una nuova partita
            // Avvio di una nuova partita utilizzando un endpoint API
            RootFrame.showLoadVideo(() -> new StartNewGame(EndpoinApi.startNewGame()));
        } // fine di if
        else if(buttonClicked.equals(("Carica Partita"))) { // se l'utente sceglie di caricare una partita salvata
            // Caricamento delle partite salvate
            new PannelMetchSaved(0, false);
        } // fine di else if
    } // fine del metodo startActionPerformed

    /**
     * Carica l'immagine di sfondo dal percorso IMG_PATH.
     * @return Un oggetto ImagePanel contenente l'immagine di sfondo caricata.
     * @throws Exception Se l'immagine non può essere trovata o caricata correttamente.
     */
    public ImagePanel loadImage() throws Exception { // metodo che analizza l'imagePath passato in input
        URL imgURL = StartPanel.class.getResource(IMG_PATH); // creo un oggetto di tipo URL contenente l'imagePath
        ImagePanel imagePanel;

        if (imgURL == null) { // Se l'immagine non viene trovata
            throw new Exception("Impossibile trovare il file" + IMG_PATH); // solleva un'eccezione
        } // fine di if
        else{ // altrimenti
            // Creazione del pannello per l'immagine trovata
            imagePanel = new ImagePanel(imgURL);
            System.out.println("Immagine caricata con successo!\n" + imgURL);
            imagePanel.setOpaque(false); // Impostazione della trasparenza del pannello
            imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS)); // Layout verticale


            imagePanel.setAlignmentX(0.5f);
            imagePanel.setAlignmentY(0.5f);
        } // fine di else

        return imagePanel; // restituisce il pannello dell'immagine
    } // fine del metodo loadImage

} // fine della classe StartPanel