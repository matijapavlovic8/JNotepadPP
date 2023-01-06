package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableJToolbar;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.impl.DefaultMultipleDocumentModel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * {@code JNotepadPP} is a Swing based text editor that provides users with posibility of loading, reading, editing and saving
 * text files. It supports working on multiple documents simultaneously.
 *
 * @author MatijaPav
 */
public class JNotepadPP extends JFrame {

    /**
     * Text editor model.
     */
    private final DefaultMultipleDocumentModel mdmodel;

    /**
     * Localization provider.
     */
    private final FormLocalizationProvider provider;

    /**
     * Create a blank document action.
     */
    private LocalizableAction createBlankDocument;

    /**
     * Open document action.
     */
    private LocalizableAction openDocument;

    /**
     * Save document action.
     */
    private LocalizableAction saveDocument;

    /**
     * Save as action.
     */
    private LocalizableAction saveAs;

    /**
     * Tab closing action.
     */
    private LocalizableAction closeTab;

    /**
     * Copy action.
     */
    private LocalizableAction copy;

    /**
     * Paste action.
     */
    private LocalizableAction paste;

    /**
     * Cut action.
     */
    private LocalizableAction cut;

    /**
     * Showing statistics.
     */
    private LocalizableAction statistical;

    /**
     * Exiting application.
     */
    private LocalizableAction exit;

    /**
     * Switching localization to english language.
     */
    private LocalizableAction eng;

    /**
     * Switching localization to croatian language.
     */
    private LocalizableAction hr;

    /**
     * Switching localization to german language.
     */
    private LocalizableAction ger;

    /**
     * Switching localization to serbian language.
     */
    private LocalizableAction srb;

    /**
     * Action for converting selected text to upper case.
     */
    private LocalizableAction upperCase;

    /**
     * Action for converting selected text to lower case.
     */
    private LocalizableAction lowerCase;

    /**
     * Invert case.
     */
    private LocalizableAction invertCase;

    /**
     * Sort in ascending order.
     */
    private LocalizableAction ascending;

    /**
     * Sort in descending order.
     */
    private LocalizableAction descending;

    /**
     * Removing duplicate lines.
     */
    private LocalizableAction unique;

    /**
     * {@link JLabel} containing length of file.
     */
    private final JLabel length;

    /**
     * {@link JLabel} containing current date and time.
     */
    private final JLabel dateTime;

    /**
     * {@link JLabel} containing file info.
     */
    private final JLabel info;

    /**
     * List of actions that modify the document.
     */
    private final List<LocalizableAction> modificationActions;

    /**
     * List of actions.
     */
    private final List<LocalizableAction> documentActions;

