package logicielsystems.thepunchapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import logicielsystems.thepunchapp.Adapters.EmployeesAdapter;
import logicielsystems.thepunchapp.AsyncTasks.AddEmployeeAsyncTask;
import logicielsystems.thepunchapp.ItemDecorations.EmployeesAnimationDecorator;
import logicielsystems.thepunchapp.Listners.AddEmployeeListner;
import logicielsystems.thepunchapp.Listners.LoginListner;
import logicielsystems.thepunchapp.Schema.CompanySchema;
import logicielsystems.thepunchapp.Schema.Employees;

public class EmployeesActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private FloatingActionButton btnNewEmployee;
    private ProgressDialog waitProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        setViews();

        setUpRecyclerView();

        setActions();
    }

    private void setViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnNewEmployee=(FloatingActionButton)findViewById(R.id.btnNewEmployee);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new EmployeesAdapter());
        mRecyclerView.setHasFixedSize(true);
        getAdapter().setUndoOn(true);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    private void setActions(){
        btnNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEmployeeDialog();
            }
        });
    }

    private void showAddEmployeeDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmployeesActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_employee, null);
        dialogBuilder.setView(dialogView);

        final EditText txtEmployeeId = (EditText) dialogView.findViewById(R.id.txtEmployeeId);
        final EditText txtEmployeeName = (EditText) dialogView.findViewById(R.id.txtEmployeeName);

        dialogBuilder.setPositiveButton(getResources().getString(R.string.action_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CompanySchema company= getCompany();
                Employees employee=new Employees(company.getId(),txtEmployeeId.getText().toString(),txtEmployeeName.getText().toString());
                addEmployee(company.getEmail(),employee);
            }
        });

        dialogBuilder.setNegativeButton(getResources().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        /*Button btnSave=(Button)dialogView.findViewById(R.id.btnSave);
        Button btnCancel=(Button)dialogView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void addEmployee(String email, Employees employee) {
        AddEmployeeAsyncTask addEmployeeAsyncTask=new AddEmployeeAsyncTask(email,employee,getAddEmployeeListner());
        addEmployeeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AddEmployeeListner getAddEmployeeListner(){
        return new AddEmployeeListner() {

            @Override
            public void beforeAddEmployee() {
                showProgress();
            }

            @Override
            public void afterAddEmployee(boolean isSuccess) {
                cancleProgress();
                getAdapter().notifyDataSetChanged();
            }
        };
    }

    private void showProgress() {
        waitProgressDialog = ProgressDialog.show(EmployeesActivity.this, getResources().getString(R.string.please_wait), getResources().getString(R.string.adding_employee), true);
        waitProgressDialog.setCancelable(true);
    }

    private void cancleProgress(){
        waitProgressDialog.cancel();
    }

    private CompanySchema getCompany() {
        try{
            return CompanySchema.listAll(CompanySchema.class).get(0);
        }catch (Exception ex){
            return null;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_undo_checkbox) {
            item.setChecked(!item.isChecked());
            ((EmployeesAdapter)mRecyclerView.getAdapter()).setUndoOn(item.isChecked());
        }
        if (item.getItemId() == R.id.menu_item_add_5_items) {
            ((EmployeesAdapter)mRecyclerView.getAdapter()).addItems(5);
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(EmployeesActivity.this, R.drawable.ic_clear);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) EmployeesActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                EmployeesAdapter employeesAdapter = (EmployeesAdapter)recyclerView.getAdapter();
                if (employeesAdapter.isUndoOn() && employeesAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                EmployeesAdapter adapter = (EmployeesAdapter)mRecyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new EmployeesAnimationDecorator());
    }

    private EmployeesAdapter getAdapter(){
        return (EmployeesAdapter)mRecyclerView.getAdapter();
    }
}
