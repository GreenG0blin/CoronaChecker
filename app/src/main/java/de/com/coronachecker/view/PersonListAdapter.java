package de.com.coronachecker.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import de.com.coronachecker.R;
import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.persistence.entities.enums.Status;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonViewHolder> {

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameItemView;
        private final TextView countyItemView;
        private final TextView lastUpdatedView;
        private final Chip incidenceChip;

        private PersonViewHolder(View itemView) {
            super(itemView);
            nameItemView = itemView.findViewById(R.id.nameView);
            countyItemView = itemView.findViewById(R.id.countyView);
            lastUpdatedView = itemView.findViewById(R.id.lastUpdatedView);
            incidenceChip = itemView.findViewById(R.id.incidenceView);
        }

    }

    private final DecimalFormat df = new DecimalFormat("0.0");
    private final LayoutInflater mInflater;
    private List<Person> mPersons;
    private final PersonListAdapter.OnDismissListener listener;

    public PersonListAdapter(Context context, OnDismissListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonListAdapter.PersonViewHolder holder, int position) {
        if (mPersons != null) {
            Person current = mPersons.get(position);
            holder.nameItemView.setText(current.getName());
            holder.incidenceChip.setText(df.format(current.sevenDaysIncidence));
            holder.countyItemView.setText(current.getCounty());
            holder.lastUpdatedView.setText(current.getLastUpdated());

            if(current.getStatus().equals(Status.GREEN)) {
                holder.incidenceChip.setChipBackgroundColorResource(R.color.statusGreen);
            }
            if(current.getStatus().equals(Status.YELLOW)) {
                holder.incidenceChip.setChipBackgroundColorResource(R.color.statusYellow);
            }
            if(current.getStatus().equals(Status.RED)) {
                holder.incidenceChip.setChipBackgroundColorResource(R.color.statusRed);
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.nameItemView.setText("No Name");
            holder.incidenceChip.setText("0");
        }

    }

    public void setPersons(List<Person> persons) {
        mPersons = persons;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPersons != null)
            return mPersons.size();
        else return 0;
    }

    public void deleteItem(int position) {
        Person personToDelete = mPersons.get(position);
        listener.onDismiss(personToDelete);

        mPersons.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnDismissListener {
        void onDismiss(Person person);
    }

}

