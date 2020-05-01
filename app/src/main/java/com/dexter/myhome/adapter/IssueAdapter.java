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
import com.dexter.myhome.model.Issue;

import java.text.SimpleDateFormat;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {

    private Context context;
    private List<Issue> issues;

    public IssueAdapter(Context context, List<Issue> issues) {
        this.context = context;
        this.issues = issues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(issues.get(position));

        Issue issue = issues.get(position);

        holder.title.setText(issue.getTitle());
        holder.description.setText(issue.getDescription());
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy").format(issue.getIssueDate()));
        holder.raisedBy.setText(issue.getRaisedBy());
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView date;
        public TextView raisedBy;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            raisedBy = itemView.findViewById(R.id.raised_by);

            itemView.setOnLongClickListener(v -> {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
                alertDialogBuilder.setMessage("Are you sure you want to delete the issue?");
                alertDialogBuilder.setPositiveButton("Yes", (arg0, arg1) -> {
                    int position = getAdapterPosition();
                    issues.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, issues.size());
                    Toast.makeText(itemView.getContext(), "Issue Deleted !", Toast.LENGTH_LONG).show();
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
