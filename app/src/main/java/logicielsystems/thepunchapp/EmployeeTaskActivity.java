package logicielsystems.thepunchapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmployeeTaskActivity extends AppCompatActivity {

    private Button btnEmployees;
    private Button btnTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_task);

        setViews();

        setActions();
    }

    private void setViews() {
        btnEmployees=(Button)findViewById(R.id.btnEmplooyees);
        btnTasks=(Button)findViewById(R.id.btnTasks);
    }

    private void setActions() {
        btnEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeTaskActivity.this,EmployeesActivity.class);
                startActivity(intent);
            }
        });
        btnTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeTaskActivity.this,TasksActivity.class);
                startActivity(intent);
            }
        });
    }
}
