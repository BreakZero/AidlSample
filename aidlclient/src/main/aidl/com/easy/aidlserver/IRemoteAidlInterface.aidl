// IRemoteAidlInterface.aidl
package com.easy.aidlserver;
import com.easy.aidlserver.User;

// Declare any non-default types here with import statements

interface IRemoteAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int currPid();
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void addUser(inout User user);
    User theFirstUser();
}