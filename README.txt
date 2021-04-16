3RD party sources:

as database in this project we've chosen to use firebase for which we don't need any extra code.
We just need to be sure that all the dependecies in the build gradle are up to date. 
This is a list of dependecies of 3rd party java sources that we've used:

dependencies {
    implementation platform('com.google.firebase:firebase-bom:26.7.0')
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.3.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'androidx.navigation:navigation-fragment:2.3.4'
    implementation 'androidx.navigation:navigation-ui:2.3.4'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.3.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0'
    implementation 'androidx.annotation:annotation:1.1.0'
    implementation 'com.google.firebase:firebase-firestore:22.1.1'
    implementation 'com.google.firebase:firebase-auth:20.0.3'
    implementation 'com.google.firebase:firebase-core:18.0.2'
    implementation 'com.google.firebase:firebase-database'
    implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'
    implementation 'androidx.recyclerview:recyclerview-selection:1.1.0'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'com.google.firebase:firebase-database:19.7.0'
    implementation 'com.firebaseui:firebase-ui-database:7.1.1'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.google.firebase:firebase-storage'
}