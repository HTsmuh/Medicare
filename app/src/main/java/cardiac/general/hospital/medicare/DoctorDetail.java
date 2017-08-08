package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DoctorDetail extends AppCompatActivity {

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("DocId");
        Toast.makeText(this, "Doctor ID : "+id, Toast.LENGTH_SHORT).show();
    }
}
