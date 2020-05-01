package com.dexter.myhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dexter.myhome.R;
import com.dexter.myhome.model.Event;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(events.get(position));

        Event event = events.get(position);

        holder.title.setText(event.getTitle());
        holder.description.setText(event.getDescription());
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy").format(event.getEventDate()));
        holder.organizer.setText(event.getOrganizer());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView date;
        public TextView organizer;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            organizer = itemView.findViewById(R.id.organizer);

            itemView.setOnLongClickListener(v -> {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
                alertDialogBuilder.setMessage("Are you sure you want to delete the event?");
                alertDialogBuilder.setPositiveButton("Yes", (arg0, arg1) -> {
                    int position = getAdapterPosition();
                    events.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, events.size());
                    Toast.makeText(itemView.getContext(), "Event Deleted !", Toast.LENGTH_LONG).show();
                });
                alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(Boolean.TRUE);
                alertDialog.show();
                return false;
            });
        }
    }
}
