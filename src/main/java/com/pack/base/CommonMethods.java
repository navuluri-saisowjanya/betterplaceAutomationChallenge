package com.pack.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CommonMethods {
	public Map<String,String> testDataMap=new HashMap<String, String>();
	public Logger log = LogManager.getLogger(CommonMethods.class);
	
	
	
	/**
	 * This method is used to read data from Excel 
	 * @param path
	 * @param sheetName
	 * @param scenarioName
	 * @return Map
	 */
    public Map<String,String> readExcelData(String path,String sheetName, String scenarioName)
{
    try{
        File testData=new File(path);
        FileInputStream testDataStream= new FileInputStream(testData);
        XSSFWorkbook testDataWorkBook = new XSSFWorkbook(testDataStream);
        XSSFSheet testDataSheet= testDataWorkBook.getSheet(sheetName);
        int noOfRows= testDataSheet.getPhysicalNumberOfRows();
        int desiredRowNumber=0;
        
        log.info(scenarioName);
        for(int i=0;i<noOfRows;i++)
        {
            if(testDataSheet.getRow(i).getCell(0).toString().equalsIgnoreCase(scenarioName)) {
                desiredRowNumber = i;
                break;
            }

        }
        if(desiredRowNumber==0)
        {
            log.info("The scenario is not found");
        }
        else
        {
            for(int i=0;i<testDataSheet.getRow(0).getPhysicalNumberOfCells(); i++)
            {
                testDataMap.put(testDataSheet.getRow(0).getCell(i).toString(),testDataSheet.getRow(desiredRowNumber).getCell(i).toString());
            }
        }
        testDataWorkBook.close();
        return testDataMap;

    }
    catch(Exception e)
    {
        log.info("Exception is : "+e);
        return null;
    }
}
		
    
    public float roundingOfftoTwo(String amount)
    {
    	try {
    		
    		float amountInFloat= Float.parseFloat(amount);
    		float roundedValueAmountInFloat=(Math.round(amountInFloat*100));
    		float actualRoundedValue = roundedValueAmountInFloat/100;
    		return actualRoundedValue;
    		
    	}
    	catch(Exception e)
    	{
    		Assert.fail("Exception Occured : "+e); 
    		return 0;
    	}
    	
    }
}
