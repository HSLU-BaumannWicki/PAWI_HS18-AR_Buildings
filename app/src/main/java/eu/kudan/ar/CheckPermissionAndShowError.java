package eu.kudan.ar;

import android.Manifest;
import android.app.LauncherActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

public class CheckPermissionAndShowError extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_TO_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_TO_ACCESS_CAMERA = 2;
    private static final int MY_PERMISSIONS_REQUEST_TO_ACCESS_STORAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission_and_show_error);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.checkThisActivityPermisionFor(Manifest.permission.ACCESS_FINE_LOCATION,
                MY_PERMISSIONS_REQUEST_TO_ACCESS_FINE_LOCATION, R.id.location_permission_checkbox);
        this.checkThisActivityPermisionFor(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                MY_PERMISSIONS_REQUEST_TO_ACCESS_STORAGE, R.id.storage_permission_checkbox);
        this.checkThisActivityPermisionFor(Manifest.permission.CAMERA,
                MY_PERMISSIONS_REQUEST_TO_ACCESS_CAMERA, R.id.camera_permission_checkbox);
        this.startActivityIfAllChecked();

    }

    private void checkThisActivityPermisionFor(String systemPermissionIdentifier, int myPermissionID, int checkboxID){
        if (ContextCompat.checkSelfPermission(this,systemPermissionIdentifier) != PackageManager.PERMISSION_GRANTED) {
            this.unsetCheckedForCheckboxID(checkboxID);
            ActivityCompat.requestPermissions(this, new String[]{systemPermissionIdentifier}, myPermissionID);
        } else {
            this.setCheckedForCheckboxID(R.id.location_permission_checkbox);
            Log.d("Debug", "Permission should already be granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_TO_ACCESS_FINE_LOCATION: {
                this.setCheckedIfPermissionWasGranted(R.id.location_permission_checkbox, grantResults);
            }
            case MY_PERMISSIONS_REQUEST_TO_ACCESS_CAMERA: {
                this.setCheckedIfPermissionWasGranted(R.id.camera_permission_checkbox, grantResults);
            }
            case MY_PERMISSIONS_REQUEST_TO_ACCESS_STORAGE: {
                this.setCheckedIfPermissionWasGranted(R.id.storage_permission_checkbox, grantResults);
            }
        }
        startActivityIfAllChecked();
    }

    private void startActivityIfAllChecked() {
        if(isChecked(R.id.camera_permission_checkbox) && isChecked(R.id.location_permission_checkbox) && isChecked(R.id.storage_permission_checkbox)){
            Intent startARActivityIntent = new Intent(this, ARMainActivity.class);
            this.startActivity(startARActivityIntent);
        }
    }

    private void setCheckedIfPermissionWasGranted(int checkboxID, int[] grantResults){
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.setCheckedForCheckboxID(checkboxID);
        } else {
            this.unsetCheckedForCheckboxID(checkboxID);
        }
    }

    private void setCheckedForCheckboxID(int checkboxID){
        CheckBox checkbox = findViewById(checkboxID);
        checkbox.setChecked(true);
    }

    private void unsetCheckedForCheckboxID(int checkboxID){
        CheckBox checkbox = findViewById(checkboxID);
        checkbox.setChecked(false);
    }

    private boolean isChecked(int checkboxID){
        CheckBox checkbox = findViewById(checkboxID);
        return checkbox.isChecked();
    }
}

