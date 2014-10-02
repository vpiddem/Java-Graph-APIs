package dgraphs;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.view.mxGraph;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class DirectedGraphWindow {

    void initComponents() {
        frame = new JFrame();
        panel_North = new JPanel();
        lbl_SelectLayout = new JLabel();
        combobox_SelectLayout = new JComboBox();
        lbl_ChooseInputFile = new JLabel();
        txt_ChooseFileFromPC = new JTextField();
        btn_GenerateData = new JButton();
        btn_GenerateGraph = new JButton();
        panel_Center = new JPanel();
        splitPane_Center = new JSplitPane();
        //======== frame ========
        {
            Container frameContentPane = frame.getContentPane();
            frameContentPane.setLayout(new BorderLayout(5, 5));

            //======== panel_North ========
            {
                panel_North.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_North.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0};
                ((GridBagLayout) panel_North.getLayout()).rowHeights = new int[]{0, 0, 0};
                ((GridBagLayout) panel_North.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout) panel_North.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

                //---- lbl_SelectLibrary ----
                lbl_SelectLayout.setText("Select Layout :");
                panel_North.add(lbl_SelectLayout, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                combobox_SelectLayout.setModel(new DefaultComboBoxModel(new String[]{
                    "Fast Organic",
                    "Tree",
                    "Circle",
                    "Hierarchial"
                }));
                combobox_SelectLayout.setSelectedItem("Fast Organic");
                panel_North.add(combobox_SelectLayout, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- lbl_ChooseInputFile ----
                lbl_ChooseInputFile.setText("Choose File :");
                panel_North.add(lbl_ChooseInputFile, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                txt_ChooseFileFromPC.setText("Double Click to Choose.. ");
                txt_ChooseFileFromPC.setForeground(Color.lightGray);
                panel_North.add(txt_ChooseFileFromPC, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));
                JLabel copyRight = new JLabel(new ImageIcon("img/image.png"));
//                copyRight.setPreferredSize(new Dimension(240, 40));
                panel_North.add(copyRight, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
                txt_ChooseFileFromPC.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            txt_ChooseFileFromPC.setText("");
                            JFileChooser inputFileChooser = new JFileChooser();
                            System.out.println(" " + e.getX() + ", " + e.getY());
                            inputFileChooser.setLocation(e.getLocationOnScreen());
                            inputFileChooser.setCurrentDirectory(new File("C:\\"));
                            inputFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                            FileNameExtensionFilter filter_Extension = new FileNameExtensionFilter("CSV Extension", "csv");
                            inputFileChooser.addChoosableFileFilter(filter_Extension);
                            inputFileChooser.setDialogTitle("Choose Input Data File");
                            int var_FileChooser = inputFileChooser.showDialog(panel_North, "Select");
                            if (var_FileChooser == JFileChooser.APPROVE_OPTION) {
                                String inputFilePath = inputFileChooser.getSelectedFile().getParent() + "\\" + inputFileChooser.getSelectedFile().getName();
                                txt_ChooseFileFromPC.setText(inputFilePath);
                            }
                        }

                    }
                });

                //---- btn_GenerateData ----
                btn_GenerateData.setText(
                        "Generate Data");
                btn_GenerateData.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action_btnGenerateData(txt_ChooseFileFromPC.getText().trim());
                    }

                });
                panel_North.add(btn_GenerateData,
                        new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5), 0, 0));

                //---- btn_GenerateGraph ----
                btn_GenerateGraph.setText(
                        "Generate Graph");
                panel_North.add(btn_GenerateGraph,
                        new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0), 0, 0));
                btn_GenerateGraph.setEnabled(false);
                btn_GenerateGraph.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action_btnGenerateGraph();
                    }

                });
            }
            frameContentPane.add(panel_North, BorderLayout.NORTH);

            //======== panel_Center ========
            {
                panel_Center.setBackground(Color.white);
                panel_Center.setLayout(new BorderLayout(5, 5));

                //======== splitPane_Center ========
                {
                    splitPane_Center.setDividerLocation(250);
                    splitPane_Center.setOneTouchExpandable(true);
                    JScrollPane scrollPane_CsvDataTable = new JScrollPane();
                    headers_CsvTable = new Vector<>();
                    headers_CsvTable.add("Current Node");
                    headers_CsvTable.add("Next Node");
                    headers_CsvTable.add("Edge Weight");
                    DefaultTableModel model_CsvDataTable = new DefaultTableModel(null, headers_CsvTable) {
                        boolean[] cellEditable = {false, false, false};

                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return cellEditable[column];
                        }
                    };
                    table_CsvData = new JXTable(model_CsvDataTable);
                    table_CsvData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    table_CsvData.setShowGrid(true);
                    table_CsvData.getTableHeader().setReorderingAllowed(false);
                    table_CsvData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    table_CsvData.setVisibleRowCount(30);
                    table_CsvData.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
                    table_CsvData.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));

                    scrollPane_CsvDataTable.setViewportView(table_CsvData);
                    panel_GraphDisplay = new JXTitledPanel("Graph View");
                    panel_GraphDisplay.setLayout(new BorderLayout(5, 5));

                    // Start
                    // create toolbar (graphOutline and toolbar Buttons
                    JPanel panel_ZoomAndCenter = new JPanel();
                    panel_ZoomAndCenter.setLayout(new BorderLayout());

                    // graphOutline
                    DirectedGraphWindow.graphOutline = new mxGraphOutline(graphComponent);
                    DirectedGraphWindow.graphOutline.setPreferredSize(new Dimension(100, 100));
                    panel_ZoomAndCenter.add(DirectedGraphWindow.graphOutline, BorderLayout.WEST);

                    // panel_ZoomAndCenter Buttons
                    JPanel buttonBar = new JPanel();
                    buttonBar.setLayout(new FlowLayout());
                    // zoom to fit
                    JButton btn_ZoomToFit = new JButton("Zoom & Fit");
                    btn_ZoomToFit.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            double newScale = 1;
                            Dimension graphSize = graphComponent.getGraphControl().getSize();
                            Dimension viewPortSize = graphComponent.getViewport().getSize();
                            int gWidth = (int) graphSize.getWidth();
                            int gHeight = (int) graphSize.getHeight();
                            if (gWidth > 0 && gHeight > 0) {
                                int w = (int) viewPortSize.getWidth();
                                int h = (int) viewPortSize.getHeight();
                                newScale = Math.min((double) w / gWidth, (double) h / gHeight);
                            }
                            graphComponent.zoom(newScale);
                        }
                    });
                    buttonBar.add(btn_ZoomToFit);

                    // center graph
                    JButton btn_CenterView = new JButton("Center Graph in ViewPort");
                    btn_CenterView.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            Dimension graphSize = graphComponent.getGraphControl().getSize();
                            Dimension viewPortSize = graphComponent.getViewport().getSize();
                            int x = graphSize.width / 2 - viewPortSize.width / 2;
                            int y = graphSize.height / 2 - viewPortSize.height / 2;
                            int w = viewPortSize.width;
                            int h = viewPortSize.height;

                            graphComponent.getGraphControl().scrollRectToVisible(new Rectangle(x, y, w, h));

                        }
                    });
                    buttonBar.add(btn_CenterView);

                    // put components on frame
                    panel_ZoomAndCenter.add(buttonBar, BorderLayout.CENTER);

                    JXTitledPanel panel_SplitPaneLeft = new JXTitledPanel("CSV Data");
                    JPanel panel_MyObservationsButton = new JPanel();
                    JButton btn_MyNotes = new JButton("My Notes");
                    panel_MyObservationsButton.add(btn_MyNotes);
                    btn_MyNotes.addActionListener(new AbstractAction() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            action_btnMyNotes();
                        }

                    });

                    panel_SplitPaneLeft.add(scrollPane_CsvDataTable, BorderLayout.CENTER);
                    panel_MyObservationsButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
                    panel_SplitPaneLeft.add(panel_MyObservationsButton, BorderLayout.SOUTH);

                    panel_GraphDisplay.add(panel_ZoomAndCenter, BorderLayout.SOUTH);
                    scrollPane_GraphDisplay = new JScrollPane();
                    scrollPane_GraphDisplay.setForeground(Color.WHITE);
                    panel_GraphDisplay.add(scrollPane_GraphDisplay);
                    splitPane_Center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_SplitPaneLeft, panel_GraphDisplay);
                    splitPane_Center.setDividerLocation(400);
                }
                panel_Center.add(splitPane_Center, BorderLayout.CENTER);
            }
            frameContentPane.add(panel_Center, BorderLayout.CENTER);
            frame.setTitle("DGraph Application-Dr.Barnes Laboratory");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(true);
            frame.setSize(1024, 800);
        }
    }

    private void action_btnGenerateData(String inputFilePath) {
        btn_GenerateGraph.setEnabled(true);

        //<editor-fold defaultstate="collapsed" desc="Read Data From CSV File">
        //Get scanner instance
        Scanner scanner = null;
        try {
            if (!inputFilePath.endsWith(".csv")) {
                inputFilePath = inputFilePath + ".csv";
            }
            scanner = new Scanner(new File(inputFilePath));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel_North, "File Not Found@Source \n Please Select a Valid File", "Error", JOptionPane.WARNING_MESSAGE);
        }
        //Set the delimiter used in file
        scanner.useDelimiter("\n");

        //Get all tokens and store them in some data structure
        vector_Outer = new Vector<>();
        Vector<Object> vector_InnerData = new Vector<>();
        while (scanner.hasNext()) {
            vector_InnerData = new Vector<>();
            String[] csvLineDataArray = scanner.next().split(",");
            System.out.println(csvLineDataArray[0] + " " + csvLineDataArray[1] + " " + csvLineDataArray[2]);
            vector_InnerData.addAll(Arrays.asList(csvLineDataArray));
            vector_Outer.add(vector_InnerData);
        }
        System.out.println(" \n ****** \n " + vector_Outer);
        //Scanner Close
        scanner.close();
