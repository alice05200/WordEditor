package wordeditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author User
 */
public class WordEditor extends JFrame{
    private JMenuItem menuNew, menuOpen, menuSave, menuSaveAs, menuClose;
    private JMenu editMenu;
    private JMenuItem menuCut, menuCopy, menuPaste, menuSelectAll;
    private JMenuItem menuAbout;
    private JTextArea textArea;
    private JPopupMenu popUpMenu;
    private JLabel stateBar;
    private JFileChooser fileChooser;
    
    public WordEditor(){
        super("新增文字檔案");
        setUpUIComponent();
        setUpEventListener();
        setVisible(true);
    }
    private void setUpUIComponent(){
        setSize(640, 480);
        //選單列
        JMenuBar menuBar = new JMenuBar();
        //設置檔案選單
        JMenu fileMenu = new JMenu("檔案");
        menuNew = new JMenuItem("開啟新檔");
        menuOpen = new JMenuItem("開啟舊檔");
        menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));//快捷鍵設置
        menuSave = new JMenuItem("儲存檔案");
        menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuSaveAs = new JMenuItem("另存新檔");
        menuClose = new JMenuItem("關閉");
        menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        fileMenu.add(menuNew);
        fileMenu.add(menuOpen);
        fileMenu.add(menuSave);
        fileMenu.add(menuSaveAs);
        fileMenu.addSeparator();//分隔線
        fileMenu.add(menuClose);
        //設置編輯選單
        editMenu = new JMenu("編輯");
        menuCut = new JMenuItem("剪下");
        menuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));//快捷鍵設置
        menuCopy = new JMenuItem("複製");
        menuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        menuPaste = new JMenuItem("貼上");
        menuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        menuSelectAll = new JMenuItem("全選");
        menuSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        editMenu.add(menuCut);
        editMenu.add(menuCopy);
        editMenu.add(menuPaste);
        editMenu.add(menuSelectAll);
        //設置關於選單
        JMenu aboutMenu = new JMenu("關於");
        menuAbout = new JMenuItem("關於WordEditor");
        aboutMenu.add(menuAbout);
        //加到功能列
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        //文字編輯區域
        textArea = new JTextArea();
        textArea.setFont(new Font("細明體", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        JScrollPane panel = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Container contentPane = getContentPane();
        contentPane.add(panel, BorderLayout.CENTER);
        //狀態列
        stateBar = new JLabel("未儲存");
        stateBar.setHorizontalAlignment(SwingConstants.LEFT);
        stateBar.setBorder(BorderFactory.createEtchedBorder());
        contentPane.add(stateBar, BorderLayout.SOUTH);
        popUpMenu = editMenu.getPopupMenu();
        fileChooser = new JFileChooser();
    }
    private void setUpEventListener(){
        //按下視窗關閉鈕事件處理
        addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) { 
                    closeFile();
                }
            }
        );
        //開啟新檔
        menuNew.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    openNewFile();
                }
            }
        );
        //開啟舊檔
        menuOpen.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openFile();
                }
            }
        );
        //儲存檔案
        menuSave.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveFile();
                }
            }
        );
        //另存新檔
        menuSaveAs.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveFileAs();
                }
            }
        );
        //關閉檔案
        menuClose.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeFile();
                }
            }
        );
        //剪下
        menuCut.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cut();
                }
            }
        );
        //複製
        menuCopy.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    copy();
                }
            }
        );
        //貼上
        menuPaste.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    paste();
                }
            }
        );
        //全選
        menuSelectAll.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    selectAll();
                }
        });
        //關於
        menuAbout.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // 顯示對話方塊
                    JOptionPane.showOptionDialog(null,
                        "程式名稱:\n    WordEditor \n" +
                        "簡介:\n    一個簡單的文字編輯器\n" +
                        "來源：http://caterpillar.onlyfun.net/",
                        "關於WordEditor",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, null, null);
                }
            }
        );
        //編輯區鍵盤事件
        textArea.addKeyListener(
            new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    if(!e.isControlDown())
                        processTextArea();
                }
            }
        );
        //編輯區滑鼠事件
        textArea.addMouseListener(
            new MouseAdapter() {
                public void mouseReleased(MouseEvent e){
                    if(e.getButton() == MouseEvent.BUTTON3)
                        popUpMenu.show(editMenu, e.getX(), e.getY());
                }
                public void mouseClicked(MouseEvent e){
                    if(e.getButton() == MouseEvent.BUTTON1)
                        popUpMenu.setVisible(false);
                }
            }
        );
    }
    private void openNewFile(){
        if(!isCurrentFileSaved()){
            int option = JOptionPane.showConfirmDialog(null, "檔案已修改，是否儲存？",
                    "儲存檔案？", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
            if(option == JOptionPane.YES_OPTION)
                saveFile();//儲存檔案
        }
        textArea.setText("");
        stateBar.setText("未儲存");
    }
    private void openFile(){
        if(!isCurrentFileSaved()){
            //顯示對話方塊
            int option = JOptionPane.showConfirmDialog(null, "檔案已修改，是否儲存？",
                    "儲存檔案？", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
            if(option == JOptionPane.YES_OPTION)
                saveFile();//儲存檔案
        }
        open();
    }
    private void saveFile(){
        //從標題列取得檔案名稱
        File file = new File(getTitle());
        //若指定的檔案不存在
        if(!file.exists())
            saveFileAs();//執行另存新檔
        else{
            try{
                //開啟指定的檔案
                BufferedWriter buf = new BufferedWriter(new FileWriter(file));
                //將文字編輯區的文字寫入檔案
                buf.write(textArea.getText());
                buf.close();
                stateBar.setText("已儲存");
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, e.toString(), "寫入檔案失敗", JOptionPane.ERROR_MESSAGE);
            }
        }
    }    
    private void saveFileAs(){
        //顯示檔案對話方塊
        int option = fileChooser.showDialog(null, null);
        //如果確認選取檔案
        if(option == JFileChooser.APPROVE_OPTION){
            //取得選擇的檔案
            File file = fileChooser.getSelectedFile();
            //在標題列上設定檔案名稱
            setTitle(file.toString());
            try{
                //建立檔案
                file.createNewFile();
                //進行檔案儲存
                saveFile();
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, e.toString(), "無法建立新檔", JOptionPane.ERROR_MESSAGE);
            }
        }
    }    
    private void closeFile(){
        //是否已儲存文件
        if(isCurrentFileSaved())
            dispose();//釋放視窗資源，而後關閉程式
        else{
            int option = JOptionPane.showConfirmDialog(
                    null, "檔案已修改，是否儲存？",
                    "儲存檔案？", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE, null);
            switch(option) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                    break;
                case JOptionPane.NO_OPTION:
                   dispose();
            }
        }
    }
    private void cut() {
        textArea.cut();
        stateBar.setText("未儲存");
        popUpMenu.setVisible(false);
    }
    private void copy() {
        textArea.copy();
        popUpMenu.setVisible(false);    
    }
    private void paste() {
        textArea.paste();
        stateBar.setText("未儲存");
        popUpMenu.setVisible(false);
    }
    private void selectAll(){
        textArea.selectAll();
        popUpMenu.setVisible(false);
    }
    private void processTextArea() {
        stateBar.setText("未儲存");
        popUpMenu.setVisible(false);
    }
    private void open(){
        //顯示檔案選取的對話方塊
        int option = fileChooser.showDialog(null, null);
        // 使用者按下確認鍵
        if(option == JFileChooser.APPROVE_OPTION) {
            try {
                //開啟選取的檔案
                BufferedReader buf = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                //設定文件標題
                setTitle(fileChooser.getSelectedFile().toString());
                //清除前一次文件
                textArea.setText("");
                //設定狀態列
                stateBar.setText("未儲存");
                //取得系統相依的換行字元
                String lineSeparator = System.getProperty("line.separator");
                //讀取檔案並附加至文字編輯區
                String text;
                while((text = buf.readLine()) != null){
                    textArea.append(text);
                    textArea.append(lineSeparator);
                }
                buf.close();
            }   
            catch(IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(), "開啟檔案失敗", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean isCurrentFileSaved(){
        if(stateBar.getText().equals("未儲存"))//狀態列判斷檔案狀態
            return false;
        else
            return true;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new WordEditor();
    }
}
