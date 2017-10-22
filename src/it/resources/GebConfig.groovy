import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService

driver = {
  new ChromeDriver(new ChromeDriverService.Builder().usingAnyFreePort().usingDriverExecutable(new File('./target/drivers/chromedriver-mac-64bit')).build())
}
baseUrl = 'http://localhost:8080/xkcd/'
reportsDir = './target/geb-reports'
