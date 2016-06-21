package logicielsystems.thepunchapp.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import logicielsystems.thepunchapp.R;

/**
 * Created by Deep on 21/06/2016.
 */
public class EmployeesViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView;
    public Button undoButton;

    public EmployeesViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false));
        titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
        undoButton = (Button) itemView.findViewById(R.id.undo_button);
    }
}
