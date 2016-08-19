# FilteredIntent
[![Release](https://img.shields.io/badge/jcenter-1.0.0-blue.svg)](https://bintray.com/mcsong/maven/filteredintent)

## Overview
This library helps to select apps when you want to share text or file to other apps. 

## Features
- You can add filters to select apps.
- A filter is app name and package name.
- You can show choosing dialog more easy.

## Gradle & Maven
Gradle
```groovy
    dependencies {
    	compile 'net.sjava:filteredintent:1.0.2'
    }
```

Maven
```xml
    <dependency>
     <groupId>net.sjava</groupId>
     <artifactId>filteredintent</artifactId>
     <version>1.0.2</version>
    </dependency>
```

Ivy
```
<dependency org='net.sjava' name='filteredintent' rev='1.0.2'> 
    <artifact name='$AID' ext='pom'></artifact> 
</dependency>
```

##Screenshots

<img src="https://raw.githubusercontent.com/mcsong/FilteredIntent/master/screenshots/Screenshot_20160630-170244.png"  width="320" height="540" />&nbsp;&nbsp;<img src="https://raw.githubusercontent.com/mcsong/FilteredIntent/master/screenshots/Screenshot_20160630-170254.png"  width="320" height="540" /> 

## Usage
### Example 1
- Show sns apps for sharing "text/plain" contents.

``` java
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share intent subject");
    shareIntent.putExtra(Intent.EXTRA_TEXT, "share intent value");
    shareIntent.setType("text/plain");

    // for testing 
    //String[] filters = new String[]{"1222222222222222222222"};

    // real 
    String[] filters = new String[]{"twitter", "facebook", "kakao.talk", "com.facebook.orca", "com.tencent.mm"};

    FilteredIntent filteredIntent = FilteredIntent.newInstance(MainActivity.this, shareIntent);
    filteredIntent.startIntent("Share sns", filters);
```

### Example 2
- Show only cloud apps for sharing image file.

``` java
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.setType("image/*");
    // You have to add image file to shareIntent
    
    String[] filters = new String[]{"dropbox", "com.microsoft.skydrive", "com.google.android.apps.docs", "com.box.android", "com.amazon.drive"};
    FilteredIntent filteredIntent = FilteredIntent.newInstance(MainActivity.this, shareIntent);
    filteredIntent.startIntent("Share file to clouds", filters);
```


## License

Copyright 2016 Justin Song

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.