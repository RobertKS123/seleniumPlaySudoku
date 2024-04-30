package Suduko;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import  org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ManageGrid{

    protected WebDriver driver;
    protected WebDriverWait wait;
    long timeout = 5;
    String url = "https://west.websudoku.com/?level=";

    protected ChromeDriver startChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setImplicitWaitTimeout(Duration.ofSeconds(1));
        //options.setExperimentalOption("devTools", true);
        return startChromeDriver(options);
    }

    protected ChromeDriver startChromeDriver(ChromeOptions options) {
        driver = new ChromeDriver(options);
        return (ChromeDriver) driver;
    }

    @BeforeEach
    public void start() {
        startChromeDriver();
    }

    @AfterEach
    public void stop() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }

    void solveSudoku() {

       // driver.findElement(By.xpath("//a[text()='Hide the advertisement below']")).click();

        WebElement gridTable = driver.findElement(By.id("puzzle_grid"));
        WebElement grid = gridTable.findElement(By.tagName("tbody"));

        List<List<Integer>> tableGrid = new ArrayList<>();

        List<WebElement> rows = grid.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            List<Integer> rowData = new ArrayList<>();
            List<WebElement> cells = row.findElements(By.tagName("td"));
            for (WebElement cell : cells) {
                WebElement input = cell.findElement(By.tagName("input"));
                String cellText = input.getAttribute("value");
                int cellValue = cellText.isEmpty() ? 0 : Integer.parseInt(cellText);
                rowData.add(cellValue);
            }
            tableGrid.add(rowData);
        }

        SudokuGrid sudokuGrid = new SudokuGrid(tableGrid);
        SudokuSolver.solveSudoku(sudokuGrid.grid, sudokuGrid.grid.length);

        for (int i = 0; i < sudokuGrid.grid.length; i++) {
            for (int j = 0; j < sudokuGrid.grid.length; j++) {
                WebElement currentCell = grid.findElement(By.id("c" + j +i));
                WebElement cellInput = currentCell.findElement(By.tagName("input"));
                if(!"readonly".equals(cellInput.getAttribute("readonly"))) {
                    cellInput.sendKeys(sudokuGrid.cellValue(i,j));
                }
            }
        }

        driver.findElement(By.xpath("//input[@value='How am I doing?']")).click();
        String solutionString = driver.findElement(By.xpath("//b[text()='Congratulations! You solved this Sudoku!']")).getText();

        Assertions.assertEquals("Congratulations! You solved this Sudoku!", solutionString);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "4"})
    void doSudoku(String difficulty) throws InterruptedException {
        driver.get(url + difficulty);
        Actions action = new Actions(driver);
        action.sendKeys(Keys.F5).perform();
        solveSudoku();
    }

}