//</editor-fold>
        updateDataInView(vector_Outer);
    }

    private void action_btnGenerateGraph() {
        // Get Distinct Current Node Set
        DirectedGraphStructure<Integer> graphStructure = new DirectedGraphStructure<Integer>();
        for (int i = 1; i < vector_Outer.size(); i++) {
            Vector<Object> vector_Inner = vector_Outer.get(i);
            graphStructure.add(Integer.parseInt(vector_Inner.get(0).toString()), Integer.parseInt(vector_Inner.get(1).toString()), Integer.parseInt(vector_Inner.get(2).toString().substring(0, vector_Inner.get(2).toString().length() - 1)));
            System.out.println(" Added V1:" + vector_Inner.get(0).toString() + " V2: " + vector_Inner.get(1).toString() + " Weight: " + vector_Inner.get(2).toString().trim() + "\n");;
        }

        System.out.println("The no. of vertices is: " + graphStructure.getNeighbors().keySet().size());
        System.out.println("The no. of edges is: " + graphStructure.getNumberOfEdges()); // to be fixed
        System.out.println("The current graphStructure: " + graphStructure);
        System.out.println("In-degrees for 1: " + graphStructure.inDegree(1));
        System.out.println("Out-degrees for 7: " + graphStructure.outDegree(7));
        System.out.println("In-degrees for 3: " + graphStructure.inDegree(3));
        System.out.println("Out-degrees for 3: " + graphStructure.outDegree(3));
        System.out.println("Outbounds for 5: " + graphStructure.outboundNeighbors(5));
        System.out.println("Inbounds for 7: " + graphStructure.inboundNeighbors(7));
        System.out.println("Cost for 1,2: " + graphStructure.getCost(1, 2));
        System.out.println("(1,2)? " + (graphStructure.isEdge(1, 2) ? "It's an edge" : "It's not an edge"));
        System.out.println("(1,3)? " + (graphStructure.isEdge(1, 3) ? "It's an edge" : "It's not an edge"));

        btn_GenerateGraph.setEnabled(false);
        GraphFrame gFrame = new GraphFrame();
        graphComponent = gFrame.createGraphFromCsvData(graphStructure, combobox_SelectLayout.getSelectedItem().toString());
        scrollPane_GraphDisplay.setViewportView(graphComponent);
    }

    private void updateDataInView(Vector<Vector<Object>> vector_Outer) {
        table_CsvData.setModel(new DefaultTableModel(
                vector_Outer,
                headers_CsvTable));
    }

    //Variable Declaration
    mxGraphComponent graphComponent = new mxGraphComponent(new mxGraph());
    private static mxGraphOutline graphOutline;
    private JFrame frame;
    private JPanel panel_North;
    private JLabel lbl_SelectLayout;
    private JComboBox combobox_SelectLayout;
    private JLabel lbl_ChooseInputFile;
    private JTextField txt_ChooseFileFromPC;
    private JButton btn_GenerateData;
    private JButton btn_GenerateGraph;
    private JPanel panel_Center;
    private JXTable table_CsvData;
    private Vector<String> headers_CsvTable;
    private JSplitPane splitPane_Center;
    private JXTitledPanel panel_GraphDisplay;
    private JScrollPane scrollPane_GraphDisplay;
    Vector<Vector<Object>> vector_Outer = new Vector<>();
    //End of variables declaration 

    private void action_btnMyNotes() {
        System.out.println("****Entered Button Open***");
//        Desktop desktop = Desktop.getDesktop();
//        desktop.open(new File("C:\\\\Vikas_Piddempally\\\\Projects\\\\Java\\\\DirectedGraphs\\\\docs\\\\MyObservationss.docx"));
        try {
//                                Runtime.getRuntime().exec("cmd /c MyObservations.doc");
            Runtime.getRuntime().exec("cmd /c C:\\\\Vikas_Piddempally\\\\Projects\\\\Java\\\\DirectedGraphs\\\\docs\\\\MyObservations.docx");
        } catch (Exception ex) {
            Logger.getLogger(DirectedGraphWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param graphOutline the graphOutline to set
     */
    public static void setGraphOutline(mxGraphOutline graphOutline) {
        DirectedGraphWindow.graphOutline = graphOutline;
    }
}
