package com.fbu.kip.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

 public abstract class PhotoActivity extends AppCompatActivity {

  public final String APP_TAG = "KIP";
  public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
  public String photoFileName = "photo.jpg";
  File photoFile;

  public void launchCamera() {
    // create Intent to take a picture and return control to the calling application
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Create a File reference for future access
    photoFile = getTakenPhotoFileUri(photoFileName);

    // wrap File object into a content provider
    // required for API >= 24
    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
    Uri fileProvider = FileProvider.getUriForFile(PhotoActivity.this, "com.KIP.fileprovider", photoFile);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
    // So as long as the result is not null, it's safe to use the intent.
    if (intent.resolveActivity(getPackageManager()) != null) {
      // Start the image capture intent to take photo
      startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
  }

  // Returns the File for a photo stored on disk given the fileName
  public File getTakenPhotoFileUri(String fileName) {
    // Get safe storage directory for photos
    // Use `getExternalFilesDir` on Context to access package-specific directories.
    // This way, we don't need to request external read/write runtime permissions.
    File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
      Log.d(APP_TAG, "failed to create directory");
    }

    // Return the file target for the photo based on filename
    File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

    return file;
  }

   public Bitmap rotateBitmapOrientation(String photoFilePath) {
     // Create and configure BitmapFactory
     BitmapFactory.Options bounds = new BitmapFactory.Options();
     bounds.inJustDecodeBounds = true;
     BitmapFactory.decodeFile(photoFilePath, bounds);
     BitmapFactory.Options opts = new BitmapFactory.Options();
     Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);

     // Read EXIF Data
     ExifInterface exif = null;
     try {
       exif = new ExifInterface(photoFilePath);
     } catch (IOException e) {
       e.printStackTrace();
     }
     String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
     int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
     int rotationAngle = 0;
     if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
     if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
     if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

     // Rotate Bitmap
     Matrix matrix = new Matrix();
     matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
     Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

     // Return result
     return rotatedBitmap;
   }

  // PICK_PHOTO_CODE is a constant integer
  public final static int PICK_PHOTO_CODE = 1046;

  // Trigger gallery selection for a photo
  public void pickPhoto() {
    // Create intent for picking a photo from the gallery
    Intent intent = new Intent(Intent.ACTION_PICK,
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
    // So as long as the result is not null, it's safe to use the intent.
    if (intent.resolveActivity(getPackageManager()) != null) {
      // Bring up gallery to select a photo
      startActivityForResult(intent, PICK_PHOTO_CODE);
    }
  }

  public Bitmap loadChosenPhotoFromUri(Uri photoUri) {
    Bitmap image = null;
    try {
      // check version of Android on device
      if(Build.VERSION.SDK_INT > 27){
        // on newer versions of Android, use the new decodeBitmap method
        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
        image = ImageDecoder.decodeBitmap(source);
      } else {
        // support older versions of Android by using getBitmap
        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
      }
    } catch (IOException e) {
      Log.e(APP_TAG, "loadFromUri: ", e);
      e.printStackTrace();
    }
    return image;
  }

  public File bitmapToFile(Bitmap bitmap) {
    File file = new File(this.getCacheDir(), photoFileName);
    try {
      file.createNewFile();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, byteArrayOutputStream);
      byte[] bitmapData = byteArrayOutputStream.toByteArray();

      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(bitmapData);
      fileOutputStream.flush();
      fileOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }
}
