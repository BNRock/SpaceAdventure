package adventure;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.UIManager.*;
import javax.swing.Box.*;
import org.json.simple.JSONObject;

/**
 * This class represent the GUI the player sees
 */
public class AdventureView extends JFrame {
	private Game game;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 500;
	public static final int TWELVE = 12;
	public static final int TEN = 10;
	public static final int EIGHT = 8;
	public static final int THREE = 3;
	public static final int FIVE = 5;
	private Container contentPane;
	private JButton btnName;
	private JButton btnJson;
	private JButton btnLoadSave;
	private JButton btnSaveGame;
	private JButton btnLoadDefault;
	private JScrollPane scrl;
	private JTextArea txtOutput;
	private JTextArea txtInventory;
	private JLabel lblName;
	private JTextField txtCommandBar;
	private Adventure adventure;
	private Player player;

	/**
	 * Default constructor for gui. Initializes members.
	 */
	public AdventureView(){
		super();
		game = null;
		setTheme();
		setSize(WIDTH, HEIGHT);
		setTitle("Adventure");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setMainContainer();
		adventure = null;
	}

	/**
	 * Overridden constructor for gui. Initializes members.
	 * @param g Game object being used to execute commands
	 */
	public AdventureView(Game g)
	{
		super();
		game = g;
		setTheme();
		setSize(WIDTH, HEIGHT);
		setTitle("Adventure");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setMainContainer();
		adventure = null;
	}

	/**
	 * Tries to set the Nimbus theme for the GUI.
	 */
	private void setTheme(){
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {}
	}

	/**
	 * Sets up the main container of the gui that holds everything.
	 */
	private void setMainContainer(){
		contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
		JLabel theLabel = new JLabel("Welcome to the adventure!");
		theLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(theLabel, BorderLayout.PAGE_START);
		contentPane.add(mainPanel(), BorderLayout.CENTER);
	}

	/**
	 * Sets up the main panel of the GUI that contains all the other panels
	 * @return the panel that was set up
	 */
	private JPanel mainPanel(){
		 JPanel panel = new JPanel();
		 panel.setLayout(new GridBagLayout());
		 panel.add(buttonsPanel(), getGBC(0,0,TWELVE));

		 panel.add(userInputPanel(), getGBC(0,2,EIGHT));
		 panel.add(inventoryPanel(), getGBC(EIGHT,2,EIGHT/2));

		return panel;
	}

	/**
	 * Sets up the panel with the buttons.
	 * @return the panel that was set up
	 */
	private JPanel buttonsPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		setButtons();
		panel.add(btnName);
		panel.add(btnJson);
		panel.add(btnLoadSave);
		panel.add(btnSaveGame);
		panel.add(btnLoadDefault);

