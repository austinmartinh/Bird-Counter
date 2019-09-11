/**
 * @author Austin Heraughty 20076679
 * @date 28/03/19
 */

package application.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class mainController {

	
	@FXML
	private AnchorPane labelPane;
	@FXML
	private MenuItem newImage;
	@FXML
	private MenuItem deleteImage;
	@FXML
	private MenuItem duplicateImage;
	@FXML
	private MenuItem redChannel;
	@FXML
	private MenuItem greenChannel;
	@FXML
	private MenuItem blueChannel;
	@FXML
	private MenuItem exit;
	@FXML
	private MenuItem fullAdjust;
	@FXML
	private ImageView imageViewer;
	@FXML
	private ImageView averageColorViewer;
	@FXML
	private Slider redSlider;
	@FXML
	private Slider greenSlider;
	@FXML
	private Slider blueSlider;
	@FXML
	private Slider noiseSlider;
	@FXML
	private Slider clusterSizeSlider;
	@FXML
	private Slider brightnessThreshold;
	@FXML
	private Label redLabel;
	@FXML
	private Label greenLabel;
	@FXML
	private Label blueLabel;
	@FXML
	private Label sizeLabel;
	@FXML
	private Label flightDirection;
	@FXML
	private Label imageName;
	@FXML
	private Label imageSize;
	@FXML
	private Label totalBirds;
	@FXML
	private Label adjustedTotalBirds;
	
	File chosenImage;
	Image convertedImage;
	WritableImage wImage;
	WritableImage averageColorDisplay;
	PixelReader myPixelReader;
	PixelWriter myPixelWriter;
	int[] pixelCoordinates2;
	public int width;
	public int averageBirdSize=0;
	int numberOfBirdsWiderThanTall=0;
	int numberOfBirdsTallerThanWide = 0;
	int noiseThreshold = 0;
	Color[] rainbow = {Color.AQUA, Color.BLUEVIOLET, Color.BROWN, Color.ORANGE, 
					   Color.YELLOW, Color.RED, Color.ROSYBROWN, Color.LIGHTSALMON,
					   Color.DEEPPINK, Color.PURPLE, Color.MEDIUMPURPLE,
					   Color.DARKCYAN,Color.PLUM, Color.ORANGERED, Color.TOMATO,
					   Color.GREENYELLOW, Color.DARKRED, Color.CHARTREUSE,
					   Color.BLUE, Color.DARKGOLDENROD,Color.DARKKHAKI, Color.SPRINGGREEN, Color.DARKOLIVEGREEN};
	
	boolean analysisExists = false;
	int labelNumber;
	ArrayList<Integer> rootList = new ArrayList<Integer>();

	
	

	public void openImagePicker(ActionEvent event) {
		numberOfBirdsWiderThanTall=0;
		numberOfBirdsTallerThanWide = 0;
		labelNumber =0;
		rootList.clear();
		FileChooser imagePicker = new FileChooser();
		imagePicker.setTitle("Pick an Image");
		imagePicker.getExtensionFilters().addAll(new ExtensionFilter("JPG files", "*.jpg", "*.JPG"), 
				new ExtensionFilter("PNG files", "*.png", "*.PNG"));

		chosenImage = imagePicker.showOpenDialog(null);

		try {
			convertedImage = new Image(chosenImage.toURI().toString(),
					imageViewer.getFitWidth(), 
					imageViewer.getFitHeight(), true, false);
			width = (int) convertedImage.getWidth();
			wImage = new WritableImage((int) width, (int) convertedImage.getHeight());
			imageViewer.setImage(convertedImage);
			
		} catch (Exception ex) {
			System.out.print("file invalid");
		}
		System.out.println("button clicked");
		averageImageColorDisplay(averageImageColorCalc());
		imageName.setText("Figure this out");
		imageSize.setText(width + "x" + convertedImage.getHeight());
		
		System.out.println("The image is " + wImage.getHeight() + " by " + wImage.getWidth() + " in size.");
	}

	public void deleteImage(ActionEvent event) {
		chosenImage = null;
		convertedImage = null;
		imageViewer.setImage(null);
	}

	public void returnPixelInfo() {
		myPixelReader = convertedImage.getPixelReader();
		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color color = myPixelReader.getColor(xPixel, yPixel);
				System.out.println("\nPixel color at coordinates (" + xPixel + "," + yPixel + ") " + color.toString());
				System.out.println("R = " + color.getRed());
				System.out.println("G = " + color.getGreen());
				System.out.println("B = " + color.getBlue());
				System.out.println("Opacity = " + color.getOpacity());
				System.out.println("Saturation = " + color.getSaturation());
			}
		}
	}

	public void redShiftImage() {
		myPixelReader = convertedImage.getPixelReader();
		myPixelWriter = wImage.getPixelWriter();

		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color color = myPixelReader.getColor(xPixel, yPixel);

				double redValue = color.getRed();
				double greenValue = 0;
				double blueValue = 0;

				Color newColor = new Color(redValue, greenValue, blueValue, 1.0);
				myPixelWriter.setColor(xPixel, yPixel, newColor);
			}

		}
		imageViewer.setImage(wImage);
		averageImageColorDisplay(averageImageColorCalc());

	}

	public void greenShiftImage() {
		myPixelReader = convertedImage.getPixelReader();
		myPixelWriter = wImage.getPixelWriter();

		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color color = myPixelReader.getColor(xPixel, yPixel);

				double redValue = 0;
				double greenValue = color.getGreen();
				double blueValue = 0;

				Color newColor = new Color(redValue, greenValue, blueValue, 1.0);

				myPixelWriter.setColor(xPixel, yPixel, newColor);
			}
		}
		imageViewer.setImage(wImage);
		averageImageColorDisplay(averageImageColorCalc());

	}

	public void blueShiftImage() {
		myPixelReader = convertedImage.getPixelReader();
		myPixelWriter = wImage.getPixelWriter();

		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color color = myPixelReader.getColor(xPixel, yPixel);

				double redValue = 0;
				double greenValue = 0;
				double blueValue = color.getBlue();

				Color newColor = new Color(redValue, greenValue, blueValue, 1.0);

				myPixelWriter.setColor(xPixel, yPixel, newColor);
			}

		}
		imageViewer.setImage(wImage);
		averageImageColorDisplay(averageImageColorCalc());
	}

	public void colorShift() {
		double redAdjust = redSlider.getValue();
		double greenAdjust = greenSlider.getValue();
		double blueAdjust = blueSlider.getValue();

		myPixelReader = imageViewer.getImage().getPixelReader();
		myPixelWriter = wImage.getPixelWriter();

		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color color = myPixelReader.getColor(xPixel, yPixel);

				double redValue = color.getRed();
				double greenValue = color.getGreen();
				double blueValue = color.getBlue();

				redValue += (redAdjust);
				greenValue += (greenAdjust);
				blueValue += (blueAdjust);

				if (redValue < 0) {
					redValue = 0;
				} else if (redValue > 1) {
					redValue = 1;
				}

				if (greenValue <= 0) {
					greenValue = 0;
				} else if (greenValue > 1) {
					greenValue = 1;
				}
				if (blueValue < 0) {
					blueValue = 0;
				} else if (blueValue > 1) {
					blueValue = 1;
				}
				Color newColor = new Color(redValue, greenValue, blueValue, 1);
				myPixelWriter.setColor(xPixel, yPixel, newColor);
			}
		}
		imageViewer.setImage(wImage);
		averageImageColorDisplay(averageImageColorCalc());

	}

	public void greyShiftImage() {
		myPixelReader = imageViewer.getImage().getPixelReader();
		myPixelWriter = wImage.getPixelWriter();

		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color color = myPixelReader.getColor(xPixel, yPixel);

				double greyValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

				Color newColor = new Color(greyValue, greyValue, greyValue, 1.0);
				myPixelWriter.setColor(xPixel, yPixel, newColor);
			}

		}
		imageViewer.setImage(wImage);
		averageImageColorDisplay(averageImageColorCalc());
	}

	public void blackAndWhiteShift() {

		myPixelReader = imageViewer.getImage().getPixelReader();
		myPixelWriter = wImage.getPixelWriter();

	
		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color color = myPixelReader.getColor(xPixel, yPixel);

				if ((color.getRed() + color.getGreen())> 1  ) {
					myPixelWriter.setColor(xPixel, yPixel, Color.BLACK);
				} else {
					myPixelWriter.setColor(xPixel, yPixel, Color.WHITE);
				}
			}
		}
		imageViewer.setImage(wImage);
	}
	
	public Color noBlue(Color colorToCull)
	{		
		return new Color(colorToCull.getRed(), colorToCull.getGreen(), 0,1);
	}

	public Color averageImageColorCalc() {
		double averageR = 0;
		double averageG = 0;
		double averageB = 0;
		
		double totalPixels = imageViewer.getImage().getHeight() * imageViewer.getImage().getWidth();
	
			myPixelReader = imageViewer.getImage().getPixelReader();
		
		for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < width; xPixel++) {
				Color currentColor = myPixelReader.getColor(xPixel, yPixel);

				averageR += currentColor.getRed();
				averageG += currentColor.getGreen();
				averageB += currentColor.getBlue();

			}
		}
		Color averageColor = new Color(averageR / totalPixels, averageG / totalPixels, averageB / totalPixels, 1);
		return averageColor;
	}

	public void averageImageColorDisplay(Color averageColor) {
		averageColorDisplay = new WritableImage(30, 30);
		myPixelWriter = averageColorDisplay.getPixelWriter();
		for (int yPixel = 0; yPixel < averageColorDisplay.getHeight(); yPixel++) {
			for (int xPixel = 0; xPixel < averageColorDisplay.getWidth(); xPixel++) {
				myPixelWriter.setColor(xPixel, yPixel, averageColor);
			}
		}
		averageColorViewer.setImage(averageColorDisplay);
	}

	public void resizeImage(ActionEvent event) {
//		imageViewer.setFitHeight(sizeSlider.getValue());
//		imageViewer.setFitWidth(sizeSlider.getValue());
	}

	public void drawBWImage()
	{	
		checkEachPixelForBlack();
		myPixelWriter = wImage.getPixelWriter();
		for(int y=0; y <convertedImage.getHeight(); y++)
		{
			for(int x =0; x<width; x++)
			{
				if(pixelCoordinates2[convertCoordinates(x,y)] >= 1)
				{
					myPixelWriter.setColor(x, y, Color.BLACK);
				}
				else
				{
					myPixelWriter.setColor(x, y, Color.WHITE);
				}
			}
		}	
		imageViewer.setImage(wImage);
	}
	
	public void scrubCoordinateArray(int[] array)
	{
		for(int x = 0; x<array.length;x++)
		{
			array[x] = -1;
		}
	}

	public void estimateBirdNumber()
	{
		int numberOfBirds=0;
		this.averageBirdSize =calculateAverageBirdSizeWithoutOutliers(getSizeList());
		System.out.println("Average Bird Size: " + averageBirdSize + " pixels per bird" + "\nThreshold for large birds: " + clusterSizeSlider.getValue() * averageBirdSize);
		
		for(int size:getSizeList())
		{
			System.out.println("size of bird is : " + size);
			if(size>(10*averageBirdSize))
			{
			}
			else if(size>=(clusterSizeSlider.getValue()*averageBirdSize))
			{
				numberOfBirds +=estimateNumberOfBirdsInFlock(size);
			}
			else if(size >0)
			{
				numberOfBirds++;
			}
			
		}
		adjustedTotalBirds.setText(Integer.toString(numberOfBirds));
		guessBirdFlightDirection();
		
	}

	public int convertCoordinates(int xPixel, int yPixel)
	{
		return (width*yPixel) + xPixel;
	}

	public void recolor() {
		if (imageViewer.getImage() != null) {
			myPixelReader = imageViewer.getImage().getPixelReader();
		} 
		myPixelWriter = wImage.getPixelWriter();

		for (int root : rootList) {
			Color reColor = rainbow[rootList.indexOf(root) % rainbow.length];
			for (int x = 0; x < pixelCoordinates2.length; x++) {
				if (findRoot(pixelCoordinates2[x]) == root) {
					myPixelWriter.setColor(x % width, x / width, reColor);
				}
				imageViewer.setImage(wImage);
			}
		}
		for(int x = 0; x < pixelCoordinates2.length; x++)			//sets noise back to black
		{
			if(pixelCoordinates2[x] >0 
					&& !rootList.contains(findRoot(pixelCoordinates2[x])))
			{
				if(x%width != width && x/width != wImage.getHeight()) {
					myPixelWriter.setColor(x%width, x/width , Color.BLACK);
				}
			}
		}
	}
	
	
