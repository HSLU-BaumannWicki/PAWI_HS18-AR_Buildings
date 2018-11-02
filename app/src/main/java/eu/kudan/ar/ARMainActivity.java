package eu.kudan.ar;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import eu.kudan.ar.ar.ARBuildingsPositioner;
import eu.kudan.ar.di.ProjectInitializer;
import eu.kudan.kudan.ARActivity;

public class ARMainActivity extends ARActivity implements SeekBar.OnSeekBarChangeListener {
    private ARBuildingsPositioner arBuilding;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arBuilding = ProjectInitializer.initGPSSingleBuildingSolution(this);

        // Comment this out for the time being unless you plan to create UI elements
        setContentView(R.layout.ar_main_activity);
        this.seekBar = findViewById(R.id.seekBar2);
        this.seekBar.setOnSeekBarChangeListener(this);
        this.setProgressText();
     }

    @Override
    public void setup()
    {
        super.setup();

    }

    @Override
    public void onPause(){
        super.onPause();
        this.arBuilding.stopPositioning();
    }

    @Override
    public void onResume(){
        super.onResume();
        this.arBuilding.startPositioning();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void onButtonRightClicked(View element){
        SeekBar mySlider = findViewById(R.id.seekBar2);
        float value = mySlider.getProgress()+1;
        this.arBuilding.rotateAllByDeg(-value);
    }
    public void onButtonLeftClicked(View element){
        SeekBar mySlider = findViewById(R.id.seekBar2);
        float value = mySlider.getProgress()+1;
        this.arBuilding.rotateAllByDeg(value);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.setProgressText();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void setProgressText(){
        TextView tv = findViewById(R.id.progressText);
        tv.setText((this.seekBar.getProgress()+1)+"");
    }
}

