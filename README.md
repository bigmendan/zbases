# zbases
平时开发的封装
首先在 项目路径下的build.gradle 中的 

allprojects {

    repositories {

         google()

         jcenter()

         maven { url 'https://jitpack.io' }

    }

}


在 app 下的build.gradle 中的 

dependencies{

  implementation 'com.github.zhangmazi7357:zbases:v1.1'

}

