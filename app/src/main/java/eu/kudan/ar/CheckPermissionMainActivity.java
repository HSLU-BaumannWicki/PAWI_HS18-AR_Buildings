package eu.kudan.ar;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CheckPermissionMainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_TO_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_TO_ACCESS_CAMERA = 2;
    private static final int MY_PERMISSIONS_REQUEST_TO_ACCESS_STORAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_MULTIPLE_PERMISSIONS = 4;

    private Map<String, Integer> myPermissionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission_and_show_error);
        myPermissionsList = new HashMap<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.requestPermissions();
        this.startActivityIfAllChecked();
    }

    private void requestPermissions() {
        this.checkThisActivityPermissionFor(Manifest.permission.ACCESS_FINE_LOCATION, myPermissionsList,
                R.id.location_permission_checkbox);
        this.checkThisActivityPermissionFor(Manifest.permission.WRITE_EXTERNAL_STORAGE, myPermissionsList,
                R.id.storage_permission_checkbox);
        this.checkThisActivityPermissionFor(Manifest.permission.CAMERA, myPermissionsList,
                R.id.camera_permission_checkbox);
        if (this.myPermissionsList.size() > 0) {
            Set<String> keySet = this.myPermissionsList.keySet();
            String[] myPermissionsArray = keySet.toArray(new String[keySet.size()]);
            ActivityCompat.requestPermissions(this, myPermissionsArray, MY_PERMISSIONS_REQUEST_MULTIPLE_PERMISSIONS);
        }
    }

    private void checkThisActivityPermissionFor(String systemPermissionIdentifier, Map<String, Integer> permissions, int checkboxID){
        if (ContextCompat.checkSelfPermission(this,systemPermissionIdentifier) != PackageManager.PERMISSION_GRANTED) {
            this.unsetCheckedForCheckboxID(checkboxID);
            permissions.put(systemPermissionIdentifier, checkboxID);
            this.myPermissionsList.put(systemPermissionIdentifier, checkboxID);
        } else {
            this.setCheckedForCheckboxID(checkboxID);
            Log.d("Debug", "Permission " + systemPermissionIdentifier + " should already be granted");
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
            case MY_PERMISSIONS_REQUEST_MULTIPLE_PERMISSIONS:{
                for(int i=0; i<grantResults.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        this.setCheckedForCheckboxID(this.myPermissionsList.get(permissions[i]));
                        this.myPermissionsList.remove(permissions[i]);
                    } else {
                        this.unsetCheckedForCheckboxID(this.myPermissionsList.get(permissions[i]));
                    }
                }
            }
        }
        startActivityIfAllChecked();
    }

    private void startActivityIfAllChecked() {
        Log.i("Debug", "The Activity should now start");
        if(isChecked(R.id.camera_permission_checkbox) && isChecked(R.id.location_permission_checkbox) && isChecked(R.id.storage_permission_checkbox)){
            Intent startARActivityIntent = new Intent(this, NorthInitializer.class);
            startARActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(startARActivityIntent);
            ActivityCompat.finishAffinity(this);
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

    public void requestPermissionAgain(View element){
        this.requestPermissions();
    }
}
