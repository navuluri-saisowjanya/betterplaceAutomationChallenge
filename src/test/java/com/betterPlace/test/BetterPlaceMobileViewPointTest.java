package com.betterPlace.test;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.pack.base.CommonMethods;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class BetterPlaceMobileViewPointTest {
	public WebDriver driver;
	CommonMethods commonMethodsObj = new CommonMethods();
	public Map<String, String> testDataMap = new HashMap<String, String>();
	String paymentMethod = "", totalDonationAmountString = "";
	float totalDonationAmount = 0, actualDonationAmountForBetterPlace = 0, actualDonationAmount = 0;
	By acceptCookies = By.xpath("//button[@class='btn btn-primary btn-large flex-grow mb-3']");
	By amountTextBox = By.xpath("//input[@name='amount_cents']");
	By paymentMethodSelection = By.xpath("//label[@for='direct_deposit']/span");
	By firstNameTextBox = By.xpath("//input[@id='first_name']");
	By lastNameTextBox = By.xpath("//input[@id='last_name']");
	By emailIdTextBox = By.xpath("//input[@id='email']");
	By commentTextBox = By.xpath("//textarea[@id='message']");
	By submitButtonElement = By.xpath("//button[@type='submit']");
	By paymentReceiptPage = By.xpath("//div[@class='direct-deposit-receipt']/h2");
	By donationPrepared = By.xpath("//div[@class='direct-deposit-receipt']/div");
	By donationForBetterPlace = By.xpath("//button[@class='simulated-link text-base whitespace-normal text-left']");
	By donationAmountForBetterPlace = By.xpath("//div[@class='input-group']/input");
	By donationAmountForBetterPlaceSubmitButton = By.xpath("//button[@class='btn btn-primary']");

	@BeforeMethod
	@Parameters("url")
	public void setUp(String url) {
		try {
			// setting up Chrome Driver for Mobile emulation
			commonMethodsObj.log.info("Driver SetUp execution started");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			Map<String, String> mobileEmulation = new HashMap<>();
			mobileEmulation.put("deviceName", "Galaxy S5");
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
			driver = new ChromeDriver(chromeOptions);
			driver.manage().window().maximize();

			// Launching respective URLs
			driver.get(url);

			// Accepting Cookies
			WebDriverWait urlLoadingwait = new WebDriverWait(driver, Duration.ofSeconds(3));
			urlLoadingwait.until(ExpectedConditions.presenceOfElementLocated(acceptCookies));
			driver.findElement(acceptCookies).click();
			commonMethodsObj.log.info("The Driver is initialized");
		} catch (Exception e) {
			Assert.fail("Exception Occured : " + e);
		}
	}

	@Test
	public void testDonationFormSubmission() {
		try {
			// taking testData
			commonMethodsObj.log.info("Test 1 Execution started ");
			testDataMap = commonMethodsObj.readExcelData("src/test/resources/TestData/TestDataSheet.xlsx", "Sheet1",
					"Preparing payment with payment method Überweisung");
			commonMethodsObj.log.info("test Data :" + testDataMap);
			
			// Providing the data for donation form
			WebElement amountTextBoxelement = driver.findElement(amountTextBox);
			String defaultAmount = amountTextBoxelement.getAttribute("value");
			commonMethodsObj.log.info("Amount present default :" + defaultAmount);
			for (int i = 0; i < defaultAmount.length(); i++)
				amountTextBoxelement.sendKeys(Keys.BACK_SPACE);
			Thread.sleep(1000);
			amountTextBoxelement.sendKeys(testDataMap.get("donationamount"));
			Thread.sleep(2000);

			// selecting the payment method based on input test data
			WebElement paymentMethodSelectionElement = driver.findElement(paymentMethodSelection);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", paymentMethodSelectionElement);

			// taking screenshot of payment methods
			TakesScreenshot scrshot = ((TakesScreenshot) driver);
			File SrcFile = scrshot.getScreenshotAs(OutputType.FILE);
			Thread.sleep(2000);
			paymentMethodSelectionElement.click();
			driver.findElement(firstNameTextBox).sendKeys(testDataMap.get("firstName"));
			driver.findElement(lastNameTextBox).sendKeys(testDataMap.get("lastName"));
			driver.findElement(emailIdTextBox).sendKeys(testDataMap.get("mailId"));
			driver.findElement(commentTextBox).sendKeys(testDataMap.get("comments"));
			commonMethodsObj.log.info("The donation form is filled with mandatory details..");

			// Clicking on the link Better place Donation link
			WebElement donationForBetterPlaceLink = driver.findElement(donationForBetterPlace);
			JavascriptExecutor jseDonationForBetterPlaceLink = (JavascriptExecutor) driver;
			jseDonationForBetterPlaceLink.executeScript("arguments[0].scrollIntoView(true);",
					donationForBetterPlaceLink);
			Thread.sleep(1000);
			donationForBetterPlaceLink.click();

			// Providing Data in Better place donation form
			WebElement donationAmountForBetterPlaceElement = driver.findElement(donationAmountForBetterPlace);
			String defaultdonationBetterPlaceAmount = donationAmountForBetterPlaceElement.getAttribute("value");
			commonMethodsObj.log
					.info(" Donation for better place Amount present default :" + defaultdonationBetterPlaceAmount);
			for (int i = 0; i < defaultdonationBetterPlaceAmount.length(); i++)
				donationAmountForBetterPlaceElement.sendKeys(Keys.BACK_SPACE);
			Thread.sleep(1000);
			donationAmountForBetterPlaceElement.sendKeys(testDataMap.get("donationamountforbetterplace"));
			driver.findElement(donationAmountForBetterPlaceSubmitButton).click();
			Thread.sleep(1000);

			// Submitting the donation form
			WebElement submitButton = driver.findElement(submitButtonElement);
			JavascriptExecutor jse1 = (JavascriptExecutor) driver;
			jse1.executeScript("arguments[0].scrollIntoView(true);", submitButton);
			Thread.sleep(1000);
			submitButton.click();
			commonMethodsObj.log.info("The donation form is submitted");

			// Verifying the donation form submission
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.presenceOfElementLocated(paymentReceiptPage));
			Assert.assertTrue(driver.findElement(paymentReceiptPage).getText().contains("Mein Überweisungsauftrag"),
					"The payment receipt page is displayed");

			// Rounding off the Better place donation amount
			actualDonationAmountForBetterPlace = commonMethodsObj
					.roundingOfftoTwo(testDataMap.get("donationamountforbetterplace"));

			// Rounding off the generic donation amount
			actualDonationAmount = commonMethodsObj.roundingOfftoTwo(testDataMap.get("donationamount"));

			// Formatting the total amount as per the display on screen
			totalDonationAmount = actualDonationAmountForBetterPlace + actualDonationAmount;
			commonMethodsObj.log.info("The total donation Amount :" + totalDonationAmount);
			totalDonationAmountString = String.valueOf(totalDonationAmount);
			if (totalDonationAmountString.contains("."))
				totalDonationAmountString = totalDonationAmountString.replace(".", ",");
			else
				totalDonationAmountString = totalDonationAmountString + ",00";
			commonMethodsObj.log.info("The total amount donated" + totalDonationAmountString);
			commonMethodsObj.log
					.info("The total amount donated in screen " + driver.findElement(donationPrepared).getText());

			// Verifying the total donation amount
			Assert.assertTrue(
					(driver.findElement(donationPrepared).getText().contains(totalDonationAmountString))
							&& (driver.findElement(donationPrepared).getText().contains("€")),
					"The value entered doesnt match with the value retrieved");
			commonMethodsObj.log.info("The donation receipt is displayed with accurate details..");

			// Copy file at destination
			File DestFile = new File("screenshot.png");
			FileUtils.copyFile(SrcFile, DestFile);

			// Validating the payment methods text displayed in the webpage
			ITesseract instance = new Tesseract();
			String path = System.getProperty("user.dir");
			instance.setDatapath(path + "/src/test/resources/tessdata");
			instance.setLanguage("deu");
			String imgText = instance.doOCR(SrcFile);
			commonMethodsObj.log.info("Payment Labels :" + imgText);
			Assert.assertTrue(imgText.contains("Überweisung"),
					"The payment label Überweisung is not displayed correctly ");

		} catch (Exception e) {
			Assert.fail("Exception Occured : " + e);
		}

	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
		commonMethodsObj.log.info("Driver Closed");
	}

}