    /**
     * Creates a new {@link JNotepadPP} instance.
     */
    public JNotepadPP(){
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("JNotepad++");
        setLocation(0, 0);
        setSize(1080, 760);
        addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             */
            @Override
            public void windowClosing(WindowEvent e) {
                exit.actionPerformed(null);
            }
        });
        this.provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
        this.mdmodel = new DefaultMultipleDocumentModel();
        this.length = new JLabel();
        this.info = new JLabel();
        this.dateTime = new JLabel();
        this.documentActions = new ArrayList<>();
        this.modificationActions = new ArrayList<>();
        documentListenerSetUp();
        initActions();
        initGUI();
    }

    /**
     * Document listener setup.
     */
    private void documentListenerSetUp(){
        SingleDocumentListener sl = new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                saveDocument.setEnabled(model.isModified());
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                String title = model.getFilePath() == null ? "untitled" : model.getFilePath().getFileName().toString();
                String tooltip = model.getFilePath().toString();
                mdmodel.setTitleAt(mdmodel.getSelectedIndex(), title);
                mdmodel.setToolTipTextAt(mdmodel.getSelectedIndex(), tooltip);

            }
        };

        MultipleDocumentListener ml = new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if(previousModel == null && currentModel == null)
                    throw new NullPointerException("Only one argument at a time can be null!");
                if(currentModel != null)
                    currentModel.addSingleDocumentListener(sl);
                else
                    previousModel.removeSingleDocumentListener(sl);

                if(currentModel != null){
                    Caret caret = currentModel.getTextComponent().getCaret();
                    saveDocument.setEnabled(currentModel.isModified());
                    updateInfoAndLen(currentModel);
                    documentActions.forEach(a -> a.setEnabled(true));
                    modificationActions.forEach(a -> a.setEnabled(caret.getDot() != caret.getMark()));
                } else {
                    length.setText("");
                    info.setText("");
                    documentActions.forEach(a -> a.setEnabled(false));
                    modificationActions.forEach(a -> a.setEnabled(false));
                }
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                Caret caret = model.getTextComponent().getCaret();
                caret.addChangeListener(l -> {
                    modificationActions.forEach(m -> m.setEnabled(caret.getDot() != caret.getMark()));
                    updateInfoAndLen(model);
                });
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        };
        mdmodel.addMultipleDocumentListener(ml);
    }

    private void updateInfoAndLen(SingleDocumentModel currentModel) {
        JTextArea comp = currentModel.getTextComponent();

        String len = this.provider.getString("length");
        this.length.setText(len + " " + comp.getDocument().getLength());

        int pos = currentModel.getTextComponent().getCaretPosition();
        Element root = currentModel.getTextComponent().getDocument().getDefaultRootElement();
        int line = root.getElementIndex(pos) + 1;
        int col = pos - root.getElement(line - 1).getStartOffset() + 1;
        int selected = comp.getSelectionEnd() - comp.getSelectionStart();

        this.info.setText(this.provider.getString("info")
            + " " + line + " " + col + " " + selected);
    }

    /**
     * Initializes GUI
     */
    private void initGUI(){
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(this.mdmodel, BorderLayout.CENTER);
        setDateTime();
        createMenus();
        createToolbars();
        createStatBar();
    }

    /**
     * Initializes all actions.
     */
    private void initActions(){
        this.createBlankDocument = new LocalizableAction("createDocument", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                mdmodel.createNewDocument();
            }
        };
        mapAction(createBlankDocument, "control N", KeyEvent.VK_N);
        this.openDocument = new LocalizableAction("openDocument", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle(provider.getString("openDocument"));
                if(jfc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
                    return;
                File file = jfc.getSelectedFile();
                Path path = file.toPath();

                if(Files.notExists(path))
                    JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("fileNonExistent"), "Error", JOptionPane.ERROR_MESSAGE);
                if(!Files.isReadable(path))
                    JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("notReadable"), "Error", JOptionPane.ERROR_MESSAGE);

                try{
                    mdmodel.loadDocument(path);
                } catch (RuntimeException exc){
                    System.out.println(exc.getMessage());
                    JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("loadingError"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        mapAction(openDocument, "control O", KeyEvent.VK_O);
        this.saveDocument = new LocalizableAction("saveDocument", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleDocumentModel curr = mdmodel.getCurrentDocument();
                Path currPath = curr.getFilePath();
                if(currPath != null){
                    try {
                        mdmodel.saveDocument(curr, currPath);
                        JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("saved"));
                    } catch (RuntimeException e1){
                        System.out.println(e1.getMessage());
                        JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("saveErr"),provider.getString("error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    saveAs.actionPerformed(e);
                }
            }
        };
        this.documentActions.add(saveDocument);
        mapAction(saveDocument, "control S", KeyEvent.VK_S);

        this.saveAs = new LocalizableAction("saveAs", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle(provider.getString("saveDocument"));
                if(jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION){
                    JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("notSaved"), provider.getString("error"), JOptionPane.WARNING_MESSAGE);
                } else {
                    Path path = Path.of(jfc.getSelectedFile().getPath());
                    if(Files.exists(path)){
                        int opt = JOptionPane
                            .showConfirmDialog(JNotepadPP.this, provider.getString("overwrite"),
                                provider.getString("saveDocument"),
                                JOptionPane.YES_NO_OPTION);
                        if(opt == JOptionPane.YES_OPTION){
                            mdmodel.getCurrentDocument().setFilePath(path);
                            saveDocument.actionPerformed(e);
                            JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("saved"));
                        } else {
                            JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("notSaved"),provider.getString("notSaved"), JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        mdmodel.getCurrentDocument().setFilePath(path);
                        mdmodel.saveDocument(mdmodel.getCurrentDocument(), mdmodel.getCurrentDocument().getFilePath());
                    }
                }
            }
        };
        this.documentActions.add(saveAs);
        mapAction(saveAs, "control shift S", KeyEvent.VK_A);

        this.closeTab = new LocalizableAction("closeTab", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleDocumentModel curr = mdmodel.getCurrentDocument();
                if(curr.isModified()){
                    int opt = JOptionPane.showConfirmDialog(JNotepadPP.this,
                        provider.getString("closeModified"),
                        provider.getString("warning"),
                        JOptionPane.YES_NO_OPTION);
                    if(opt == JOptionPane.NO_OPTION)
                        return;
                    mdmodel.closeDocument(curr);
                } else {
                    mdmodel.closeDocument(curr);
                }
            }
        };

        this.exit = new LocalizableAction("exit", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleDocumentModel curr = mdmodel.getCurrentDocument();
                //while(curr != null){
                    //closeTab.actionPerformed(e);
                    //curr = mdmodel.getCurrentDocument();
                //}
                dispose();
            }
        };

        this.cut = new LocalizableAction("cut", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action cut = new DefaultEditorKit.CutAction();
                cut.actionPerformed(e);
            }
        };
        this.modificationActions.add(cut);
        mapAction(cut, "control X", KeyEvent.VK_X);

        this.copy = new LocalizableAction("copy", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action copy = new DefaultEditorKit.CopyAction();
                copy.actionPerformed(e);
            }
        };
        this.modificationActions.add(copy);
        mapAction(copy, "control C", KeyEvent.VK_C);


        this.paste = new LocalizableAction("paste", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action paste = new DefaultEditorKit.PasteAction();
                paste.actionPerformed(e);
            }
        };
        this.modificationActions.add(paste);
        mapAction(paste, "control V", KeyEvent.VK_V);

        this.statistical = new LocalizableAction("stat", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleDocumentModel doc = mdmodel.getCurrentDocument();
                String text = doc.getTextComponent().getText();
                StringBuilder sb = new StringBuilder();
                sb.append(provider.getString("charCount"))
                    .append(" ").append(text.length()).append(" ")
                    .append(provider.getString("nonBlank"))
                    .append(" ").append(text.replaceAll("\\s+", "").length())
                    .append(" ").append(provider.getString("lineNum")).append(" ")
                    .append(doc.getTextComponent().getLineCount());
                JOptionPane.showMessageDialog(JNotepadPP.this, sb.toString(), provider.getString("statTitle"), JOptionPane.INFORMATION_MESSAGE);
            }
        };

        this.upperCase = new LocalizableAction("upperCase", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyText("upper");
            }
        };
        this.modificationActions.add(upperCase);

        this.lowerCase = new LocalizableAction("lowerCase", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyText("lower");
            }
        };
        this.modificationActions.add(lowerCase);

        this.invertCase = new LocalizableAction("invertCase", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyText("invert");
            }
        };
        this.modificationActions.add(invertCase);

        this.eng = new LocalizableAction("en", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("en");
            }
        };

        this.hr = new LocalizableAction("hr", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("hr");
            }
        };

        this.srb = new LocalizableAction("srb", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("sr");
            }
        };

        this.ger = new LocalizableAction("ger", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("de");
            }
        };

        this.ascending = new LocalizableAction("asc", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyText("asc");
            }
        };
        this.modificationActions.add(ascending);

        this.descending = new LocalizableAction("desc", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyText("desc");
            }
        };
        this.modificationActions.add(descending);

        this.unique = new LocalizableAction("unique", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyText("unique");
            }
        };
        this.modificationActions.add(unique);

    }

    /**
     * Maps actions to shortcut keys.
     */
    private void mapAction(LocalizableAction action, String accKey, int mnKey){
        action.putValue(Action.MNEMONIC_KEY, mnKey);
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accKey));
    }

    /**
     * Changes case of selected part of document.
     * @param newCase case to which the text will be changed.
     */
    private void modifyText(String newCase){
        SingleDocumentModel curr = mdmodel.getCurrentDocument();
        JTextArea comp = curr.getTextComponent();
        Document doc = curr.getTextComponent().getDocument();
        Caret caret = comp.getCaret();
        int dot = caret.getDot();
        int mark = caret.getMark();

        int len = Math.abs(dot - mark);
        int offset = len != 0 ? Math.min(dot, mark) : 0;

        try {
            String s = doc.getText(offset, len);
            switch (newCase) {
                case "upper" -> s = s.toUpperCase();
                case "lower" -> s = s.toLowerCase();
                case "invert" -> s = invert(s);
                case "asc" -> {
                    Collator collator = Collator.getInstance(new Locale(provider.getCurrentLanguage()));
                    String[] splits = s.split("\\r?\\n");
                    Arrays.sort(splits, collator);
                    s = String.join("\n", splits);
                }
                case "desc" -> {
                    Collator collator = Collator.getInstance(new Locale(provider.getCurrentLanguage()));
                    String[] splits = s.split("\\r?\\n");
                    Arrays.sort(splits, collator.reversed());
                    s = String.join("\n", splits);
                }
                case "unique" -> {
                    String[] splits = s.split("\\r?\\n");
                    LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList(splits));
                    s = String.join("\n", set);
                }
            }

            doc.remove(offset, len);
            doc.insertString(offset, s, null);
        } catch (BadLocationException e1){
            JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("selectErr"), provider.getString("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inverts case of passed {@code String}
     * @param s {@code String} whose case is inverted.
     */
    private String invert(String s){
        char[] arr = s.toCharArray();
        for(int i = 0; i < arr.length; i++){
            if(Character.isLowerCase(arr[i])){
                arr[i] = Character.toUpperCase(arr[i]);
            } else if(Character.isUpperCase(arr[i])){
                arr[i] = Character.toLowerCase(arr[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        for(char c: arr)
            sb.append(c);
        return sb.toString();
    }

    private void createMenus(){
        JMenuBar menu = new JMenuBar();

        JMenu file = new LocalizableJMenu("file", provider);
        file.add(new JMenuItem(createBlankDocument));
        file.add(new JMenuItem(openDocument));
        file.add(new JMenuItem(saveDocument));
        file.add(new JMenuItem(saveAs));
        file.addSeparator();
        file.add(new JMenuItem(closeTab));
        file.add(new JMenuItem(exit));
        menu.add(file);

        JMenu edit = new LocalizableJMenu("edit", provider);
        edit.add(new JMenuItem(copy));
        edit.add(new JMenuItem(cut));
        edit.add(new JMenuItem(paste));
        menu.add(edit);

        JMenu tools = new LocalizableJMenu("tools", provider);
        tools.add(new JMenuItem(upperCase));
        tools.add(new JMenuItem(lowerCase));
        tools.add(new JMenuItem(invertCase));
        tools.add(new JMenuItem(unique));
        JMenu sort = new LocalizableJMenu("sort", provider);
        sort.add(new JMenuItem(descending));
        sort.add(new JMenuItem(ascending));
        tools.add(sort);
        tools.add(new JMenuItem(statistical));
        menu.add(tools);

        JMenu languages = new LocalizableJMenu("languages", provider);
        languages.add(eng);
        languages.add(hr);
        languages.add(srb);
        languages.add(ger);
        menu.add(languages);

        setJMenuBar(menu);
    }

    private void createToolbars(){
        JToolBar toolBar = new LocalizableJToolbar("toolbar", provider);
        toolBar.setFloatable(true);

        toolBar.add(new JButton(createBlankDocument));
        toolBar.add(new JButton(openDocument));
        toolBar.add(new JButton(saveDocument));
        toolBar.add(new JButton(saveAs));
        toolBar.addSeparator();
        toolBar.add(new JButton(closeTab));
        toolBar.add(new JButton(exit));
        toolBar.addSeparator();
        toolBar.add(new JButton(cut));
        toolBar.add(new JButton(copy));
        toolBar.add(new JButton(paste));
        toolBar.add(new JButton(statistical));
        toolBar.addSeparator();

        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    private void createStatBar(){
        JToolBar status = new LocalizableJToolbar("statBar", provider);
        status.setFloatable(true);
        status.setLayout(new BorderLayout());

        status.add(length, BorderLayout.WEST);
        status.add(info, BorderLayout.CENTER);
        status.add(dateTime, BorderLayout.EAST);

        getContentPane().add(status, BorderLayout.PAGE_END);
    }

    private void setDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        new Timer(100 , e -> this.dateTime.setText(dtf.format(LocalDateTime.now()))).start();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
    }

}
