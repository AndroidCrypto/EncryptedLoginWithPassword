# Encrypted login with password

This concept study shows how to make an app login with a password.

The password related data got stored in the app directory with this workflow:

1) is an app password already stored, if yes proceed to 8) **Login activity**.

2) if an app password is NOT stored (e.g. first start of the app or after a password reset)  
the app runs the **Settings activity**. The only option at that stage is to enter a new 
password.

3) the entered app password is forwarded to the **EncryptedSharedPreferences** class using the  
'androidx.security:security-crypto:1.0.0' import. The data is secured with the "AES256_GCM" 
encryption scheme and for the keys the "AES256_SIC" scheme is in use.

4) the app password is forwarded to the **EncryptionNewAppPasswordUtil** class that runs in a 
background thread to avoid blocking the UI.

5) the app password runs through a "password based key derivation function" that is named 
"PBKDF2" with these parameters:

- number of iterations: 10000 (more is better but slower, esp. on older devices) 
- hash algorithm: HmacSha256
- salt length: 64 random bytes
- output key length: 64 byte

6) The generated key and salt is stored in Base64 encoding in the EncryptedSharedPreferences class.  

7) now your regular app workflow can start ...

If the app starts detect an previously entered app password the workflow starts after 1)  

8) The app starts the **Login activity** where to enter the app password for verification

9) the entered app password is forwarded to the **EncryptedSharedPreferences** class

10) the app password is forwarded to the **EncryptionVerifyAppPasswordUtil** class that runs in a 
background thread to avoid blocking the UI. The storedAppPassword and storedAppSalt are read from 
storage and are forwarded as well.

11) the app password runs through a "password based key derivation function" that is named 
"PBKDF2" with the previously stored salt. The result gets compared to the stored password and   
if both values are equal the function return a positive verification result ("true"), otherwise 
it return a negative one ("false").

12) depending on your needs your app can start the regular workflow if the verification result is  
positive or ask the user to re-enter the password if the result is negative. 

Just for clarification: **the app never stores the password but only the PBKDF2 hashed password**.

Additional features in **Settings activity**:

There are two more buttons on the Settings screen:

- change password: enter the old and the new password and if the old password gets verified AND the  
password length of the new password is saved.

- reset password: the stored password hash and salt is deleted.
