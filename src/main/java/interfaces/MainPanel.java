package interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class MainPanel extends JPanel{
    private SettingsPanel settingsPanel;
    private TablePanel tablePanel;
    private GraphsPanel graphsPanel;
    private FilePanel filePanel;
    private CatalogPanel catalogPanel;
    private JSplitPane panelRight;
    private JSplitPane panelLeft;
    private JSplitPane splitPane;

    public MainPanel(){
        setLayout(new BorderLayout());

        catalogPanel = new CatalogPanel();
        settingsPanel = new SettingsPanel();
        graphsPanel = new GraphsPanel();
        tablePanel = new TablePanel();
        filePanel = new FilePanel();
        panelRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, graphsPanel);
        panelLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, catalogPanel, filePanel);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeft, panelRight);

        panelRight.setOneTouchExpandable(true);
        panelRight.setDividerLocation(150);
        panelLeft.setOneTouchExpandable(true);
        panelLeft.setDividerLocation(150);
        panelLeft.setPreferredSize(new Dimension(300, 200));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        add(settingsPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
}