/********************* DISJOINT SET *******************************/
	
	/**
	 * Prepares array of disjoint sets by setting each cell to hold its own id
	 * @param array The array being scrubbed, a 1d array equal in size to th total number of pixels in the image to be analyzed
	 */
	public void scrubCoordinateArray2(int[] array)
	{
		for(int x = 0; x<array.length;x++)			
		{
			array[x] = x;
		}
	}

	/**
	 * Takes coordinates and calls adjacent cell checker to reassign this cells parent in case of adjacent black cell
	 * @param xPixel xCoordinate of current cell
	 * @param yPixel yCoordinate of current cell
	 */
	public void assignCellToSet(int xPixel, int yPixel)
	{
			int cellNumber = pixelCoordinates2[convertCoordinates(xPixel,yPixel)];
			
			switch(checkForAdjacentBlackPixelDirection(xPixel,yPixel)) {
			default:
				if(!rootList.contains(cellNumber))
				rootList.add(cellNumber);
				int comboValue = incrementSetSize(cellNumber, 1);				
				pixelCoordinates2[cellNumber] =comboValue;

				break;
			case 1:	//match left, so set this cells value to left cell's root 
			
				pixelCoordinates2[findRoot(cellNumber -1)] = incrementSetSize(pixelCoordinates2[cellNumber-1] , 1);

				pixelCoordinates2[cellNumber] = findRoot(pixelCoordinates2[cellNumber-1]);				
				break;
			case 2:	//match up left, so set this cells value to above left cells value
				pixelCoordinates2[findRoot(cellNumber -1-width)] = incrementSetSize(pixelCoordinates2[cellNumber-1 -width] , 1);

				pixelCoordinates2[cellNumber] = findRoot(pixelCoordinates2[cellNumber-1 - width]);
				break;
			case 3:	//match up, so set this cells value to above cell's id number
				pixelCoordinates2[findRoot(cellNumber -width)] = incrementSetSize(pixelCoordinates2[cellNumber-width] , 1);

				
				pixelCoordinates2[cellNumber] = findRoot(pixelCoordinates2[cellNumber-width]);				
				break;
			case 4:	//match up right, so set this cells value to above right's cell id number
				pixelCoordinates2[cellNumber +1 - width] = incrementSetSize(pixelCoordinates2[cellNumber+1-width] , 1);

				pixelCoordinates2[cellNumber] = findRoot(pixelCoordinates2[cellNumber+1 - width]);
				break;
			}

	}

	/**
	 * Checks cells adjacent to input for a black cell
	 * @param xPixel	xCoordinate of image
	 * @param yPixel	yCoordinate of image
	 * @return 1-4 depending on direction of black cell located, 0 for no black cell 
	 * 	 
	 * */
	public int checkForAdjacentBlackPixelDirection(int xPixel,int yPixel)	
	{	
		if(xPixel-1 >=0) { 									//check left
			if(checkSpecificPixelForBlack(xPixel-1,yPixel)){
				return 1;
			}
		}
		else if(xPixel-1 >=0 && yPixel-1 >=0) { 									//check up left
			if(checkSpecificPixelForBlack(xPixel-1,yPixel-1)){
				return 2;
			}
		}
		else if(yPixel -1 >=0){							//check up						
			if(checkSpecificPixelForBlack(xPixel,yPixel-1)){
				return 3;
			}
		}
		else if(xPixel+1 <= width && yPixel -1 >=0) { 		//check up right
			if(checkSpecificPixelForBlack(xPixel+1,yPixel-1)){
				return 4;
			}
		}
		return 0; //no adjacent black pixels
	}
	
	/**
	 * Checks each pixel in the image for black/white value. On finding a black cell, calls disjoint set assigning method, on white cell sets value to -1
	 */
	public void checkEachPixelForBlack()
{
	myPixelReader = convertedImage.getPixelReader();
	pixelCoordinates2 = new int[convertCoordinates(width, (int) convertedImage.getHeight())];
	scrubCoordinateArray2(pixelCoordinates2);
	
	for (int yPixel = 0; yPixel < convertedImage.getHeight(); yPixel++) {
		for (int xPixel = 0; xPixel < width; xPixel++) {
			if(checkSpecificPixelForBlack(xPixel,yPixel))
			{
				assignCellToSet(xPixel,yPixel);
			}
			else
			{
				pixelCoordinates2[convertCoordinates(xPixel, yPixel)] = -1;
			}
		}
	}
}

	/**
	 * Method which looks at color value of an x,y coordinate pair in the image to see if it is black
	 * @param xPixel
	 * @param yPixel
	 * @return True if pixel is black, else false
	 */
	public boolean checkSpecificPixelForBlack(int xPixel,int yPixel)
	{
		if((myPixelReader.getColor(xPixel,yPixel).getRed() 
		 + myPixelReader.getColor(xPixel,yPixel).getGreen())*100> (brightnessThreshold.getValue()*100)) 
		{
			return false;
		}
			return true;	
	}

	/**
	 * Find method for locating disjoint set root
	 * @param cellId the cell we are querying the root of
	 * @return Int value representing the cell which is the root of this set...if a white cell root will be -1 
	 */
	public int findRoot(int valueInCell) {

		if(valueInCell ==-1) //querying array cell -1 will break loop      --------- REFACTOR ARRAY TO USE ID +1 and initialise to 0 instead of -1 to remove this IF ??
		{
			return -1;
		}
		int root = getSetId(valueInCell); 
		return getSetId(pixelCoordinates2[root])==root? root: ( pixelCoordinates2[valueInCell]= findRoot(pixelCoordinates2[valueInCell]));
	}

	
	/**
	 * Prints set membership of each pixel to console
	 */
	public void printSetData(/*int[][] array*/) {
		for(int y =0; y < convertedImage.getHeight(); y++)
		{
			for(int x = 0; x < width; x++)
			{
				System.out.println("The pixel at coordinate (" + y + "," + x + ") is part of set " + findRoot( convertCoordinates(x,y)) + ".");
			}
		}
		System.out.println("Total Sets: " +rootList.size());
	}
		
	/**
	 * Iterates through 1d array holding set values and compares adjacent roots of all black cells
	 * @param array	1d array holding set values 
	 */
	public void mergeSets()
	{
		for(int x =0; x < pixelCoordinates2.length; x++)
		{
			if(pixelCoordinates2[x] != -1)
			{
			checkForAdjacentSets2(x);
			}
		}
		removeSmallSets();
		recolor();
		
		totalBirds.setText(Integer.toString(rootList.size()));
	}

	/**
	 * Merges two sets by pointing one root to the other
	 * @param array array operated on 
	 * @param root1 root to change parent of 
	 * @param root2 new parent root
	 */
	public void quickUnion(int root1, int root2)
	{
		if(root1 != -1 && root2!=-1) {
		pixelCoordinates2[root2] = incrementSetSize(pixelCoordinates2[root2], getSetSize(pixelCoordinates2[root1]));
		pixelCoordinates2[root1] = getSetId(root2);
		rootList.remove((Integer) root1);
		}
	}
	
	public void quickUnion2(int valueInCellOne, int valueInCellTwo)
	{
		if(findRoot(valueInCellOne) != -1 && findRoot(valueInCellTwo) != -1)	
		{
			int root1 = findRoot(getSetId(valueInCellOne));
			int root2 = findRoot(getSetId(valueInCellTwo));
			
			pixelCoordinates2[root1] = root2;						//The root of set 1 now points to the cell of the array holding the root of set 2
			pixelCoordinates2[root2] = incrementSetSize(valueInCellTwo, getSetSize(valueInCellOne));
			rootList.remove((Integer) root1);
		}
	}

	/**
	 * Takes a cell and checks 8 directions for mismatched roots, then calls union method upon finding mismatched adjacent roots
	 * @param pixelCoordinates2
	 * @param cell
	 * @return
	 * 
	 */
	public void checkForAdjacentSets2(int cellToCheck)
	{
		int rootOfCell = findRoot(cellToCheck);
		
		//if cell to right of current cell mismatches
		if(cellToCheck + 1 < pixelCoordinates2.length
			&& findRoot(cellToCheck+1) != rootOfCell)
		{
			quickUnion(rootOfCell, findRoot(cellToCheck+1));
		}
		//is cell to down right of current cell mismatches
		if(cellToCheck + 1 + width < pixelCoordinates2.length
				&& rootOfCell!= findRoot(cellToCheck+1+width))
		{
			quickUnion(rootOfCell, findRoot(cellToCheck+1+width));
		}
		//if cell to down of current cell mismatches
		if(cellToCheck + width < pixelCoordinates2.length
				&& rootOfCell!= findRoot(cellToCheck+width))
		{
			quickUnion(rootOfCell, findRoot(cellToCheck+width));
		}	
		//if cell to down left
		if(cellToCheck+width-1 <pixelCoordinates2.length
				&& cellToCheck-1+width >=0
				&& findRoot(cellToCheck +width -1) !=rootOfCell)
		{
			quickUnion(rootOfCell,findRoot(cellToCheck + width-1));
		}
		//if cell to left
		if(cellToCheck-1 >=0
				&& findRoot(cellToCheck-1) !=rootOfCell)
		{
			quickUnion(rootOfCell,findRoot(cellToCheck-1));
		}
		
		//if cell to up lft
		if(cellToCheck-1-width >=0
				&& findRoot(cellToCheck - width -1) !=rootOfCell)
		{
			quickUnion(rootOfCell,findRoot(cellToCheck - width-1));
		}
		//if cell to up
		if(cellToCheck-width >=0
				&& findRoot(cellToCheck- width) !=rootOfCell)
		{
			quickUnion(rootOfCell,findRoot(cellToCheck - width));
		}
		//if cell to up right
		if(cellToCheck-width +1 >=0
				&& cellToCheck-width+1<pixelCoordinates2.length
				&& findRoot(cellToCheck- width+1) !=rootOfCell)
		{
			quickUnion(rootOfCell,findRoot(cellToCheck - width+1));
		}
	
	}

