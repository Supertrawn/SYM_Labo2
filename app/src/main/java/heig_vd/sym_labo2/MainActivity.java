package heig_vd.sym_labo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import heig_vd.sym_labo2.activity.AsyncActivity;
import heig_vd.sym_labo2.activity.CompressActivity;
import heig_vd.sym_labo2.activity.GraphqlActivity;
import heig_vd.sym_labo2.activity.SerialActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAsync;
    Button btnDiff;
    Button btnGraphQL;
    Button btnSerial;
    Button btnCompressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAsync = (Button) findViewById(R.id.btnAsync);
        btnAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnAsync.getContext(), AsyncActivity.class);
                btnAsync.getContext().startActivity(intent);
            }
        });

        btnDiff = (Button) findViewById(R.id.btnDiff);
        btnDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnDiff.getContext(), DiffActivity.class);
                btnDiff.getContext().startActivity(intent);
            }
        });

        btnGraphQL = (Button) findViewById(R.id.btnGraph);
        btnGraphQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnGraphQL.getContext(), GraphqlActivity.class);
                btnGraphQL.getContext().startActivity(intent);
            }
        });

        btnSerial = (Button) findViewById(R.id.btnSer);
        btnSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnSerial.getContext(), SerialActivity.class);
                btnSerial.getContext().startActivity(intent);
            }
        });

        btnCompressed = (Button) findViewById(R.id.btnCompress);
        btnCompressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnCompressed.getContext(), CompressActivity.class);
                btnCompressed.getContext().startActivity(intent);
            }
        });
    }


}
