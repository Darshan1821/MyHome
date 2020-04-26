package com.dexter.myhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dexter.myhome.R;
import com.dexter.myhome.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private Context context;
    private List<Meeting> meetings;

    public MeetingAdapter(Context context, List<Meeting> meetings) {
        this.context = context;
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(meetings.get(position));

        Meeting meeting = meetings.get(position);

        holder.title.setText(meeting.getTitle());
        holder.description.setText(meeting.getDescription());
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy").format(meeting.getMeetingDate()));
        holder.organizer.setText(meeting.getOrganizer());
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView date;
        public TextView organizer;
        public TextView delete;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            organizer = itemView.findViewById(R.id.organizer);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                meetings.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, meetings.size());
                Toast.makeText(itemView.getContext(), "Meeting Deleted !", Toast.LENGTH_LONG).show();
            });
        }
    }
}
