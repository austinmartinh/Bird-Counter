import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.controller.mainController;
import javafx.scene.shape.Arc;

class OutlierTests {
	ArrayList<Integer> sizeList;
	mainController testController;
	int comboValue;

	
	@BeforeEach
	public void setUp()
	{
		testController = new mainController();
		 comboValue = 3145735;
		sizeList = new ArrayList<Integer>();
		sizeList.add(20);
		sizeList.add(22);
		sizeList.add(25);
		sizeList.add(30);
		sizeList.add(35);
		sizeList.add(38);
		sizeList.add(40);
		sizeList.add(70);
	}
	
	@Test
	void testCalculateMeanSize() {
		assertEquals(35,testController.calculateMeanBirdSize(sizeList));
	}
	
	@Test
	public void testCalculateStandardDeviation()
	{
		assertEquals(14, testController.calculateStandardDeviation(sizeList));
	}
	
	@Test
	public void testCalculateMeanWithoutOutliers()
	{
		assertEquals(31,testController.calculateAverageBirdSizeWithoutOutliers(sizeList));
	}
	
	@Test
	public void testNumberOfBirdsInFlock()
	{
		testController.averageBirdSize = 10;
		assertEquals(20,testController.estimateNumberOfBirdsInFlock(200) );
	}
	
	@Test
	public void testConvertCoordinates()
	{
		testController.width= 6;
		assertEquals(15, testController.convertCoordinates(3, 2));
		assertEquals(26, testController.convertCoordinates(2, 4));
		assertEquals(3, testController.convertCoordinates(3, 0));
	}
	
	@Test
	public void testFindRoot()
	{
		
	}
	
	@Test
	public void testGetSetId()
	{
		assertEquals(7,testController.getSetId(comboValue));
	}
	
	@Test
	public void testGetSetSize()
	{
		assertEquals(3,testController.getSetSize(comboValue)); 
		assertEquals(4,testController.getSetSize(4194311)); 
		assertEquals(7,testController.getSetSize(7340039)); 
		
		
		
	}
	
	@Test
	public void testIncrementSetSize()
	{
		assertEquals(5, testController.getSetSize(testController.incrementSetSize(4194311, 1)));
		assertEquals(8, testController.getSetSize(testController.incrementSetSize(7340039, 1)));
	}

}
