aShot
=====


[![release](http://github-release-version.herokuapp.com/github/yandex-qatools/ashot/release.svg?style=flat)](https://github.com/yandex-qatools/ashot/releases/latest) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.yandex.qatools.ashot/ashot/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/ru.yandex.qatools.ashot/ashot)


WebDriver Screenshot utility

* Takes screenshot of web element from different device types
* Prettifying screenshot images
* Provides configurable screenshot comparison

#####WebElement view

The objective of taking web element looks simply and consists of three goals:
* Take screenshot of the page
* Find element's size and position   
* Crop origin screenshot image

As result aShot provides image with WebElement
![images snippet](/doc/img/images_intent_blur.png)

#### Maven dependency
```xml
<dependency>
    <groupId>ru.yandex.qatools.ashot</groupId>
    <artifactId>ashot</artifactId>
<<<<<<< HEAD
    <version>1.4.3</version>
=======
    <version>1.3</version>
>>>>>>> 2da554d55c8f671cc3011a3c5587f9b24641de00
</dependency>
``` 

#####Taking screenshot of page

Different webDrivers have a different behaviour with screenshot taking. Some of them provides screenshot of whole page or viewport only. AShot cam be configured according to exact behaviour. This example configuration allows to take screenshot of whole page when driver provides viewport image only, for example, Chrome, Mobile Safari and etc. 
```java
new AShot()
  .shootingStrategy(new ViewportPastingStrategy())
  .takeScreenshot(webDriver);
```

#####Taking screenshot of web element

To take screenshot of element we just need to specify our element or collection of web elements.
 ```java
 WebElement myWebElement = webDriver.findElement(By.cssSelector("#my_element"));
 new AShot()
   .takeScreenshot(webDriver, myWebElement);
 ```
 
 In this case aShot will find element's location and position and will crop origin image. WebDriver API gives an opportunity to find coordinates of web element, but also different WebDriver implementations provides differently. In my opinion the most universal way to find coordinates is to use jQuery for it. AShot uses jQuery by default. But some drivers have a problems with Javascript execution such as Opera. In this case it's better to use another way to find web element coordinates.
  ```java
   new AShot()
     .coordsProvider(new WebDriverCoordsProvider()) //find coordinated using WebDriver API
     .takeScreenshot(webDriver, myWebElement);
   ```
 Of course, if you don't like to use jQuery, you can implement you own CoordsProvider and contribute this project.
 
#####Prettifying web element screenshot

So, let's take a simple screenshot of weather snippet at Yandex.com.

 ```java
 new AShot()
   .takeScreenshot(webDriver, yandexWeatherElement);
 ```
 AShot cropped origin images and we can see this result.
 ![simple weather snippet](/doc/img/def_crop.png)
 
 In default case DefaultCropper is using. But there is a way to use another cropper
 
 ```java
  new AShot()
    .withCropper(new IndentCropper() //overwriting cropper
                    .addIndentFilter(blur())) //adding filter for indent
    .takeScreenshot(driver, yandexWeatherElement);
  ```
  
  ![indent blur weather snippet](/doc/img/weather_indent_blur.png)
  This screenshot provides more information about element's position relatively his siblings and blurs indent to focus view on web element.
  
  
#####Comparison of screenshots
As you noticed, the ```.takeScreenshot()``` returns a Screenshot object, containing screenshot image and data for comparison.

```java
  Screenshot myScreenshot = new AShot()
    .addIgnoredElement(By.cssSelector(".#weather .blinking_element")) //ignored element
    .takeScreenshot(driver, yandexWeatherElement);
```

To get a diff between two images use ImageDiffer:

```java
  ImageDiff diff = new ImageDiffer().makeDiff(myScreenshot, anotherScreenshot);
  BufferedImage diffImage = diff.getMarkedImage(); //megred image with marked diff areas
```

#####Several elements comparison
`(since 1.2)`  
Sometimes we need to take screenshot of several independent elements. In this case AShot constructs complex comparison area.
```java
    List<WebElement> elements = webDriver.findElements(By.cssSelector("#my_element, #myPopupElement"));
    new AShot()
        .withCropper(new IndentCropper() //overwriting cropper
                        .addIndentFilter(blur())) //adding filter for indent
        .takeScreenshot(webDriver, elements);
 ```
As result we receive screenshot like this
![complex comparison area](/doc/img/complex_elements.png)
As you see at this picture, only selected elements(header and popup) are not filtered and only not filtered areas will be compared.

 




