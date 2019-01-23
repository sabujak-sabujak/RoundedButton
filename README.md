
# RoundedButton 


<img src="/pic/logo.png" width = 20%> 

[![](https://jitpack.io/v/sangcomz/RoundedButton.svg)](https://jitpack.io/#sangcomz/RoundedButton)

Chameleon deals with the Status of RecyclerView.

## What's New in 0.1.0? :tada:
- [Release] Release Library!

## How to Use

### Gradle
```groovy
    repositories {
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        compile 'com.github.sangcomz:RoundedButton:v0.1.0'
    }
```
### Usage
```xml
    <life.sabujak.roundedbutton.RoundedButton
        android:id="@+id/roundedButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="24dp"
        android:gravity="center"
        android:padding="24dp"
        android:textSize="20dp"
        android:text="Button"
        android:textColor="#ffffff"
        app:buttonGradientStartColor="@color/colorPrimary"
        app:buttonGradientEndColor="@color/colorAccent"
        app:buttonCornerRadius="20dp"
         />
```

#### attribute

|      Attribute Name        | Description                               |    Default Value    |
|:--------------------------:|-------------------------------------------|:-------------------:|
|        buttonColor         | button background color                   |       #FF5252       |
|   buttonGradientStartColor | button background start gradient color    |         -1          |
|   buttonGradientEndColor   | button background end gradient color      |         -1          |
|       buttonCornerRadius   | button corner radius                      |          0          |
|        buttonRippleColor   | button click ripple color                 |       #ffffff       |
|      buttonRippleAlpha     | button ripple start alpha value           |         100         |



## Result Screen

| Project Name | Result Screen   |
|:---------:|---|
| Sample  <p style="float:left;"> <a href="https://play.google.com/store/apps/details?id=life.sabujak.roundedbuttonsample"> <img HEIGHT="40" WIDTH="135" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" /></a></p> |  <img src="/pic/sample.gif"> |

# Contribute
We welcome any contributions.

# To do
- Add gradient direction
- shadow

# Inspired by
 * [balysv/material-ripple](https://github.com/balysv/material-ripple), I was inspired by his code.

# License

    Copyright 2019 Jeong Seok-Won

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
