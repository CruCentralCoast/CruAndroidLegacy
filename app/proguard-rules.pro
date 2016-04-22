# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/jon/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Support Library

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-keep class android.support.v7.widget.RoundRectDrawable { *; }

# Saripaar

-keep class com.mobsandgeeks.saripaar.** {*;}
-keep @com.mobsandgeeks.saripaar.annotation.ValidateUsing class * {*;}

# ButterKnife 7

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Fabric.io

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# RetroFit

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Parcel library
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class org.parceler.Parceler$$Parcels

# Stetho

-keep class com.facebook.stetho.**{ *; }

# Picasso

-dontwarn com.squareup.picasso.**

# Okio

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# OkHttp

-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# retrolambda

-dontwarn java.lang.invoke.*

# ThreeTenBP

-dontwarn java.util.spi.LocaleServiceProvider
-dontwarn sun.util.calendar.*
-keep class org.threeten.bp.zone.*

-keepclassmembers class org.threeten.bp.** { *; }

# YouTube Data API

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

# Needed by Guava
# See https://groups.google.com/forum/#!topic/guava-discuss/YCZzeCiIVoI

-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue

# Needed by google-http-client-android when linking against an older platform version

-dontwarn com.google.api.client.extensions.android.**

# Needed by google-api-client-android when linking against an older platform version

-dontwarn com.google.api.client.googleapis.extensions.android.**
