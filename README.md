# AndroidParser
find classes with java reflect mechanism
## dependency
* add repository

        maven {
            url "http://112.74.29.196:34272/repository/maven-public/"
        }
* add it to your dependencies

        implementation "com.github.seelikes.android:parser:1.0.1"

## sample

        List<Class<? extends MainEntry>> entries = AndroidParser.getClassExtends(this, MainEntry.class, getPackageName());
