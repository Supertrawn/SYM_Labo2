package heig_vd.sym_labo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnasync;
    Button btndiff;
    Button btngraph;
    Button btnser;
    Button btncompress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnasync = (Button) findViewById(R.id.btnAsync);
        btnasync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnasync.getContext(), AsyncActivity.class);
                btnasync.getContext().startActivity(intent);
            }
        });

        btndiff = (Button) findViewById(R.id.btnDiff);
        btndiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btndiff.getContext(), DifActivity.class);
                btndiff.getContext().startActivity(intent);
            }
        });

        btngraph = (Button) findViewById(R.id.btnGraph);
        btngraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btngraph.getContext(), GraphActivity.class);
                btngraph.getContext().startActivity(intent);
            }
        });

        btnser = (Button) findViewById(R.id.btnSer);
        btnser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnser.getContext(), SerialActivity.class);
                btnser.getContext().startActivity(intent);
            }
        });

        btncompress = (Button) findViewById(R.id.btnCompress);
        btncompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btncompress.getContext(), CompressActivity.class);
                btncompress.getContext().startActivity(intent);
            }
        });
    }


}
