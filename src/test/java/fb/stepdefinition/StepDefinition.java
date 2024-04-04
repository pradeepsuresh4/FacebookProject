package fb.stepdefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fb.runner.RunnerClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinition {

	WebDriver driver = RunnerClass.driver;

	@Given("User opens facebook on their device")
	public void user_opens_facebook_on_their_device() {
		// opens facebook url
		driver.get("https://www.fb.com");
		driver.manage().window().maximize();
	}

	@When("clicks on new account creation options")
	public void clicks_on_new_account_creation_options() {
		// clicks on new account creation option
		WebElement createAccount = driver.findElement(By.xpath("//*[text() ='Create new account']"));
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(createAccount));
		createAccount.click();
	}

	@Then("user enters the details rerquired for account creation")
	public void user_enters_the_details_rerquired_for_account_creation() throws IOException {
		// Fill up new user details
		FileInputStream fis = new FileInputStream("./TestData/TestData.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = sheet.getRow(1);
		int lastCell = row.getLastCellNum();
		String userData[] = new String[lastCell];
		DataFormatter dft = new DataFormatter();
		for (int i = 0; i < lastCell; i++) {

			XSSFCell cell = row.getCell(i);
			userData[i] = dft.formatCellValue(cell);
		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// FirstName
		WebElement firstName = driver.findElement(By.xpath("//*[@name='firstname']"));
		firstName.clear();
		firstName.sendKeys(userData[2]);

		// Surname
		WebElement lastName = driver.findElement(By.xpath("//*[@name='lastname']"));
		lastName.clear();
		lastName.sendKeys(userData[3]);

		// Mobile/Email
		WebElement mobileEmail = driver.findElement(By.xpath("//*[@name='reg_email__']"));
		mobileEmail.clear();
		mobileEmail.sendKeys(userData[4]);
		
		//reg_email_confirmation__
		WebElement mobileEmailConf = driver.findElement(By.xpath("//*[@name='reg_email_confirmation__']"));
		mobileEmailConf.clear();
		mobileEmailConf.sendKeys(userData[4]);
		
		//Password 
		WebElement password = driver.findElement(By.xpath("//*[@name='reg_passwd__']"));
		password.clear();
		password.sendKeys(userData[5]);

		// date
		WebElement date = driver.findElement(By.xpath("//*[@name='birthday_day']"));
		Select s = new Select(date);
		s.selectByValue(userData[6]);

		// month
		WebElement month = driver.findElement(By.xpath("//*[@name='birthday_month']"));
		Select s1 = new Select(month);
		s1.selectByVisibleText(userData[7]);

		// year
		WebElement year = driver.findElement(By.xpath("//*[@name='birthday_year']"));
		Select s2 = new Select(year);
		s2.selectByValue(userData[8]);

		// gender
		if (userData[9].equalsIgnoreCase("Male")) {
			WebElement male = driver.findElement(By.xpath("//label[text() = 'Male']"));
			male.click();
		} else {
			WebElement female = driver.findElement(By.xpath("//label[text() = 'Female']"));
			female.click();
		}
		workbook.close();
	}

	@Then("user clicks on create account icon")
	public void user_clicks_on_create_account_icon() throws IOException {
		// clicks on create account and take screenshot
		WebElement signUp = driver.findElement(By.xpath("//button[@name= 'websubmit']"));
		signUp.click();
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File des = new File("C:\\Users\\prade\\eclipse-workspace\\Facebook_Project\\Screenshots\\NewAccount.png");
		FileUtils.copyFile(src, des);
	}

}
