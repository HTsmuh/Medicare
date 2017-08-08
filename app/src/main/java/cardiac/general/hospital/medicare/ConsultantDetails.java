package cardiac.general.hospital.medicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cardiac.general.hospital.medicare.Adapter.ConsultantDetailsAdapter;

public class ConsultantDetails extends AppCompatActivity {
    String TAG = "Response";
    Object resultString;
    JSONObject jsonResponse;
    JSONArray jsonMainNode;
    String id, Sr_Name;
    String DocId, DocName, DocDept, DocSpeciality, DocDays1, DocTiming1, DocDays2, DocTiming2;
    ListView consultantList;
    ArrayList<String> DocNameArray = new ArrayList<String>();
    ArrayList<String> DocDeptArray = new ArrayList<String>();
    ArrayList<String> DocDays1Array = new ArrayList<String>();
    ArrayList<String> DocTiming1Array = new ArrayList<String>();
    ArrayList<String> DocDays2Array = new ArrayList<String>();
    ArrayList<String> DocTiming2Array = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_details);

        AsyncCallWS task = new AsyncCallWS();
        task.execute();
        consultantList = (ListView) findViewById(R.id.listView13);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("ID");
    }
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            GetJSON();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //Toast.makeText(SpecialityDetails.this, "" + Sr_Name, Toast.LENGTH_LONG).show();
            String[] docNames = Doc_Name();
            String[] docDepts = Doc_Dept();
            ConsultantDetailsAdapter simpleAdapter = new ConsultantDetailsAdapter(getBaseContext(), docDepts, docNames, docNames, docNames,docNames,docNames,docNames);
            consultantList.setAdapter(simpleAdapter);
        }
    }
    public void GetJSON() {
        String METHOD_NAME = "Get_ConultantDetails";
        String NAMESPACE = "http://medicarehospital.pk//";
        String URL = "http://medicarehospital.pk/WebService.asmx";
        String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("TreatmentId", id);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = soapEnvelope.getResponse();

            String jsonString = "{\"specialities\":" + resultString.toString() + "}";
            jsonResponse = new JSONObject(jsonString);
            jsonMainNode = jsonResponse.optJSONArray("specialities");
            Log.i(TAG, "Result: " + jsonMainNode);
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                //DocId = jsonChildNode.optString("Id");
                DocName = jsonChildNode.optString("Name");
                DocDept = jsonChildNode.optString("TreatmentName");
                DocSpeciality = jsonChildNode.optString("Speciality");
                DocDays1 = jsonChildNode.optString("Clinicdays1");
                DocTiming1 = jsonChildNode.optString("Timing1");
                DocDays2 = jsonChildNode.optString("Clinicdays2");
                DocTiming2 = jsonChildNode.optString("Timing2");
                Sr_Name = i + 1 + ": " + DocName;
                DocNameArray.add(Sr_Name);
                DocDeptArray.add(DocDept);
            }
        }catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    public String[] Doc_Name() {
        String[] Doc_Name = new String[DocNameArray.size()];
        Doc_Name = DocNameArray.toArray(Doc_Name);
        return Doc_Name;
    }
    public String[] Doc_Dept() {
        String[] Doc_dept = new String[DocDeptArray.size()];
        Doc_dept = DocDeptArray.toArray(Doc_dept);
        return Doc_dept;
    }
}