		return panel;
	}

	/**
	 * Instantiates all the buttons and adds action lisTENers
	 */
	private void setButtons(){
		btnName = new JButton("Change Name");
		btnJson = new JButton("Load JSON Adventure");
		btnLoadSave = new JButton("Load Saved Game");
		btnSaveGame = new JButton("Save Game State");
		btnSaveGame.setEnabled(false);
		btnLoadDefault = new JButton("Load Default Adventure");

		btnJson.addActionListener(ev->loadJSON());
		btnName.addActionListener(ev->changeName());
		btnLoadSave.addActionListener(ev->loadSave());
		btnSaveGame.addActionListener(ev->saveGame());
		btnLoadDefault.addActionListener(ev->loadDefault());
	}

	/**
	 * Sets up the panel that handles the user input and output.
	 * @return the panel that was set up
	 */
	private JPanel userInputPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		txtCommandBar = new JTextField(TEN*THREE);
		setOutputArea();
		scrl = new JScrollPane(txtOutput);

		txtCommandBar.setEnabled(false);
		txtCommandBar.addActionListener(ev->enterCommand(txtCommandBar));

		panel.add(scrl, getGBC(0,0,1));
		panel.add(txtCommandBar, getGBC(0,1,1));

		return panel;
	}

	/**
	 * Sets up the panel that shows the player's inventory.
	 * @return the panel that was set up
	 */
	private JPanel inventoryPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel lblInv = new JLabel("Inventory");
		lblInv.setHorizontalAlignment(JLabel.CENTER);
		lblName = new JLabel("Name:");
		txtInventory = new JTextArea(TEN,TEN+FIVE);
		txtInventory.setEditable(false);
		txtInventory.setLineWrap(true);

		panel.add(lblInv, getGBC(0, 0, 1));
		panel.add(txtInventory, getGBC(0, 1, 1));
		panel.add(lblName, getGBC(0, 2, 1));

		return panel;
	}

	/**
	 * Sets up the output area of the GUI.
	 */
	private void setOutputArea(){
		txtOutput = new JTextArea(TEN+FIVE,TEN*THREE);
		txtOutput.setEditable(false);
		txtOutput.setLineWrap(true);
		txtOutput.setWrapStyleWord(true);
	}

	/**
	 * Gets a GridBagConstraints object configured to given specs.
	 * @param x x-cord on the grid
	 * @param y y-cord on the grid
	 * @param width cell width of the component
	 * @return GridBagConstraints object configured to given specs
	 */
	public GridBagConstraints getGBC(int x, int y, int width) {
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.insets = new Insets(FIVE,FIVE,FIVE,FIVE);
		gbc.fill = GridBagConstraints.BOTH;

		return gbc;
	}

	/**
	 * Loads a JSON adventure file
	 */
	private void loadJSON(){
		String path = tryFilePath();
		if(path.equals("")) return;
		String strError = "Error in JSON file! Please choose a valid JSON "
		+ "file or load the default adventure or save.";
		
		JSONObject obj = game.loadJSON(path);
		if(obj == null){
			JOptionPane.showMessageDialog(new JFrame(), strError, "Error: Bad JSON",
			 JOptionPane.ERROR_MESSAGE);
		}else{
			loadAdventure(obj);
		}
	}

	/**
	 * Loads a game save file
	 */
	private void loadSave(){
		String strError = "Error in save file! Please choose a valid save "
		+ "file or load the default adventure or a valid JSON adventure.";
		String path = tryFilePath();
		if(path.equals("")) return;
		if(!game.loadSave(path)){
			JOptionPane.showMessageDialog(new JFrame(), strError, 
			"Error: Bad Save", JOptionPane.ERROR_MESSAGE);
		}else{	
			startSave();
		}
	}

	/**
	 * Starts the save file by welcome the user and clearing the GUI output.
	 */
	private void startSave(){
		player = Game.getPlayer();
		adventure = Game.getAdventure();
		txtOutput.setText("");
		String strSuccess = "Welcome back " + player.getName() + "!";
		JOptionPane.showMessageDialog(new JFrame(), strSuccess, "Success", JOptionPane.PLAIN_MESSAGE);
		startGame();
		updateNameLabel(player.getName());
	}

	/**
	 * Sets an adventure object based on the given JSONObject
	 * @param obj JSONObject containing adventure details
	 */
	private void loadAdventure(JSONObject obj){
		String strSuccess = "Thanks for loading an adventure! To play, enter "
		+ "your commands in the text field.";
		try {
			adventure = game.generateAdventure(obj);
			player = game.getPlayer();
			JOptionPane.showMessageDialog(new JFrame(), strSuccess, "Success", JOptionPane.PLAIN_MESSAGE);
			txtOutput.setText("");
			startGame();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "Error: " + e.getMessage(),
			 "Error: Bad JSON", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Saves the current game state
	 */
	private void saveGame(){
		String strError = "Error: Cannot save game state!";
		String filename = JOptionPane.showInputDialog(new JFrame(), 
		"What filename would you like your save file to have??");
		if(!game.saveGame(filename)){
			JOptionPane.showMessageDialog(new JFrame(), strError, "Error: Cannot Save", JOptionPane.ERROR_MESSAGE);
		}else{
			String strSuccess = "Success! Your game state has been saved as " + player.getSaveGameName();
			JOptionPane.showMessageDialog(new JFrame(), strSuccess, "Success", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Loads the default JSON adventure
	 */
	private void loadDefault(){
		String strError = "Error in JSON file! Please choose a valid JSON "
		+ "file or save.";
		JSONObject obj = game.loadJSON(game.getInputStream());
		if(obj == null){
			JOptionPane.showMessageDialog(new JFrame(), strError, "Error: Bad JSON", JOptionPane.ERROR_MESSAGE);
		}else{
			loadAdventure(obj);
		}
	}

	/**
	 * Takes command from user and puts it in output screen as well as command output
	 * @param txt the command bar the user entered command in
	 */
	private void enterCommand(JTextField txt){
		String output = game.commandOutput(txt.getText());
		updateOutput("> " + txt.getText());
			txtCommandBar.setText("");
		if(output == null){
			confirmQuit();
		}else{
			updateOutput(output);
		}
		
		updateInventory();
	}

	/**
	 * Confirms if the user wants to quit the game and does so if they do.
	 */
	private void confirmQuit(){
		int result = JOptionPane.showConfirmDialog(new JFrame(), "Do you really want to quit?",
		"Confirmation", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION){
			JOptionPane.showMessageDialog(new JFrame(), "Thanks for playing, have a great day!",
			"Quit Game", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}else{
			JOptionPane.showMessageDialog(new JFrame(), "Thanks for not giving up on me :)",
			"Cancel Quit", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Starts the game by updating the inventory and displaying current room
	 */
	private void startGame(){
		updateInventory();
		txtCommandBar.setEnabled(true);
		btnSaveGame.setEnabled(true);
		updateOutput("Starting adventure now! Type 'help' if you need instructions");
		updateOutput(adventure.newRoomString(adventure.getCurrentRoom()));
	}

	/**
	 * Updates the inventory text area
	 */
	private void updateInventory(){
		txtInventory.setText("");
		for(Item item : player.getInventory()){
			txtInventory.append(item.getName() + "\n");
		}
	}

	/**
	 * Tries to get a file from filechooser
	 * @return the chosen file path
	 */
	private String tryFilePath(){
		String path = "";
		try {
			path = getFilePath();
		} catch (Exception e) {}

		return path;
	}

	/**
	 * Opens a filechooser and lets the user choose a file.
	 * @return relative path of the chosen file
	 */
	private String getFilePath(){
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int result = fileChooser.showOpenDialog(this);
		Path pathRelative = null;
        if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			Path pathAbsolute = Paths.get(selectedFile.getPath());
			Path pathBase = Paths.get(System.getProperty("user.dir"));
			pathRelative = pathBase.relativize(pathAbsolute);
            System.out.println("Selected file: " + pathRelative);
		}
		
		return pathRelative.toString();
	}

	/**
	 * Changes the user's name and displays the change.
	 */
	private void changeName(){
		String name = JOptionPane.showInputDialog(new JFrame(), "What's your name?");
		player.setName(name);
		updateNameLabel(name);
	}

	/**
	 * Updates the label showing the user's name
	 */
	private void updateNameLabel(String name){
		lblName.setText("Name: " + name);
	}

	/**
	 * Puts the given string onto the output text area.
	 * @param str string to be appended onto output area
	 */
	private void updateOutput(String str){
		txtOutput.append(str + "\n");
	}


	public static void main(String[] args){
		Game game = new Game();
		AdventureView gui = new AdventureView(game);
		gui.pack();
		gui.setVisible(true);
	}
}