/**
 	* Takes a set of integers representing the extreme x and y values of pixels in a disjoint set then attempts to draw a box around that set of pixels
 * @param setBoundaries[]
 * 
 */
	public void drawBoxAroundBird(int[] setBoundaries)
	{
		for(int x = (setBoundaries[0] >0 ? setBoundaries[0] -1: 0) ; x<(setBoundaries[1] +2 <= width? setBoundaries[1]+2: width); x++)
		{
			myPixelWriter.setColor(x, (setBoundaries[2]-1>0?setBoundaries[2]-1:0), Color.DODGERBLUE);
			myPixelWriter.setColor(x, (int) (setBoundaries[3]+1<convertedImage.getHeight()?setBoundaries[3]+1:convertedImage.getHeight()), Color.DODGERBLUE);
		}
		for(int y = (setBoundaries[2] >0 ? setBoundaries[2] -1: 0) ; y<(setBoundaries[3] +1 <= convertedImage.getHeight()? setBoundaries[3]+1: convertedImage.getHeight()); y++)
		{
			myPixelWriter.setColor((setBoundaries[0] -1 >0? setBoundaries[0]-1:0), y, Color.DODGERBLUE);
			myPixelWriter.setColor((setBoundaries[1] +1 <=width? setBoundaries[1]+1:width), y, Color.DODGERBLUE);
		}
	}

	public void drawBoxAroundBird2(int[] setBoundaries) {
		
		Rectangle rect = new Rectangle(setBoundaries[0], setBoundaries[2], setBoundaries[1]- setBoundaries[0] , setBoundaries[3]- setBoundaries[2] );
		rect.setStroke(Color.DODGERBLUE);
		rect.setFill(Color.TRANSPARENT);
		labelPane.getChildren().add(rect);
	}
	
	/**
	 * Method which will pass sets of values to box drawing method until all sets are accounted for
	 */
	public void drawBoxes()
	{
		for(int root: rootList)
		{
			drawBoxAroundBird2(findSetBoundaries(root));
		}
	}


	/**
	 * Iterates through the 1d array and for each then returns the max and min x/y values for a given set 
	 * @param array
	 * @param setToCheck	The root of the set to check i.e. Set ID
	 * @return Set containing min max x/y values for a given set
	 */
	public int[] findSetBoundaries(int setToCheck)
	{
		int[] setBoundaries = new int[5];		//4 cells in array, [0]=minX, [1]=maxX, [2]=minY, [3]= maxY
		
		setBoundaries[0] = setToCheck%width;
		setBoundaries[1] = setToCheck%width;
		setBoundaries[2] = setToCheck/width;
		setBoundaries[3] = setToCheck/width;

		for(int x =0; x < pixelCoordinates2.length; x++)
		{
			if(findRoot(pixelCoordinates2[x]) == setToCheck)
			{
				if(x%width < setBoundaries[0])
					setBoundaries[0] = x%width;
				else if(x%width >setBoundaries[1])
					setBoundaries[1] = x%width;
				if(x/width<setBoundaries[2])
					setBoundaries[2] = x/width;
				else if(x/width > setBoundaries[3])
					setBoundaries[3] = x/width;
		
			}
		}
		
		if((setBoundaries[1] - setBoundaries[0]) > (setBoundaries[3] - setBoundaries[2]))
			numberOfBirdsWiderThanTall++;
		else
			numberOfBirdsTallerThanWide++;
		
		return setBoundaries;
	}

	/**
	 * Takes integer value from noise slider and updates noise threshold value
	 */
	public void updateNoiseThreshold()
	{
		noiseThreshold = (int) noiseSlider.getValue();
	}
	
	/**
	 * Takes a comboValue and increments the size portion 
	 * @param setId
	 * @return SetId with updated size
	 */
	public void incrementSetSize2(int comboValue, int increment)
	{
		
		int size = comboValue >>20;
		size+=increment;
		comboValue = comboValue | ( size <<20);
	
		pixelCoordinates2[getSetId(comboValue)] = comboValue;
	}
	
	/**
	 * Takes a comboValue and increments the size portion 
	 * @param setId
	 * @return SetId with updated size
	 */
	public int incrementSetSize(int comboValue, int increment)
	{
		
		int size = getSetSize(comboValue);
		size+=increment;
		size = (size <<20);
		comboValue = getSetId(comboValue) | ( size);
		return comboValue;
	}
	
	/**
	 * Performs bitshift to extract size from int representing set data
	 * @param setId
	 * @return
	 */
	public int getSetSize(int cellValue)
	{
		return cellValue >>20;
	}
		
	/**
	 * Takes the value from a cell of the array and returns the value of the set it belongs to
	 * @param setValue
	 * @return
	 */
	public int getSetId(int cellValue)
	{
		return cellValue & 0x000FFFFF;
	}
	
	/**
	 * method which prints flight direction based on dimnesions of bird sets
	 */
	public void guessBirdFlightDirection()
	{
		if(numberOfBirdsTallerThanWide>numberOfBirdsWiderThanTall)
			flightDirection.setText("Left/Right");
		else
			flightDirection.setText("Up/Down");
	}
	
	
	
	/*
	 * Removes roots from rootlist if the number of members of its set are fewer than the specified noise threshold
	 */
	public void removeSmallSets()
	{
		for(int x = 0;x <pixelCoordinates2.length; x++)
		{
			if(rootList.contains(x)  && getSetSize(pixelCoordinates2[x]) < noiseThreshold)
			{
				rootList.remove((Integer)(x));
			}
		}
	}
	
	/**
	 * Adds one label for each set recorded in the image
	 */
	public void addLabels()
	{
			labelNumber =0;
			for(int root: rootList)
			{
				labelNumber++;
				VBox vbox = new VBox();			
				Label boxLabel = new Label(Integer.toString(labelNumber));
				vbox.getChildren().add(boxLabel);
				labelPane.setTopAnchor(vbox, (double) (root/width));
				labelPane.setLeftAnchor(vbox,  (double) (root%width));
				labelPane.getChildren().add(vbox);
			}
	}
	

	/**
	 * Clears boxes and labels from the image
	 */
	public void removeAnalysis()
	{
		labelPane.getChildren().clear();
		labelNumber = 0;
	
	}

	
	/**
	 * Adds or removes analysis boxes and numbers
	 */
	public void toggleAnalysis()
	{
		if(analysisExists)
			removeAnalysis();
		else
		{
			System.out.println(rootList.size());
			addLabels();
			drawBoxes();
		}		
		analysisExists = !analysisExists;
	}

	public void exit()
	{
		System.exit(0);
	}
	

	
