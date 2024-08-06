package Quizlet_version0_9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class QuizPlayer {
    private JTextArea display;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton showAnswerButton;
    private JButton correctButton;
    private JButton incorrectButton;
    private boolean isShowingAnswer;

    public static void main(String[] args) {
        QuizPlayer player = new QuizPlayer();
        player.go();
    }

    public void go() {
        frame = new JFrame("Quiz Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(10, 20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        display.setEditable(false);
        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(qScroller);

        showAnswerButton = new JButton("Show Answer");
        showAnswerButton.addActionListener(new ShowAnswerListener());
        mainPanel.add(showAnswerButton);

        correctButton = new JButton("Correct");
        correctButton.addActionListener(new CorrectAnswerListener());
        mainPanel.add(correctButton);
        correctButton.setEnabled(false);

        incorrectButton = new JButton("Incorrect");
        incorrectButton.addActionListener(new IncorrectAnswerListener());
        mainPanel.add(incorrectButton);
        incorrectButton.setEnabled(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load Card Set");
        loadMenuItem.addActionListener(new LoadMenuListener());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setVisible(true);
    }

    private void loadFile(File file) {
        cardList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                makeCard(line);
            }
        } catch (IOException ex) {
            System.out.println("Couldn't read the card file");
            ex.printStackTrace();
        }
        showNextCard();
    }

    private void makeCard(String lineToParse) {
        String[] result = lineToParse.split("/");
        if (result.length >= 2) {
            QuizCard card = new QuizCard(result[0], result[1]);
            cardList.add(card);
        }
    }

    private void showNextCard() {
        if (cardList.size() > 0) {
            currentCard = cardList.get(0);
            currentCardIndex = 0;
            display.setText(currentCard.getQuestion());
            showAnswerButton.setEnabled(true);
            correctButton.setEnabled(false);
            incorrectButton.setEnabled(false);
            isShowingAnswer = false;
        } else {
            display.setText("No more cards!");
            showAnswerButton.setEnabled(false);
            correctButton.setEnabled(false);
            incorrectButton.setEnabled(false);
        }
    }

    private class ShowAnswerListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            display.setText(currentCard.getAnswer());
            showAnswerButton.setEnabled(false);
            correctButton.setEnabled(true);
            incorrectButton.setEnabled(true);
            isShowingAnswer = true;
        }
    }

    private class CorrectAnswerListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            cardList.remove(currentCardIndex);
            showNextCard();
        }
    }

    private class IncorrectAnswerListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            cardList.remove(currentCardIndex);
            cardList.add(currentCard);
            showNextCard();
        }
    }

    private class LoadMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }
}
