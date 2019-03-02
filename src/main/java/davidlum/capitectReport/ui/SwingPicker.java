package davidlum.capitectReport.ui;

import davidlum.capitectReport.converter.CsvConverter;
import davidlum.capitectReport.converter.Main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.Preferences;

import javax.swing.JTextArea;

public class SwingPicker extends JDialog {
  private static final String CSV_FILE_PREFS_KEY = "lastCsvFile";

  private final JPanel contentPanel = new JPanel();
  private JTextField selectedFileTextField;
  private JButton okButton;
  private JTextArea errorText;

  private File selectedCsvFile;
  Preferences prefs = Preferences.userNodeForPackage(SwingPicker.class);

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    try {
      SwingPicker dialog = new SwingPicker();
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   */
  public SwingPicker() {
    String lastCsvFileName = prefs.get(CSV_FILE_PREFS_KEY, null);
    if (lastCsvFileName != null) {
      File lastCsvFile = new File(lastCsvFileName);
      if (lastCsvFile.isFile()) {
        selectedCsvFile = lastCsvFile;
      }
    }

    setBounds(100, 100, 800, 400);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(new BorderLayout(0, 0));
    {
      JLabel lblSelectACapitect = new JLabel("Select a Capitect asset report .csv file");
      contentPanel.add(lblSelectACapitect, BorderLayout.NORTH);
    }
    {
      selectedFileTextField = new JTextField();
      selectedFileTextField.setPreferredSize(new Dimension(600, 19));
      selectedFileTextField.setMinimumSize(new Dimension(600, 19));
      if (selectedCsvFile != null) {
        selectedFileTextField.setText(selectedCsvFile.getAbsolutePath());
      }
      contentPanel.add(selectedFileTextField, BorderLayout.CENTER);
      selectedFileTextField.setColumns(10);
    }
    {
      JButton browseButton = new JButton("Browse...");
      contentPanel.add(browseButton, BorderLayout.EAST);
      {
        errorText = new JTextArea();
        errorText.setPreferredSize(new Dimension(500, 280));
        contentPanel.add(errorText, BorderLayout.SOUTH);
      }
      browseButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JFileChooser chooser = new JFileChooser();
          FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
          chooser.setFileFilter(filter);
          int returnVal = chooser.showOpenDialog(SwingPicker.this);
          if(returnVal == JFileChooser.APPROVE_OPTION) {
            selectedCsvFile = chooser.getSelectedFile();
            okButton.setEnabled(true);
            selectedFileTextField.setText(selectedCsvFile.getAbsolutePath());
            prefs.put(CSV_FILE_PREFS_KEY, selectedCsvFile.getAbsolutePath());
          }
        }
      });
    }
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.setEnabled(selectedCsvFile != null);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            try {
              File outputCsv = Main.makeOutputFileFromInputFile(selectedCsvFile);
              CsvConverter converter = new CsvConverter(selectedCsvFile, outputCsv);
              converter.process();
              System.exit(0);
            } catch (Exception e) {
              StringWriter sw = new StringWriter();
              PrintWriter pw = new PrintWriter(sw);
              e.printStackTrace(pw);
              String errrorMessage = sw.toString();
              errorText.setText(errrorMessage);
            }
          }
        });
      }
      {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.exit(0);
          }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
  }
}