/******************** Outliers *********************/	
	
	/**
	 * calls method to remove outliers from list of sizes, then calculates revised average size of sets
	 * @return
	 */
	public int calculateAverageBirdSizeWithoutOutliers( ArrayList<Integer> sizeList)
	{
		ArrayList<Integer> revisedSizeList = removeOutlierSizes(sizeList);
		
		return calculateMeanBirdSize(revisedSizeList);
	}

	/**
	 * Calculates SD and mean size for a list of sizes, then removes outliers from the list based on those figures
	 * @return list of sizes without outliers
	 */
	public ArrayList<Integer> removeOutlierSizes( ArrayList<Integer> sizeList)
	{
	
	//	ArrayList<Integer> sizeList = getSizeList();
		int sd = calculateStandardDeviation(sizeList);
		int mean = calculateMeanBirdSize(sizeList);
		ArrayList<Integer> revisedSizeList = sizeList;
		for(int x = 0 ; x<sizeList.size();x++)
		{
			if( sizeList.get(x) < (mean - sd) || sizeList.get(x) > (mean+sd))
			{
				revisedSizeList.remove(sizeList.get(x));
			}
		}		
		return revisedSizeList;
	}
	
	/**
	 * iterates through the arraylist, adds the sizes of each set and calculates average set size
	 * @return
	 */
	public int calculateMeanBirdSize(ArrayList<Integer> sizeList)
	{
		int totalPixels=0;
		int numInList = 0;
		for(int size: sizeList)
		{
			
			totalPixels+= size;
			if(size>0)
				numInList++;
		}
		
		
		return totalPixels/numInList;
	}
	
	/**
	 * Calculates SD of a list of sizes by calculating mean and variance
	 */
	public int calculateStandardDeviation(ArrayList<Integer> sizeList)
	{
		int variance =0;
		int mean = calculateMeanBirdSize(sizeList);
		
		for(int size: sizeList)
		{
			variance += ((mean - size) * (mean - size));
		}
		return (int) Math.sqrt(variance/sizeList.size());
	}


	
	/**
	 * iterates through array, extracts all size values and generates an arraylist with these sizes
	 * @return arraylist of sizes of sets
	 */
	public ArrayList<Integer> getSizeList()
	{
		ArrayList<Integer> sizeList = new ArrayList<Integer>();
		for(int root:rootList)
		{
			for(int x = 0; x<pixelCoordinates2.length;x++)
			{
				if(getSetId(pixelCoordinates2[x]) ==root) {
					sizeList.add(getSetSize(pixelCoordinates2[x]));
				}
			}
		}
		return sizeList;
	}
	
	public int estimateNumberOfBirdsInFlock(int flockSize)
	{
		return flockSize/averageBirdSize;
	}
	

	
}
	


	

