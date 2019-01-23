
# RoundedButton 


<img src="/pic/logo.png" width = 20%> 

[![](https://jitpack.io/v/sabujak-sabujak/RoundedButton.svg)](https://jitpack.io/#sabujak-sabujak/RoundedButton)

Create round buttons quickly and easily.

## What's New in 0.1.1? :tada:
- [Release] Release Library!

## How to Use

### Gradle
```groovy
    repositories {
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        implementation 'com.github.sabujak-sabujak:RoundedButton:v0.1.1'
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
 <img src="/pic/sample.gif" width = 50%>

# Contribute
We welcome any contributions.

# To do
- Add gradient direction
- Shadow
- Support AndroidX

# Inspired by
 * [balysv/material-ripple](https://github.com/balysv/material-ripple), I was inspired by his code.

# License

    Copyright 2019 sabujak-sabujak

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
