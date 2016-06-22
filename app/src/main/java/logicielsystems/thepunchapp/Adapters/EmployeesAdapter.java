package logicielsystems.thepunchapp.Adapters;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import logicielsystems.thepunchapp.Schema.Employees;
import logicielsystems.thepunchapp.Support.Constants;
import logicielsystems.thepunchapp.ViewHolders.EmployeesViewHolder;

/**
 * Created by Deep on 21/06/2016.
 */
public class EmployeesAdapter extends RecyclerView.Adapter {

    List<Employees> employeesList;
    List<Employees> itemsPendingRemoval;
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<Employees, Runnable> pendingRunnables = new HashMap<>(); // map of employeesList to pending runnables, so we can cancel a removal if need be

    public EmployeesAdapter() {
        employeesList = new ArrayList<>();
        itemsPendingRemoval = new ArrayList<>();

        Iterator<Employees> employeesIterator= Employees.findAll(Employees.class);

        while (employeesIterator.hasNext()){
            Employees employee=employeesIterator.next();
            employeesList.add(employee);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmployeesViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EmployeesViewHolder viewHolder = (EmployeesViewHolder)holder;
        final Employees employee = employeesList.get(position);

        if (itemsPendingRemoval.contains(employee)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.titleTextView.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(employee);
                    pendingRunnables.remove(employee);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(employee);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(employeesList.indexOf(employee));
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.titleTextView.setVisibility(View.VISIBLE);
            viewHolder.titleTextView.setText(employee.getName());
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return employeesList.size();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final Employees employee = employeesList.get(position);
        if (!itemsPendingRemoval.contains(employee)) {
            itemsPendingRemoval.add(employee);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(employeesList.indexOf(employee));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, Constants.PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(employee, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Employees employee = employeesList.get(position);
        if (itemsPendingRemoval.contains(employee)) {
            itemsPendingRemoval.remove(employee);
        }
        if (employeesList.contains(employee)) {
            employeesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        Employees employee = employeesList.get(position);
        return itemsPendingRemoval.contains(employee);
    }
}
