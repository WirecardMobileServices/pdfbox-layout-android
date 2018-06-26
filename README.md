[![Release](https://jitpack.io/v/ralfstuckert/pdfbox-layout.svg)](https://jitpack.io/#ralfstuckert/pdfbox-layout)
[![MIT licensed](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE)

# pdfbox-layout-android
Android port of 2 libraries:

* [pdfbox-layout](https://github.com/ralfstuckert/pdfbox-layout)
* [easytable](https://github.com/vandeseer/easytable)

Which are build on top of [PdfBox-Android](https://github.com/TomRoush/PdfBox-Android) instead of [pdfbox](https://github.com/apache/pdfbox). 
#### Maven:

```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ...
    <dependency>
        <groupId>com.github.WirecardMobileServices</groupId>
        <artifactId>pdfbox-layout-android</artifactId>
        <version>1.0</version>
    </dependency>
```

#### Gradle:

```groovy
   repositories { 
        jcenter()
        maven { url "https://jitpack.io" }
   }
   dependencies {
         compile 'com.github.WirecardMobileServices:pdfbox-layout-android:1.0'
   }
```
