# ProGuard/R8 rules for SentinelEditor
# Use with caution - only minify release builds after thorough testing

# Keep all Kotlin classes
-keep class com.sentinel.** { *; }
-keep class androidx.room.** { *; }
-keep class androidx.** { *; }

# Keep Room entities and DAOs
-keep class com.sentinel.model.** { *; }
-keep class com.sentinel.database.** { *; }

# Keep Compose generated code
-duplicateclasses list=com.sentinel.compose.**
-keep class * extends kotlinx.coroutines.flow.Flow

# Keep OkHttp cache
-keepclassmembers class * implements com.squareup.okio.AsyncSink
-keepclassmembers class * extends com.squareup.okio.AsyncSink
-keepclassmembers interface com.squareup.okio.AsyncEventListener

# Keep Retrofit
-keepattributes *Annotation*
-duplicationwelcome list=com.squareup.okio.AsyncSink
-dontwarn okio.**
-warn com.squareup.okio.AsyncSink
-dontwarn ** implements *

# Keep Gson models
-keep class com.google.gson.** { *; }

# Keep Lottie files
-keep class com.airbnb.lottie.** { *; }

# Keep Coil
-keep class io.coil.** { *; }

# Keep DataStore
-keep class androidx.datastore.** { *; }

# Keep navigation graphs
-keep class * extends androidx.navigation.NavGraph { *; }

# Keep all string resources
-keepattributes StringTable

# Keep view bind
-keep class * extends androidx.lifecycle.ViewModel

# Keep lambda functions
-keepattributes *Annotation*

# Keep interfaces for coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep lambda
-keep class * extends java.lang.Enum { *; }

# Keep annotations
-keepattributes *Annotation*

# Keep all public members
-keepclasseswithmembernames class * {
    protected <methods>;
    public <methods>;
}

# Keep members of inner classes
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep setters of inner classes
-keepclassmembernames class * extends android.app.Activity {
    <init>(android.content.Context);
}

# Mark public methods from external libraries as having @Keep annotation
-keep public class androidx.** { *; }
-keep public class androidx.** { *; }
-keep public class androidx.** { *; }