import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * Created by nick on 21.01.17.
 */
public class Gui implements KeyListener{
    JFrame frame;
    JMenuBar menuBar;
    JMenu menu;
    JMenu helpMenu;
    JPanel panel;
    JPanel panelRussian;
    JPanel panelChar;
    JPanel panelPinyin;
    JScrollPane panelExample;
    JLabel labelRussian;
    JLabel labelChar;
    JLabel labelPinyin;
    JLabel labelExample;
    JTextArea textRussian;
    JLabel textChar;
    JLabel textPinyin;
    JTextArea textExample;
    JButton checkButton;
    JButton nextButton;
    JPanel buttonPanel;
    String fileName;
    ArrayList<Card> cards = new ArrayList<Card>();
    Random randomNumber = new Random();
    Card currentCard;
    int currentNumb;

    public static void main(String[] args) {
        Gui gui = new Gui();
        String cardFileName=null;
        ArrayList<Card> cardSet=null;

        try {
            BufferedReader inputReader = new BufferedReader(new FileReader("LastCards.car"));
            cardFileName = inputReader.readLine();
            inputReader.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        if(cardFileName!=null) {
            String ext = cardFileName.substring(cardFileName.length() - 3, cardFileName.length());
            if (ext.equals("car")) {
                try{
                    ObjectInputStream obin = new ObjectInputStream(new FileInputStream(cardFileName));
                    cardSet = (ArrayList<Card>) obin.readObject();
                    obin.close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            } else if (ext.equals("xls")) {
                try{
                    Workbook book = new HSSFWorkbook(new FileInputStream(cardFileName));
                    Sheet sheet = book.getSheetAt(0);
                    cardSet = new ArrayList<Card>();
                    Iterator<Row> iterator = sheet.rowIterator();
                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        if(row.getRowNum()>1) {
                            Card card = new Card();
                            card.setCharacters(row.getCell(0).getStringCellValue());
                            card.setPinyin(row.getCell(1).getStringCellValue());
                            card.setRussian(row.getCell(2).getStringCellValue());
                            card.setExamples(row.getCell(3).getStringCellValue());
                            cardSet.add(card);
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        if(cardSet==null) {
            try {
                Workbook book = new HSSFWorkbook(new FileInputStream("HSK_Level_2.xls"));
                Sheet sheet = book.getSheetAt(0);
                cardSet = new ArrayList<Card>();
                Iterator<Row> iterator = sheet.rowIterator();
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    if(row.getRowNum()>1) {
                        Card card = new Card();
                        card.setCharacters(row.getCell(0).getStringCellValue());
                        card.setPinyin(row.getCell(1).getStringCellValue());
                        card.setRussian(row.getCell(2).getStringCellValue());
                        card.setExamples(row.getCell(3).getStringCellValue());
                        cardSet.add(card);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        gui.setCards(cardSet);
        gui.setFileName(cardFileName);
        gui.go();
    }

    public void setCards(ArrayList<Card> cards1){
        cards=cards1;
    }

    public void setFileName(String fileName1){fileName=fileName1;}

    public String getFileName(){return fileName;}

    public void addCard(Card card){
        cards.add(card);
    }

    public void go(){
        frame=new JFrame("Chinese cards");
        panel=new JPanel();

        labelRussian = new JLabel("RUSSIAN");
        labelRussian.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelRussian= new JPanel();
        panelRussian.setPreferredSize(new Dimension(0, 100));
        panelRussian.setBackground(Color.green);
        panelRussian.setLayout(new GridBagLayout());
        textRussian = new JTextArea();
        textRussian.addKeyListener(this);
        textRussian.setBackground(Color.green);
        textRussian.setEditable(false);
        textRussian.setFont(new Font("arial", Font.BOLD, 18));
        panelRussian.add(textRussian);

        labelChar = new JLabel("CHARACTERS");
        labelChar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelChar= new JPanel();
        panelChar.setPreferredSize(new Dimension(0, 100));
        panelChar.setBackground(Color.yellow);
        panelChar.setLayout(new GridBagLayout());
        textChar = new JLabel();
        textChar.setFont(new Font("serif", Font.BOLD, 40));
        panelChar.add(textChar);

        labelPinyin = new JLabel("PINYIN");
        labelPinyin.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPinyin= new JPanel();
        panelPinyin.setPreferredSize(new Dimension(0, 100));
        panelPinyin.setBackground(Color.lightGray);
        panelPinyin.setLayout(new GridBagLayout());
        textPinyin = new JLabel();
        textPinyin.setFont(new Font("arial", Font.BOLD, 18));
        panelPinyin.add(textPinyin);

        labelExample = new JLabel("EXAMPLES");
        labelExample.setAlignmentX(Component.CENTER_ALIGNMENT);
        textExample = new JTextArea();
        textExample.addKeyListener(this);
        textExample.setEditable(false);
        textExample.setFont(new Font("arial", Font.BOLD, 18));
        panelExample= new JScrollPane(textExample);
        panelExample.setPreferredSize(new Dimension(0, 100));
        panelExample.setBackground(Color.white);

        checkButton = new JButton("Check");
        nextButton = new JButton("Next");

        checkButton.addActionListener(new CheckListener());
        nextButton.addActionListener(new NextListener());
        checkButton.addKeyListener(this);
        nextButton.addKeyListener(this);

        buttonPanel = new JPanel();
        buttonPanel.add(checkButton);
        buttonPanel.add(nextButton);


        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(labelRussian);
        panel.add(Box.createRigidArea(new Dimension(0,2)));
        panel.add(panelRussian);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(labelChar);
        panel.add(Box.createRigidArea(new Dimension(0,2)));
        panel.add(panelChar);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(labelPinyin);
        panel.add(Box.createRigidArea(new Dimension(0,2)));
        panel.add(panelPinyin);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(labelExample);
        panel.add(Box.createRigidArea(new Dimension(0,2)));
        panel.add(panelExample);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(buttonPanel);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        helpMenu = new JMenu("Help");

        JMenuItem menuItemOpen = new JMenuItem("Open");
        menuItemOpen.addActionListener(new MenuOpenListener());
        JMenuItem menuItemClose = new JMenuItem("Close");
        JMenuItem menuItemNew = new JMenuItem("New");
        menu.add(menuItemOpen);
        menu.add(menuItemClose);
        menu.add(menuItemNew);
        menuBar.add(menu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) dim.getWidth()/2-frame.getWidth()/2, (int) dim.getHeight()/2-frame.getHeight()/2);
        frame.setVisible(true);

        if(cards!=null){
            currentNumb=randomNumber.nextInt(cards.size());
            currentCard=cards.get(currentNumb);
            textRussian.setText(currentCard.getRussian());
        }
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()== KeyEvent.VK_ENTER){
            if(textChar.getText()==""){
                if(currentCard!=null){
                    textChar.setText(currentCard.getCharacters());
                    textPinyin.setText(currentCard.getPinyin());
                    textExample.setText(currentCard.getExamples());
                }
            }else{
                if((cards!=null)&(cards.size()>1)){
                    Card newCard=currentCard;
                    while(newCard==currentCard){
                        newCard=cards.get(randomNumber.nextInt(cards.size()));
                    }
                    currentCard=newCard;
                    textRussian.setText(currentCard.getRussian());
                    textChar.setText("");
                    textPinyin.setText("");
                    textExample.setText("");
                }
            }
        }
    }

    public void keyReleased(KeyEvent e){

    }
    public void keyTyped(KeyEvent e){

    }


    class MenuOpenListener implements ActionListener{
        public void actionPerformed(ActionEvent ev) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    private void loadFile(File file){
        try{
            Workbook book = new HSSFWorkbook(new FileInputStream(file));
            Sheet sheet = book.getSheetAt(0);
            cards = new ArrayList<Card>();
            Iterator<Row> iterator = sheet.rowIterator();
            while(iterator.hasNext()){
                Row row = iterator.next();
                if(row.getRowNum()>1) {
                    Card card = new Card();
                    card.setCharacters(row.getCell(0).getStringCellValue());
                    card.setPinyin(row.getCell(1).getStringCellValue());
                    card.setRussian(row.getCell(2).getStringCellValue());
                    card.setExamples(row.getCell(3).getStringCellValue());
                    cards.add(card);
                }
            }

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("LastCards.car"));
            writer.write(file.getAbsolutePath());
            writer.close();
            this.setFileName(file.getAbsolutePath());
            currentNumb=randomNumber.nextInt(cards.size());
            currentCard=cards.get(currentNumb);
            textRussian.setText(currentCard.getRussian());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    class CheckListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            if(currentCard!=null){
                textChar.setText(currentCard.getCharacters());
                textPinyin.setText(currentCard.getPinyin());
                textExample.setText(currentCard.getExamples());
            }
        }
    }

    class NextListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            if((cards!=null)&&(cards.size()>1)){
                Card newCard=currentCard;
                while(newCard==currentCard){
                    newCard=cards.get(randomNumber.nextInt(cards.size()));
                }
                currentCard=newCard;
                textRussian.setText(currentCard.getRussian());
                textChar.setText("");
                textPinyin.setText("");
                textExample.setText("");
            }
        }
    }

}
