# AndroidParser
find classes with java reflect mechanism
## dependency
* add repository

        maven {
            url "http://112.74.29.196:34272/repository/maven-public/"
        }
* add it to your dependencies

        implementation "com.github.seelikes.android:parser:1.0.2"

* you can also add dex as dependency if you want to build your own parser other than the provided ones

        implementation "com.github.seelikes.android:dex:1.0.0"

* it is very important to call instantRun(true) if you enable instant Run

        DexUtils.with(this).basePackage(getPackageName().substring(0, getPackageName().lastIndexOf("."))).instantRun(BuildConfig.DEBUG)

## sample

        List<Class<? extends MainEntry>> entries = AndroidParser.getClassExtends(DexUtils.with(this).basePackage(getPackageName().substring(0, getPackageName().lastIndexOf("."))).instantRun(BuildConfig.DEBUG), MainEntry.class);

## custom your own dex parse logic

        you can custom your own parse logic by call DexUtils directly

        DexUtils.with(this)
            .basePackage(getPackageName().substring(0, getPackageName().lastIndexOf(".")))
            .instantRun(BuildConfig.DEBUG)
            .getClass(classObject -> {
                if (classObject == null) {
                    return;
                }
                if (superClass.isAssignableFrom(classObject) && classObject != superClass && (classObject.getModifiers() & Modifier.ABSTRACT) == 0) {
                    list.add((Class<? extends T>) classObject);
                }
            });
