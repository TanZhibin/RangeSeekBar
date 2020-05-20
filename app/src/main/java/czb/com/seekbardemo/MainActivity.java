package czb.com.seekbardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import czb.com.seekbarlib.RangeSeekBar;

public class MainActivity extends AppCompatActivity {
    private TextView tv_progress1;

    private TextView tv_progress2;

    private RangeSeekBar bar1;

    private RangeSeekBar bar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_progress1 = findViewById(R.id.tv_progress1);
        tv_progress2 = findViewById(R.id.tv_progress2);
        bar1=findViewById(R.id.rangeSeekBar1);
        bar2=findViewById(R.id.rangeSeekBar2);

        bar1.setOnProgressListener(new RangeSeekBar.OnProgressListener() {
            @Override
            public void onProgress(int progress, float percent) {
                int p = (int) (percent * 100);
                tv_progress1.setText(p + "");
            }

            @Override
            public void onStopTrackingTouch(int progress, float percent) {

            }

            @Override
            public void onOrigin() {

            }
        });

        bar2.setOnProgressListener(new RangeSeekBar.OnProgressListener() {
            @Override
            public void onProgress(int progress, float percent) {
                int p = (int) (percent * 100);
                tv_progress2.setText(p + "");
            }

            @Override
            public void onStopTrackingTouch(int progress, float percent) {

            }

            @Override
            public void onOrigin() {

            }
        });
    }
}
